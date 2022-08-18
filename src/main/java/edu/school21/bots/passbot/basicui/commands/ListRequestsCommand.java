package edu.school21.bots.passbot.basicui.commands;

import edu.school21.bots.passbot.basicui.commands.meta.SimpleCommand;
import edu.school21.bots.passbot.kernel.service.UserService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Component
public class ListRequestsCommand implements SimpleCommand {
    @Getter
    private final String name = "/list";
    @Setter
    @Getter
    Long chatId;
    private final UserService userService;
    public ListRequestsCommand(UserService userService) {
        this.userService = userService;
    }

//    !TODO list only active
    @Override
    public SendMessage execute() {
        SendMessage response = new SendMessage();
        response.setChatId(chatId);
        response.setText("Здесь должен быть список заявок");
        return response;
    }
}
