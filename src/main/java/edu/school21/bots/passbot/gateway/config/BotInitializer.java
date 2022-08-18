package edu.school21.bots.passbot.gateway.config;

import edu.school21.bots.passbot.basicui.commands.meta.CommandsFactory;
import edu.school21.bots.passbot.gateway.bot.PassBot;
import edu.school21.bots.passbot.kernel.service.UserService;
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