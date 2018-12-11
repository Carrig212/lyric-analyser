package me.william.anderson.lyricanalyser.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import me.william.anderson.lyricanalyser.exception.StatusCodeException;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import static me.william.anderson.lyricanalyser.api.ApiConstants.ALBUM_TRACKS_GET;
import static me.william.anderson.lyricanalyser.api.ApiConstants.ARTIST_ALBUMS_GET;
import static me.william.anderson.lyricanalyser.api.ApiConstants.ARTIST_GET;
import static me.william.anderson.lyricanalyser.api.ApiConstants.PAGE_SIZE;
import static me.william.anderson.lyricanalyser.api.ApiConstants.RELEASE_TYPE_ALBUM;
import static me.william.anderson.lyricanalyser.api.ApiConstants.RELEASE_TYPE_EP;
import static me.william.anderson.lyricanalyser.api.ApiConstants.RELEASE_TYPE_SINGLE;
import static me.william.anderson.lyricanalyser.api.ApiConstants.TRACK_LYRICS_GET;
import static me.william.anderson.lyricanalyser.api.ApiConstants.URL;

@Component
@PropertySource("api.properties")
public class ApiConsumer {
    @Value("${api.key}")
    private String apiKey;

    public JSONObject getArtist(long artistId) throws UnirestException, StatusCodeException {
        var url = URL + ARTIST_GET;
        var parameters = new HashMap<String, Object>();

        parameters.put("apikey", apiKey);
        parameters.put("artist_id", artistId); // The ID of the artist we want to find

        return sendRequest(url, parameters);
    }

    public ArrayList<JSONObject> getArtistAlbums(long artistId) throws UnirestException, StatusCodeException {
        var url = URL + ARTIST_ALBUMS_GET;
        var parameters = new HashMap<String, Object>();
        int pageNumber = 1; // The results will be paginated, starting at 1

        parameters.put("apikey", apiKey);
        parameters.put("artist_id", artistId); // The ID of the artist we want to find
        parameters.put("g_album_name", 1); // Group by album name to remove duplicates
        parameters.put("page_size", PAGE_SIZE); // Maximum results per page is 100
        parameters.put("page", pageNumber);

        var albums = new ArrayList<JSONObject>();
        var response = new JSONObject(); // This needs to be created here so we can access it in the loop parameters

        do {
            response = sendRequest(url, parameters);

            if (response.getJSONObject("body").getJSONArray("album_list").length() != 0) {
                albums.addAll(extractAlbumsFromJson(response));
            }

            pageNumber++; // Increment the page number
            parameters.replace("page", pageNumber); // Update the page number
        } while (response.getJSONObject("body")
                .getJSONArray("album_list")
                .length() == PAGE_SIZE); // Loop until we run out of results

        return albums;
    }

    public ArrayList<JSONObject> getAlbumTracks(long albumId) throws UnirestException, StatusCodeException {
        var url = URL + ALBUM_TRACKS_GET;
        var parameters = new HashMap<String, Object>();

        parameters.put("apikey", apiKey);
        parameters.put("album_id", albumId); // The ID of the album we want to find
        parameters.put("f_has_lyrics", 1);

        var response = sendRequest(url, parameters);

        return extractTracksFromJSON(response);
    }

    public String getTrackLyrics(long trackId) throws UnirestException, StatusCodeException {
        var url = URL + TRACK_LYRICS_GET;
        var parameters = new HashMap<String, Object>();

        parameters.put("apikey", apiKey);
        parameters.put("track_id", trackId);

        var response = sendRequest(url, parameters);

        return response.getJSONObject("body").getJSONObject("lyrics").getString("lyrics_body");
    }

    // Internal utility methods
    private JSONObject sendRequest(String url, Map<String, Object> parameters) throws UnirestException, StatusCodeException {
        var response = Unirest.get(url)
                .header("accept", "application/json")
                .queryString(parameters) // Add the parameters to the url
                .asJson()
                .getBody()
                .getObject()
                .getJSONObject("message"); // Get the message object from the response

        checkStatusCode(response);

        return response;
    }

    private void checkStatusCode(JSONObject body) throws StatusCodeException {
        int statusCode = body
                .getJSONObject("header")
                .getInt("status_code"); // The actual status code is in the body

        if (statusCode != 200) {
            throw new StatusCodeException(200, statusCode); // The only status code that's acceptable is 200 OK
        }
    }

    private ArrayList<JSONObject> extractAlbumsFromJson(JSONObject response) {
        var albums = new ArrayList<JSONObject>();
        var results = response.getJSONObject("body").getJSONArray("album_list"); // Get the list of results

        for (int i = 0; i < results.length(); i++) { // Loop through the list
            var album = results.getJSONObject(i).getJSONObject("album"); // Extract the album object
            var type = album.getString("album_release_type");

            if (type.equals(RELEASE_TYPE_ALBUM) || type.equals(RELEASE_TYPE_SINGLE) || type.equals(RELEASE_TYPE_EP)) {
                albums.add(album); // Filter out compilations and remixes
            }
        }

        return albums;
    }

    private ArrayList<JSONObject> extractTracksFromJSON(JSONObject response) {
        var tracks = new ArrayList<JSONObject>();
        var results = response.getJSONObject("body").getJSONArray("track_list"); // Get the list of results

        for (int i = 0; i < results.length(); i++) { // Loop through the list
            tracks.add(results.getJSONObject(i).getJSONObject("track")); // Extract the track object
        }

        return tracks;
    }
}
