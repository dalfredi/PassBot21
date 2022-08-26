package edu.school21.passbot.commands;

import edu.school21.passbot.commandsfactory.Command;
import edu.school21.passbot.models.Order;
import edu.school21.passbot.models.User;
import edu.school21.passbot.service.OrderService;
import edu.school21.passbot.service.UserService;
import lombok.Getter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.Collections;
import java.util.List;

@Component
@Scope("prototype")
public class ListRequestsCommand extends Command {
    @Getter
    private final String name = "/list";
    @Getter
    private final String name2 = "Мои заявки";
    private final UserService userService;
    private final OrderService orderService;

    public ListRequestsCommand(UserService userService, OrderService orderService) {
        this.userService = userService;
        this.orderService = orderService;
    }

    @Override
    public void onCreate() {
        User user = userService.getByChatId(chatId);
        if (user == null) {
            setError("Представьтесь, чтобы выполнить эту команду /start");
            return;
        }
    }

    @Override
    public List<SendMessage> execute() {
        SendMessage response = new SendMessage();
        response.setChatId(chatId);

        User user = userService.getByChatId(chatId);
        List<Order> orders = orderService.getAllByUserId(user);
        if (orders.size() == 0) {
            response.setText("Сейчас активных заявок нет");
            return Collections.singletonList(response);
        }

        StringBuilder stringBuilder = new StringBuilder();
        for (Order order : orders) {
            stringBuilder.append(order.toMarkdownPrettyString()).append("\n");
        }

        response.setText(stringBuilder.toString());
        response.enableMarkdown(true);
        return Collections.singletonList(response);
    }
}
