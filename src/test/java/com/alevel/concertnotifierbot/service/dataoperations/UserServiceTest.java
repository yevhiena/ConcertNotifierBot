package com.alevel.concertnotifierbot.service.dataoperations;

import com.alevel.concertnotifierbot.exceptions.DataNotFoundException;
import com.alevel.concertnotifierbot.model.dto.ConcertDto;
import com.alevel.concertnotifierbot.model.entity.Concert;
import com.alevel.concertnotifierbot.model.entity.User;
import com.alevel.concertnotifierbot.repository.ConcertRepository;
import com.alevel.concertnotifierbot.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.Mockito.*;

public class UserServiceTest {
    private UserService userService;
    private UserRepository userRepository;
    private ConcertRepository concertRepository;
    private static final long absentId = 1L;
    private static final long presentChatId = 1111L;
    private static final long absentChatId = 2222L;
    private static final long presentConcertId = 3333L;
    private static final long absentConcertId = 4444L;
    private static final String token = "token";
    private User user;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        concertRepository =mock(ConcertRepository.class);
        userService = new UserServiceImpl(userRepository, concertRepository);
        user = new User(presentChatId, token);
    }

    @Test
    void testGetUsersToken() throws DataNotFoundException {

        when(userRepository.findUserByChatId(absentChatId)).thenReturn(Optional.empty());
        when(userRepository.findUserByChatId(presentChatId)).thenReturn(Optional.of(user));

        assertThatExceptionOfType(DataNotFoundException.class)
                .isThrownBy(() -> userService.getUsersToken(absentChatId));

        verify(userRepository).findUserByChatId(absentChatId);

        String presentToken = userService.getUsersToken(presentChatId);
        verify(userRepository).findUserByChatId(presentChatId);

        assertThat(presentToken).isEqualTo(token);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void testIsExist(){
        when(userRepository.existsByChatId(absentChatId)).thenReturn(false);
        when(userRepository.existsByChatId(presentChatId)).thenReturn(true);

        assertThat(userService.isExist(absentChatId)).isEqualTo(false);
        assertThat(userService.isExist(presentChatId)).isEqualTo(true);

        verify(userRepository).existsByChatId(presentChatId);
        verify(userRepository).existsByChatId(absentChatId);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void testUpdateUserAddConcert() throws DataNotFoundException {

        var concert = new Concert();

        when(userRepository.findUserByChatId(absentChatId)).thenReturn(Optional.empty());
        when(userRepository.findUserByChatId(presentChatId)).thenReturn(Optional.of(user));
        when(concertRepository.findById(presentConcertId)).thenReturn(Optional.of(concert));
        when(concertRepository.findById(absentConcertId)).thenReturn(Optional.empty());
        when(userRepository.save(same(user))).thenReturn(user);

        assertThatExceptionOfType(DataNotFoundException.class)
                .isThrownBy(() -> userService.updateUserAddConcert(absentChatId, presentConcertId));

        verify(userRepository).findUserByChatId(absentChatId);

        assertThatExceptionOfType(DataNotFoundException.class)
                .isThrownBy(() -> userService.updateUserAddConcert(presentChatId, absentConcertId));

        userService.updateUserAddConcert(presentChatId, presentConcertId);
        verify(userRepository).findUserByChatId(presentChatId);
        verify(userRepository).save(user);

        assertThat(user.getConcerts().contains(concert)).isTrue();

        verifyNoMoreInteractions(userRepository);
    }


    @Test
    void testUpdateUserDeleteConcert() throws DataNotFoundException {

        var concert = new Concert();

        when(userRepository.findUserByChatId(absentChatId)).thenReturn(Optional.empty());
        when(userRepository.findUserByChatId(presentChatId)).thenReturn(Optional.of(user));
        when(concertRepository.findById(presentConcertId)).thenReturn(Optional.of(concert));
        when(concertRepository.findById(absentConcertId)).thenReturn(Optional.empty());
        when(userRepository.save(same(user))).thenReturn(user);

        userService.updateUserAddConcert(presentChatId, presentConcertId);
        assertThat(user.getConcerts().contains(concert)).isTrue();

        assertThatExceptionOfType(DataNotFoundException.class)
                .isThrownBy(() -> userService.updateUserAddConcert(absentChatId, presentConcertId));

        assertThatExceptionOfType(DataNotFoundException.class)
                .isThrownBy(() -> userService.updateUserAddConcert(presentChatId, absentConcertId));

        userService.updateUserDeleteConcert(presentChatId, presentConcertId);

        assertThat(user.getConcerts().contains(concert)).isFalse();
    }

    @Test
    void testGetUsersConcertsForNotification(){
        Date date = new Date();

        when(userRepository.findUsersConcertByDate(date)).thenReturn(new ArrayList<>());
        Map<Long, List<ConcertDto>> usersConcertsEmpty = userService.getUsersConcertsForNotification(date);
        verify(userRepository, times(1)).findUsersConcertByDate(date);
        assertThat(usersConcertsEmpty.isEmpty()).isTrue();

        var concert = new Concert(presentConcertId, "artist", date, "price", "place", "url");
        user.getConcerts().add(concert);
        List<User> users = new ArrayList<>();
        users.add(user);

        when(userRepository.findUsersConcertByDate(date)).thenReturn(users);

        Map<Long, List<ConcertDto>> usersConcerts = userService.getUsersConcertsForNotification(date);
        verify(userRepository, times(2)).findUsersConcertByDate(date);

        assertThat(usersConcerts.containsKey(presentChatId)).isTrue();
        assertThat(usersConcerts.get(presentChatId).size()).isEqualTo(1);

        ConcertDto concertDto = usersConcerts.get(presentChatId).get(0);

        assertThat(concertDto.getId() == concert.getId()
                && concertDto.getArtist().equals(concert.getArtist())
        && concertDto.getDate().equals(concert.getDate())
        && concertDto.getPrice().equals(concert.getPrice())
        && concertDto.getAddress().equals(concert.getPlace())
        && concertDto.getSourceUrl().equals(concert.getUrl())).isTrue();

    }


    @Test
    void testSaveUser() {

        when(userRepository.findUserByChatId(absentChatId)).thenReturn(Optional.empty());
        when(userRepository.findUserByChatId(presentChatId)).thenReturn(Optional.of(user));
        when(userRepository.save(notNull())).thenAnswer(invocation -> {
            User entity = invocation.getArgument(0);
            assertThat(entity.getChatId()).isEqualTo(absentChatId);
            assertThat(entity.getRefreshToken()).isEqualTo(token);
            entity.setId(absentId);
            return entity;
        });

        long id = userService.saveUser(absentChatId, token);
        assertThat(id).isEqualTo(absentId);

        verify(userRepository).findUserByChatId(absentChatId);
        verify(userRepository).save(notNull());

        verifyNoMoreInteractions(userRepository);
    }
}
