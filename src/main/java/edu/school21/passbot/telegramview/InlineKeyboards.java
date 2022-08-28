package edu.school21.passbot.telegramview;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class InlineKeyboards {
    protected static ReplyKeyboard orderCardUser(Long orderId) {
        List<InlineKeyboardButton> buttons = Arrays.asList(
                InlineKeyboardButton.builder()
                        .text(Buttons.Edit.TEXT)
                        .callbackData(Buttons.Edit.CALLBACK + Buttons.SEPARATOR + orderId)
                        .build(),
                InlineKeyboardButton.builder()
                        .text(Buttons.Delete.TEXT)
                        .callbackData(Buttons.Delete.CALLBACK + Buttons.SEPARATOR + orderId)
                        .build()
        );
        return InlineKeyboardMarkup.builder()
                .keyboardRow(buttons)
                .build();
    }

    protected static ReplyKeyboard orderCardAdmin(Long orderId) {
        return InlineKeyboardMarkup.builder()
                .keyboardRow(Collections.singletonList((InlineKeyboardButton.builder()
                        .text(Buttons.Approve.TEXT)
                        .callbackData(Buttons.Approve.CALLBACK + Buttons.SEPARATOR + orderId)
                        .build())))
                .keyboardRow(Collections.singletonList(InlineKeyboardButton.builder()
                        .text(Buttons.Decline.TEXT)
                        .callbackData(Buttons.Decline.CALLBACK + Buttons.SEPARATOR + orderId)
                        .build()))
                .build();
    }
}
