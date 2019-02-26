package me.william.anderson.lyricanalyser.api;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import lombok.NonNull;
import lombok.val;
import me.william.anderson.lyricanalyser.exception.StatusCodeException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("api.properties")
public class ApiConsumer {

    private static final Logger logger = LoggerFactory.getLogger(ApiConsumer.class);

    // Constants
    private static final int STATUS = 200;
    private static final String URL = "https://api.genius.com/";
    private static final String RESPONSE = "response";

    private static final String ARTISTS = "artists/";
    private static final String ARTIST = "artist";

    private static final String ALBUMS = "albums/";
    private static final String ALBUM = "album";

    private static final String SONGS = "songs/";
    private static final String SONG = "song";

    private static final String ACCEPT = "Accept";
    private static final String JSON = "application/json";
    private static final String AUTH = "Authorization";
    private static final String BEARER = "Bearer ";
    private static final String FORMAT = "text_format";
    private static final String PLAIN = "plain";

    @NonNull
    @Value("${client.access.token}")
    private String accessToken;

    public JSONObject getArtist(long id) throws StatusCodeException, UnirestException {
        val url = URL + ARTISTS + id;

        return sendRequest(url)
                .getBody()
                .getObject()
                .getJSONObject(RESPONSE)
                .getJSONObject(ARTIST);
    }

    public JSONObject getAlbum(long id) throws StatusCodeException, UnirestException {
        val url = URL + ALBUMS + id;

        return sendRequest(url)
                .getBody()
                .getObject()
                .getJSONObject(RESPONSE)
                .getJSONObject(ALBUM);
    }

    public JSONObject getTrack(long id) throws StatusCodeException, UnirestException {
        val url = URL + SONGS + id;

        return sendRequest(url)
                .getBody()
                .getObject()
                .getJSONObject(RESPONSE)
                .getJSONObject(SONG);
    }

    private HttpResponse<JsonNode> sendRequest(String url) throws StatusCodeException, UnirestException {
        val response = Unirest.get(url) // Send the request
                .header(ACCEPT, JSON)
                .header(AUTH, BEARER + accessToken) // OAuth 2.0
                .queryString(FORMAT, PLAIN)
                .asJson();

        // Since we're only querying the api, 200 OK is the only acceptable status code
        if (response.getStatus() != STATUS) {
            throw new StatusCodeException(STATUS, response.getStatus(), url);
        }

        logger.debug(url + " responded with status " + response.getStatus() + " " + response.getStatusText());

        return response;
    }
}
