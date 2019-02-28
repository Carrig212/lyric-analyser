package me.william.anderson.lyricanalyser.model.builder;

import java.io.IOException;
import java.util.ArrayList;

import me.william.anderson.lyricanalyser.analyser.LyricAnalyser;
import me.william.anderson.lyricanalyser.api.ApiConsumer;
import me.william.anderson.lyricanalyser.api.HtmlScraper;
import me.william.anderson.lyricanalyser.exception.MalformedRequestException;
import me.william.anderson.lyricanalyser.exception.StatusCodeException;
import me.william.anderson.lyricanalyser.model.Album;
import me.william.anderson.lyricanalyser.model.Artist;
import me.william.anderson.lyricanalyser.model.Music;
import me.william.anderson.lyricanalyser.model.Track;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mashape.unirest.http.exceptions.UnirestException;
import lombok.NonNull;
import lombok.val;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
@SuppressWarnings("Duplicates")
public class MusicEntityBuilder {

    // Constants
    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String URL = "url";

    private static final String IMAGE_URL = "image_url";

    private static final String COVER_URL = "cover_art_url";
    private static final String RELEASE_DATE = "release_date";

    private static final String TITLE = "title";
    private static final String SONG_ART_URL = "song_art_image_url";
    private static final String FEATURED = "featured_artists";
    private static final String LYRICS_STATE = "lyrics_state";

    @NonNull
    private final ApiConsumer consumer;
    private Logger logger = LoggerFactory.getLogger(MusicEntityBuilder.class);

    @Autowired
    public MusicEntityBuilder(ApiConsumer consumer) {
        this.consumer = consumer;
    }

    public Artist buildArtist(String url) throws IOException, MalformedRequestException, StatusCodeException, UnirestException {
        val artist = new Artist();

        logger.info("Now building artist " + url);
        val json = consumer.getArtist(HtmlScraper.scrapeArtistId(url));

        artist.setApiId(json.getLong(ID));
        artist.setName(getString(json, NAME).toLowerCase());
        artist.setGeniusUrl(getString(json, URL));
        artist.setImageUrl(getString(json, IMAGE_URL));

        artist.setAlbums(buildAlbumList(artist));

        artist.setWordFrequencies(LyricAnalyser.parseArtistLyrics(artist));
        buildStatistics(artist);

        logger.info("Artist \"" + artist.getName() + "\" has been built successfully");

        return artist;
    }

    private ArrayList<Album> buildAlbumList(Artist artist) throws MalformedRequestException, IOException {
        val albums = new ArrayList<Album>();

        for (var id : HtmlScraper.scrapeAlbumIdList(artist.getApiId())) {
            try {
                albums.add(buildAlbum(consumer.getAlbum(id), artist));
            } catch (Exception e) {
                logger.warn("API request for album " + id + " threw an exception. It will be excluded from analysis", e);
            }
        }

        logger.debug("Album list for artist \"" + artist.getName() + "\" has been built successfully");

        return albums;
    }

    private Album buildAlbum(JSONObject json, Artist artist) throws MalformedRequestException, IOException {
        val album = new Album();

        album.setApiId(json.getLong(ID));
        album.setName(getString(json, NAME).toLowerCase());
        album.setGeniusUrl(getString(json, URL));
        album.setImageUrl(getString(json, COVER_URL));

        album.setReleaseDate(getString(json, RELEASE_DATE));

        album.setArtist(artist);
        album.setTracks(buildTrackList(album));

        album.setWordFrequencies(LyricAnalyser.parseAlbumLyrics(album));
        buildStatistics(album);

        logger.info("Album \"" + album.getName() + "\" has been built successfully");

        return album;
    }

    private ArrayList<Track> buildTrackList(Album album) throws MalformedRequestException, IOException {
        val tracks = new ArrayList<Track>();

        for (var trackData : HtmlScraper.scrapeTrackList(album.getGeniusUrl())) {
            try {
                val json = consumer.getTrack(trackData.getId());
                tracks.add(buildTrack(json, album, trackData.getLyrics()));
            } catch (Exception e) {
                logger.warn("API request for track " + trackData.getId() + " threw an exception. It will be excluded from analysis", e);
            }
        }

        logger.debug("Track list for album \"" + album.getName() + "\" has been built successfully");

        return tracks;
    }

    private Track buildTrack(JSONObject json, Album album, String lyrics) {
        val track = new Track();

        track.setApiId(json.getLong(ID));
        track.setName(getString(json, TITLE).toLowerCase());
        track.setGeniusUrl(getString(json, URL));
        track.setImageUrl(getString(json, SONG_ART_URL));

        val featuresJson = json.getJSONArray(FEATURED);
        val features = new ArrayList<String>();

        for (int i = 0; i < featuresJson.length(); i++) {
            features.add(getString(featuresJson.getJSONObject(i), NAME));
        }

        track.setFeaturedArtists(features);
        track.setLyricsState(getString(json, LYRICS_STATE));
        track.setLyrics(lyrics);

        track.setAlbum(album);

        track.setWordFrequencies(LyricAnalyser.parseTrackLyrics(track.getLyrics()));
        buildStatistics(track);

        logger.info("Track \"" + track.getName() + "\" has been built successfully");

        return track;
    }

    private void buildStatistics(Music music) {
        val statistics = LyricAnalyser.generateStatistics(music);

        music.setUniqueWordCount(statistics.getUniqueWordCount());
        music.setWordCount(statistics.getWordCount());
        music.setUniqueWordDensity(statistics.getUniqueWordDensity());

        logger.debug("Statistics for " + music.getClass().getSimpleName() + " \"" + music.getName() + "\" have been generated successfully");
    }

    private String getString(JSONObject json, String key) {
        try {
            return json.getString(key);
        } catch (JSONException e) {
            logger.warn("The field with key \"" + key + "\" could not be found. It has been replaced with a default value");
            return "N/A";
        }
    }
}
