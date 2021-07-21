package com.alevel.concertnotifierbot.service.dataoperations;

import com.alevel.concertnotifierbot.exceptions.DataNotFoundException;
import com.alevel.concertnotifierbot.model.dto.ConcertDto;
import com.alevel.concertnotifierbot.model.entity.Concert;
import com.alevel.concertnotifierbot.model.entity.User;
import com.alevel.concertnotifierbot.repository.ConcertRepository;
import com.alevel.concertnotifierbot.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
@Transactional
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private ConcertRepository concertRepository;

    public UserServiceImpl(UserRepository userRepository, ConcertRepository concertRepository) {
        this.userRepository = userRepository;
        this.concertRepository = concertRepository;
    }

    @Override
    public String getUsersToken(long chatId) throws DataNotFoundException {
        User user = findUser(chatId);
        return user.getRefreshToken();
    }

    @Override
    public boolean isExist(long chatId) {
        return userRepository.existsByChatId(chatId);
    }

    @Override
    public boolean isAdmin(long chatId) throws DataNotFoundException {
        User user = findUser(chatId);
        return user.isAdmin();
    }

    @Override
    public long saveUser(long chatId, String token) {
        Optional<User> user = userRepository.findUserByChatId(chatId);
        if (user.isEmpty()){
            User savedUser = userRepository.save(new User(chatId, token));
            log.info("User saved with id:{}", savedUser.getId());
            return savedUser.getId();
        }else {
            user.get().setRefreshToken(token);
            long id = userRepository.save(user.get()).getId();
            log.info("Update token for user with id:{}", id);
            return id;
        }
    }

    @Override
    public void updateUserAddConcert(long chatId, long concertId) throws DataNotFoundException {
        Concert concert = findConcert(concertId);
        User user = findUser(chatId);
        user.getConcerts().add(concert);
        userRepository.save(user);
        log.info("Update user with id:{}. Add concert with id{}", concert.getId(), user.getId());
    }

    @Override
    public void updateUserDeleteConcert(long chatId, long concertId) throws DataNotFoundException {
        Concert concert = findConcert(concertId);
        User user = findUser(chatId);
        user.getConcerts().remove(concert);
        userRepository.save(user);
        log.info("Update user with id:{}. Delete concert with id{}", concert.getId(), user.getId());
    }

    @Override
    public Map<Long, List<ConcertDto>> getUsersConcertsForNotification(Date concertDate){
        Map<Long, List<ConcertDto>> usersConcerts = new HashMap<>();

        List<User> users = userRepository.findUsersConcertByDate(concertDate);

        for (User u : users) {
            List<ConcertDto> concerts = u.getConcerts()
                    .stream().filter(concert -> concert.getDate().before(concertDate) || concert.getDate().equals(concertDate))
                    .map(c -> new ConcertDto(c.getId(), c.getArtist(), c.getDate(), c.getUrl(), c.getPlace(), c.getPrice()))
                    .collect(Collectors.toList());
            usersConcerts.put(u.getChatId(), concerts);
        }
        return usersConcerts;
    }

    private User findUser(long chatId) throws DataNotFoundException {
        Optional<User> user = userRepository.findUserByChatId(chatId);
        if(user.isEmpty()){
            log.info("User with chatId:{} not found", chatId);
            throw new DataNotFoundException("User does not exist, chatId: " + chatId);
        }
        return user.get();
    }

    private Concert findConcert(long concertId) throws DataNotFoundException {
        Optional<Concert> concert = concertRepository.findById(concertId);
        if(concert.isEmpty()){
            log.info("Concert with id:{} not found", concertId);
            throw new DataNotFoundException("Concert does not exist, id: " + concertId);
        }
        return concert.get();
    }

}
