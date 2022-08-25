package edu.school21.passbot.commands;

import edu.school21.passbot.commandsfactory.CommandsFactory;
import edu.school21.passbot.commandsfactory.Command;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Component
@Scope("prototype")
public class ShowHelpCommand implements Command {
    @Getter
    private final String name = "/help";
    @Getter
    private final String name2 = "Помощь";
    @Setter
    @Getter
    Long chatId;

    public ShowHelpCommand() {}

    @Override
    public SendMessage execute() {
        SendMessage response = new SendMessage();
        response.setChatId(chatId);
        response.setText(CommandsFactory.HELP_TEXT);
        return response;
    }
}
