package com.alevel.concertnotifierbot.service.dataoperations;

import com.alevel.concertnotifierbot.model.dto.ConcertDto;
import com.alevel.concertnotifierbot.model.entity.Concert;
import com.alevel.concertnotifierbot.repository.ConcertRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
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

    @Override
    public boolean save(ConcertDto concertDto) {
        Optional<Concert> concertInDB =
                concertRepository.findConcertsByArtistAndDate(concertDto.getArtist(), concertDto.getDate());
        if(concertInDB.isPresent()) return false;
        Concert concert = convertFromDto(concertDto);
        long id = concertRepository.save(concert).getId();
        log.info("Concert saved with id:{}", id);
        return true;
    }

    private ConcertDto convert(Concert concert){
        return new ConcertDto(concert.getId(), concert.getArtist(), concert.getDate(), concert.getUrl(), concert.getPlace(), concert.getPrice());
    }

    private Concert convertFromDto(ConcertDto concertDto){
        Concert concert = new Concert();
        concert.setArtist(concertDto.getArtist());
        concert.setDate(concertDto.getDate());
        concert.setPrice(concertDto.getPrice());
        concert.setPlace(concertDto.getAddress());
        concert.setUrl(concertDto.getSourceUrl());
        return concert;
    }
}
