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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static me.william.anderson.lyricanalyser.api.Constants.*;

@Component
@PropertySource("application.properties")
public class ApiConsumer {

    private static final Logger logger = LoggerFactory.getLogger(ApiConsumer.class);

    @NonNull
    @Value("${client.access.token}")
    private String accessToken;

    public JSONObject getArtist(long id) throws StatusCodeException, UnirestException {
        val url = API_GENIUS_COM + ARTISTS + id;

        logger.debug("Sending API request to URL " + url + " for Artist \"" + id + "\".");

        return sendRequest(url)
                .getBody()
                .getObject()
                .getJSONObject(RESPONSE)
                .getJSONObject(ARTIST);
    }

    public JSONObject getAlbum(long id) throws StatusCodeException, UnirestException {
        val url = API_GENIUS_COM + ALBUMS + id;

        logger.debug("Sending API request to URL " + url + " for Album \"" + id + "\".");

        return sendRequest(url)
                .getBody()
                .getObject()
                .getJSONObject(RESPONSE)
                .getJSONObject(ALBUM);
    }

    public JSONObject getTrack(long id) throws StatusCodeException, UnirestException {
        val url = API_GENIUS_COM + SONGS + id;

        logger.debug("Sending API request to URL " + url + " for Track \"" + id + "\".");

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
            logger.warn("URL " + url + " responded with incorrect status code \"" + response.getStatus() + "\".");
            throw new StatusCodeException(STATUS, response.getStatus(), url);
        }

        logger.debug("URL + \"" + url + "\" responded with status code \"" + response.getStatus() + "\".");

        return response;
    }
}
