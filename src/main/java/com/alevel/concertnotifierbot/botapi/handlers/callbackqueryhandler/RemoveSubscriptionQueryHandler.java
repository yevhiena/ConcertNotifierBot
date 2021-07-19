package com.alevel.concertnotifierbot.botapi.handlers.callbackqueryhandler;

import com.alevel.concertnotifierbot.botapi.QueryType;
import com.alevel.concertnotifierbot.botapi.handlers.CallbackQueryHandler;
import com.alevel.concertnotifierbot.service.replycallbackquery.ReplyCallbackQuery;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.api.methods.BotApiMethod;
import org.telegram.telegrambots.api.objects.CallbackQuery;



@Component
public class RemoveSubscriptionQueryHandler implements CallbackQueryHandler {
    private ReplyCallbackQuery replyCallbackQuery;

    public RemoveSubscriptionQueryHandler(@Qualifier("removeSubscriptionService") ReplyCallbackQuery replyCallbackQuery) {
        this.replyCallbackQuery = replyCallbackQuery;
    }


    @Override
    public BotApiMethod<?> handle(CallbackQuery callbackQuery) {
        return processCallback(callbackQuery);
    }

    @Override
    public QueryType getHandlerName() {
        return QueryType.REMOVE;
    }

    private BotApiMethod<?> processCallback(CallbackQuery callbackQuery){
        String callbackData = callbackQuery.getData();
        int messageId = callbackQuery.getMessage().getMessageId();
        long chatId = callbackQuery.getMessage().getChatId();
        return replyCallbackQuery.reply(chatId, messageId, callbackData);
    }


}
