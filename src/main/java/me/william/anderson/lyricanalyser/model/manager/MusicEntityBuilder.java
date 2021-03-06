package me.william.anderson.lyricanalyser.model.manager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

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

import static me.william.anderson.lyricanalyser.model.manager.Constants.*;

@Component
@SuppressWarnings("Duplicates")
public class MusicEntityBuilder {

    private static final Logger logger = LoggerFactory.getLogger(MusicEntityBuilder.class);

    @NonNull
    private final ApiConsumer consumer;

    @Autowired
    public MusicEntityBuilder(ApiConsumer consumer) {
        this.consumer = consumer;
    }

    public Artist buildArtist(String url) throws IOException, MalformedRequestException, StatusCodeException, UnirestException {
        logger.info("Building Artist \"" + url + "\".");

        val artist = new Artist();

        val json = consumer.getArtist(HtmlScraper.scrapeArtistId(url));

        artist.setApiId(json.getLong(ID));
        artist.setName(getString(json, NAME).toLowerCase());
        artist.setGeniusUrl(getString(json, URL));
        artist.setImageUrl(getString(json, IMAGE_URL));

        artist.setAlbums(buildAlbumList(artist));

        artist.setWordFrequencies(LyricAnalyser.parseArtistLyrics(artist));
        buildStatistics(artist);
        artist.setTrends(LyricAnalyser.generateArtistTrends(artist));

        logger.info("Artist \"" + artist.getName() + "\" was built successfully.");

        return artist;
    }

    private ArrayList<Album> buildAlbumList(Artist artist) throws MalformedRequestException, IOException {
        logger.info("Building Album list for Artist \"" + artist.getName() + "\".");

        var albums = new ArrayList<Album>();

        for (var id : HtmlScraper.scrapeAlbumIdList(artist.getApiId())) {
            try {
                albums.add(buildAlbum(consumer.getAlbum(id), artist));
            } catch (Exception e) {
                logger.debug("Album \"" + id + "\" has been excluded from analysis, as it threw a fatal exception: \"" + e.toString() + "\".");
            }
        }

        Collections.sort(albums);

        logger.info("Album list for Artist \"" + artist.getName() + "\" was built successfully.");

        return albums;
    }

    private Album buildAlbum(JSONObject json, Artist artist) throws MalformedRequestException, IOException {
        logger.info("Building Album \"" + getString(json, NAME).toLowerCase() + "\".");

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
        album.setTrends(LyricAnalyser.generateAlbumTrends(album));

        logger.info("Album \"" + album.getName() + "\" was built successfully.");

        return album;
    }

    private ArrayList<Track> buildTrackList(Album album) throws MalformedRequestException, IOException {
        logger.info("Building Track list for Album \"" + album.getName() + "\".");

        val tracks = new ArrayList<Track>();

        for (var trackData : HtmlScraper.scrapeTrackList(album.getGeniusUrl())) {
            try {
                val json = consumer.getTrack(trackData.getId());
                tracks.add(buildTrack(json, album, trackData.getLyrics()));
            } catch (Exception e) {
                logger.debug("Track \"" + trackData.getId() + "\" has been excluded from analysis, as it threw a fatal exception: \"" + e.toString() + "\".");
            }
        }

        logger.info("Track list for Album \"" + album.getName() + "\" was built successfully.");

        return tracks;
    }

    private Track buildTrack(JSONObject json, Album album, String lyrics) {
        logger.info("Building Track \"" + getString(json, TITLE).toLowerCase() + "\".");

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

        track.setWordFrequencies(LyricAnalyser.parseTrackLyrics(track));
        buildStatistics(track);

        logger.info("Track \"" + track.getName() + "\" was built successfully.");

        return track;
    }

    private void buildStatistics(Music music) {
        logger.debug("Building statistics for " + music.getClass().getSimpleName() + " \"" + music.getName() + "\".");

        val statistics = LyricAnalyser.generateStatistics(music);

        music.setUniqueWordCount(statistics.getUniqueWordCount());
        music.setWordCount(statistics.getWordCount());
        music.setUniqueWordDensity(statistics.getUniqueWordDensity());

        logger.debug("Statistics have been built successfully for " + music.getClass().getSimpleName() + " \"" + music.getName() + "\".");
    }

    private String getString(JSONObject json, String key) {
        try {
            return json.getString(key);
        } catch (JSONException e) {
            logger.debug("Property key \"" + key + "\" could not be found. It has been replaced with a default value.");
            return "N/A";
        }
    }
}
