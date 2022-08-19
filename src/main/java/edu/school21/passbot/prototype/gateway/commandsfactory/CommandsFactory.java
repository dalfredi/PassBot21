package edu.school21.passbot.prototype.gateway.commandsfactory;

import edu.school21.passbot.prototype.basicui.NoCommand;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CommandsFactory {
    public static final String HELP_TEXT = "Этот бот поможет вам провести гостей в кампус Школы 21\n" +
            "/start - запустить бота\n" +
            "/register - ввести свои ФИО\n" +
            "/new - создать новую заявку\n" +
            "/list - показать все заявки\n" +
            "/help - показать все доступные команды";
    private final Map<String, Command> commandNameMapping = new HashMap<>();
    private final BeanFactory context;
    private List<Command> commands;

    public CommandsFactory(List<Command> commands, BeanFactory context) {
        for (Command command : commands) {
            commandNameMapping.put(command.getName(), command);
        }
        this.context = context;
    }

    public Command getCommandByName(Long chatId, String name) {
        Class<? extends Command> classname;
        if (commandNameMapping.get(name) == null)
            classname = NoCommand.class;
        else
            classname = commandNameMapping.get(name).getClass();
        Command command = context.getBean(classname);
        command.setChatId(chatId);
        return command;
    }
}
