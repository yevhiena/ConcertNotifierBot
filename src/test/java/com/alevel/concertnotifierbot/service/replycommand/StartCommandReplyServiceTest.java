package com.alevel.concertnotifierbot.service.replycommand;

import com.alevel.concertnotifierbot.service.musicapi.SpotifyApiService;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboard;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class StartCommandReplyServiceTest {

    private static String messageText = "Tap the button below to log in with your Spotify account";

    @ParameterizedTest
    @ValueSource(longs = {1L, 2L, 3L})
    void testStartCommandReply(long chatId){

        SpotifyApiService spotifyApiService = mock(SpotifyApiService.class);
        when(spotifyApiService.login(String.valueOf(chatId))).thenReturn("url");

        StartCommandReplyService startCommandReplyService = new StartCommandReplyService(spotifyApiService);

        SendMessage sendMessage = startCommandReplyService.getReply(chatId);

        assertThat(sendMessage.getChatId()).isEqualTo(String.valueOf(chatId));
        assertThat(sendMessage.getText()).isEqualTo(messageText);

        ReplyKeyboard replyKeyboard = sendMessage.getReplyMarkup();
        assertThat(replyKeyboard).isNotNull();

        verify(spotifyApiService, times(1)).login(String.valueOf(chatId));
        verifyNoMoreInteractions(spotifyApiService);
    }
}
