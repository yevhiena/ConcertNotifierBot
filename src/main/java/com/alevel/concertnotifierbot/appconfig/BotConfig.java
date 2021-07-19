package com.alevel.concertnotifierbot.appconfig;

import com.alevel.concertnotifierbot.botapi.ConcertNotifierBot;
import com.alevel.concertnotifierbot.botapi.TelegramFacade;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.ApiContext;
import org.telegram.telegrambots.bots.DefaultBotOptions;



@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "telegrambot")
public class BotConfig {

    private String webHookPath;
    private String userName;
    private String botToken;


    @Bean
    public ConcertNotifierBot ConcertNotifierBot (TelegramFacade telegramFacade) {

        DefaultBotOptions options = ApiContext.getInstance(DefaultBotOptions.class);

        ConcertNotifierBot  concertNotifierBot  = new ConcertNotifierBot (options, telegramFacade );
        concertNotifierBot.setBotUsername(userName);
        concertNotifierBot.setBotToken(botToken);
        concertNotifierBot.setBotPath(webHookPath);

        return concertNotifierBot;
    }


}
