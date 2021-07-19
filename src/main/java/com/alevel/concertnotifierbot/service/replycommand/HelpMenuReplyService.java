package com.alevel.concertnotifierbot.service.replycommand;


import com.alevel.concertnotifierbot.util.Emoji;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.api.methods.send.SendMessage;

@Service
public class HelpMenuReplyService implements ReplyCommandService {

    @Override
    public SendMessage getReply(long chatId) {
        String defaultMessage = Emoji.QUESTION + "*What can this bot do?*" + Emoji.QUESTION + "\n\n" +
                "This bot allows you to get information about upcoming concerts of your fav artists." +
                "It gets the top artists from your Spotify account\n\n" +
                "/start - login Spotify\n" +
                "/help - show bot description\n" +
                "/events - show all upcoming events\n" +
                "/subscriptions - show your events";
        return new SendMessage(chatId, defaultMessage).enableMarkdown(true);
    }
}
