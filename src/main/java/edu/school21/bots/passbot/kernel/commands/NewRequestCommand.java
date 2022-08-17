package edu.school21.bots.passbot.kernel.commands;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewRequestCommand extends Command {
    private static final Integer ARGS_COUNT = 4;
    private Integer currentStep;
    private final List<String> arguments = new ArrayList<>();
    private final Map<Integer, String> prompts = new HashMap<>();

    public NewRequestCommand(Long chatId) {
        super(chatId);
        currentStep = 0;
        prompts.put(0, "Введите фамилию гостя");
        prompts.put(1, "Введите имя гостя");
        prompts.put(2, "Введите отчество гостя");
        prompts.put(3, "Введите дату в формате ДД.ММ.ГГГГ");
    }
    @Override
    public SendMessage execute() {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(super.getChatId());
        // Здесь нужно передать команду и аргументы дальше, на контроллер
        sendMessage.setText("You command have been created with this arguments:" + arguments);
        return sendMessage;
    }

    @Override
    public void addArgument(String text) {
        arguments.add(text);
    }

    @Override
    public boolean isReady() {
        return arguments.size() == ARGS_COUNT;
    }

    @Override
    public SendMessage getNextPrompt() {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(super.getChatId());
        sendMessage.setText(prompts.get(currentStep));
        currentStep++;
        return sendMessage;
    }
}
