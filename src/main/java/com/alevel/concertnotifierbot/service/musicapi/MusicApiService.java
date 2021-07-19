package com.alevel.concertnotifierbot.service.musicapi;

import com.alevel.concertnotifierbot.exceptions.ApiAccessException;
import com.alevel.concertnotifierbot.exceptions.DataNotFoundException;

import java.util.List;

public interface MusicApiService {

    List<String> getUsersTopArtists(long chatId) throws ApiAccessException, DataNotFoundException;
    String login(String id);
}
