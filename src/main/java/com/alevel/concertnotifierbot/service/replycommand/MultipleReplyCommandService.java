package com.alevel.concertnotifierbot.service.replycommand;

import org.telegram.telegrambots.api.methods.send.SendMessage;

import java.util.List;

public interface MultipleReplyCommandService {

    List<SendMessage> getMultipleReply(long chatId);
}
