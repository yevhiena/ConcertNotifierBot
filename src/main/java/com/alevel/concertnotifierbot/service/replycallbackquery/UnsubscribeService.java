package com.alevel.concertnotifierbot.service.replycallbackquery;

import com.alevel.concertnotifierbot.botapi.QueryType;
import com.alevel.concertnotifierbot.exceptions.DataNotFoundException;
import com.alevel.concertnotifierbot.service.dataoperations.UserService;
import com.alevel.concertnotifierbot.util.ParseCallbackDataUtil;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.api.methods.BotApiMethod;
import org.telegram.telegrambots.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Service
public class UnsubscribeService implements ReplyCallbackQuery{
    private UserService userService;

    public UnsubscribeService(UserService userService) {
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
        return editReplyMarkup(messageId, chatId, concertId);
    }

    private EditMessageReplyMarkup editReplyMarkup(int messageId, long chatId, long concertId){
        EditMessageReplyMarkup editMessageReplyMarkup = new EditMessageReplyMarkup();
        editMessageReplyMarkup.setChatId(chatId).setMessageId(messageId);
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        rowInline.add(new InlineKeyboardButton().setText("Subscribe").setCallbackData(QueryType.SUBSCRIBE + " " + concertId));
        rowsInline.add(rowInline);
        markupInline.setKeyboard(rowsInline);
        editMessageReplyMarkup.setReplyMarkup(markupInline);

        return editMessageReplyMarkup;
    }
}
