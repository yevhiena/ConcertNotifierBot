package com.alevel.concertnotifierbot.botapi.handlers;

import com.alevel.concertnotifierbot.botapi.BotState;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;


public interface InputMessageHandler {
    SendMessage handle(Message message);

    BotState getHandlerName();
}
