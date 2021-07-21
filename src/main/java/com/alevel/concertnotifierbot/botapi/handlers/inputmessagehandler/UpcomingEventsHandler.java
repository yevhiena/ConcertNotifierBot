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
public class UpcomingEventsHandler implements InputMessageHandler {
    private MultipleReplyCommandService multipleReplyService;
    private ConcertNotifierBot concertNotifierBot;

    public UpcomingEventsHandler(@Qualifier("upcomingEventsReplyService") MultipleReplyCommandService multipleReplyService, @Lazy ConcertNotifierBot concertNotifierBot) {
        this.multipleReplyService = multipleReplyService;
        this.concertNotifierBot = concertNotifierBot;
    }

    @Override
    public SendMessage handle(Message message) {
        return processUsersInput(message);
    }

    @Override
    public BotState getHandlerName() {
        return BotState.SHOW_FUTURE_EVENTS;
    }

    private SendMessage processUsersInput(Message inputMsg){
        long chatId = inputMsg.getChatId();
        List<SendMessage> messages = multipleReplyService.getMultipleReply(chatId);
        if(messages.size() == 1) return messages.get(0);
        for (SendMessage mes : messages) {
            concertNotifierBot.send(mes);
        }
        return new SendMessage(chatId, "Your top artist's concerts are listed above");
    }
}
