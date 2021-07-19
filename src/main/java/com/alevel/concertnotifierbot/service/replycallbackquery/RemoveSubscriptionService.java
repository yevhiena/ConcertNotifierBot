package com.alevel.concertnotifierbot.service.replycallbackquery;

import com.alevel.concertnotifierbot.exceptions.DataNotFoundException;
import com.alevel.concertnotifierbot.service.dataoperations.UserService;
import com.alevel.concertnotifierbot.util.ParseCallbackDataUtil;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.api.methods.BotApiMethod;
import org.telegram.telegrambots.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.api.methods.updatingmessages.EditMessageText;

@Service
public class RemoveSubscriptionService implements ReplyCallbackQuery{
    private UserService userService;

    public RemoveSubscriptionService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public BotApiMethod<?> reply(long chatId, int messageId, String callbackData) {
        long concertId = ParseCallbackDataUtil.getConcertId(callbackData);
        try {
            userService.updateUserDeleteConcert(chatId, concertId);
        } catch (DataNotFoundException e) {
            return new EditMessageText()
                    .setChatId(chatId)
                    .setMessageId(messageId)
                    .setText("Concert already passed. You were automatically unsubscribed")
                    .setReplyMarkup(null);
        }
        return editReplyMarkup(messageId, chatId);
    }

    private DeleteMessage editReplyMarkup(int messageId, long chatId){
        DeleteMessage deleteMessage = new DeleteMessage();
        deleteMessage.setChatId(String.valueOf(chatId)).setMessageId(messageId);

        return deleteMessage;
    }
}
