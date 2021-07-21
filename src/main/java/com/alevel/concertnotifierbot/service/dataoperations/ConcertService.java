package com.alevel.concertnotifierbot.service.dataoperations;

import com.alevel.concertnotifierbot.model.dto.ConcertDto;

import java.util.List;

public interface ConcertService {
    List<ConcertDto> findConcertsByArtist(String name);

    List<ConcertDto> findAllUsersConcerts(long id);

    boolean save(ConcertDto concertDto);
}
