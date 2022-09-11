package edu.school21.passbot.callbacks;

import edu.school21.passbot.telegramview.Buttons;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Component
public class CallbackQueryFacade {
    private final Map<String, CallbackQueryHandler> handlers;
    @Setter
    private Consumer<BotApiMethod<Serializable>> sender;

    public CallbackQueryFacade(Map<String, CallbackQueryHandler> handlers) {
        this.handlers = handlers;
    }

    public List<SendMessage> handle(CallbackQuery callbackQuery) {
        String data = callbackQuery.getData();
        if (data == null) {
            return null;
        }

        if (data.startsWith(Buttons.Decline.CALLBACK)
            || data.startsWith(Buttons.Approve.CALLBACK)) {
            return handlers.get("orderStatusCallbackHandler")
                .handle(callbackQuery, sender);
        }
        return null;
    }
}
