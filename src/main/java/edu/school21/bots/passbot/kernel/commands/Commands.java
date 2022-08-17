package edu.school21.bots.passbot.kernel.commands;

import lombok.SneakyThrows;

import java.util.HashMap;
import java.util.Map;

public class Commands {
    public static final String HELP_TEXT = "Этот бот поможет вам провести гостей в кампус Школы 21\n" +
            "/start - запустить бота\n" +
            "/new - создать новую заявку на посещение\n" +
            "/list - показать все активные заявки\n" +
            "/help - вывести эту справку";
    private final Map<String, Class<? extends Command>> nameMapping;
    public Commands() {
        nameMapping = new HashMap<>();
        nameMapping.put("/start", StartCommand.class);
        nameMapping.put("/new", NewRequestCommand.class);
        nameMapping.put("/list", ListRequestsCommand.class);
        nameMapping.put("/help", ShowHelpCommand.class);
    }
    @SneakyThrows
    public Command getCommandByName(Long chatId, String name) {
        Class<? extends Command> commandType = nameMapping.get(name);
        if (commandType == null)
            return new NoCommand(chatId);
        return commandType.getConstructor(new Class[]{Long.class}).newInstance(chatId);
    }
}
