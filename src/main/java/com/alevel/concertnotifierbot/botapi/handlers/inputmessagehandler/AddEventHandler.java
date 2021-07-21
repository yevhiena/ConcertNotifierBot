package com.alevel.concertnotifierbot.botapi.handlers.inputmessagehandler;

import com.alevel.concertnotifierbot.botapi.BotState;
import com.alevel.concertnotifierbot.botapi.handlers.InputMessageHandler;
import com.alevel.concertnotifierbot.service.replycommand.ReplyCommandService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;


@Component
public class AddEventHandler implements InputMessageHandler{
    private ReplyCommandService replyCommandService;

    public AddEventHandler(@Qualifier("addEventReplyService") ReplyCommandService replyCommandService) {
        this.replyCommandService = replyCommandService;
    }

    @Override
    public SendMessage handle(Message message) {
        return processUsersInput(message);
    }

    @Override
    public BotState getHandlerName() {
        return BotState.ADD_EVENT;
    }

    private SendMessage processUsersInput(Message inputMsg){
        long chatId = inputMsg.getChatId();
        return replyCommandService.getReply(chatId);
    }

}
