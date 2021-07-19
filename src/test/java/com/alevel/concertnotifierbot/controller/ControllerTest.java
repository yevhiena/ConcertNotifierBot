package com.alevel.concertnotifierbot.controller;

import com.alevel.concertnotifierbot.botapi.ConcertNotifierBot;
import com.alevel.concertnotifierbot.service.musicapi.SpotifyApiService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.telegram.telegrambots.api.methods.BotApiMethod;
import org.telegram.telegrambots.api.methods.send.SendMessage;


import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


public class ControllerTest {

    private MockMvc mvc;

    private ConcertNotifierBot telegramBot;
    private SpotifyApiService spotifyApiService;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        telegramBot = mock(ConcertNotifierBot.class);
        spotifyApiService = mock(SpotifyApiService.class);
        mvc = MockMvcBuilders
                .standaloneSetup(new Controller(telegramBot, spotifyApiService))
                .build();
    }

    @Test
    public void testShouldReturnBotApiMethod() throws Exception {
        String updateJSON = "{\"update_id\":618658711,\"message\":{\"chatId\":328453545,\"groupMessage\":false," +
                "\"userMessage\":true,\"channelMessage\":false,\"superGroupMessage\":false,\"command\":false," +
                "\"reply\":false,\"message_id\":191,\"from\":{\"bot\":false,\"id\":328453545,\"first_name\":\"Евгения\"," +
                "\"is_bot\":false,\"last_name\":\"Гончаренко\",\"username\":\"Evgenia_Gonch\",\"language_code\":\"en\"}," +
                "\"date\":1626613204,\"chat\":{\"groupChat\":false,\"userChat\":true,\"channelChat\":false," +
                "\"superGroupChat\":false,\"id\":328453545,\"type\":\"private\",\"first_name\":\"Евгения\"," +
                "\"last_name\":\"Гончаренко\",\"username\":\"Evgenia_Gonch\"},\"text\":\"Unknown command\"}}";

        BotApiMethod botApiMethod = new SendMessage(328453545L, "Unknown command.");
        when(telegramBot.onWebhookUpdateReceived(any())).thenReturn(botApiMethod);

        String expectedJSON = objectMapper.writeValueAsString(botApiMethod);
        mvc.perform(post("/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateJSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJSON));

        verify(telegramBot, times(1)).onWebhookUpdateReceived(any());
    }

    @Test
    public void testGetUserCodeFromSpotify() throws Exception {

        doNothing().when(spotifyApiService).getSpotifyUserCode("NApCCgBkWtQ", "profile");

        mvc.perform(get("/api/get_user_code")
                .param("code", "NApCCgBkWtQ")
                .param("state", "profile"))
                .andExpect(redirectedUrl("https://t.me/"));

        verify(spotifyApiService, times(1)).getSpotifyUserCode("NApCCgBkWtQ", "profile");
    }

    @Test
    public void testErrorFromSpotify() throws Exception {
        doNothing().when(telegramBot).send(any());
        mvc.perform(get("/error")
                .param("error", "access_denied")
                .param("state", "profile"))
                .andExpect(redirectedUrl("https://t.me/"));

        verify(telegramBot, times(1)).send(any());
    }

}