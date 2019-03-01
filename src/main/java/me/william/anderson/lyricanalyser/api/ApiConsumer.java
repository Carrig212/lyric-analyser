package me.william.anderson.lyricanalyser.api;

import me.william.anderson.lyricanalyser.exception.StatusCodeException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import lombok.NonNull;
import lombok.val;
import org.json.JSONObject;

import static me.william.anderson.lyricanalyser.api.Constants.*;

@Component
@PropertySource("application.properties")
public class ApiConsumer {

    @NonNull
    @Value("${client.access.token}")
    private String accessToken;

    public JSONObject getArtist(long id) throws StatusCodeException, UnirestException {
        val url = API_GENIUS_COM + ARTISTS + id;

        return sendRequest(url)
                .getBody()
                .getObject()
                .getJSONObject(RESPONSE)
                .getJSONObject(ARTIST);
    }

    public JSONObject getAlbum(long id) throws StatusCodeException, UnirestException {
        val url = API_GENIUS_COM + ALBUMS + id;

        return sendRequest(url)
                .getBody()
                .getObject()
                .getJSONObject(RESPONSE)
                .getJSONObject(ALBUM);
    }

    public JSONObject getTrack(long id) throws StatusCodeException, UnirestException {
        val url = API_GENIUS_COM + SONGS + id;

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

        return response;
    }
}
