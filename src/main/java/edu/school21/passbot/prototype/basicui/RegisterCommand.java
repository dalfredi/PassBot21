package edu.school21.passbot.prototype.basicui;

import edu.school21.passbot.prototype.gateway.commandsfactory.CommandWithArguments;
import edu.school21.passbot.prototype.kernel.models.User;
import edu.school21.passbot.prototype.kernel.service.UserService;
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
    private boolean error;
    private String responseText;

    public RegisterCommand(UserService userService) {
        this.userService = userService;
        prompts.put(0, "Введите вашу фамилию");
        prompts.put(1, "Введите ваше имя");
        prompts.put(2, "Введите ваше отчество");
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
            responseText = "Сначала вам представиться: /start";
            return;
        }
        CommandWithArguments.super.init();
    }

//    @SneakyThrows
//    @Override
//    public void addArgument(String argument) {
//        // Perform check of argument number 0 and set responseText if error
//        if (getCurrentStep() - 1 == 0) {
//            User user = userService.getByLogin(argument);
//            if (user != null) {
//                error = true;
//                responseText = "Вы уже зарегистрированы и можете создать новую заявку командой /new";
//                return;
//            }
//            try {
//                user = apiService.requestAccessToken(argument);
//            } catch (Exception e) {
//                e.getMessage();
//            }
//            if (user == null) {
//                error = true;
//                responseText = "Такого пользователя нет в интре, попробуй заново /start";
//                return;
//            }
//        }
//        CommandWithArguments.super.addArgument(argument);
//    }

    @Override
    public SendMessage execute() {
        SendMessage response = new SendMessage();
        response.setChatId(chatId);


        User user = userService.getByChatId(chatId);
        user.setRegistered(true);
        user.setSurname(arguments.get(0));
        user.setName(arguments.get(1));
        user.setPatronymic(arguments.get(2));
        userService.updateUser(user);

        response.setText("Отлично! Вы успешно зарегистрировались со следующими данными:\n" +
                "логин: " + user.getLogin() + "\n" +
                "роль: " + user.getRole() + "\n" +
                "ФИО: " + user.getSurname() + " " + user.getName() + " " + user.getPatronymic() + "\n");

        return response;
    }
}
