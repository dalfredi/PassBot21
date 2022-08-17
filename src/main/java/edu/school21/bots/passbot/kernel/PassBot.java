package edu.school21.bots.passbot.kernel;

import edu.school21.bots.passbot.config.BotConfig;
import edu.school21.bots.passbot.kernel.commands.Command;
import edu.school21.bots.passbot.kernel.commands.Commands;
import org.apache.shiro.session.InvalidSessionException;
import org.apache.shiro.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.session.TelegramLongPollingSessionBot;

import java.util.Optional;

public class PassBot extends TelegramLongPollingSessionBot {
    private static final Logger logger = LoggerFactory.getLogger(PassBot.class);
    BotConfig config;
    private final Commands commands;

    public PassBot(BotConfig config) {
        this.config = config;
        commands = new Commands();
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

    private SendMessage manageMessage(Message message, Session session) {
        Command command = (Command) session.getAttribute("command");
        SendMessage response;

        if (command == null) {
            command = commands.getCommandByName(message.getChatId(), message.getText());
            session.setAttribute("command", command);
        }
        else {
            command.addArgument(message.getText());
        }
        if (command.isReady()) {
            response = command.execute();
            session.setAttribute("command", null);
        }
        else
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
}


