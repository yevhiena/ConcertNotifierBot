package com.alevel.concertnotifierbot.service.addconcert;


import com.alevel.concertnotifierbot.model.dto.ConcertDto;
import com.alevel.concertnotifierbot.service.dataoperations.ConcertService;
import com.alevel.concertnotifierbot.service.dataoperations.ConcertServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
@Service
public class AddEventService {

    private ConcertService concertService;

    public AddEventService(ConcertServiceImpl concertService) {
        this.concertService = concertService;
    }


    public SendMessage addEvent(long chatId, String text) {
        ConcertDto concertDto = new ConcertDto();

        String[] partsOfData = text.split("\n");

        if(partsOfData.length!=5) {
            log.info("Cannot add concert by chatId: {}. Invalid input with text:{}", chatId, text);
            return new SendMessage(chatId, "Invalid input. Check example above");
        }

        try {
            concertDto.setArtist(partsOfData[0]);
            concertDto.setDate(parseDate(partsOfData[1]));
            concertDto.setPrice(partsOfData[2]);
            concertDto.setAddress(partsOfData[3]);
            concertDto.setSourceUrl(partsOfData[4]);
        } catch (ParseException e) {
            log.error("Cannot add concert. Invalid date", e);
            return new SendMessage(chatId, "Invalid input. Invalid date");
        }

        boolean isSaved = concertService.save(concertDto);

        if(isSaved) {
            log.info("Add concert with artist:{} and date:{} by chatId: {}.",
                    concertDto.getArtist(), concertDto.getDate(), chatId);
            return new SendMessage(chatId, "Successfully added!");
        }
        else {
            log.info("Attempt to add concert with artist:{} and date:{} by chatId: {}. Already present in db",
                    concertDto.getArtist(), concertDto.getDate(), chatId);
            return new SendMessage(chatId, "Already present in database");
        }
    }

    private Date parseDate(String date) throws ParseException {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            df.setLenient(false);
        return df.parse(date);

    }

}
