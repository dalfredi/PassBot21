package edu.school21.passbot.commands;

import edu.school21.passbot.commandsfactory.Command;
import edu.school21.passbot.models.User;
import edu.school21.passbot.service.UserService;
import lombok.Getter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.*;

@Component
@Scope("prototype")
public class RegisterCommand extends Command {
    @Getter
    private final String name = "/register";
    @Getter
    private final String name2 = "Ввести ФИО";
    private final UserService userService;

    public RegisterCommand(UserService userService) {
        this.userService = userService;
        maxArgs = 3;
        prompts.put(0, "Введите вашу фамилию");
        prompts.put(1, "Введите ваше имя");
        prompts.put(2, "Введите ваше отчество");
    }

    @Override
    public void onCreate() {
        User user = userService.getByChatId(chatId);
        if (user == null) {
            setError("Сначала вам нужно представиться: /start");
        }
    }

    @Override
    public List<SendMessage> execute() {
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

        return Collections.singletonList(response);
    }
}
