package edu.school21.passbot.prototype.admin;

import edu.school21.passbot.prototype.gateway.bot.PassBot;
import edu.school21.passbot.prototype.gateway.commandsfactory.Command;
import edu.school21.passbot.prototype.kernel.models.Order;
import edu.school21.passbot.prototype.kernel.models.User;
import edu.school21.passbot.prototype.kernel.service.OrderService;
import edu.school21.passbot.prototype.kernel.service.UserService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

@Component
@Scope("prototype")
public class ListRequestsAdminCommand implements Command {
    @Getter
    private final String name = "/listall";
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

        List<Order> orders = orderService.getAllActive();
        if (orders.size() == 0) {
            response.setText("Сейчас активных заявок нет");
            return response;
        }

//        StringBuilder builder = new StringBuilder();
        for (Order order : orders) {
            response = new SendMessage();
            response.setChatId(chatId);
            response.enableMarkdown(true);
            response.setReplyMarkup(getInlineKeyboard(order.getId()));

            User peer = order.getPeer();
            User guest = order.getGuest();
            response.setText(String.format(
                            "*Заявка №%d*\n" +
                            "Логин: %s\n" +
                            "ФИО пира: %s %s %s\n" +
                            "ФИО гостя: %s %s %s\n" +
                            "Дата посещения: %s\n" +
                            "Статус: %s\n\n",
               order.getId(),
               peer.getLogin(),
               peer.getSurname(), peer.getName(), peer.getPatronymic(),
               guest.getSurname(), guest.getName(), guest.getPatronymic(),
               order.getStartTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")),
               order.getStatus()
            ));
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
