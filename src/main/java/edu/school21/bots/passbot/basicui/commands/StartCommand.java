package edu.school21.bots.passbot.basicui.commands;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.school21.bots.passbot.basicui.commands.meta.Command;
import edu.school21.bots.passbot.basicui.commands.meta.CommandWithArguments;
import edu.school21.bots.passbot.dal.models.User;
import edu.school21.bots.passbot.kernel.service.ApiService;
import edu.school21.bots.passbot.kernel.service.UserService;
import edu.school21.bots.passbot.mail.NotificationService;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.apache.commons.codec.binary.Base64;
import org.springframework.context.annotation.Scope;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Scope("prototype")
@Setter
@Getter
public class StartCommand implements CommandWithArguments, Command {
    private final NotificationService notificationService;
    private final ApiService apiService;
    private final String name = "/start";
    private final Integer maxArgs = 4;
    private final Map<Integer, String> prompts = new HashMap<>();
    private Long chatId;
    private Integer currentStep;
    private List<String> arguments = new ArrayList<>();
    private final UserService userService;

    public StartCommand(UserService userService,
                        NotificationService notificationService,
                        ApiService apiService) {
        this.userService = userService;
        this.notificationService = notificationService;
        this.apiService = apiService;
        prompts.put(0, "Привет! Давай познакомимся. Введи свой ник в Школе21");
        prompts.put(1, "Теперь введи свою фамилию");
        prompts.put(2, "Введи своё имя");
        prompts.put(3, "Введи своё отчество");
        error = false;
    }

    @Override
    public boolean isError() {
        return error;
    }

    @Override
    public void init() {
        User user = userService.getByChatId(chatId);
        if (user != null) {
            error = true;
            responseText = "Вы уже зарегистрированы и можете создать новую заявку командой /new";
            return;
        }
        CommandWithArguments.super.init();
    }

    @SneakyThrows
    @Override
    public void addArgument(String argument) {
        if (getCurrentStep() - 1 == 0) {
            // Perform check of argument number 0 and set responseText if error
            User user = userService.getByLogin(argument);
            if (user != null) {
                error = true;
                responseText = "Вы уже зарегистрированы и можете создать новую заявку командой /new";
                return;
            }
            try {
                user = requestAccessToken(argument);
            } catch (Exception e) {
                e.getMessage();
            }
            if (user == null) {
                error = true;
                responseText = "Такого пользователя нет в интре, попробуй заново /start";
                return;
            }
        }
        CommandWithArguments.super.addArgument(argument);
    }

    @Override
    public SendMessage execute() {
        SendMessage response = new SendMessage();
        response.setChatId(chatId);

        if (error) {
            response.setText(responseText);
            return response;
        }
        User user = null;
        try {
            user = apiService.requestAccessToken(arguments.get(0));
            System.out.println(user);
            userService.saveUser(user);
            notificationService.sendEmail();
        } catch (Exception e) {
            e.getMessage();
        }
        if (user == null) {
            response.setText("Такого пользователя нет в Интре! Попробуй ещё.");
            return response;
        }
        user.setChatId(chatId);
        user.setLogin(arguments.get(0));
        user.setSurname(arguments.get(1));
        user.setName(arguments.get(2));
        user.setPatronymic(arguments.get(3));
        userService.saveUser(user);

        response.setText("Отлично! Вы успешно вошли со следующими данными:\n" +
                "логин в Интре: " + user.getLogin() + "\n" +
                "ФИО: " + user.getSurname() + " " + user.getName() + " " + user.getPatronymic() + "\n");
        return response;
    }

    private User requestAccessToken(String name) throws IOException {
        ResponseEntity<String> response;
        User user = new User();

        RestTemplate restTemplate = new RestTemplate();
        String clientId = credentialId + ":" + clientSecret;
        String encodedCredentials = new String(Base64.encodeBase64(clientId.getBytes()));
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.add("Authorization", "Basic " + encodedCredentials);

        HttpEntity<String> request = new HttpEntity<String>(headers);
        String access_token_url = "https://api.intra.42.fr/oauth/token";
        access_token_url += "?code=" + clientId;
        access_token_url += "&grant_type=client_credentials";

        response = restTemplate.exchange(access_token_url, HttpMethod.POST, request, String.class);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(response.getBody());
        String token = node.path("access_token").asText();

        String url = "https://api.intra.42.fr/v2/users?filter[login]=" + name;
        HttpHeaders headers1 = new HttpHeaders();
        headers1.add("Authorization", "Bearer " + token);
        HttpEntity<String> entity = new HttpEntity<>(headers1);

        ResponseEntity<String> peers = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        JsonNode node1 = mapper.readTree(peers.getBody());
        Iterator<Map.Entry<String, JsonNode>> node2 = node1.path(0).fields();
        while (node2.hasNext()) {
            Map.Entry<String, JsonNode> tmp = node2.next();
            if (tmp.getKey().equals("login")) {
                user.setLogin(tmp.getValue().asText());
            }
            if (tmp.getKey().equals("staff?")) {
                user.setRole(tmp.getValue().asText().equals("false") ? "USER" : "ADMIN");
            }
        }
        return user;
    }
}
