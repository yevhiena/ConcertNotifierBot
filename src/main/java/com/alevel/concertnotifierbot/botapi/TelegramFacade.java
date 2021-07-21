package com.alevel.concertnotifierbot.botapi;

import com.alevel.concertnotifierbot.cache.UserDataCache;
import com.alevel.concertnotifierbot.util.ParseCallbackDataUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.api.methods.BotApiMethod;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.CallbackQuery;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;


@Slf4j
@Component
public class TelegramFacade {
    private BotStateContext botStateContext;
    private CallbackQueryContext callbackQueryContext;
    private UserDataCache userDataCache;

    public TelegramFacade(BotStateContext botStateContext, CallbackQueryContext callbackQueryContext, UserDataCache userDataCache) {
        this.botStateContext = botStateContext;
        this.callbackQueryContext = callbackQueryContext;
        this.userDataCache = userDataCache;
    }

    public BotApiMethod<?> handleUpdate(Update update) {

        Message message = update.getMessage();
        if (message != null && message.hasText()) {
            log.info("New message from User:{}, chatId: {},  with text: {}",
                    message.getFrom().getUserName(), message.getChatId(), message.getText());
            return handleInputMessage(message);
        } else if(update.hasCallbackQuery()){
            log.info("New message from User:{}, chatId: {},  with callback: {}",
                    update.getCallbackQuery().getFrom().getUserName(),
                    update.getCallbackQuery().getFrom().getId(),
                    update.getCallbackQuery().getData());

            return handleCallback(update.getCallbackQuery());
        }
        return null;
    }

    private SendMessage handleInputMessage(Message message) {
        String inputMsg = message.getText();
        int userId = message.getFrom().getId();
        long chatId = message.getChatId();
        BotState botState;

        switch (inputMsg) {
            case "/start":
                botState = BotState.LOGIN_SPOTIFY;
                break;
            case "/help":
                botState = BotState.SHOW_HELP_MENU;
                break;
            case "/subscriptions":
                botState = BotState.SHOW_SUBSCRIPTIONS;
                break;
            case "/events":
                botState = BotState.SHOW_FUTURE_EVENTS;
                break;
            case "/add_event":
                botState = BotState.ADD_EVENT;
                break;
            default:
                if(userDataCache.getUsersCurrentBotState(userId).equals(BotState.ADD_EVENT))
                    botState = BotState.FILLING_EVENT;
                else return new SendMessage(chatId, "Unknown command.");
        }

        userDataCache.setUsersCurrentBotState(userId, botState);
        return botStateContext.processInputMessage(botState, message);
    }

    private BotApiMethod<?> handleCallback(CallbackQuery callbackQuery){
        String callbackData = ParseCallbackDataUtil.getAction(callbackQuery.getData());
        QueryType queryType;
        switch (callbackData) {
            case "SUBSCRIBE":
                 queryType = QueryType.SUBSCRIBE;
                break;
            case "UNSUBSCRIBE":
                queryType = QueryType.UNSUBSCRIBE;
                break;
            case "REMOVE":
                queryType = QueryType.REMOVE;
                break;
            default:
                return null;
        }
        return callbackQueryContext.processQuery(queryType, callbackQuery);
    }

}
