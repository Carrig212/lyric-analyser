package me.william.anderson.lyricanalyser.api;

import java.io.IOException;
import java.util.ArrayList;

import me.william.anderson.lyricanalyser.exception.MalformedRequestException;
import me.william.anderson.lyricanalyser.model.data.TrackDataModel;

import lombok.val;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import static me.william.anderson.lyricanalyser.api.Constants.*;

public class HtmlScraper {

    public static long scrapeArtistId(String url) throws IOException, MalformedRequestException {
        // Get the meta tag with the artist ID from the head
        val contentString = getHtmlDocument(url)
                .head()
                .getElementsByAttributeValueContaining(ARTIST_ID_KEY, ARTIST_ID_VALUE)
                .first()
                .attr(ALBUM_ID_ATTRIBUTE);

        // Remove everything but the ID from the string
        val artistIdString = contentString.substring(contentString.lastIndexOf(ARTIST_ID_START) + 1);

        return Long.parseLong(artistIdString);
    }

    public static ArrayList<Long> scrapeAlbumIdList(long id) throws IOException, MalformedRequestException {
        val url = GENIUS_COM + ARTIST_ALBUMS + id;

        // Get a list of all album links from the body
        val albumLinkList = getHtmlDocument(url).body().getElementsByClass(ALBUM_LINK_CLASS);
        val albumIdList = new ArrayList<Long>();

        for (val link : albumLinkList) {
            // Get the ID from each link and add it to the list
            try {
                albumIdList.add(scrapeAlbumId(GENIUS_COM + link.attr(ALBUM_LINK_ATTRIBUTE)));
            } catch (Exception ignored) {
            }
        }

        return albumIdList;
    }

    private static long scrapeAlbumId(String url) throws IOException, MalformedRequestException {
        // Get the meta tag with the album ID from the head
        val contentString = getHtmlDocument(url)
                .head()
                .getElementsByAttributeValueContaining(ALBUM_ID_KEY, ALBUM_ID_VALUE)
                .attr(ALBUM_ID_ATTRIBUTE);

        // Extract the album ID from the string
        val albumIdString = contentString.substring(
                contentString.indexOf(ALBUM_ID_START) + ALBUM_ID_START.length(),
                contentString.indexOf(ALBUM_ID_END)
        );

        return Long.parseLong(albumIdString);
    }

    public static ArrayList<TrackDataModel> scrapeTrackList(String url) throws IOException, MalformedRequestException {
        // Get the list of track links from the body
        val trackLinkList = getHtmlDocument(url)
                .body()
                .getElementsByClass(TRACK_LINK_CLASS);

        val trackDataList = new ArrayList<TrackDataModel>();

        for (val link : trackLinkList) {
            // Get the ID and lyrics from each page and add them to the list
            try {
                trackDataList.add(scrapeTrackIdAndLyrics(link.attr(ALBUM_LINK_ATTRIBUTE)));
            } catch (Exception ignored) {
            }
        }

        return trackDataList;
    }

    private static TrackDataModel scrapeTrackIdAndLyrics(String url) throws IOException, MalformedRequestException {
        val document = getHtmlDocument(url);

        // Get the track ID from the meta tag in the head
        val trackIdString = document
                .head()
                .getElementsByAttributeValueContaining(ARTIST_ID_KEY, ARTIST_ID_VALUE)
                .first()
                .attr(TRACK_ID_ATTRIBUTE);

        // Get the lyrics from the body
        val trackLyrics = document
                .body()
                .getElementsByClass(TRACK_LYRICS_CLASS)
                .first()
                .text();

        // Extract the track ID from the string
        val trackId = Long.parseLong(trackIdString.substring(trackIdString.lastIndexOf(TRACK_ID_START) + 1));

        // Return the ID and lyrics in a data object
        return new TrackDataModel(trackId, trackLyrics);
    }

    private static Document getHtmlDocument(String url) throws IOException, MalformedRequestException {
        // Check that the GENIUS_COM is formatted correctly
        if (!url.substring(0, GENIUS_COM.length()).equals(GENIUS_COM)) {
            throw new MalformedRequestException(url, GENIUS_COM);
        }

        // Test to see if we can actually connect to the server
        Jsoup.connect(url).execute();

        // Execute the request and return the HTML document
        return Jsoup.connect(url).get();
    }
}
