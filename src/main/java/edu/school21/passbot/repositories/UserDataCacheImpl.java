package edu.school21.passbot.repositories;

import edu.school21.passbot.commandsfactory.Command;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public class UserDataCacheImpl implements UserDataCache {
    private final Map<Long, Command> userCommandMap = new HashMap<>();

    @Override
    public void setCommand(Long userId, Command command) {
        userCommandMap.put(userId, command);
    }

    @Override
    public Command getCommand(Long chatId) {
        return userCommandMap.get(chatId);
    }

    @Override
    public void clearCommand(Long chatId) {
        userCommandMap.remove(chatId);
    }
}
