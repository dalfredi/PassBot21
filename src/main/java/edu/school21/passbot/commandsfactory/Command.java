package edu.school21.passbot.commandsfactory;

import lombok.NoArgsConstructor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//@Component
//@Scope("prototype")
@NoArgsConstructor
public abstract class Command {
    @NotNull
    protected Long chatId;
    protected Integer maxArgs = 0;
    protected final Map<Integer, String> arguments = new HashMap<>();
    protected final Map<Integer, String> prompts = new HashMap<>();
    protected Integer currentStep = 0;
    private boolean error = false;
    private String errorText;

    public Command(Long chatId) {
        this.chatId = chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public Long getChatId() {
        return chatId;
    }

    public void onCreate() {
    }

    public void addArgument(String text) {
        arguments.put(currentStep, text);
        currentStep++;
    }

    public SendMessage checkArgument(String argument) {
        return null;
    }

    public boolean isReady() {
        return maxArgs.equals(currentStep);
    }

    public boolean isError() {
        return error;
    }

    public String getResponseText() {
        return "";
    }

    public SendMessage getNextPrompt() {
        SendMessage response = new SendMessage();
        response.setChatId(chatId);
        response.setText(prompts.get(currentStep));
        return response;
    }

    public void setError(String errorText) {
        error = true;
        this.errorText = errorText;
    }

    abstract public List<SendMessage> execute();
    abstract public String getName();

    abstract public String getName2();
}
