package com.alevel.concertnotifierbot.repository;

import com.alevel.concertnotifierbot.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findUserByChatId(long chatId);

    boolean existsByChatId(long chatId);

    @Query("select c.users from Concert c where c.date < :date")
    List<User> findUsersConcertByDate(@Param("date") Date date);

}
