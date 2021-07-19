package com.alevel.concertnotifierbot.service.replycommand;

import com.alevel.concertnotifierbot.util.Emoji;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.telegram.telegrambots.api.methods.send.SendMessage;

import static org.assertj.core.api.Assertions.assertThat;

public class HelpMenuReplyServiceTest {

    private static String defaultMessage = Emoji.QUESTION + "*What can this bot do?*" + Emoji.QUESTION + "\n\n" +
            "This bot allows you to get information about upcoming concerts of your fav artists." +
            "It gets the top artists from your Spotify account\n\n" +
            "/start - login Spotify\n" +
            "/help - show bot description\n" +
            "/events - show all upcoming events\n" +
            "/subscriptions - show your events";


    @ParameterizedTest
    @ValueSource(longs = {1L, 2L, 3L})
    void testHelpMenuReply(long chatId){
        ReplyCommandService replyCommandService = new HelpMenuReplyService();
        SendMessage sendMessage = replyCommandService.getReply(chatId);

        assertThat(sendMessage.getChatId()).isEqualTo(String.valueOf(chatId));
        assertThat(sendMessage.getText()).isEqualTo(defaultMessage);
    }
}
