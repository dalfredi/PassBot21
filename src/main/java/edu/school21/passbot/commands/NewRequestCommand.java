package edu.school21.passbot.commands;

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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.List;

@Component
@Scope("prototype")
@Setter
@Getter
public class NewRequestCommand extends Command {

    private final String name = "/new";
    private final String name2 = "Новая заявка";
    private final OrderService orderService;
    private final UserService userService;


    public NewRequestCommand(OrderService orderService, UserService userService) {
        this.userService = userService;
        this.orderService = orderService;
        maxArgs = 4;
        prompts.put(0, "Чтобы создать новую заявку, введите фамилию гостя");
        prompts.put(1, "Теперь введите имя гостя");
        prompts.put(2, "Введите отчество гостя");
        prompts.put(3, "Введите дату в формате ДД.ММ.ГГГГ");
    }

    @Override
    public void onCreate() {
        User user = userService.getByChatId(chatId);
        if (user == null) {
            setError("Сначала вам нужно представиться /start");
            return;
        }
        if (!user.getRegistered()) {
            setError("Сначала вам нужно зарегистрироваться /register");
            return;
        }
    }

    public void addArgument(String argument) {
        if (currentStep == 4) {
            try {
                LocalDate.parse(argument, DateTimeFormatter.ofPattern("dd.MM.yyyy"));
            } catch (DateTimeParseException e) {
                setError(" - неправильная дата, попробуйте создать заявку заново командой /new");
                return;
            }
        }
        super.addArgument(argument);
    }

    @Override
    public List<SendMessage> execute() {
        SendMessage response = new SendMessage();
        response.setChatId(chatId);

        LocalDate localDate;
        try {
            localDate = LocalDate.parse(arguments.get(3), DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        } catch (DateTimeParseException e) {
            response.setText("Вы неправильно ввели дату, создайте заявку заново");
            return Collections.singletonList(response);
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
        return Collections.singletonList(response);
    }
}
