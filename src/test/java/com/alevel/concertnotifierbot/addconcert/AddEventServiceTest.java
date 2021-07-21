package com.alevel.concertnotifierbot.addconcert;

import com.alevel.concertnotifierbot.model.dto.ConcertDto;
import com.alevel.concertnotifierbot.service.addconcert.AddEventService;
import com.alevel.concertnotifierbot.service.dataoperations.ConcertServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.api.methods.send.SendMessage;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AddEventServiceTest {
    private ConcertServiceImpl concertService;
    private AddEventService addEventService;

    private static String defaultMessage = "Successfully added!";
    private static String alreadyInDB = "Already present in database";
    private static String invalidDate = "Invalid input. Invalid date";
    private static String invalidInput = "Invalid input. Check example above";
    private static final long chatId = 1L;

    @BeforeEach
    void setUp() {
        concertService = mock(ConcertServiceImpl.class);
        addEventService = new AddEventService(concertService);
    }

    @Test
    void testReturnReplyOnSuccess() throws ParseException {
        String inputText = "Artist\n2021-10-10\nPrice\nPlace\nlink";
        ConcertDto concertDto = new ConcertDto();
        concertDto.setArtist("Artist");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        concertDto.setDate(df.parse("2021-10-10"));
        concertDto.setPrice("Price");
        concertDto.setAddress("Place");
        concertDto.setSourceUrl("link");

        when(concertService.save(concertDto)).thenReturn(true);

        SendMessage sendMessage = addEventService.addEvent(chatId, inputText);

        assertThat(sendMessage.getChatId()).isEqualTo(String.valueOf(chatId));
        assertThat(sendMessage.getText()).isEqualTo(defaultMessage);
        verify(concertService, timeout(1)).save(concertDto);
    }

    @Test
    void testReplyIfConcertIsAlreadyInDb() throws ParseException {
        String inputText = "Artist\n2021-10-10\nPrice\nPlace\nlink";
        ConcertDto concertDto = new ConcertDto();
        concertDto.setArtist("Artist");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        concertDto.setDate(df.parse("2021-10-10"));
        concertDto.setPrice("Price");
        concertDto.setAddress("Place");
        concertDto.setSourceUrl("link");

        when(concertService.save(concertDto)).thenReturn(false);

        SendMessage sendMessage = addEventService.addEvent(chatId, inputText);

        assertThat(sendMessage.getChatId()).isEqualTo(String.valueOf(chatId));
        assertThat(sendMessage.getText()).isEqualTo(alreadyInDB);
        verify(concertService, timeout(1)).save(concertDto);
    }

    @Test
    void testReplyIfInvalidInput() throws ParseException {
        String inputText = "Artist\n2021-10-10\nPrice";

        SendMessage sendMessage = addEventService.addEvent(chatId, inputText);

        assertThat(sendMessage.getChatId()).isEqualTo(String.valueOf(chatId));
        assertThat(sendMessage.getText()).isEqualTo(invalidInput);
        verifyNoInteractions(concertService);
    }

    @Test
    void testReplyIfInvalidDate() throws ParseException {
        String inputText = "Artist\n2021-13-32\nPrice\nPlace\nlink";

        SendMessage sendMessage = addEventService.addEvent(chatId, inputText);
        assertThat(sendMessage.getChatId()).isEqualTo(String.valueOf(chatId));
        assertThat(sendMessage.getText()).isEqualTo(invalidDate);
        verifyNoInteractions(concertService);
    }


}
