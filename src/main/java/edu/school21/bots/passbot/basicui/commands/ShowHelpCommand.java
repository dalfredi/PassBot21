package edu.school21.bots.passbot.basicui.commands;

import edu.school21.bots.passbot.basicui.commands.meta.SimpleCommand;
import edu.school21.bots.passbot.basicui.commands.meta.Commands;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Component
public class ShowHelpCommand implements SimpleCommand {
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
        response.setText(Commands.HELP_TEXT);
        return response;
    }
}
