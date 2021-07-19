package com.alevel.concertnotifierbot.service.replycallbackquery;

import org.telegram.telegrambots.api.methods.BotApiMethod;
import org.telegram.telegrambots.api.objects.CallbackQuery;

public interface ReplyCallbackQuery {
    BotApiMethod<?> reply(long chatId, int messageId, String callbackData);
}
