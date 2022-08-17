package edu.school21.bots.passbot.basicui.commands;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StartCommand extends Command {
    private static final Integer ARGS_COUNT = 1;
    private Integer currentStep;
    private final List<String> arguments = new ArrayList<>();
    private final Map<Integer, String> prompts = new HashMap<>();

    public StartCommand(Long chatId) {
        super(chatId);
        currentStep = 0;
        prompts.put(0, "Введи свой ник в Школе21");
    }

    @Override
    public SendMessage execute() {
        SendMessage response = new SendMessage();
        response.setChatId(super.getChatId());
        response.setText("Вы успешно вошли");
        return response;
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
