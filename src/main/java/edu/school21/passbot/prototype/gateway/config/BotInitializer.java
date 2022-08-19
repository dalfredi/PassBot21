package edu.school21.passbot.prototype.gateway.config;

import edu.school21.passbot.prototype.gateway.commandsfactory.CommandsFactory;
import edu.school21.passbot.prototype.gateway.bot.PassBot;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Component
public class BotInitializer {
    final BotConfig config;
    final CommandsFactory commandsFactory;

    public BotInitializer(BotConfig config, CommandsFactory commandsFactory) {
        this.config = config;
        this.commandsFactory = commandsFactory;
    }

    @EventListener(ContextRefreshedEvent.class)
    public void init() throws TelegramApiException {
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        botsApi.registerBot(new PassBot(config, commandsFactory));
    }
}