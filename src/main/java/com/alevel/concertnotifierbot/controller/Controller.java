package com.alevel.concertnotifierbot.controller;


import com.alevel.concertnotifierbot.botapi.ConcertNotifierBot;
import com.alevel.concertnotifierbot.exceptions.ApiAccessException;
import com.alevel.concertnotifierbot.service.musicapi.SpotifyApiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import org.telegram.telegrambots.api.methods.BotApiMethod;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;


@Slf4j
@RestController
public class Controller implements ErrorController {

    private ConcertNotifierBot telegramBot;
    private SpotifyApiService spotifyApiService;

    public Controller(ConcertNotifierBot telegramBot, SpotifyApiService spotifyApiService) {
        this.telegramBot = telegramBot;
        this.spotifyApiService = spotifyApiService;
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public BotApiMethod<?> onUpdateReceived(@RequestBody Update update) {
       return telegramBot.onWebhookUpdateReceived(update);
    }


    @GetMapping(value = "/api/get_user_code")
    public RedirectView getUserCodeFromSpotify(@RequestParam("code") String userCode, @RequestParam("state") String state) {
        log.info("Successfully got user code:{} from Spotify API for user:{}", userCode, state);
           try {
               spotifyApiService.getSpotifyUserCode(userCode, state);
               telegramBot.send(new SendMessage(state, "Successfully logged in Spotify"));
           } catch (ApiAccessException e) {
               telegramBot.send(new SendMessage(state, "Access denied"));
           }
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl("https://t.me/");
        return redirectView;
    }

    @RequestMapping(value = "/error")
    public RedirectView getErrorFromSpotify(@RequestParam("error") String error, @RequestParam("state") String state) {
        log.error("Cannot get user code from Spotify API for user:" + state + ". " + error);
        telegramBot.send(new SendMessage(state, "Access denied"));
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl("https://t.me/");
        return redirectView;
    }

}
