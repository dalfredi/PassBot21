package edu.school21.passbot.commandsfactory;

import edu.school21.passbot.commands.NoCommand;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CommandsFactory {
    public static final String HELP_TEXT = "Этот бот поможет вам провести гостей в кампус Школы 21\n" +
            "/start - запустить бота и ввести ник в Интре\n" +
            "/register - ввести свои ФИО\n" +
            "/new - создать новую заявку\n" +
            "/list - показать все ваши заявки\n" +
            "/listall - показать все активные заявки (доступно только сотрудникам школы)\n" +
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
        Object obj1 = commandNameMapping.get(name);
        Object obj2 = commandNameMapping2.get(name);
        if (obj1 == null && obj2 == null)
            classname = NoCommand.class;
        if (obj1 != null) {
            classname = (Class<? extends Command>) obj1.getClass();
        }
        if (obj2 != null) {
            classname = (Class<? extends Command>) obj2.getClass();
        }
        Command command = context.getBean(classname);
        command.setChatId(chatId);
        return command;
    }
}
