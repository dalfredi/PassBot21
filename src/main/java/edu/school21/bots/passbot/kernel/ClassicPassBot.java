package edu.school21.bots.passbot.kernel;

import edu.school21.bots.passbot.config.BotConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class ClassicPassBot extends TelegramLongPollingBot {

    private static final Logger logger = LoggerFactory.getLogger(ClassicPassBot.class);
    BotConfig config;

    public ClassicPassBot(BotConfig config) {
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
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String message_text = update.getMessage().getText();
            Long chat_id = update.getMessage().getChatId();
            switch(message_text) {
                case "/start":
                    sendMessage(chat_id, "Write your name");
//                    sendMessage(chat_id, );
                    sendMessage(chat_id, "Write your name");
                    break ;
                default:
                    sendMessage(chat_id, "Sorry!");
            }
//            sendMessage(chat_id, message_text);
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
}
