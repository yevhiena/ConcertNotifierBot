package com.alevel.concertnotifierbot.botapi.handlers;

import com.alevel.concertnotifierbot.botapi.QueryType;
import org.telegram.telegrambots.api.methods.BotApiMethod;
import org.telegram.telegrambots.api.objects.CallbackQuery;

public interface CallbackQueryHandler {
    BotApiMethod<?> handle(CallbackQuery callbackQuery);

    QueryType getHandlerName();
}
