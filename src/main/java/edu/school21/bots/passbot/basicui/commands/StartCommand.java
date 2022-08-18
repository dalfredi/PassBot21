package edu.school21.bots.passbot.basicui.commands;

import edu.school21.bots.passbot.basicui.commands.meta.SimpleCommand;
import edu.school21.bots.passbot.basicui.commands.meta.CommandWithArguments;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Setter
@Getter
public class StartCommand implements CommandWithArguments, SimpleCommand {
    private final String name = "/start";
    private final Integer maxArgs = 1;
    private final Map<Integer, String> prompts = new HashMap<>();
    private Long chatId;
    private Integer currentStep;
    private List<String> arguments = new ArrayList<>();

    public StartCommand() {
        prompts.put(0, "Введи свой ник в Школе21");
    }

    @Override
    public SendMessage execute() {
        SendMessage response = new SendMessage();
        response.setChatId(chatId);
        response.setText("Вы успешно вошли!");
        return response;
    }
}
