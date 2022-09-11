package edu.school21.passbot.callbacks;

import edu.school21.passbot.models.Order;
import edu.school21.passbot.service.OrderService;
import edu.school21.passbot.telegramview.Buttons;
import edu.school21.passbot.telegramview.Renderer;
import java.io.Serializable;
import java.util.List;
import java.util.function.Consumer;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Component
public class OrderStatusCallbackHandler implements CallbackQueryHandler {
    private final OrderService orderService;

    public OrderStatusCallbackHandler(OrderService orderService) {
        this.orderService = orderService;
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

