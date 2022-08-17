package edu.school21.bots.passbot.basicui.commands;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public class NoCommand extends Command {
    public NoCommand(Long chatId) {
        super(chatId);
    }

    @Override
    public SendMessage execute() {
        SendMessage response = new SendMessage();
        response.setChatId(super.getChatId());
        response.setText("Командна не найдена.\nВведите /help чтобы посмотреть доступные команды");
        return response;
    }
}
