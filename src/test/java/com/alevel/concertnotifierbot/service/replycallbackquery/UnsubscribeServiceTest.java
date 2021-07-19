package com.alevel.concertnotifierbot.service.replycallbackquery;

import com.alevel.concertnotifierbot.exceptions.DataNotFoundException;
import com.alevel.concertnotifierbot.service.dataoperations.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.api.methods.BotApiMethod;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class UnsubscribeServiceTest {

    private UserServiceImpl userService;
    private UnsubscribeService unsubscribeService;

    private static final String callbackData = "Unsubscribe 1";
    private static final long chatId = 1L;
    private static final int messageId = 1;

    @BeforeEach
    void setUp() {
        userService = mock(UserServiceImpl.class);
        unsubscribeService = new UnsubscribeService(userService);
    }

    @Test
    void testShouldEditReplyMarkup() throws DataNotFoundException {
        doNothing().when(userService).updateUserDeleteConcert(chatId, 1);

        BotApiMethod<?> method = unsubscribeService.reply(chatId, messageId, callbackData);
        verify(userService, times(1)).updateUserDeleteConcert(chatId, 1);

        assertThat(method.getMethod()).isEqualTo("editmessagereplymarkup");
    }

    @Test
    void testGetMessageWhenDataNotFoundExceptionIsCaught() throws DataNotFoundException {
        doThrow(new DataNotFoundException("")).when(userService).updateUserDeleteConcert(chatId, 1);

        BotApiMethod<?> method = unsubscribeService.reply(chatId, messageId, callbackData);
        verify(userService, times(1)).updateUserDeleteConcert(chatId, 1);

        assertThat(method.getMethod()).isEqualTo("editmessagetext");
    }
}
