package edu.school21.bots.passbot.basicui.commands;

import edu.school21.bots.passbot.basicui.commands.meta.CommandWithArguments;
import edu.school21.bots.passbot.kernel.service.UserService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Scope("prototype")
@Setter
@Getter
public class RegisterCommand implements CommandWithArguments {
    private final String name = "/register";
    private final Integer maxArgs = 3;
    private final Map<Integer, String> prompts = new HashMap<>();
    Long chatId;
    private Integer currentStep;
    private List<String> arguments = new ArrayList<>();

    private final UserService userService;

    public RegisterCommand(UserService userService) {
        prompts.put(0, "Введите вашу фамилию");
        prompts.put(1, "Введите ваше имя");
        prompts.put(2, "Введите ваше отчество");
        this.userService = userService;
    }

    @Override
    public SendMessage execute() {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("Вы успешно зарегистрированы!");

        userService.createUser(
                chatId,
                arguments.get(0), arguments.get(1), arguments.get(2));

        return sendMessage;
    }
}
