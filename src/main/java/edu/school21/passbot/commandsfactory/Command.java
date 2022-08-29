package edu.school21.passbot.commandsfactory;

import edu.school21.passbot.telegramview.Renderer;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

@NoArgsConstructor
public abstract class Command {
    protected final Map<Integer, String> arguments = new HashMap<>();
    protected final Map<Integer, String> prompts = new HashMap<>();
    protected final Map<Integer, Predicate<String>> validators = new HashMap<>();
    protected final Map<Integer, String> errorMessages = new HashMap<>();
    @NotNull
    @Setter
    @Getter
    protected Long chatId;
    protected Integer currentStep = 0;
    protected Integer maxArgs = 0;
    private boolean error = false;
    @Getter
    private List<SendMessage> errorMessage;

    public Command(Long chatId) {
        this.chatId = chatId;
    }

    /**
     * This block of code runs right after creation of your command class instance
     * and before command starts getting any argument from user.
     * You can override this method to do some pre-execute checks.
     * For example, you can check if user with given chatId has rights to execute your command
     * and setError if not.*/
    public void init() {
    }

    /**
     * Initialization of command argument.
     * Should be called in constructor or init() method,
     * before the command starts accepting input from user.
     * @param prompt the message prompting the user to enter an argument
     * @param validator a Predicate which checks a user input string
     * @param errorMessage the message which user gets if validation of argument fails*/
    protected void initArgument(
            String prompt, Predicate<String> validator, String errorMessage) {
        prompts.put(maxArgs, prompt);
        validators.put(maxArgs, validator);
        errorMessages.put(maxArgs, errorMessage);
        maxArgs++;
    }

    public void addArgument(String text) {
        arguments.put(currentStep, text);
        currentStep++;
    }

    public void validateArgument(@NotNull String argument) {
        error = false;
        Predicate<String> validator = validators.get(currentStep);
        if (validator == null)
            return;
        if (!validator.test(argument)) {
            setError(errorMessages.get(currentStep));
        }
    }

    public boolean isReady() {
        return maxArgs.equals(currentStep);
    }

    public boolean isError() {
        return error;
    }

    protected void setError(@NotNull String errorText) {
        error = true;
        this.errorMessage = Renderer.plainMessage(chatId, errorText);
    }

    public List<SendMessage> getNextPrompt() {
        return Renderer.plainMessage(chatId, prompts.get(currentStep));
    }

    /**
     * Main logic of command.
     * In this method you should place the code which will be executed when command gets all
     * its arguments from user
     * Method should produce the list of SendMessages which will be sent to user
     * as a response of executing the command
     * @return messages which user gets after executing the command*/
    abstract public List<SendMessage> execute();
    /**
     * Here you should set up a name of you command.
     * For example /start or /help */
    abstract public String getName();

    /**
     * You can also add a second alias to you command to use it with ReplyKeyboard.*/
    abstract public String getName2();
}
