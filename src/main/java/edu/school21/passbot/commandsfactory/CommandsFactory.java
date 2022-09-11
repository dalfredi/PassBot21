package edu.school21.passbot.commandsfactory;

import edu.school21.passbot.commands.NoCommand;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.stereotype.Component;

@Component
public class CommandsFactory {
    public static final String HELP_TEXT =
        "Этот бот поможет вам провести гостей в кампус Школы 21\n" +
            "/start - запустить бота и авторизоваться\n" +
            "/register - ввести свои ФИО\n" +
            "/new - создать новую заявку\n" +
            "/list - показать все ваши заявки\n" +
            "/list_active - показать все активные заявки (ADM)\n" +
            "/help - показать все доступные команды";
    private final Map<String, Command> commandNameMapping = new HashMap<>();
    private final Map<String, Command> commandNameMapping2 = new HashMap<>();
    private final BeanFactory context;
    private List<Command> commands;

    public CommandsFactory(List<Command> commands, BeanFactory context) {
        for (Command command : commands) {
            commandNameMapping.put(command.getName(), command);
            commandNameMapping2.put(command.getName2(), command);
        }
        this.context = context;
    }

    public Command getCommandByName(Long chatId, String name) {
        Class<? extends Command> classname = null;
        Command obj1 = commandNameMapping.get(name);
        Command obj2 = commandNameMapping2.get(name);
        if (obj1 == null && obj2 == null) {
            classname = NoCommand.class;
        }
        if (obj1 != null) {
            classname = obj1.getClass();
        }
        if (obj2 != null) {
            classname = obj2.getClass();
        }
        Command command = context.getBean(classname);
        command.setChatId(chatId);
        return command;
    }
}
