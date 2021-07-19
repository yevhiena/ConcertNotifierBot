package com.alevel.concertnotifierbot.service.replycommand;

import com.alevel.concertnotifierbot.service.musicapi.SpotifyApiService;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Service
public class StartCommandReplyService implements ReplyCommandService {

    private SpotifyApiService spotifyService;

    public StartCommandReplyService(SpotifyApiService spotifyService) {
        this.spotifyService = spotifyService;
    }

    @Override
    public SendMessage getReply(long chatId) {
        SendMessage replyToUser = new SendMessage(chatId, "Tap the button below to log in with your Spotify account");
        replyToUser.setReplyMarkup(getLoginButton(String.valueOf(chatId)));
        return replyToUser;
    }

    private InlineKeyboardMarkup getLoginButton(String chatId) {
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        String url = spotifyService.login(chatId);
        rowInline.add(new InlineKeyboardButton().setText("Login Spotify").setUrl(url));
        rowsInline.add(rowInline);
        markupInline.setKeyboard(rowsInline);

        return markupInline;
    }
}
