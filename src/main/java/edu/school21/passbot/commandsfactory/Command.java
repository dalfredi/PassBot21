package edu.school21.passbot.commandsfactory;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public interface Command {
    String getName();
    String getName2();
    void setChatId(Long id);
    Long getChatId();
    SendMessage execute();

    default void init() {
    }

    default void addArgument(String text) {}

    default SendMessage checkArgument(String argument) {return null;}

    default boolean isReady() {
        return true;
    }
    default boolean isError() {
        return false;
    }
    default String getResponseText() {
        return "";
    }

    default SendMessage getNextPrompt() {
        return null;
    }
}
