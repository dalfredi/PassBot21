package edu.school21.bots.passbot.basicui.commands;

import lombok.Getter;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public abstract class Command {
    @Getter
    private final Long chatId;
    public Command(Long chatId) {
        this.chatId = chatId;
    }
    public abstract SendMessage execute();

    public void addArgument(String text) {}

    public boolean isReady() {
        return true;
    }

    public SendMessage getNextPrompt() {
        return null;
    }
}
