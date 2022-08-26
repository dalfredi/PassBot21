package edu.school21.passbot.commands;

import edu.school21.passbot.commandsfactory.Command;
import edu.school21.passbot.commandsfactory.CommandsFactory;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.Collections;
import java.util.List;

@Component
@Scope("prototype")
@NoArgsConstructor
public class ShowHelpCommand extends Command {
    @Getter
    private String name = "/help";
    @Getter
    private String name2 = "Помощь";


    public ShowHelpCommand(Long chatId) {
        super(chatId);
    }

    @Override
    public List<SendMessage> execute() {
        SendMessage response = new SendMessage();
        response.setChatId(chatId);
        response.setText(CommandsFactory.HELP_TEXT);
        return Collections.singletonList(response);
    }
}
