package com.alevel.concertnotifierbot.botapi.handlers.inputmessagehandler;

import com.alevel.concertnotifierbot.botapi.BotState;
import com.alevel.concertnotifierbot.botapi.handlers.InputMessageHandler;
import com.alevel.concertnotifierbot.service.replycommand.ReplyCommandService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;


@Slf4j
@Component
public class LoginHandler implements InputMessageHandler {
    private ReplyCommandService replyService;

    public LoginHandler(@Qualifier("startCommandReplyService") ReplyCommandService replyService) {
        this.replyService = replyService;
    }


    @Override
    public SendMessage handle(Message message) {
        return processUsersInput(message);
    }

    @Override
    public BotState getHandlerName() {
        return BotState.LOGIN_SPOTIFY;
    }

    private SendMessage processUsersInput(Message inputMsg){
        long chatId = inputMsg.getChatId();

        return replyService.getReply(chatId);

    }

}