package com.alevel.concertnotifierbot.service.replycommand;

import com.alevel.concertnotifierbot.exceptions.ApiAccessException;
import com.alevel.concertnotifierbot.exceptions.DataNotFoundException;
import com.alevel.concertnotifierbot.model.dto.ConcertDto;
import com.alevel.concertnotifierbot.service.dataoperations.ConcertServiceImpl;
import com.alevel.concertnotifierbot.service.musicapi.SpotifyApiService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.api.methods.send.SendMessage;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class UpcomingEventsReplyServiceTest {
    private ConcertServiceImpl concertService;
    private SpotifyApiService spotifyApiService;
    private UpcomingEventsReplyService upcomingEventsReplyService;

    private static final String artistName = "artist";
    private static final long chatId = 1L;

    @BeforeEach
    void setUp() {
        concertService = mock(ConcertServiceImpl.class);
        spotifyApiService = mock(SpotifyApiService.class);
        upcomingEventsReplyService = new UpcomingEventsReplyService(spotifyApiService, concertService);
    }


    @Test
    void testGetUpcomingEventsByArtist() throws DataNotFoundException, ApiAccessException {

        List<String> usersTopArtists = new ArrayList<>();
        usersTopArtists.add(artistName);
        usersTopArtists.add(artistName);

        List<ConcertDto> concerts = new ArrayList<>();
        ConcertDto concertDto = new ConcertDto(1, artistName, new Date(), "price", "place", "url");
        concerts.add(concertDto);

        when(spotifyApiService.getUsersTopArtists(chatId)).thenReturn(usersTopArtists);
        when(concertService.findConcertsByArtist(artistName)).thenReturn(concerts);

        List<SendMessage> messages = upcomingEventsReplyService.getUpcomingEvents(chatId);

        verify(spotifyApiService, times(1)).getUsersTopArtists(chatId);
        verifyNoMoreInteractions(spotifyApiService);
        verify(concertService, times(2)).findConcertsByArtist(artistName);
        verifyNoMoreInteractions(concertService);

        assertThat(messages.size()).isEqualTo(2);

        for (SendMessage sendMessage : messages) {

            assertThat(sendMessage.getChatId()).isEqualTo(String.valueOf(chatId));
            assertThat(sendMessage.getReplyMarkup()).isNotNull();

            String text = sendMessage.getText();

            assertThat(text).isNotEmpty().isNotBlank();
        }
    }

    @Test
    void testReplyMessageIfDataNotFoundExceptionIsCaught() throws DataNotFoundException, ApiAccessException {

        when(spotifyApiService.getUsersTopArtists(chatId)).thenThrow(new DataNotFoundException("Data not found"));

        List<SendMessage> messages = upcomingEventsReplyService.getUpcomingEvents(chatId);

        verify(spotifyApiService, times(1)).getUsersTopArtists(chatId);
        verifyNoMoreInteractions(spotifyApiService);
        verifyNoMoreInteractions(concertService);

        assertThat(messages.size()).isEqualTo(1);

        SendMessage sendMessage = messages.get(0);

        assertThat(sendMessage.getChatId()).isEqualTo(String.valueOf(chatId));
        assertThat(sendMessage.getReplyMarkup()).isNull();
        String text = sendMessage.getText();

        assertThat(text).isEqualTo("Sorry, login Spotify first");

    }

    @Test
    void testReplyMessageIfApiAccessExceptionIsCaught() throws DataNotFoundException, ApiAccessException {

        when(spotifyApiService.getUsersTopArtists(chatId)).thenThrow(new ApiAccessException("Unable to get users top artists"));

        List<SendMessage> messages = upcomingEventsReplyService.getUpcomingEvents(chatId);

        verify(spotifyApiService, times(1)).getUsersTopArtists(chatId);
        verifyNoMoreInteractions(spotifyApiService);
        verifyNoMoreInteractions(concertService);

        assertThat(messages.size()).isEqualTo(1);

        SendMessage sendMessage = messages.get(0);

        assertThat(sendMessage.getChatId()).isEqualTo(String.valueOf(chatId));
        assertThat(sendMessage.getReplyMarkup()).isNull();
        String text = sendMessage.getText();

        assertThat(text).isEqualTo("Sorry, something wrong happened");
    }

}
