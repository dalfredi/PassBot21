package edu.school21.passbot.callbacks;

import java.io.Serializable;
import java.util.List;
import java.util.function.Consumer;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

public interface CallbackQueryHandler {
    List<SendMessage> handle(CallbackQuery query,
                             Consumer<BotApiMethod<Serializable>> sender);
}
