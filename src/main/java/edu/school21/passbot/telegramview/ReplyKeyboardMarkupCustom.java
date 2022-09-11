package edu.school21.passbot.telegramview;

import java.util.ArrayList;
import java.util.List;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

@Component
@NoArgsConstructor
public class ReplyKeyboardMarkupCustom {

    private List<KeyboardRow> getUserDefaultKeyboard(SendMessage sendMessage,
                                                     ReplyKeyboardMarkup replyKeyboardMarkup) {
        // Create a list of keyboard rows
        List<KeyboardRow> keyboard = new ArrayList<>();

        // First keyboard row
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        // Add buttons to the first keyboard row
        keyboardFirstRow.add(new KeyboardButton("Новая заявка"));
        keyboardFirstRow.add(new KeyboardButton("Мои заявки"));

        // Second keyboard row
        KeyboardRow keyboardSecondRow = new KeyboardRow();
        // Add the buttons to the second keyboard row
        keyboardSecondRow.add(new KeyboardButton("Помощь"));
        keyboardSecondRow.add(new KeyboardButton("Ввести ФИО"));

        // Add all the keyboard rows to the list
        keyboard.add(keyboardFirstRow);
        keyboard.add(keyboardSecondRow);

        // and assign this list to our keyboard
        replyKeyboardMarkup.setKeyboard(keyboard);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);

        return keyboard;
    }

    private List<KeyboardRow> getAdminDefaultKeyboard(SendMessage sendMessage,
                                                      ReplyKeyboardMarkup replyKeyboardMarkup) {
        // Create a list of keyboard rows
        List<KeyboardRow> keyboard = new ArrayList<>();

        // First keyboard row
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        // Add buttons to the first keyboard row
        keyboardFirstRow.add(new KeyboardButton("Все заявки"));
        keyboardFirstRow.add(new KeyboardButton("Новая заявка"));

        // Second keyboard row
        KeyboardRow keyboardSecondRow = new KeyboardRow();
        // Add the buttons to the second keyboard row
        keyboardSecondRow.add(new KeyboardButton("Мои заявки"));
        keyboardSecondRow.add(new KeyboardButton("Ввести ФИО"));

        // Third keyboard row
        KeyboardRow keyboardThirdRow = new KeyboardRow();
        // Add the buttons to the second keyboard row
        keyboardSecondRow.add(new KeyboardButton("Помощь"));

        // Add all the keyboard rows to the list
        keyboard.add(keyboardFirstRow);
        keyboard.add(keyboardSecondRow);
        keyboard.add(keyboardThirdRow);
        // and assign this list to our keyboard
        replyKeyboardMarkup.setKeyboard(keyboard);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);

        return keyboard;
    }

    public synchronized void setButtons(SendMessage sendMessage,
                                        String status) {
        // Create a keyboard
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        // Create a list of keyboard rows
        if (status.equals("ADMIN")) {
            this.getAdminDefaultKeyboard(sendMessage, replyKeyboardMarkup);
        } else if (status.equals("USER")) {
            this.getUserDefaultKeyboard(sendMessage, replyKeyboardMarkup);
        }
    }
}
