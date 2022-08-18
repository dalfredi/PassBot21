package edu.school21.bots.passbot.basicui.commands;

import edu.school21.bots.passbot.basicui.commands.meta.Command;
import edu.school21.bots.passbot.basicui.commands.meta.CommandWithArguments;
import edu.school21.bots.passbot.dal.models.User;
import edu.school21.bots.passbot.kernel.service.ApiService;
import edu.school21.bots.passbot.kernel.service.UserService;
import edu.school21.bots.passbot.mail.NotificationService;
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
public class StartCommand implements CommandWithArguments, Command {
    private final NotificationService notificationService;
    private final ApiService apiService;
    private final String name = "/start";
    private final Integer maxArgs = 4;
    private final Map<Integer, String> prompts = new HashMap<>();
    private Long chatId;
    private Integer currentStep;
    private List<String> arguments = new ArrayList<>();
    private final UserService userService;

    public StartCommand(UserService userService,
                        NotificationService notificationService,
                        ApiService apiService) {
        this.userService = userService;
        this.notificationService = notificationService;
        this.apiService = apiService;
        prompts.put(0, "Привет! Давай познакомимся. Введи свой ник в Школе21");
        prompts.put(1, "Теперь введи свою фамилию");
        prompts.put(2, "Введи своё имя");
        prompts.put(3, "Введи своё отчество");
    }

    @Override
    public SendMessage execute() {
        SendMessage response = new SendMessage();
        response.setChatId(chatId);

        User user = null;
        try {
            user = apiService.requestAccessToken(arguments.get(0));
            System.out.println(user);
            userService.saveUser(user);
            notificationService.sendEmail();
        } catch (Exception e) {
            e.getMessage();
        }
        if (user == null) {
            response.setText("Такого пользователя нет в Интре! Попробуй ещё.");
            return response;
        }
        user.setChatId(chatId);
        user.setLogin(arguments.get(0));
        user.setSurname(arguments.get(1));
        user.setName(arguments.get(2));
        user.setPatronymic(arguments.get(3));
        userService.saveUser(user);

        response.setText("Отлично! Вы успешно вошли со следующими данными:\n" +
                "логин в Интре: " + user.getLogin() + "\n" +
                "ФИО: " + user.getSurname() + " " + user.getName() + " " + user.getPatronymic() + "\n");
        return response;
    }
}
