package edu.school21.bots.passbot.basicui.commands;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public class ListRequestsCommand extends Command {
    public ListRequestsCommand(Long chatId) {
        super(chatId);
    }

    @Override
    public SendMessage execute() {
        SendMessage response = new SendMessage();
        // Запросить из бд все заявки и выслать карточки
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText("Кнопочка");
        button.setUrl("/edit");
        row.add(button);
        rows.add(row);
        inlineKeyboardMarkup.setKeyboard(rows);

        response.setChatId(super.getChatId());
        response.setReplyMarkup(inlineKeyboardMarkup);
        response.setText("Список всех заявок");
        return response;
    }
}
