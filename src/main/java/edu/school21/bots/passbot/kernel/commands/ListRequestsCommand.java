package edu.school21.bots.passbot.kernel.commands;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public class ListRequestsCommand extends Command {
    public ListRequestsCommand(Long chatId) {
        super(chatId);
    }

    @Override
    public SendMessage execute() {
        SendMessage response = new SendMessage();
        response.setChatId(super.getChatId());
        // Запросить из бд все заявки и выслать карточки
        response.setText("Список всех заявок");
        return response;
    }
}
