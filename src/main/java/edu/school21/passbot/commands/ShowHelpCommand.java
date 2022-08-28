package edu.school21.passbot.commands;

import edu.school21.passbot.commandsfactory.Command;
import edu.school21.passbot.commandsfactory.CommandsFactory;
import edu.school21.passbot.telegramview.Renderer;
import lombok.Getter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.List;

@Component
@Scope("prototype")
public class ShowHelpCommand extends Command {
    @Getter
    private final String name = "/help";
    @Getter
    private final String name2 = "Помощь";

    @Override
    public List<SendMessage> execute() {
        return Renderer.plainMessage(chatId, CommandsFactory.HELP_TEXT);
    }
}
