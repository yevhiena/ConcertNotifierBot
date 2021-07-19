package com.alevel.concertnotifierbot.service.notification;

import com.alevel.concertnotifierbot.botapi.ConcertNotifierBot;
import com.alevel.concertnotifierbot.model.dto.ConcertDto;
import com.alevel.concertnotifierbot.service.dataoperations.UserService;
import com.alevel.concertnotifierbot.util.Emoji;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.api.methods.send.SendMessage;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class NotificationService {
    private UserService userService;
    private ConcertNotifierBot concertNotifierBot;
    private static final int daysBeforeConcert = 2;

    public NotificationService(UserService userService, @Lazy ConcertNotifierBot concertNotifierBot) {
        this.userService = userService;
        this.concertNotifierBot = concertNotifierBot;
    }

    @Scheduled(fixedRateString = "${notifications.processPeriod}")
    public void sendNotification(){
        Date currentDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        calendar.add(Calendar.DATE, daysBeforeConcert);
        Date concertDate = calendar.getTime();

        Map<Long, List<ConcertDto>> data = userService.getUsersConcertsForNotification(concertDate);

        for (Map.Entry<Long, List<ConcertDto>> pair: data.entrySet())
        {
            for (ConcertDto c: pair.getValue()) {
                SendMessage notification = new SendMessage(pair.getKey(), makeNotificationMessage(c))
                        .enableNotification()
                        .enableMarkdown(true);
                concertNotifierBot.send(notification);
            }
        }
    }


    private String makeNotificationMessage(ConcertDto concertDto){
        return  Emoji.NOTIFICATION_BELL + "NOTIFICATION"+ Emoji.SPEAKER + "\n"+
                "Don't miss the concert of *" + concertDto.getArtist() + "* tomorrow!\n" +
                Emoji.MAP + " Place: " + concertDto.getAddress() + "\n" +
                Emoji.LINK + " Url: [visit site]("+ concertDto.getSourceUrl() + ")";
    }
}
