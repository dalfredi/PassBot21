package edu.school21.bots.passbot.gateway.bot;

import edu.school21.bots.passbot.basicui.commands.meta.Command;
import edu.school21.bots.passbot.basicui.commands.meta.CommandsFactory;
import edu.school21.bots.passbot.dal.models.User;
import edu.school21.bots.passbot.gateway.config.BotConfig;
import lombok.SneakyThrows;
import org.apache.shiro.session.InvalidSessionException;
import org.apache.shiro.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.session.TelegramLongPollingSessionBot;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class PassBot extends TelegramLongPollingSessionBot {
    private static final Logger logger = LoggerFactory.getLogger(PassBot.class);
    private final BotConfig config;
    private final CommandsFactory commandsFactory;
    public PassBot(BotConfig config, CommandsFactory commandsFactory) {
        this.config = config;
        this.commandsFactory = commandsFactory;
    }

    @Override
    public void onUpdateReceived(Update update, Optional<Session> optional) {
        if (!optional.isPresent())
            throw new InvalidSessionException("Session not found");

        Session session = optional.get();
        SendMessage response = null;
        if (update.hasMessage() && update.getMessage().hasText()) {
            Message message = update.getMessage();
            response = manageMessage(message, session);
        }
        if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            response = manageCallback(callbackQuery, session);
        }
        if (response != null) {
            sendMessage(response);
        }
    }

    private SendMessage manageCallback(CallbackQuery callbackQuery, Session session) {
        return null;
    }

    @SneakyThrows
    private SendMessage manageMessage(Message message, Session session) {
        Command command = (Command) session.getAttribute("command");
        User user = (User) session.getAttribute("user");
        SendMessage response;

        if (command == null) {
            command = commandsFactory.getCommandByName(message.getChatId(), message.getText());
            command.init();
            if (command.isError()) {
                response = new SendMessage();
                response.setChatId(command.getChatId());
                response.setText(command.getResponseText());
                return response;
            }
            session.setAttribute("command", command);
        }
        else {
            command.addArgument(message.getText());
            if (command.isError()) {
                response = new SendMessage();
                response.setChatId(command.getChatId());
                response.setText(command.getResponseText());
                session.setAttribute("command", null);
                return response;
            }
        }
        if (command.isReady()) {
            response = command.execute();
            session.setAttribute("command", null);
        } else
            response = command.getNextPrompt();
        return response;
    }

    private void sendMessage(SendMessage message) {
        try {
            execute(message);
            logger.info("Message sent \"{}\" ", message.toString());
        } catch (TelegramApiException e) {
            logger.error("Failed to send message \"{}\"", message.toString());
        }
    }

    @Override
    public String getBotUsername() {
        return config.getName();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }

    @SneakyThrows
    private void setCommands() {
        List<BotCommand> commandsList = new ArrayList<BotCommand>();
        commandsList.add(
                new BotCommand("start", "запустить бота и зарегистрироваться"));
        commandsList.add(
                new BotCommand("new", "создать новую заявку на посещение гостя"));
        commandsList.add(
                new BotCommand("list", "показать все активные заявки"));
        commandsList.add(
                new BotCommand("help", "показать все доступные команды"));
        this.execute(new SetMyCommands(
                commandsList, new BotCommandScopeDefault(), null));
    }
//    private User requestAccessToken(long chatId, String name) throws IOException {
//        ResponseEntity<String> response;
//        RestTemplate restTemplate = new RestTemplate();
//        String clientId = config.getCredentialId() + ":" + config.getCredentialSecret();
//        String encodedCredentials = new String(Base64.encodeBase64(clientId.getBytes()));
//        HttpHeaders headers = new HttpHeaders();
//        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
//        headers.add("Authorization", "Basic " + encodedCredentials);
//
//        HttpEntity<String> request = new HttpEntity<String>(headers);
//        String access_token_url = "https://api.intra.42.fr/oauth/token";
//        access_token_url += "?code=" + clientId;
//        access_token_url += "&grant_type=client_credentials";
//
//        response = restTemplate.exchange(access_token_url, HttpMethod.POST, request, String.class);
//        ObjectMapper mapper = new ObjectMapper();
//        JsonNode node = mapper.readTree(response.getBody());
//        String token = node.path("access_token").asText();
//
//        String url = "https://api.intra.42.fr/v2/users?filter[email]=" + name;
//        HttpHeaders headers1 = new HttpHeaders();
//        headers1.add("Authorization", "Bearer " + token);
//        HttpEntity<String> entity = new HttpEntity<>(headers1);
//
//        ResponseEntity<String> peers = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
//        JsonNode node1 = mapper.readTree(peers.getBody());
//        Iterator<Map.Entry<String, JsonNode>> node2 = node1.path(0).fields();
//        while (node2.hasNext()) {
//            Map.Entry<String, JsonNode> tmp = node2.next();
//            System.out.println(tmp.getKey() + " : " + tmp.getValue());
//        }
//
//        return null;
//    }
}


