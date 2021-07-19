package com.alevel.concertnotifierbot.service.replycommand;


import com.alevel.concertnotifierbot.botapi.QueryType;
import com.alevel.concertnotifierbot.exceptions.ApiAccessException;
import com.alevel.concertnotifierbot.exceptions.DataNotFoundException;
import com.alevel.concertnotifierbot.model.dto.ConcertDto;
import com.alevel.concertnotifierbot.service.musicapi.MusicApiService;
import com.alevel.concertnotifierbot.service.dataoperations.ConcertService;
import com.alevel.concertnotifierbot.util.Emoji;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class UpcomingEventsReplyService implements MultipleReplyCommandService {
    private MusicApiService artistsService;
    private ConcertService concertService;

    public UpcomingEventsReplyService(MusicApiService artistsService, ConcertService concertService) {
        this.artistsService = artistsService;
        this.concertService = concertService;
    }

    @Override
    public List<SendMessage> getMultipleReply(long chatId) {
        return getUpcomingEvents(chatId);
    }


    public List<SendMessage> getUpcomingEvents(long chatId){
        List<SendMessage> response = new ArrayList<>();
        try {
            List<String> artists = artistsService.getUsersTopArtists(chatId);
            if(artists.isEmpty()){
                response.add(new SendMessage(chatId, "You don't have top artists statistic on Spotify yet"));
                return response;
            }
            List<ConcertDto> concertDtos = new ArrayList<>();
            for (String artist : artists) {
                concertDtos.addAll(concertService.findConcertsByArtist(artist));
            }
            for (ConcertDto concert : concertDtos) {
                SendMessage reply = new SendMessage();
                reply.setChatId(chatId);
                reply.setText(this.makeMessageWithConcert(concert));
                reply.enableMarkdown(true);
                reply.setReplyMarkup(this.getSubscribeButton(concert.getId()));
                response.add(reply);

            }
            log.info("Successfully got upcoming events for user with chatId: {}", chatId);
            return response;
        } catch (ApiAccessException e) {
            response.add(new SendMessage(chatId, "Sorry, something wrong happened"));
            return response;
        }catch (DataNotFoundException e){
            response.add(new SendMessage(chatId, "Sorry, login Spotify first"));
            return response;
        }
    }


    private String makeMessageWithConcert(ConcertDto concertDto){
        return  Emoji.PAPERCLIP + "*" + concertDto.getArtist() + "*\n" +
                Emoji.DATE +  " Date: " + concertDto.getDate() + "\n" +
                Emoji.MONEY + " Price: " + concertDto.getPrice() + "\n" +
                Emoji.MAP + " Place: " + concertDto.getAddress() + "\n" +
                Emoji.LINK + " Url: [visit site]("+ concertDto.getSourceUrl() + ")";
    }

    private InlineKeyboardMarkup getSubscribeButton(long concertId) {
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        rowInline.add(new InlineKeyboardButton().setText("Subscribe").setCallbackData(QueryType.SUBSCRIBE + " " + concertId));
        rowsInline.add(rowInline);
        markupInline.setKeyboard(rowsInline);
        return markupInline;
    }
}
