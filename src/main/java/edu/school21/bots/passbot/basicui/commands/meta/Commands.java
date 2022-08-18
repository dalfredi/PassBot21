package edu.school21.bots.passbot.basicui.commands.meta;

import edu.school21.bots.passbot.basicui.commands.NoCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class Commands {
    public static final String HELP_TEXT = "Этот бот поможет вам провести гостей в кампус Школы 21\n" +
            "/start - запустить бота\n" +
            "/register - ввести своё имя\n" +
            "/new - создать новую заявку на посещение\n" +
            "/list - показать все активные заявки\n" +
            "/help - вывести эту справку";
    private static final Map<String, SimpleCommand> commandNameMapping = new HashMap<>();
    @Autowired
    private List<SimpleCommand> commands;

    @PostConstruct
    private void initCommandNameMapping() {
        for (SimpleCommand command : commands) {
            commandNameMapping.put(command.getName(), command);
        }
    }

    public static SimpleCommand getCommandByName(Long chatId, String name) {
        SimpleCommand command = commandNameMapping.get(name);
        if (command == null) command = new NoCommand();
        command.setChatId(chatId);
        return command;
    }
}
