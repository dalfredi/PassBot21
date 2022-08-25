package edu.school21.passbot.admin;

import edu.school21.passbot.bot.PassBot;
import edu.school21.passbot.commandsfactory.Command;
import edu.school21.passbot.models.Order;
import edu.school21.passbot.models.User;
import edu.school21.passbot.service.OrderService;
import edu.school21.passbot.service.UserService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.Collections;
import java.util.List;

@Component
@Scope("prototype")
public class ListRequestsAdminCommand implements Command {
    @Getter
    private final String name = "/listall";

    @Getter
    private final String name2 = "Все заявки";
    @Setter
    @Getter
    private Long chatId;
    private final UserService userService;
    private final OrderService orderService;
    @Setter
    private PassBot passBot;
    private boolean error;
    @Getter
    private String responseText;

    public ListRequestsAdminCommand(UserService userService, OrderService orderService) {
        this.userService = userService;
        this.orderService = orderService;
    }

    @Override
    public boolean isError() {
        return error;
    }

    @Override
    public void init() {
        User user = userService.getByChatId(chatId);
        if (user == null) {
            error = true;
            responseText = "Представьтесь, чтобы выполнить эту команду /start";
            return;
        }
        if (!user.getRole().equals("ADMIN")) {
            error = true;
            responseText = "Это команда только для сотрудников Школы 21";
            return;
        }
        Command.super.init();
    }

    @Override
    public SendMessage execute() {
        SendMessage response = new SendMessage();
        response.setChatId(chatId);

        User admin = userService.getByChatId(chatId);

        List<Order> orders = orderService.getAllActive();
        if (orders.size() == 0) {
            response.setText("Сейчас активных заявок нет");
            return response;
        }

        for (Order order : orders) {
            response = new SendMessage();
            response.setChatId(chatId);
            response.enableMarkdown(true);
            response.setReplyMarkup(getInlineKeyboard(order.getId()));

            response.setText(order.toMarkdownPrettyString());
            passBot.sendMessage(response);
        }
        return null;
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
