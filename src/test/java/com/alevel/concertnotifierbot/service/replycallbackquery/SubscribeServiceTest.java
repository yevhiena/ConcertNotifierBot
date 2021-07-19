package com.alevel.concertnotifierbot.service.replycallbackquery;

import com.alevel.concertnotifierbot.exceptions.DataNotFoundException;
import com.alevel.concertnotifierbot.service.dataoperations.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.api.methods.BotApiMethod;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class SubscribeServiceTest {

    private UserServiceImpl userService;
    private SubscribeService subscribeService;

    private static final String callbackData = "Subscribe 1";
    private static final long chatId = 1L;
    private static final int messageId = 1;

    @BeforeEach
    void setUp() {
        userService = mock(UserServiceImpl.class);
        subscribeService = new SubscribeService(userService);
    }

    @Test
    void testShouldEditReplyMarkup() throws DataNotFoundException {
        doNothing().when(userService).updateUserAddConcert(chatId, 1);

        BotApiMethod<?> method = subscribeService.reply(chatId, messageId, callbackData);
        verify(userService, times(1)).updateUserAddConcert(chatId, 1);

        assertThat(method.getMethod()).isEqualTo("editmessagereplymarkup");
    }

    @Test
    void testGetMessageWhenDataNotFoundExceptionIsCaught() throws DataNotFoundException {
        doThrow(new DataNotFoundException("")).when(userService).updateUserAddConcert(chatId, 1);

        BotApiMethod<?> method = subscribeService.reply(chatId, messageId, callbackData);
        verify(userService, times(1)).updateUserAddConcert(chatId, 1);

        assertThat(method.getMethod()).isEqualTo("editmessagetext");
    }

}