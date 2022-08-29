package edu.school21.passbot.telegramview;

import edu.school21.passbot.models.Order;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Renderer {
    private static final String APPROVE_POSTFIX = " ✅";
    private static final String DECLINE_POSTFIX = " ❌";

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
            if (order.getStatus().equals(Order.OrderStatus.PROCESSING.toString()))
                response.setReplyMarkup(InlineKeyboards.orderCardUser(order.getId()));
            response.enableMarkdown(true);
            sendMessageList.add(response);
        }
        return sendMessageList;
    }

    public static List<SendMessage> statusChangedMessage(Long chatId, Order order) {
        String status = order.getStatus();
        String emojiStatus = status + (status.equals(Order.OrderStatus.APPROVED.getText())
                                       ? APPROVE_POSTFIX
                                       : DECLINE_POSTFIX);
        SendMessage response = new SendMessage();
        response.setChatId(chatId);
        String text = "Ваша заявка на посещение сменила статус: \n"
                + order.toMarkdownPrettyString().replaceFirst(status, emojiStatus);
        response.setText(text);
        if (order.getStatus().equals(Order.OrderStatus.PROCESSING.getText()))
            response.setReplyMarkup(InlineKeyboards.orderCardUser(order.getId()));
        response.enableMarkdown(true);
        return Collections.singletonList(response);
    }

    public static BotApiMethod<Serializable> editedOrderMessage(Long chatId,
                                                                Integer messageId,
                                                                Order order) {
        String status = order.getStatus();
        String emojiStatus = status + (status.equals(Order.OrderStatus.APPROVED.getText())
                             ? APPROVE_POSTFIX
                             : DECLINE_POSTFIX);
        EditMessageText editMessage = new EditMessageText();
        editMessage.setChatId(chatId);
        editMessage.setMessageId(Math.toIntExact(messageId));
        editMessage.enableMarkdown(true);
        editMessage.setText(order.toMarkdownPrettyString()
                .replaceFirst(status, emojiStatus)
        );
        return editMessage;
    }
}
