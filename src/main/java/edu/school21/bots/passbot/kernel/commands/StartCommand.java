package edu.school21.bots.passbot.kernel.commands;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public class StartCommand extends Command {

    public StartCommand(Long chatId) {
        super(chatId);
    }

    @Override
    public SendMessage execute() {
        SendMessage response = new SendMessage();
        response.setChatId(super.getChatId());
        response.setText("Бот запущен, вот что он может:\n" + Commands.HELP_TEXT);
        return response;
    }
}
