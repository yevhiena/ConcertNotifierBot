package com.alevel.concertnotifierbot.notification;

import com.alevel.concertnotifierbot.botapi.ConcertNotifierBot;
import com.alevel.concertnotifierbot.model.dto.ConcertDto;
import com.alevel.concertnotifierbot.service.dataoperations.UserServiceImpl;
import com.alevel.concertnotifierbot.service.notification.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class NotificationServiceTest {

    private UserServiceImpl userService;
    private ConcertNotifierBot concertNotifierBot;
    private NotificationService notificationService;

    private static final long chatId = 1L;

    @BeforeEach
    void setUp() {
        userService = mock(UserServiceImpl.class);
        concertNotifierBot = mock(ConcertNotifierBot.class);
        notificationService = new NotificationService(userService, concertNotifierBot);
    }

    @Test
    void testShouldSendNotification(){
        Map<Long, List<ConcertDto>> data = new HashMap<>();
        List<ConcertDto> concertDtos = new ArrayList<>();
        concertDtos.add(new ConcertDto(1, "artist", new Date(), "price", "place", "url"));
        data.put(chatId, concertDtos);

        when(userService.getUsersConcertsForNotification(any())).thenReturn(data);
        doNothing().when(concertNotifierBot).send(any());

        notificationService.sendNotification();
        verify(userService).getUsersConcertsForNotification(any());
        verify(concertNotifierBot, times(1)).send(any());

    }

    @Test
    void testShouldDoNothingWhenNoRelevantConcertsPresent(){
        Map<Long, List<ConcertDto>> data = new HashMap<>();

        when(userService.getUsersConcertsForNotification(any())).thenReturn(data);
        doNothing().when(concertNotifierBot).send(any());

        notificationService.sendNotification();
        verify(userService).getUsersConcertsForNotification(any());
        verifyNoInteractions(concertNotifierBot);
    }


}
