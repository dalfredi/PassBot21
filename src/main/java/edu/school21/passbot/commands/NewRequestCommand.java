package edu.school21.passbot.commands;

import edu.school21.passbot.commandsfactory.Command;
import edu.school21.passbot.utils.Validators;
import edu.school21.passbot.models.Order;
import edu.school21.passbot.models.User;
import edu.school21.passbot.utils.ParseUtils;
import edu.school21.passbot.service.OrderService;
import edu.school21.passbot.service.UserService;
import edu.school21.passbot.telegramview.Renderer;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        initArgument(
                "Чтобы создать новую заявку, введите фамилию гостя",
                Validators::isCorrectName,
                "Вы ввели некорректную фамилию! Попробуйте ещё раз"
        );
        initArgument(
                "Теперь введите имя гостя",
                Validators::isCorrectName,
                "Вы ввели некрректное имя! Попробуйте ещё раз"
        );
        initArgument(
                "Введите отчество гостя",
                Validators::isCorrectName,
                "Вы ввели некорректное отчество! Попробуйте ещё раз"
        );
        initArgument(
                "Введите дату в формате ДД.MM.ГГГГ",
                Validators::isCorrectDate,
                "Неверная дата, попробуйте ещё раз"
        );
    }

    @Override
    public void init() {
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

    @Override
    public List<SendMessage> execute() {
        LocalDate localDate = ParseUtils.parseDate(arguments.get(3));
        Order order = orderService.createOrder(
                chatId,
                arguments.get(0),
                arguments.get(1),
                arguments.get(2),
                localDate
        );
        return Stream.concat(
                Renderer.plainMessage(chatId, "Заявка успешно создана!\n").stream(),
                Renderer.toUserOrderCards(chatId, Collections.singletonList(order)).stream()
        ).collect(Collectors.toList());
    }
}
