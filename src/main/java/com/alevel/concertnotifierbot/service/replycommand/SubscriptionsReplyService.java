package com.alevel.concertnotifierbot.service.replycommand;

import com.alevel.concertnotifierbot.botapi.QueryType;
import com.alevel.concertnotifierbot.model.dto.ConcertDto;
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
public class SubscriptionsReplyService implements MultipleReplyCommandService {

    private ConcertService concertService;

    public SubscriptionsReplyService(ConcertService concertService) {
        this.concertService = concertService;
    }

    @Override
    public List<SendMessage> getMultipleReply(long chatId) {
        return getSubscriptions(chatId);
    }

    private List<SendMessage> getSubscriptions(long chatId){
        List<SendMessage> response = new ArrayList<>();

        List<ConcertDto> concertDtos = new ArrayList<>(concertService.findAllUsersConcerts(chatId));
        for (ConcertDto concert : concertDtos) {
            SendMessage reply = new SendMessage();
            reply.setChatId(chatId);
            reply.setText(this.makeMessageWithConcert(concert));
            reply.enableMarkdown(true);
            reply.setReplyMarkup(this.getRemoveButton(concert.getId()));
            response.add(reply);
        }
        log.info("Successfully got concerts for user with chatId: {}", chatId);
        return response;
    }

    private String makeMessageWithConcert(ConcertDto concertDto){
        return  Emoji.PIN + "*" + concertDto.getArtist() + "*\n" +
                Emoji.DATE +  " Date: " + concertDto.getDate() + "\n" +
                Emoji.MONEY + " Price: " + concertDto.getPrice() + "\n" +
                Emoji.MAP + " Place: " + concertDto.getAddress() + "\n" +
                Emoji.LINK + " Url: [visit site]("+ concertDto.getSourceUrl() + ")";
    }

    private InlineKeyboardMarkup getRemoveButton(long concertId) {
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        rowInline.add(new InlineKeyboardButton().setText("Remove " + Emoji.UNSUBSCRIBE).setCallbackData(QueryType.REMOVE + " " + concertId));
        rowsInline.add(rowInline);
        markupInline.setKeyboard(rowsInline);
        return markupInline;
    }
}
