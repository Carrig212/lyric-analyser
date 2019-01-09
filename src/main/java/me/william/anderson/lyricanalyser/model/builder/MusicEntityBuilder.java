package me.william.anderson.lyricanalyser.model.builder;

import java.util.ArrayList;

import me.william.anderson.lyricanalyser.analyser.LyricAnalyser;
import me.william.anderson.lyricanalyser.api.ApiConsumer;
import me.william.anderson.lyricanalyser.api.HtmlScraper;
import me.william.anderson.lyricanalyser.exception.MalformedRequestException;
import me.william.anderson.lyricanalyser.exception.MalformedResponseException;
import me.william.anderson.lyricanalyser.exception.StatusCodeException;
import me.william.anderson.lyricanalyser.model.Album;
import me.william.anderson.lyricanalyser.model.Artist;
import me.william.anderson.lyricanalyser.model.Music;
import me.william.anderson.lyricanalyser.model.Track;

import com.mashape.unirest.http.exceptions.UnirestException;
import lombok.NonNull;
import lombok.val;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

    @Autowired
    public MusicEntityBuilder(ApiConsumer consumer) {
        this.consumer = consumer;
    }

    public Artist buildArtist(String url) throws StatusCodeException, MalformedResponseException, MalformedRequestException, UnirestException {
        val json = consumer.getArtist(HtmlScraper.scrapeArtistId(url));
        val artist = new Artist();

        artist.setApiId(json.getLong(ID));
        artist.setName(json.getString(NAME));
        artist.setGeniusUrl(json.getString(URL));
        artist.setImageUrl(json.getString(IMAGE_URL));

        artist.setAlbums(buildAlbumList(artist));

        artist.setWordFrequencies(LyricAnalyser.parseArtistLyrics(artist));
        buildStatistics(artist);

        return artist;
    }

    private ArrayList<Album> buildAlbumList(Artist artist) throws StatusCodeException, UnirestException, MalformedRequestException, MalformedResponseException {
        val albums = new ArrayList<Album>();

        for (var id : HtmlScraper.scrapeAlbumIdList(artist.getApiId())) {
            albums.add(buildAlbum(consumer.getAlbum(id), artist));
        }

        return albums;
    }

    private Album buildAlbum(JSONObject json, Artist artist) throws StatusCodeException, UnirestException, MalformedResponseException, MalformedRequestException {
        val album = new Album();

        album.setApiId(json.getLong(ID));
        album.setName(json.getString(NAME));
        album.setGeniusUrl(json.getString(URL));
        album.setImageUrl(json.getString(COVER_URL));

        album.setReleaseDate(json.getString(RELEASE_DATE));

        album.setArtist(artist);
        album.setTracks(buildTrackList(album));

        album.setWordFrequencies(LyricAnalyser.parseAlbumLyrics(album));
        buildStatistics(album);

        return album;
    }

    private ArrayList<Track> buildTrackList(Album album) throws StatusCodeException, UnirestException, MalformedRequestException, MalformedResponseException {
        val tracks = new ArrayList<Track>();

        for (var trackData : HtmlScraper.scrapeTrackList(album.getGeniusUrl())) {
            val json = consumer.getTrack(trackData.getId());
            tracks.add(buildTrack(json, album, trackData.getLyrics()));
        }

        return tracks;
    }

    private Track buildTrack(JSONObject json, Album album, String lyrics) {
        val track = new Track();

        track.setApiId(json.getLong(ID));
        track.setName(json.getString(TITLE));
        track.setGeniusUrl(json.getString(URL));
        track.setImageUrl(json.getString(SONG_ART_URL));

        val featuresJson = json.getJSONArray(FEATURED);
        val features = new ArrayList<String>();

        for (int i = 0; i < featuresJson.length(); i++) {
            features.add(featuresJson.getJSONObject(i).getString(NAME));
        }

        track.setFeaturedArtists(features);
        track.setLyricsState(json.getString(LYRICS_STATE));
        track.setLyrics(lyrics);

        track.setAlbum(album);

        track.setWordFrequencies(LyricAnalyser.parseTrackLyrics(track.getLyrics()));
        buildStatistics(track);

        return track;
    }

    private void buildStatistics(Music music) {
        val statistics = LyricAnalyser.generateStatistics(music);

        music.setUniqueWordCount(statistics.getUniqueWordCount());
        music.setWordCount(statistics.getWordCount());
        music.setUniqueWordDensity(statistics.getUniqueWordDensity());
    }
}
