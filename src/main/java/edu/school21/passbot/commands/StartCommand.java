package edu.school21.passbot.commands;

import edu.school21.passbot.commandsfactory.Command;
import edu.school21.passbot.models.User;
import edu.school21.passbot.service.IntraApiService;
import edu.school21.passbot.service.UserService;
import edu.school21.passbot.telegramview.Renderer;
import edu.school21.passbot.telegramview.ReplyKeyboardMarkupCustom;
import java.io.IOException;
import java.util.List;
import lombok.Getter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Component
@Scope("prototype")
public class StartCommand extends Command {
    @Getter
    private final String name = "/start";
    @Getter
    private final String name2 = "/start";
    private final IntraApiService intraApiService;
    private final UserService userService;


    public StartCommand(UserService userService,
                        IntraApiService intraApiService) {
        this.userService = userService;
        this.intraApiService = intraApiService;
        initArgument("Привет! Давай познакомимся. Введи свой ник в Школе21",
            null,
            ""
        );
    }

    @Override
    public void init() {
        User user = userService.getByChatId(chatId);
        if (user != null) {
            setError("Вы уже авторизованы как " + user.getLogin() + "!\n" +
                "Введите свои ФИО /register или создайте новую заявку /new");
        }
    }

    @Override
    public List<SendMessage> execute() {
        User user = null;
        try {
            user = intraApiService.requestAccessToken(arguments.get(0));
        } catch (IOException | HttpClientErrorException e) {
            e.getMessage();
        }
        if (user == null || user.getLogin() == null) {
            return Renderer.plainMessage(chatId,
                "Такого пользователя нет в Интре! Попробуй ещё раз /start. " +
                    "Чтобы протестировать полный функционал бота с правами администратора, введи ник at");
        }
        user.setChatId(chatId);
        user.setLogin(arguments.get(0));
        user.setRegistered(false);

        List<SendMessage> response = Renderer.plainMessage(chatId,
            "Отлично! Вы успешно представились:\n" +
                "Ваш логин: " + user.getLogin() + "\n" +
                "Ваша роль: " + user.getRole() + "\n"
        );

        ReplyKeyboardMarkupCustom keyboard = new ReplyKeyboardMarkupCustom();
        keyboard.setButtons(response.get(0), user.getRole());

        userService.saveUser(user);

        return response;
    }
}
