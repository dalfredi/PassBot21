package edu.school21.bots.passbot.kernel.commands;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public class ShowHelpCommand extends Command {

    public ShowHelpCommand(Long chatId) {
        super(chatId);
    }
    @Override
    public SendMessage execute() {
        SendMessage response = new SendMessage();
        response.setChatId(super.getChatId());
        response.setText(Commands.HELP_TEXT);
        return response;
    }
}
