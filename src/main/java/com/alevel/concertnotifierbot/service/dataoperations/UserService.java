package com.alevel.concertnotifierbot.service.dataoperations;

import com.alevel.concertnotifierbot.exceptions.DataNotFoundException;
import com.alevel.concertnotifierbot.model.dto.ConcertDto;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface UserService {
   String getUsersToken(long chatId) throws DataNotFoundException;
   boolean isExist(long chatId);
   boolean isAdmin(long chatId) throws DataNotFoundException;
   long saveUser(long chatId, String token);
   void updateUserAddConcert(long chatId, long concertId) throws DataNotFoundException;
   void updateUserDeleteConcert(long chatId, long concertId) throws DataNotFoundException;
   Map<Long, List<ConcertDto>> getUsersConcertsForNotification(Date dateOfConcert);
}
