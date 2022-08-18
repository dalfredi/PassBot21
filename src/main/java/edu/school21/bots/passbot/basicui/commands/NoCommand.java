package edu.school21.bots.passbot.basicui.commands;

import edu.school21.bots.passbot.basicui.commands.meta.SimpleCommand;
import lombok.Getter;
import lombok.Setter;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public class NoCommand implements SimpleCommand {
    @Setter
    @Getter
    Long chatId;
    public NoCommand() {
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public SendMessage execute() {
        SendMessage response = new SendMessage();
        response.setChatId(chatId);
        response.setText("Командна не найдена.\nВведите /help чтобы посмотреть доступные команды");
        return response;
    }
}
