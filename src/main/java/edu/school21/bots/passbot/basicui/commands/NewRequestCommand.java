package edu.school21.bots.passbot.basicui.commands;

import edu.school21.bots.passbot.basicui.commands.meta.CommandWithArguments;
import edu.school21.bots.passbot.dal.models.Order;
import edu.school21.bots.passbot.kernel.service.OrderService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Setter
@Getter
public class NewRequestCommand implements CommandWithArguments {

    private final String name = "/new";
    private final Integer maxArgs = 4;
    private final Map<Integer, String> prompts = new HashMap<>();
    private Long chatId;
    private Integer currentStep;
    private List<String> arguments = new ArrayList<>();
    private final OrderService orderService;

    public NewRequestCommand(OrderService orderService) {
        prompts.put(0, "Введите фамилию гостя");
        prompts.put(1, "Введите имя гостя");
        prompts.put(2, "Введите отчество гостя");
        prompts.put(3, "Введите дату в формате ДД.ММ.ГГГГ");
        this.orderService = orderService;
    }

    @Override
    public SendMessage execute() {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        // Здесь нужно передать команду и аргументы дальше, на контроллер
        Order order = orderService.createOrder(
                chatId,
                arguments.get(0),
                arguments.get(1),
                arguments.get(2),
                LocalDate.parse(arguments.get(3), DateTimeFormatter.ofPattern("dd.MM.yyyy"))
        );
        sendMessage.setText("Заявка " + order.toString() + " успешно создана!");
        return sendMessage;
    }
}
