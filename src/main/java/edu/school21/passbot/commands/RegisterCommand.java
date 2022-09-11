package edu.school21.passbot.commands;

import edu.school21.passbot.commandsfactory.Command;
import edu.school21.passbot.models.User;
import edu.school21.passbot.service.UserService;
import edu.school21.passbot.telegramview.Renderer;
import edu.school21.passbot.utils.Validators;
import java.util.List;
import lombok.Getter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

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
        initArgument(
            "Введите свою фамилию",
            Validators::isCorrectName,
            "Вы ввели некорректную фамилию! Попробуйте ещё раз"
        );
        initArgument(
            "Введите своё имя",
            Validators::isCorrectName,
            "Вы ввели некрректное имя! Попробуйте ещё раз"
        );
        initArgument(
            "Введите отчество",
            Validators::isCorrectName,
            "Вы ввели некорректное отчество! Попробуйте ещё раз"
        );
    }

    @Override
    public void init() {
        User user = userService.getByChatId(chatId);
        if (user == null) {
            setError("Сначала вам нужно представиться: /start");
        }
    }

    @Override
    public List<SendMessage> execute() {
        User user = userService.getByChatId(chatId);

        user.setRegistered(true);
        user.setSurname(arguments.get(0));
        user.setName(arguments.get(1));
        user.setPatronymic(arguments.get(2));
        userService.updateUser(user);

        return Renderer.plainMessage(chatId,
            "Отлично! Вы успешно обновили свои данные:\n" +
                "логин: " + user.getLogin() + "\n" +
                "роль: " + user.getRole() + "\n" +
                "ФИО: " + user.getSurname() + " " + user.getName() + " " +
                user.getPatronymic() + "\n");
    }
}
