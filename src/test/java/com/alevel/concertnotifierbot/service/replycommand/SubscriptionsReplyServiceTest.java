package com.alevel.concertnotifierbot.service.replycommand;

import com.alevel.concertnotifierbot.model.dto.ConcertDto;
import com.alevel.concertnotifierbot.service.dataoperations.ConcertServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.api.methods.send.SendMessage;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;


public class SubscriptionsReplyServiceTest {
    private ConcertServiceImpl concertService;
    private SubscriptionsReplyService subscriptionsReplyService;
    private static final long id = 1L;

    @BeforeEach
    void setUp() {
        concertService = mock(ConcertServiceImpl.class);
        subscriptionsReplyService = new SubscriptionsReplyService(concertService);
    }

    @Test
    void testGetSubscriptionsReply() {

        List<ConcertDto> concerts = new ArrayList<>();
        ConcertDto concertDto = new ConcertDto(1, "artist 1", new Date(), "price", "place", "url");
        concerts.add(concertDto);

        when(concertService.findAllUsersConcerts(id)).thenReturn(concerts);

        List<SendMessage> messages = subscriptionsReplyService.getMultipleReply(id);

        verify(concertService, times(1)).findAllUsersConcerts(id);
        verifyNoMoreInteractions(concertService);

        assertThat(messages.size()).isEqualTo(1);
        SendMessage sendMessage = messages.get(0);
        assertThat(sendMessage.getChatId()).isEqualTo(String.valueOf(id));
        assertThat(sendMessage.getReplyMarkup()).isNotNull();

        String text = sendMessage.getText();

        assertThat(text).isNotEmpty().isNotBlank();
        assertThat(text.contains(concertDto.getArtist())
                && text.contains(concertDto.getDate().toString())
                && text.contains(concertDto.getPrice())
                && text.contains(concertDto.getAddress())
                && text.contains(concertDto.getSourceUrl())).isTrue();
    }

    @Test
    void testGetMultipleSubscriptionsReply() {
        List<ConcertDto> concerts = new ArrayList<>();
        concerts.add(new ConcertDto(1, "artist 1", new Date(), "price", "place", "url"));
        concerts.add(new ConcertDto(2, "artist 2", new Date(), "price", "place", "url"));

        when(concertService.findAllUsersConcerts(id)).thenReturn(concerts);

        List<SendMessage> messages = subscriptionsReplyService.getMultipleReply(id);

        verify(concertService, times(1)).findAllUsersConcerts(id);
        verifyNoMoreInteractions(concertService);

        assertThat(messages.size()).isEqualTo(2);

        for (SendMessage sendMessage : messages) {
            assertThat(sendMessage.getChatId()).isEqualTo(String.valueOf(id));
            assertThat(sendMessage.getReplyMarkup()).isNotNull();
            String text = sendMessage.getText();
            assertThat(text).isNotEmpty().isNotBlank();
        }
    }
}