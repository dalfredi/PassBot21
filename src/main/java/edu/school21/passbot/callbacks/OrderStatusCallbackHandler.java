package edu.school21.passbot.callbacks;

import edu.school21.passbot.models.Order;
import edu.school21.passbot.service.MailNotificationService;
import edu.school21.passbot.service.OrderService;
import edu.school21.passbot.telegramview.Buttons;
import edu.school21.passbot.telegramview.Renderer;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.io.Serializable;
import java.util.List;
import java.util.function.Consumer;

@Component
public class OrderStatusCallbackHandler implements CallbackQueryHandler {
    private final OrderService orderService;
    private final MailNotificationService mailNotificationService;

    public OrderStatusCallbackHandler(OrderService orderService, MailNotificationService mailNotificationService) {
        this.orderService = orderService;
        this.mailNotificationService = mailNotificationService;
    }

    @Override
    public List<SendMessage> handle(CallbackQuery query,
                                    Consumer<BotApiMethod<Serializable>> sender) {
        Long chatId = query.getMessage().getChatId();
        Integer messageId = query.getMessage().getMessageId();
        // callback data is the text of following structure:
        // "[action] [orderId]" e.g. "approve 21" or "decline 42"
        String[] data = query.getData().split(Buttons.SEPARATOR);
        String newStatus = data[0].equals(Buttons.Approve.CALLBACK)
                           ? Order.OrderStatus.APPROVED.getText()
                           : Order.OrderStatus.DECLINED.getText();
        Long orderId = Long.valueOf(data[1]);

        Order order = orderService.changeStatus(orderId, newStatus);
        sender.accept(Renderer.editedOrderMessage(chatId, messageId, order));
        return Renderer.statusChangedMessage(chatId, order);
    }
}
// TODO: уведомление по имейлу

//        StringBuilder sb = new StringBuilder();
//        sb.append("Ваша заявка №")
//                .append(orderId).append(" перешла в статус ").append(status)
//                .append("\r\n").append("--------------").append("\r\n")
//                .append("C уважением, Администрация Школы 21 города ")
//                .append(order.getCampus())
//                .append("\r\n");
//        mailNotificationService.sendEmail(sb.toString(), "postfedor@gmail.com");
