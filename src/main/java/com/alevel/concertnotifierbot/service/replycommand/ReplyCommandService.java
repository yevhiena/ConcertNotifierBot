package com.alevel.concertnotifierbot.service.replycommand;

import org.telegram.telegrambots.api.methods.send.SendMessage;

public interface ReplyCommandService {

    SendMessage getReply(long chatId);
}
