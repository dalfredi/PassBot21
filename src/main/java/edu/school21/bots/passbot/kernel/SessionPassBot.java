package edu.school21.bots.passbot.kernel;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import edu.school21.bots.passbot.config.BotConfig;
import lombok.SneakyThrows;
import org.apache.commons.codec.binary.Base64;
import org.apache.shiro.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.session.TelegramLongPollingSessionBot;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;

//@Controller
//@RequestMapping
public class SessionPassBot extends TelegramLongPollingSessionBot {
    private static final Logger logger = LoggerFactory.getLogger(SessionPassBot.class);
    BotConfig config;

    public SessionPassBot(BotConfig config) {
        this.config = config;
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
    @Override
    public void onUpdateReceived(Update update, Optional<Session> optional) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String message_text = update.getMessage().getText();
            Long chat_id = update.getMessage().getChatId();
            sendMessage(chat_id, message_text);
            if (!optional.isPresent())
                return;
            Session session = optional.get();
            requestAccessToken(chat_id, message_text);
            sendMessage(chat_id, "Your session id: " + session.getId().toString());
            sendMessage(chat_id, "Your host: " + session.getHost());
        }
    }

//    @RequestMapping
    private void requestAccessToken(long chatId, String name) throws IOException {
        ResponseEntity<String> response;
        RestTemplate restTemplate = new RestTemplate();
        String clientId = config.getCredentialId() + ":" + config.getCredentialSecret();
        String encodedCredentials = new String(Base64.encodeBase64(clientId.getBytes()));
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.add("Authorization", "Basic " + encodedCredentials);

        HttpEntity<String> request = new HttpEntity<String>(headers);
        String access_token_url = "https://api.intra.42.fr/oauth/token";
        access_token_url += "?code=" + clientId;
        access_token_url += "&grant_type=client_credentials";
//        System.out.println(access_token_url);

        response = restTemplate.exchange(access_token_url, HttpMethod.POST, request, String.class);
//        System.out.println("Access Token Response ---------" + response.getBody());

        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(response.getBody());
        String token = node.path("access_token").asText();

        String url = "https://api.intra.42.fr/v2/users?filter[login]=" + name;

        HttpHeaders headers1 = new HttpHeaders();
        headers1.add("Authorization", "Bearer " + token);
        HttpEntity<String> entity = new HttpEntity<>(headers1);

        ResponseEntity<String> peers = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
//        System.out.println(peers);
//        List<String> links = peers.getHeaders().get("Link");
//        Map<String, String> linkMap = parseLinkHeader(links);
//        int i = links.size();
//        for (int j = 0; j < i; j++) {
//            System.out.println(links.get(j));
//        }
//        System.out.println(peers.getHeaders().get("Link"));
        JsonNode node1 = mapper.readTree(peers.getBody());
//        System.out.println(node1.path(0).fieldNames());
        Iterator<Map.Entry<String, JsonNode>> node2 = node1.path(0).fields();
        while (node2.hasNext()) {
            Map.Entry<String, JsonNode> tmp = node2.next();
            System.out.println(tmp.getKey() + " : " + tmp.getValue());
        }
//        ObjectReader reader = mapper.readerFor(new TypeReference<Map<String, String>>() {});
//        Map<String, String> list = reader.readValue(node1);
//        for (Map.Entry<String, String> entry : list.entrySet()) {
//            System.out.println(entry.getKey() + " : " + entry.getValue());
//        }
//        System.out.println(node1);
//        Iterator<JsonNode> fields = node1.elements();
//        while (fields.hasNext()) {
//            JsonNode field = fields.next();
//        Iterator<JsonNode> fields = node1.elements();
//        while (fields.hasNext()) {
//            JsonNode field = fields.next();
//
//            System.out.println(field);
//        }
//            if (result.get("login").equals(name)) {
//                for (Map.Entry<String, String> entry : result.entrySet()) {
//                    System.out.println(entry.getKey() + ":" + entry.getValue());
//                }
//            }
//        }
//        List<String> links = peers.getHeaders().get("Link");
//        Map<String, String> linkMap = parseLinkHeader(links);
//        url = linkMap.get("next");
//        while (!url.isEmpty()) {
//            System.out.println(url);
//            List<String> links = peers.getHeaders().get("Link");
//            Map<String, String> linkMap = parseLinkHeader(links);
//            url = linkMap.get("next");
//            peers = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
//            System.out.println(peers.getBody());
//            node1 = mapper.readTree(peers.getBody());
//        }
//        System.out.println(node1);
//            Iterator<JsonNode> fields1 = node1.elements();
//            while (fields1.hasNext()) {
//                JsonNode field = fields1.next();
//                Map<String, String> result = mapper.convertValue(field, new TypeReference<Map<String, String>>() {});
//                System.out.println(result.get("login"));
//                if (result.get("login").equals(name)) {
//                    for (Map.Entry<String, String> entry : result.entrySet()) {
//                        System.out.println(entry.getKey() + ":" + entry.getValue());
//                        return ;
//                    }
//                }
//            }
//        }
    }

//    private Map<String, String> parseLinkHeader(List<String> link) {
//        Map<String, String> map = new HashMap<>();
//        String[] arr = link.get(0).split(", ");
//        for (int i = 0; i < arr.length; i++) {
//            String[] tmp = arr[i].split("; ");
//            map.put(tmp[1].substring(5, tmp[1].length() - 1), tmp[0].substring(1, tmp[0].length() - 1));
//        }
//        return map;
//        for (Map.Entry<String, String> entry : map.entrySet()) {
//            System.out.println(entry.getKey() + " : " + entry.getValue());
//        }
//    }

    private void sendMessage(long chatId, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText(text);
        try {
            execute(sendMessage);
            logger.info("Sent message \"{}\" to {}", text, chatId);
        } catch (TelegramApiException e) {
            logger.error("Failed to send message \"{}\" to {} due to error: {}", text, chatId, e.getMessage());
        }
    }

    @PostConstruct
    public void start() {
        logger.info("username: {}, token: {}", config.getName(), config.getToken());
    }
}


