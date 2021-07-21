package com.alevel.concertnotifierbot.repository;

import com.alevel.concertnotifierbot.model.entity.Concert;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface ConcertRepository extends JpaRepository<Concert, Long> {

    List<Concert> findConcertsByArtist(String name);

    Optional<Concert> findConcertsByArtistAndDate(String artist, Date date);

    @Query("select c.concerts from User c where c.chatId = :id")
    List<Concert> findAllUsersConcerts(@Param("id") long id);

}
