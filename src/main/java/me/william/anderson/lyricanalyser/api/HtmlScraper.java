package me.william.anderson.lyricanalyser.api;

import lombok.val;
import me.william.anderson.lyricanalyser.exception.MalformedRequestException;
import me.william.anderson.lyricanalyser.model.data.TrackData;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;

public class HtmlScraper {

    private static final Logger logger = LoggerFactory.getLogger(HtmlScraper.class);

    private static final String URL = "https://genius.com";

    private static final String ARTIST_ID_KEY = "name";
    private static final String ARTIST_ID_VALUE = "newrelic-resource-path";
    private static final String ARTIST_ID_START = "/";
    private static final String ARTIST_ALBUMS = "/artists/albums?for_artist_page=";

    private static final String ALBUM_LINK_CLASS = "album_link";
    private static final String ALBUM_LINK_ATTRIBUTE = "href";
    private static final String ALBUM_ID_KEY = "itemprop";
    private static final String ALBUM_ID_VALUE = "page_data";
    private static final String ALBUM_ID_ATTRIBUTE = "content";
    private static final String ALBUM_ID_START = "album_ids\":\"[";
    private static final String ALBUM_ID_END = "]\",\"artist\"";

    private static final String TRACK_LINK_CLASS = "u-display_block";
    private static final String TRACK_LYRICS_CLASS = "lyrics";
    private static final String TRACK_ID_ATTRIBUTE = "content";
    private static final String TRACK_ID_START = "/";

    public static long scrapeArtistId(String url) throws IOException, MalformedRequestException {
        // Get the meta tag with the artist ID from the head
        val contentString = getHtmlDocument(url)
                .head()
                .getElementsByAttributeValueContaining(ARTIST_ID_KEY, ARTIST_ID_VALUE)
                .first()
                .attr(ALBUM_ID_ATTRIBUTE);

        // Remove everything but the ID from the string
        val artistIdString = contentString.substring(contentString.lastIndexOf(ARTIST_ID_START) + 1);

        logger.info("Artist ID " + artistIdString + " has been successfully scraped from " + url);

        return Long.parseLong(artistIdString);
    }

    public static ArrayList<Long> scrapeAlbumIdList(long id) throws IOException, MalformedRequestException {
        val url = URL + ARTIST_ALBUMS + id;

        // Get a list of all album links from the body
        val albumLinkList = getHtmlDocument(url).body().getElementsByClass(ALBUM_LINK_CLASS);
        val albumIdList = new ArrayList<Long>();

        logger.info(url + " has been successfully scraped of all album links");

        for (var link : albumLinkList) {
            // Get the ID from each link and add it to the list
            try {
                albumIdList.add(scrapeAlbumId(URL + link.attr(ALBUM_LINK_ATTRIBUTE)));
            } catch (Exception e) {
                logger.warn(link.attr(ALBUM_LINK_ATTRIBUTE) + " threw an exception. It will be excluded from analysis", e);
            }
        }

        logger.info(url + " has been successfully scraped of all album IDs");

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

        logger.info("Album ID " + albumIdString + " has been successfully scraped from " + url);

        return Long.parseLong(albumIdString);
    }

    public static ArrayList<TrackData> scrapeTrackList(String url) throws IOException, MalformedRequestException {
        // Get the list of track links from the body
        val trackLinkList = getHtmlDocument(url)
                .body()
                .getElementsByClass(TRACK_LINK_CLASS);

        logger.info(url + " has been successfully scraped of all track links");

        val trackDataList = new ArrayList<TrackData>();

        for (var link : trackLinkList) {
            // Get the ID and lyrics from each page and add them to the list
            try {
                trackDataList.add(scrapeTrackIdAndLyrics(link.attr(ALBUM_LINK_ATTRIBUTE)));
            } catch (Exception e) {
                logger.warn(link.attr(ALBUM_LINK_ATTRIBUTE) + " threw an exception. It will be excluded from analysis", e);
            }
        }

        logger.info(url + " has been successfully scraped of all track IDs and lyrics");

        return trackDataList;
    }

    private static TrackData scrapeTrackIdAndLyrics(String url) throws IOException, MalformedRequestException {
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

        logger.info("Track ID " + trackId + " and lyrics have been successfully scraped from " + url);

        // Return the ID and lyrics in a data object
        return new TrackData(trackId, trackLyrics);
    }

    private static Document getHtmlDocument(String url) throws IOException, MalformedRequestException {
        // Check that the URL is formatted correctly
        if (!url.substring(0, URL.length()).equals(URL)) {
            throw new MalformedRequestException(url, URL);
        }

        // Test to see if we can actually connect to the server
        Jsoup.connect(url).execute();

        // Execute the request and return the HTML document
        return Jsoup.connect(url).get();
    }
}
