package edu.school21.bots.passbot.basicui.commands.meta;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public interface Command {
    String getName();
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
    };

    default SendMessage getNextPrompt() {
        return null;
    }
}
