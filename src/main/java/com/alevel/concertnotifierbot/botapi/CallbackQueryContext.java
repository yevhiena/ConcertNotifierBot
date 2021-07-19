package com.alevel.concertnotifierbot.botapi;

import com.alevel.concertnotifierbot.botapi.handlers.CallbackQueryHandler;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.api.methods.BotApiMethod;
import org.telegram.telegrambots.api.objects.CallbackQuery;


import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
public class CallbackQueryContext {
    private final Map<QueryType, CallbackQueryHandler> callbackHandlers = new HashMap<>();

    public CallbackQueryContext(List<CallbackQueryHandler> callbackHandlers) {
        callbackHandlers.forEach(handler -> this.callbackHandlers.put(handler.getHandlerName(), handler));
    }

    public BotApiMethod<?> processQuery(QueryType currentType, CallbackQuery callbackQuery) {
        CallbackQueryHandler callbackQueryHandler = findQueryHandler(currentType);
        return callbackQueryHandler.handle(callbackQuery);
    }

    private CallbackQueryHandler findQueryHandler(QueryType currentType) {
        return callbackHandlers.get(currentType);
    }
}
