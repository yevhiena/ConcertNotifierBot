package com.alevel.concertnotifierbot.service.replycommand;

import com.alevel.concertnotifierbot.exceptions.DataNotFoundException;
import com.alevel.concertnotifierbot.service.dataoperations.UserServiceImpl;
import com.alevel.concertnotifierbot.util.Emoji;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.api.methods.send.SendMessage;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AddEventReplyServiceTest {
    private UserServiceImpl userService;
    private AddEventReplyService addEventReplyService;

    private static String defaultMessage = Emoji.EXCLAMATION + "Please input data which matches the following pattern:\n\n" +
            "Artist name \nyyyy-mm-dd \nPrice \nPlace \nLink to website";

    private static String errorMessage = "Sorry, only admin can add concerts";
    private static final long chatId = 1L;

    @BeforeEach
    void setUp() {
        userService = mock(UserServiceImpl.class);
        addEventReplyService = new AddEventReplyService(userService);
    }

    @Test
    void testReturnReplyOnSuccess() throws DataNotFoundException {

        when(userService.isAdmin(chatId)).thenReturn(true);

        SendMessage sendMessage = addEventReplyService.getReply(chatId);

        assertThat(sendMessage.getChatId()).isEqualTo(String.valueOf(chatId));
        assertThat(sendMessage.getText()).isEqualTo(defaultMessage);
        verify(userService, timeout(1)).isAdmin(chatId);
    }

    @Test
    void testReturnReplyIfUserIsNotAdmin() throws DataNotFoundException {

        when(userService.isAdmin(chatId)).thenReturn(false);

        SendMessage sendMessage = addEventReplyService.getReply(chatId);

        assertThat(sendMessage.getChatId()).isEqualTo(String.valueOf(chatId));
        assertThat(sendMessage.getText()).isEqualTo(errorMessage);
        verify(userService, timeout(1)).isAdmin(chatId);
    }

    @Test
    void testReturnReplyIfUserNotFound() throws DataNotFoundException {

        when(userService.isAdmin(chatId)).thenThrow(DataNotFoundException.class);

        SendMessage sendMessage = addEventReplyService.getReply(chatId);

        assertThat(sendMessage.getChatId()).isEqualTo(String.valueOf(chatId));
        assertThat(sendMessage.getText()).isEqualTo(errorMessage);
        verify(userService).isAdmin(chatId);
    }
}
