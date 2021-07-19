package com.alevel.concertnotifierbot.botapi;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.api.methods.BotApiMethod;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;


@Slf4j
@Getter
@Setter
public class ConcertNotifierBot extends TelegramWebhookBot {

    private String botPath;
    private String botUsername;
    private String botToken;
    private TelegramFacade telegramFacade;

    public ConcertNotifierBot(DefaultBotOptions options,  TelegramFacade telegramFacade){
        super(options);
        this.telegramFacade = telegramFacade;
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public String getBotPath() {
        return botPath;
    }


    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        return telegramFacade.handleUpdate(update);
    }

    public void send(SendMessage sendMessage) {
        try {
            execute(sendMessage);
            log.info("Send message to user with chatId:{} and text:{} with inlineKeyboard:{}",
                    sendMessage.getChatId(), sendMessage.getText(), sendMessage.getReplyMarkup());
        } catch (TelegramApiException e) {
            log.error("Cannot send message to user with chatId: " + sendMessage.getChatId() +
                    " and text: " + sendMessage.getText(), e);
        }
    }

}
