package edu.school21.passbot.commands;

import edu.school21.passbot.commandsfactory.Command;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.Collections;
import java.util.List;

@Component
@Scope("prototype")
public class NoCommand extends Command {

    public NoCommand() {
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getName2() {
        return null;
    }
    @Override
    public List<SendMessage> execute() {
        SendMessage response = new SendMessage();
        response.setChatId(chatId);
        response.setText("Командна не найдена.\nВведите /help чтобы посмотреть доступные команды");
        return Collections.singletonList(response);
    }
}
