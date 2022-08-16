package edu.school21.bots.passbot.kernel;

import edu.school21.bots.passbot.config.BotConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.abilitybots.api.objects.Ability;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static org.telegram.abilitybots.api.objects.Locality.ALL;
import static org.telegram.abilitybots.api.objects.Privacy.PUBLIC;

public class AbilityPassBot extends AbilityBot {
    private static final Logger logger = LoggerFactory.getLogger(AbilityPassBot.class);
    BotConfig config;

    public Ability saysHelloWorld() {
        return Ability.builder()
                .name("add") // Name and command (/hello)
                .info("Says hello world!") // Necessary if you want it to be reported via /commands
                .privacy(PUBLIC)  // Choose from Privacy Class (Public, Admin, Creator)
                .locality(ALL) // Choose from Locality enum Class (User, Group, PUBLIC)
                .input(3) // Arguments required for command (0 for ignore)
                .action(ctx -> {
          /*
          ctx has the following main fields that you can utilize:
          - ctx.update() -> the actual Telegram update from the basic API
          - ctx.user() -> the user behind the update
          - ctx.firstArg()/secondArg()/thirdArg() -> quick accessors for message arguments (if any)
          - ctx.arguments() -> all arguments
          - ctx.chatId() -> the chat where the update has emerged
          NOTE that chat ID and user are fetched no matter what the update carries.
          If the update does not have a message, but it has a callback query, the chatId and user will be fetched from that query.
           */
                    // Custom sender implementation
                    sendMessage(ctx.chatId(), "Фамилия: " + ctx.arguments()[0]);
                    sendMessage(ctx.chatId(), "Имя: " + ctx.arguments()[1]);
                    sendMessage(ctx.chatId(), "Отчество: " + ctx.arguments()[2]);
                })
                .post(ctx -> logger.info("processed action with arguments \"{}\", \"{}\", \"{}\"",
                        ctx.arguments()[0], ctx.arguments()[1], ctx.arguments()[2]))
                .build();
    }

    public AbilityPassBot(BotConfig config) {
        super(config.getToken(), config.getName());
    }

    private void sendMessage(long chatId, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText(text);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            logger.error("Failed to send message \"{}\" to {} due to error: {}", text, chatId, e.getMessage());
        }
    }

    @Override
    public long creatorId() {
        return 287621776;
    }
}
