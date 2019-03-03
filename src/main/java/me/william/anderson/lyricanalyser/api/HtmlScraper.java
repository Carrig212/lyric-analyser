package me.william.anderson.lyricanalyser.api;

import java.io.IOException;
import java.util.ArrayList;

import me.william.anderson.lyricanalyser.exception.MalformedRequestException;
import me.william.anderson.lyricanalyser.model.data.TrackDataModel;

import lombok.val;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static me.william.anderson.lyricanalyser.api.Constants.*;

public class HtmlScraper {

    private static final Logger logger = LoggerFactory.getLogger(HtmlScraper.class);

    public static long scrapeArtistId(String url) throws IOException, MalformedRequestException {
        logger.debug("Scraping Artist ID from URL \"" + url + "\".");

        // Get the meta tag with the artist ID from the head
        val contentString = getHtmlDocument(url)
                .head()
                .getElementsByAttributeValueContaining(ARTIST_ID_KEY, ARTIST_ID_VALUE)
                .first()
                .attr(ALBUM_ID_ATTRIBUTE);

        // Remove everything but the ID from the string
        val artistIdString = contentString.substring(contentString.lastIndexOf(ARTIST_ID_START) + 1);

        logger.debug("Artist ID \"" + artistIdString + "\" " + " has been scraped from URL \"" + url + "\" successfully.");

        return Long.parseLong(artistIdString);
    }

    public static ArrayList<Long> scrapeAlbumIdList(long id) throws IOException, MalformedRequestException {
        val url = GENIUS_COM + ARTIST_ALBUMS + id;

        logger.debug("Scraping Album ID list from URL \"" + url + "\".");

        // Get a list of all album links from the body
        val albumLinkList = getHtmlDocument(url).body().getElementsByClass(ALBUM_LINK_CLASS);
        val albumIdList = new ArrayList<Long>();

        for (val link : albumLinkList) {
            // Get the ID from each link and add it to the list
            try {
                albumIdList.add(scrapeAlbumId(GENIUS_COM + link.attr(ALBUM_LINK_ATTRIBUTE)));
            } catch (Exception e) {
                logger.debug("Album URL \"" + url + "\" threw an Exception: \"" + e.getMessage() + "\".");
            }
        }

        logger.debug("Album ID list has been scraped from URL \"" + url + "\" successfully.");

        return albumIdList;
    }

    private static long scrapeAlbumId(String url) throws IOException, MalformedRequestException {
        logger.debug("Scraping Album ID from URL \"" + url + "\".");

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

        logger.debug("Album ID \"" + albumIdString + "\" " + " has been scraped from URL \"" + url + "\" successfully.");

        return Long.parseLong(albumIdString);
    }

    public static ArrayList<TrackDataModel> scrapeTrackList(String url) throws IOException, MalformedRequestException {
        logger.debug("Scraping Track list from URL \"" + url + "\".");

        // Get the list of track links from the body
        val trackLinkList = getHtmlDocument(url)
                .body()
                .getElementsByClass(TRACK_LINK_CLASS);

        val trackDataList = new ArrayList<TrackDataModel>();

        for (val link : trackLinkList) {
            // Get the ID and lyrics from each page and add them to the list
            try {
                trackDataList.add(scrapeTrackIdAndLyrics(link.attr(ALBUM_LINK_ATTRIBUTE)));
            } catch (Exception e) {
                logger.debug("Track URL \"" + url + "\" threw an Exception: \"" + e.toString() + "\".");
            }
        }

        logger.debug("Track ID list has been scraped from URL \"" + url + "\" successfully.");

        return trackDataList;
    }

    private static TrackDataModel scrapeTrackIdAndLyrics(String url) throws IOException, MalformedRequestException {
        logger.debug("Scraping Track ID and lyrics from URL \"" + url + "\".");

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

        logger.debug("ID and lyrics for Track \"" + trackId + "\" " + " has been scraped from URL \"" + url + "\" successfully.");

        // Return the ID and lyrics in a data object
        return new TrackDataModel(trackId, trackLyrics);
    }

    private static Document getHtmlDocument(String url) throws IOException, MalformedRequestException {
        logger.debug("Sending HTTP GET request to URL \"" + url + "\".");

        // Check that the URL is formatted correctly
        if (!url.substring(0, GENIUS_COM.length()).equals(GENIUS_COM)) {
            logger.debug("URL \"" + url + "\" is not formatted correctly. The URl should start with \"" + GENIUS_COM + "\".");
            throw new MalformedRequestException(url, GENIUS_COM);
        }

        // Test to see if we can actually connect to the server
        val response = Jsoup.connect(url).execute();

        logger.debug("URL \"" + url + "\" responded with status code \"" + response.statusCode() + "\".");

        // Execute the request and return the HTML document
        return Jsoup.connect(url).get();
    }
}
