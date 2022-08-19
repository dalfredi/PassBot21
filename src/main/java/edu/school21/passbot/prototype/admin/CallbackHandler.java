package edu.school21.passbot.prototype.admin;

import edu.school21.passbot.prototype.gateway.bot.PassBot;
import edu.school21.passbot.prototype.kernel.models.Order;
import edu.school21.passbot.prototype.kernel.service.NotificationService;
import edu.school21.passbot.prototype.kernel.service.OrderService;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.Serializable;

@Component
public class CallbackHandler {
    private static final String APPROVE_PREFIX = "approve_";
    private static final String APPROVE_POSTFIX = " ✅";
    private static final String DECLINE_PREFIX = "decline_";
    private static final String DECLINE_POSTFIX = " ❌";
    private static final String APPROVE_STATUS = "Одобрена";
    private static final String DECLINE_STATUS = "Отклонена";
    @Setter
    private PassBot passBot;
    private final OrderService orderService;
    private final NotificationService notificationService;

    public CallbackHandler(OrderService orderService, NotificationService notificationService) {
        this.orderService = orderService;
        this.notificationService = notificationService;
    }

    private void sendUpdate(BotApiMethod<Serializable> update) {
        try {
            passBot.execute(update);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public SendMessage handle(CallbackQuery callbackQuery) {
        String data = callbackQuery.getData();
        long chatId = callbackQuery.getMessage().getChatId();
        long messageId = callbackQuery.getMessage().getMessageId();

        if (data.startsWith(APPROVE_PREFIX)) {
            approveOrder(messageId, chatId, data);
        }
        else if (data.startsWith(DECLINE_PREFIX)) {
            declineOrder(messageId, chatId, data);
        }

        return null;
    }

    private void approveOrder(long messageId, long chatId, String data) {
        changeOrderStatus(messageId, chatId, data, APPROVE_PREFIX, APPROVE_STATUS);
    }

    private void declineOrder(long messageId, long chatId, String data) {
        changeOrderStatus(messageId, chatId, data, DECLINE_PREFIX, DECLINE_STATUS);
    }

    private void changeOrderStatus(
            long messageId, long chatId, String data, String prefix, String status)
    {
        EditMessageText response = new EditMessageText();
        response.setChatId(chatId);
        response.setMessageId(Math.toIntExact(messageId));
        response.enableMarkdown(true);

        Long orderId = Long.valueOf(data.replaceFirst(prefix, ""));
        Order order = orderService.changeStatus(orderId, status);

        response.setText(order.toMarkdownPrettyString()
                .replaceFirst(status, status +
                        (status.equals(APPROVE_STATUS) ? APPROVE_POSTFIX : DECLINE_POSTFIX))
        );

        StringBuilder sb = new StringBuilder();
        sb.append("Ваша заявка №")
                .append(orderId).append(" перешла в статус ").append(status)
                .append("\r\n").append("--------------").append("\r\n")
                .append("C уважением, Администрация Школы 21 города ")
                .append(order.getCampus())
                .append("\r\n");
        notificationService.sendEmail(sb.toString(), "postfedor@gmail.com");

//        SendMessage notification = new SendMessage();
//        notification.setChatId(order.getPeer().getChatId());
//        notification.setText("Ваша заявка на посещение перешла в статус " + status);
//        passBot.sendMessage(notification);

        sendUpdate(response);
    }
}
