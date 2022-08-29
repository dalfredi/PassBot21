package edu.school21.passbot.bot;

import edu.school21.passbot.callbacks.CallbackQueryFacade;
import edu.school21.passbot.commandsfactory.Command;
import edu.school21.passbot.commandsfactory.CommandsFactory;
import edu.school21.passbot.config.PassBotConfig;
import edu.school21.passbot.repositories.UserDataCache;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Component
public class PassBot extends TelegramLongPollingBot {
    private static final Logger logger = LoggerFactory.getLogger(PassBot.class);
    private final PassBotConfig config;
    private final CommandsFactory commandsFactory;
    private final CallbackQueryFacade callbackHandlerFacade;
    private final UserDataCache usersDataCache;
    public PassBot(PassBotConfig config, CommandsFactory commandsFactory,
                   CallbackQueryFacade callbackHandlerFacade, UserDataCache usersDataCache) {
        this.config = config;
        this.commandsFactory = commandsFactory;
        this.callbackHandlerFacade = callbackHandlerFacade;
        this.usersDataCache = usersDataCache;
        callbackHandlerFacade.setSender(this::sendUpdate);
    }

    @Override
    public void onUpdateReceived(Update update) {
        List<SendMessage> responses = null;
        if (update.hasMessage() && update.getMessage().hasText()) {
            Message message = update.getMessage();
            responses = manageMessage(message);
        }
        else if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            responses = manageCallback(callbackQuery);
        }
        if (responses != null && !responses.isEmpty()) {
            for (SendMessage response : responses)
                if (response != null)
                    sendMessage(response);
        }
    }

    private List<SendMessage> manageCallback(CallbackQuery callbackQuery) {
        return callbackHandlerFacade.handle(callbackQuery);
    }

    private List<SendMessage> manageMessage(Message message) {
        Long chatId = message.getChatId();
        String text = message.getText();
        Command command = usersDataCache.getCommand(chatId);
        List<SendMessage> response;

        if (command == null) {
            command = commandsFactory.getCommandByName(chatId, text);
            command.init();
            if (command.isError())
                return command.getErrorMessage();
            usersDataCache.setCommand(chatId, command);
        }
        else {
            command.validateArgument(text);
            if (command.isError())
                return command.getErrorMessage();
            command.addArgument(text);
        }
        if (command.isReady()) {
            response = command.execute();
            usersDataCache.clearCommand(chatId);
        } else
            response = command.getNextPrompt();
        return response;
    }

    public void sendMessage(SendMessage message) {
        try {
            execute(message);
            logger.info("Message sent \"{}\" ", message.toString());
        } catch (TelegramApiException e) {
            logger.error("Failed to send message \"{}\"", message.toString());
            e.printStackTrace();
        }
    }

    public void sendUpdate(BotApiMethod<Serializable> update) {
        try {
            execute(update);
            logger.info("Update sent \"{}\" ", update.toString());
        } catch (TelegramApiException e) {
            logger.error("Failed to send update \"{}\"", update.toString());
            e.printStackTrace();
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
        List<BotCommand> commandsList = new ArrayList<>();
        commandsList.add(
                new BotCommand("start", "запустить бота и ввести свой ник в интре"));
        commandsList.add(
                new BotCommand("register", "ввести свои ФИО"));
        commandsList.add(
                new BotCommand("new", "создать новую заявку на посещение гостя"));
        commandsList.add(
                new BotCommand("list", "показать все мои заявки"));
        commandsList.add(
                new BotCommand("listall", "показать все активные заявки (только для сотрудников школы)"));
        commandsList.add(
                new BotCommand("help", "показать все доступные команды"));
        this.execute(new SetMyCommands(
                commandsList, new BotCommandScopeDefault(), null));
    }
}


