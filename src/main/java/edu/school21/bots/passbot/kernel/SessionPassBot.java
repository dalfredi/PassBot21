package edu.school21.bots.passbot.kernel;

import edu.school21.bots.passbot.config.BotConfig;
import org.apache.shiro.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.session.TelegramLongPollingSessionBot;

import javax.annotation.PostConstruct;
import java.util.Optional;

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

    @Override
    public void onUpdateReceived(Update update, Optional<Session> optional) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String message_text = update.getMessage().getText();
            Long chat_id = update.getMessage().getChatId();
            sendMessage(chat_id, message_text);
            if (!optional.isPresent())
                return;
            Session session = optional.get();
            sendMessage(chat_id, "Your session id: " + session.getId().toString());
            sendMessage(chat_id, "Your host: " + session.getHost());
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


