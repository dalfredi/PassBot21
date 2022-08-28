package edu.school21.passbot.telegramview;

import edu.school21.passbot.models.Order;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Renderer {

    public static List<SendMessage> plainMessage(@NotNull Long chatId, @NotNull String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(text);
        return Collections.singletonList(sendMessage);
    }

    public static List<SendMessage> toAdminOrderCards(Long chatId, List<Order> orders) {
        List<SendMessage> sendMessageList = new LinkedList<>();
        for (Order order : orders) {
            SendMessage response = new SendMessage();
            response.setChatId(chatId);
            response.enableMarkdown(true);
            response.setReplyMarkup(InlineKeyboards.orderCardAdmin(order.getId()));
            response.setText(order.toMarkdownPrettyString());
            sendMessageList.add(response);
        }
        return sendMessageList;
    }

    public static List<SendMessage> toUserOrderCards(Long chatId, List<Order> orders) {
        List<SendMessage> sendMessageList = new LinkedList<>();
        for (Order order : orders) {
            SendMessage response = new SendMessage();
            response.setChatId(chatId);
            response.setText(order.toMarkdownPrettyString());
            if (order.getStatus().equals("На рассмотрении"))
                response.setReplyMarkup(InlineKeyboards.orderCardUser(order.getId()));
            response.enableMarkdown(true);
            sendMessageList.add(response);
        }
        return sendMessageList;
    }
}
