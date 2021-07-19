package com.alevel.concertnotifierbot.cache;


import com.alevel.concertnotifierbot.botapi.BotState;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class UserDataCache{
    private final Map<Long, BotState> usersBotStates = new ConcurrentHashMap<>();

    public void setUsersCurrentBotState(long chatId, BotState botState) {
        usersBotStates.put(chatId, botState);
    }

    public BotState getUsersCurrentBotState(long chatId) {
        BotState botState = usersBotStates.get(chatId);
        if (botState == null) {
            botState = BotState.LOGIN_SPOTIFY;
        }

        return botState;
    }
}
