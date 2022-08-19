package edu.school21.passbot.prototype.basicui;

import edu.school21.passbot.prototype.gateway.commandsfactory.Command;
import edu.school21.passbot.prototype.gateway.commandsfactory.CommandWithArguments;
import edu.school21.passbot.prototype.kernel.models.Order;
import edu.school21.passbot.prototype.kernel.models.User;
import edu.school21.passbot.prototype.kernel.service.OrderService;
import edu.school21.passbot.prototype.kernel.service.UserService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Scope("prototype")
@Setter
@Getter
public class NewRequestCommand implements CommandWithArguments {

    private final String name = "/new";
    private final String name2 = "Новая заявка";
    private final Integer maxArgs = 4;
    private final Map<Integer, String> prompts = new HashMap<>();
    private Long chatId;
    private Integer currentStep;
    private List<String> arguments = new ArrayList<>();
    private final OrderService orderService;
    private final UserService userService;
    private boolean error;
    private String responseText;

    public NewRequestCommand(OrderService orderService, UserService userService) {
        this.userService = userService;
        this.orderService = orderService;
        prompts.put(0, "Чтобы создать новую заявку, введите фамилию гостя");
        prompts.put(1, "Теперь введите имя гостя");
        prompts.put(2, "Введите отчество гостя");
        prompts.put(3, "Введите дату в формате ДД.ММ.ГГГГ");
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
            responseText = "Сначала вам нужно представиться /start";
            return;
        }
        if (!user.getRegistered()) {
            error = true;
            responseText = "Сначала вам нужно зарегистрироваться /register";
            return;
        }
        CommandWithArguments.super.init();
    }

    public void addArgument(String argument) {
        if (currentStep == 4) {
            try {
                LocalDate.parse(argument, DateTimeFormatter.ofPattern("dd.MM.yyyy"));
            } catch (DateTimeParseException e) {
                error = true;
                responseText = argument + " - неправильная дата, попробуйте ещё раз";
                return;
            }
        }
        CommandWithArguments.super.addArgument(argument);
    }

    @Override
    public SendMessage execute() {
        SendMessage response = new SendMessage();
        response.setChatId(chatId);

        LocalDate localDate;
        try {
            localDate = LocalDate.parse(arguments.get(3), DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        } catch (DateTimeParseException e) {
            response.setText("Вы неправильно ввели дату, создайте заявку заново");
            return response;
        }
        Order order = orderService.createOrder(
                chatId,
                arguments.get(0),
                arguments.get(1),
                arguments.get(2),
                localDate
        );
        response.setText("Заявка успешно создана со следующими данными! \n" +
                "Твой логин: " +
                order.getPeer().getLogin() + "\n" +
                "Твои ФИО: " +
                order.getPeer().getSurname() + " " +
                order.getPeer().getName() + " " +
                order.getPeer().getPatronymic() + "\n" +
                "ФИО Гостя: " +
                order.getGuest().getSurname() + " " +
                order.getGuest().getName() + " " +
                order.getGuest().getPatronymic() + "\n" +
                "Дата посещения: " +
                order.getStartTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
        );
        return response;
    }
}
