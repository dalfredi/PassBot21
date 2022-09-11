package edu.school21.passbot.commands;

import edu.school21.passbot.commandsfactory.Command;
import edu.school21.passbot.models.Order;
import edu.school21.passbot.models.User;
import edu.school21.passbot.service.OrderService;
import edu.school21.passbot.service.UserService;
import edu.school21.passbot.telegramview.Renderer;
import java.util.List;
import lombok.Getter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Component
@Scope("prototype")
public class ListRequestsCommand extends Command {
    @Getter
    private final String name = "/list";
    @Getter
    private final String name2 = "Мои заявки";
    private final UserService userService;
    private final OrderService orderService;

    public ListRequestsCommand(UserService userService,
                               OrderService orderService) {
        this.userService = userService;
        this.orderService = orderService;
    }

    @Override
    public void init() {
        User user = userService.getByChatId(chatId);
        if (user == null) {
            setError("Представьтесь, чтобы выполнить эту команду /start");
        }
    }

    @Override
    public List<SendMessage> execute() {
        User user = userService.getByChatId(chatId);
        List<Order> orders = orderService.getAllByUserId(user);
        if (orders.isEmpty()) {
            return Renderer.plainMessage(chatId, "Сейчас активных заявок нет");
        }
        return Renderer.toUserOrderCards(chatId, orders);
    }
}
