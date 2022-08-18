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

    default boolean isReady() {
        return true;
    }

    default SendMessage getNextPrompt() {
        return null;
    }
}
