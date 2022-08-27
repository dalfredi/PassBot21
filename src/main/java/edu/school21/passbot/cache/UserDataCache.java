package edu.school21.passbot.cache;

import edu.school21.passbot.commandsfactory.Command;

public interface UserDataCache {
    void setCommand(Long userId, Command command);
    Command getCommand(Long chatId);

    void clearCommand(Long chatId);
}
