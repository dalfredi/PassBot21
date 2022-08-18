package edu.school21.bots.passbot.basicui.commands;

import edu.school21.bots.passbot.basicui.commands.meta.CommandsFactory;
import edu.school21.bots.passbot.basicui.commands.meta.Command;
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
