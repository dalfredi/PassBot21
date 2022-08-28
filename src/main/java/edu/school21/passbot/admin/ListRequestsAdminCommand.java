package edu.school21.passbot.admin;

import edu.school21.passbot.commandsfactory.Command;
import edu.school21.passbot.models.Order;
import edu.school21.passbot.models.User;
import edu.school21.passbot.service.OrderService;
import edu.school21.passbot.service.UserService;
import edu.school21.passbot.telegramview.Renderer;
import lombok.Getter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.List;

@Component
@Scope("prototype")
public class ListRequestsAdminCommand extends Command {
    @Getter
    private final String name = "/list_active";
    @Getter
    private final String name2 = "Все заявки";
    private final UserService userService;
    private final OrderService orderService;

    public ListRequestsAdminCommand(UserService userService, OrderService orderService) {
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
        if (!user.getRole().equals("ADMIN")) {
            setError("Это команда только для сотрудников Школы 21");
            return;
        }
    }

    @Override
    public List<SendMessage> execute() {
        List<Order> orders = orderService.getAllActive();

        if (orders.size() == 0) {
            return Renderer.plainMessage(chatId, "Сейчас активных заявок нет");
        }
        return Renderer.toAdminOrderCards(chatId, orders);
    }

}
