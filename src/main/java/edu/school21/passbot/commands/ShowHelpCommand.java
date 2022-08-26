package edu.school21.passbot.commands;

import edu.school21.passbot.commandsfactory.Command;
import edu.school21.passbot.commandsfactory.CommandsFactory;
import lombok.Getter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.Collections;
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
        SendMessage response = new SendMessage();
        response.setChatId(chatId);
        response.setText(CommandsFactory.HELP_TEXT);
        return Collections.singletonList(response);
    }
}
