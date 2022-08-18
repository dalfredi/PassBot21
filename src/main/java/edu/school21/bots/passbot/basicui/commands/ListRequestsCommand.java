package edu.school21.bots.passbot.basicui.commands;

import edu.school21.bots.passbot.basicui.commands.meta.Command;
import edu.school21.bots.passbot.kernel.service.UserService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Component
@Scope("prototype")
public class ListRequestsCommand implements Command {
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