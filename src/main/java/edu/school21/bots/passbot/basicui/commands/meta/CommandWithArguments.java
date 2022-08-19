package edu.school21.bots.passbot.basicui.commands.meta;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface CommandWithArguments extends Command {

    default void init() {
        setCurrentStep(0);
        setArguments(new ArrayList<>());
    }

    default void addArgument(String argument) {
        getArguments().add(argument);
    }
    default boolean isReady() {
        return getArguments().size() == getMaxArgs();
    }

    default SendMessage getNextPrompt() {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(getChatId());
        sendMessage.setText(getPrompts().get(getCurrentStep()));
        setCurrentStep(getCurrentStep() + 1);
        return sendMessage;
    }

    Integer getMaxArgs();
    void setCurrentStep(Integer currentStep);
    Integer getCurrentStep();
    void setArguments(List<String> arguments);
    List<String> getArguments();
    Map<Integer, String> getPrompts();

}
