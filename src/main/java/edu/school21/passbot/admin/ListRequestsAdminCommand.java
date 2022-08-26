package edu.school21.passbot.admin;

import edu.school21.passbot.commandsfactory.Command;
import edu.school21.passbot.models.Order;
import edu.school21.passbot.models.User;
import edu.school21.passbot.service.OrderService;
import edu.school21.passbot.service.UserService;
import lombok.Getter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.Collections;
import java.util.LinkedList;
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
        User admin = userService.getByChatId(chatId);
        List<Order> orders = orderService.getAllActive();

        if (orders.size() == 0) {
            SendMessage response = new SendMessage();
            response.setChatId(chatId);
            response.setText("Сейчас активных заявок нет");
            return Collections.singletonList(response);
        }

        List<SendMessage> responseList = new LinkedList<>();
        for (Order order : orders) {
            SendMessage response = new SendMessage();
            response.setChatId(chatId);
            response.enableMarkdown(true);
            response.setReplyMarkup(getInlineKeyboard(order.getId()));
            response.setText(order.toMarkdownPrettyString());
            responseList.add(response);
        }
        return responseList;
    }

    private ReplyKeyboard getInlineKeyboard(Long orderId) {
        return InlineKeyboardMarkup.builder()
                .keyboardRow(Collections.singletonList((InlineKeyboardButton.builder()
                        .text("Одобрить")
                        .callbackData("approve_" + orderId)
                        .build())))
                .keyboardRow(Collections.singletonList(InlineKeyboardButton.builder()
                        .text("Отклонить")
                        .callbackData("decline_" + orderId)
                        .build()))
                .build();
    }
}
