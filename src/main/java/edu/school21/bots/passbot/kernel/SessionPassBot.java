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
            System.out.println(tmp.getKey() + " : " + tmp.getValue());
        }
    }

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


