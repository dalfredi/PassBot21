package edu.school21.passbot.prototype.basicui;

import edu.school21.passbot.prototype.gateway.factory.Command;
import edu.school21.passbot.prototype.gateway.factory.CommandWithArguments;
import edu.school21.passbot.prototype.kernel.models.User;
import edu.school21.passbot.prototype.kernel.service.ApiService;
import edu.school21.passbot.prototype.kernel.service.UserService;
import edu.school21.passbot.prototype.kernel.service.NotificationService;
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
    private final Integer maxArgs = 1;
    private final Map<Integer, String> prompts = new HashMap<>();
    private boolean error;
    private Long chatId;
    private Integer currentStep;
    private List<String> arguments = new ArrayList<>();
    private final UserService userService;
    private String responseText;

    public StartCommand(UserService userService,
                        NotificationService notificationService,
                        ApiService apiService) {
        this.userService = userService;
        this.notificationService = notificationService;
        this.apiService = apiService;
        prompts.put(0, "Привет! Давай познакомимся. Введи свой ник в Школе21");
    }

    @Override
    public boolean isError() {
        return error;
    }

    @Override
    public void init() {
        User user = userService.getByChatId(chatId);
        if (user != null) {
            error = true;
            responseText = "Вы уже авторизованы как " + user.getLogin() + "!\n" +
                    "Введите свои ФИО /register или создайте новую заявку /new";
            return;
        }
        CommandWithArguments.super.init();
    }

    @Override
    public SendMessage execute() {
        SendMessage response = new SendMessage();
        response.setChatId(chatId);

        if (error) {
            response.setText(responseText);
            return response;
        }
        User user = null;
        try {
            user = apiService.requestAccessToken(arguments.get(0));
            System.out.println(user);
            notificationService.sendEmail();
            //terminate
        } catch (Exception e) {
            e.getMessage();
        }
        if (user == null || user.getLogin() == null) {
            response.setText("Такого пользователя нет в Интре! Попробуй ещё раз /start.");
            return response;
        }

        user.setChatId(chatId);
        user.setLogin(arguments.get(0));
        user.setRegistered(false);

        userService.saveUser(user);

        response.setText("Отлично! Вы успешно представились:\n" +
                "Ваш логин: " + user.getLogin() + "\n" +
                "Ваша роль: " + user.getRole() + "\n"
        );
        return response;
    }
}
