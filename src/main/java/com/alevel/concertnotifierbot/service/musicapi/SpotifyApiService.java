package com.alevel.concertnotifierbot.service.musicapi;

import com.alevel.concertnotifierbot.exceptions.ApiAccessException;
import com.alevel.concertnotifierbot.exceptions.DataNotFoundException;
import com.alevel.concertnotifierbot.service.dataoperations.UserService;
import com.alevel.concertnotifierbot.service.dataoperations.UserServiceImpl;
import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.SpotifyHttpManager;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import com.wrapper.spotify.model_objects.specification.Artist;
import com.wrapper.spotify.model_objects.specification.Paging;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeRefreshRequest;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;
import com.wrapper.spotify.requests.data.personalization.simplified.GetUsersTopArtistsRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.http.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class SpotifyApiService implements MusicApiService {

    @Value("${spotify.clientId}")
    private String clientId;
    @Value("${spotify.secret}")
    private String clientSecret;
    private URI redirectUri;

    private UserService userService;

    public SpotifyApiService(UserServiceImpl userService, @Value("${spotify.redirectURI}") String path) {
        this.redirectUri =  SpotifyHttpManager.makeUri(path);
        this.userService = userService;
    }

    private SpotifyApi getSpotifyApi(){
        return new SpotifyApi.Builder()
                .setClientId(clientId)
                .setClientSecret(clientSecret)
                .setRedirectUri(redirectUri)
                .build();
    }

    public String login(String state){
        SpotifyApi spotifyApi = getSpotifyApi();
        AuthorizationCodeUriRequest authorizationCodeUriRequest = spotifyApi.authorizationCodeUri()
                .scope("user-top-read")
                .show_dialog(true)
                .state(state)
                .build();
        final URI uri = authorizationCodeUriRequest.execute();
        return uri.toString();
    }


    public void getSpotifyUserCode(String userCode, String state) throws ApiAccessException {
        SpotifyApi spotifyApi = getSpotifyApi();
        AuthorizationCodeRequest authorizationCodeRequest = spotifyApi.authorizationCode(userCode)
                .build();

        final AuthorizationCodeCredentials authorizationCodeCredentials;
        try {
            authorizationCodeCredentials = authorizationCodeRequest.execute();
            String refreshToken = authorizationCodeCredentials.getRefreshToken();
            userService.saveUser(Long.parseLong(state), refreshToken);
            log.info("SpotifyApiService. Successfully logged in spotify, user with chat id:{} ", state);
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            log.error("Spotify API error. Unable to get user code ", e);
            throw new ApiAccessException("Spotify API error. Unable to get user code ", e);
        }
    }

    public List<String> getUsersTopArtists(long chatId) throws ApiAccessException, DataNotFoundException {
        SpotifyApi spotifyApi = getSpotifyApi();
        List<String> artists = new ArrayList<>();
        try {
            String accessToken = renewAccessToken(chatId);
            spotifyApi.setAccessToken(accessToken);
            final GetUsersTopArtistsRequest getUsersTopArtistsRequest = spotifyApi.getUsersTopArtists()
                    .time_range("short_term")
                    .limit(5)
                    .build();

            final Paging<Artist> artistPaging = getUsersTopArtistsRequest.execute();
            Artist[] artistsList = artistPaging.getItems();
            for (Artist a : artistsList) {
                artists.add(a.getName().toLowerCase());
            }
            log.info("SpotifyApiService. Successfully got top artists for user with chat id:{}", chatId);
            return artists;
        } catch (IOException | ParseException | SpotifyWebApiException  e) {
            log.error("Spotify API error. Unable to get users top artists. ", e);
            throw new ApiAccessException("Spotify API error. Unable to get users top artists. ", e);
        }
    }


    private String renewAccessToken(long chatId) throws IOException, ParseException, SpotifyWebApiException, DataNotFoundException {
        SpotifyApi spotifyApi = getSpotifyApi();
        String refreshToken = userService.getUsersToken(chatId);
        AuthorizationCodeRefreshRequest authorizationCodeRefreshRequest =
                spotifyApi.authorizationCodeRefresh(clientId, clientSecret, refreshToken)
                .build();

        final AuthorizationCodeCredentials authorizationCodeCredentials = authorizationCodeRefreshRequest.execute();
        return authorizationCodeCredentials.getAccessToken();
    }

}
