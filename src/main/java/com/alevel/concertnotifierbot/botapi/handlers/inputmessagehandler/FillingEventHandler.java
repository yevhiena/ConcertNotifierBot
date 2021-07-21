package com.alevel.concertnotifierbot.botapi.handlers.inputmessagehandler;

import com.alevel.concertnotifierbot.botapi.BotState;
import com.alevel.concertnotifierbot.botapi.handlers.InputMessageHandler;
import com.alevel.concertnotifierbot.service.addconcert.AddEventService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;

@Component
public class FillingEventHandler implements InputMessageHandler {
    private AddEventService addEventService;

    public FillingEventHandler(AddEventService addEventService) {
        this.addEventService = addEventService;
    }


    @Override
    public SendMessage handle(Message message) {
        return processUsersInput(message);
    }

    @Override
    public BotState getHandlerName() {
        return BotState.FILLING_EVENT;
    }

    private SendMessage processUsersInput(Message inputMsg){
        long chatId = inputMsg.getChatId();
        String text = inputMsg.getText();
        return addEventService.addEvent(chatId, text);
    }
}
