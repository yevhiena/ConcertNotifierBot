package com.alevel.concertnotifierbot.service.dataoperations;

import com.alevel.concertnotifierbot.model.dto.ConcertDto;
import com.alevel.concertnotifierbot.model.entity.Concert;
import com.alevel.concertnotifierbot.repository.ConcertRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class ConcertServiceTest {

    private ConcertRepository concertRepository;
    private ConcertService concertService;
    private static final String name = "artist name";
    private static final long userId = 1L;

    @BeforeEach
    void setUp() {
        concertRepository = mock(ConcertRepository.class);
        concertService = new ConcertServiceImpl(concertRepository);
    }

    @Test
    void testFindConcertsByArtist(){

        List<Concert> concerts = new ArrayList<>();
        var concert = new Concert(1, name, new Date(), "price", "place", "url");
        concerts.add(concert);

        when(concertRepository.findConcertsByArtist(name)).thenReturn(concerts);

        List<ConcertDto> concertDtos = concertService.findConcertsByArtist(name);
        verify(concertRepository).findConcertsByArtist(name);

        assertThat(concertDtos.size() == 1).isTrue();
        ConcertDto concertDto = concertDtos.get(0);
        assertThat(concertDto.getId() == concert.getId()
                && concertDto.getArtist().equals(concert.getArtist())
                && concertDto.getDate().equals(concert.getDate())
                && concertDto.getPrice().equals(concert.getPrice())
                && concertDto.getAddress().equals(concert.getPlace())
                && concertDto.getSourceUrl().equals(concert.getUrl())).isTrue();

        verifyNoMoreInteractions(concertRepository);
    }


    @Test
    void testFindAllUsersConcerts(){
        List<Concert> concerts = new ArrayList<>();
        var concert = new Concert(1, name, new Date(), "price", "place", "url");
        concerts.add(concert);

        when(concertRepository.findAllUsersConcerts(userId)).thenReturn(concerts);

        List<ConcertDto> concertDtos = concertService.findAllUsersConcerts(userId);
        verify(concertRepository).findAllUsersConcerts(userId);

        assertThat(concertDtos.size() == 1).isTrue();
        ConcertDto concertDto = concertDtos.get(0);
        assertThat(concertDto.getId() == concert.getId()
                && concertDto.getArtist().equals(concert.getArtist())
                && concertDto.getDate().equals(concert.getDate())
                && concertDto.getPrice().equals(concert.getPrice())
                && concertDto.getAddress().equals(concert.getPlace())
                && concertDto.getSourceUrl().equals(concert.getUrl())).isTrue();

        verifyNoMoreInteractions(concertRepository);
    }

    @Test
    void testShouldReturnEmptyList(){

        List<Concert> concerts = new ArrayList<>();

        when(concertRepository.findConcertsByArtist(name)).thenReturn(concerts);
        when(concertRepository.findAllUsersConcerts(userId)).thenReturn(concerts);

        List<ConcertDto> concertDtos = concertService.findConcertsByArtist(name);
        verify(concertRepository).findConcertsByArtist(name);
        assertThat(concertDtos.isEmpty()).isTrue();

        concertDtos = concertService.findAllUsersConcerts(userId);
        verify(concertRepository).findAllUsersConcerts(userId);
        assertThat(concertDtos.isEmpty()).isTrue();

        verifyNoMoreInteractions(concertRepository);
    }

}
