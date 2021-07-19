package com.alevel.concertnotifierbot.botapi.handlers.inputmessagehandler;


import com.alevel.concertnotifierbot.botapi.BotState;
import com.alevel.concertnotifierbot.botapi.ConcertNotifierBot;
import com.alevel.concertnotifierbot.botapi.handlers.InputMessageHandler;
import com.alevel.concertnotifierbot.service.replycommand.MultipleReplyCommandService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;

import java.util.List;

@Component
public class SubscriptionsHandler implements InputMessageHandler {
    private MultipleReplyCommandService multipleReplyService;
    public ConcertNotifierBot concertNotifierBot;

    public SubscriptionsHandler(@Qualifier("subscriptionsReplyService") MultipleReplyCommandService multipleReplyService,
                                @Lazy ConcertNotifierBot concertNotifierBot) {
        this.multipleReplyService = multipleReplyService;
        this.concertNotifierBot = concertNotifierBot;
    }

    @Override
    public SendMessage handle(Message message) {
        return processUsersInput(message);
    }

    @Override
    public BotState getHandlerName() {
        return BotState.SHOW_SUBSCRIPTIONS;
    }

    private SendMessage processUsersInput(Message inputMsg){
        long chatId = inputMsg.getChatId();
        List<SendMessage> messages = multipleReplyService.getMultipleReply(chatId);
        if(messages.isEmpty()) return new SendMessage(chatId, "You are not subscribed to any concert yet");
        for (SendMessage mes : messages) {
            concertNotifierBot.send(mes);
        }
        return new SendMessage(chatId, "That's all your concerts");
    }
}
