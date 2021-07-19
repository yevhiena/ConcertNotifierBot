package com.alevel.concertnotifierbot.service.dataoperations;

import com.alevel.concertnotifierbot.model.dto.ConcertDto;
import com.alevel.concertnotifierbot.model.entity.Concert;
import com.alevel.concertnotifierbot.repository.ConcertRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Component
@Transactional
public class ConcertServiceImpl implements ConcertService {

    private ConcertRepository concertRepository;

    public ConcertServiceImpl(ConcertRepository concertRepository) {
        this.concertRepository = concertRepository;
    }

    @Override
    public List<ConcertDto> findConcertsByArtist(String name) {
        List<Concert> concerts = concertRepository.findConcertsByArtist(name);
        return concerts.stream().map(this::convert).collect(Collectors.toList());
    }

    @Override
    public List<ConcertDto> findAllUsersConcerts(long id) {
        List<Concert> concerts = concertRepository.findAllUsersConcerts(id);
        return concerts.stream().map(this::convert).collect(Collectors.toList());
    }

    private ConcertDto convert(Concert concert){
        return new ConcertDto(concert.getId(), concert.getArtist(), concert.getDate(), concert.getUrl(), concert.getPlace(), concert.getPrice());
    }
}
