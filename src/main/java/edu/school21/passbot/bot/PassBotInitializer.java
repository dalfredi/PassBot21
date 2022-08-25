package edu.school21.passbot.bot;

import edu.school21.passbot.admin.CallbackHandler;
import edu.school21.passbot.commandsfactory.CommandsFactory;
import edu.school21.passbot.config.PassBotConfig;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Component
public class PassBotInitializer {
    final PassBotConfig config;
    final CommandsFactory commandsFactory;
    final CallbackHandler callbackHandler;

    public PassBotInitializer(PassBotConfig config, CommandsFactory commandsFactory, CallbackHandler callbackHandler) {
        this.config = config;
        this.commandsFactory = commandsFactory;
        this.callbackHandler = callbackHandler;
    }

    @EventListener(ContextRefreshedEvent.class)
    public void init() throws TelegramApiException {
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        botsApi.registerBot(new PassBot(config, commandsFactory, callbackHandler));
    }
}