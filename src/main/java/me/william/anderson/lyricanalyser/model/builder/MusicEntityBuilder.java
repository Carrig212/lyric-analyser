package me.william.anderson.lyricanalyser.model.builder;

import java.util.ArrayList;
import java.util.Map;

import me.william.anderson.lyricanalyser.analyser.LyricAnalyser;
import me.william.anderson.lyricanalyser.api.ApiConsumer;
import me.william.anderson.lyricanalyser.exception.StatusCodeException;
import me.william.anderson.lyricanalyser.model.Album;
import me.william.anderson.lyricanalyser.model.Artist;
import me.william.anderson.lyricanalyser.model.Music;
import me.william.anderson.lyricanalyser.model.Track;

import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MusicEntityBuilder {

    private final ApiConsumer consumer;
    private final LyricAnalyser analyser;

    @Autowired
    public MusicEntityBuilder(ApiConsumer consumer, LyricAnalyser analyser) {
        this.consumer = consumer;
        this.analyser = analyser;
    }

    public Artist buildArtist(long artistId) throws StatusCodeException, UnirestException {
        var artist = new Artist();
        var jsonArtist = consumer.getArtist(artistId);

        artist.setName(jsonArtist.getString("artist_name"));
        artist.setApiId(jsonArtist.getLong("artist_id"));
        artist.setRating(jsonArtist.getInt("artist_rating"));
        artist.setCountry(jsonArtist.getString("artist_country"));

        artist.setGenres(buildGenres(jsonArtist));
        artist.setAlbums(buildAlbumList(artistId, artist));
        artist.setWordFrequencies(analyser.parseArtistLyrics(artist));

        buildStatistics(artist);

        return artist;
    }

    private ArrayList<Album> buildAlbumList(long artistId, Artist artist) throws StatusCodeException, UnirestException {
        var albums = new ArrayList<Album>();
        var jsonAlbums = consumer.getArtistAlbums(artistId);

        for (JSONObject jsonAlbum : jsonAlbums) {
            albums.add(buildAlbum(jsonAlbum, artist));
        }

        return albums;
    }

    private Album buildAlbum(JSONObject jsonAlbum, Artist artist) throws StatusCodeException, UnirestException {
        var album = new Album();

        album.setName(jsonAlbum.getString("album_name"));
        album.setApiId(jsonAlbum.getLong("album_id"));
        album.setRating(jsonAlbum.getInt("album_rating"));
        album.setArtist(artist);
        album.setLabel(jsonAlbum.getString("album_label"));
        album.setReleaseType(jsonAlbum.getString("album_release_type"));
        album.setReleaseDate(jsonAlbum.getString("album_release_date"));

        album.setGenres(buildGenres(jsonAlbum));
        album.setTracks(buildTrackList(album.getApiId(), album));
        album.setWordFrequencies(analyser.parseAlbumLyrics(album));

        buildStatistics(album);

        return album;
    }

    private ArrayList<Track> buildTrackList(long albumId, Album album) throws StatusCodeException, UnirestException {
        var tracks = new ArrayList<Track>();
        var jsonTracks = consumer.getAlbumTracks(albumId);

        for (JSONObject jsonTrack : jsonTracks) {
            tracks.add(buildTrack(jsonTrack, album));
        }

        return tracks;
    }

    private Track buildTrack(JSONObject jsonTrack, Album album) throws StatusCodeException, UnirestException {
        var track = new Track();

        track.setName(jsonTrack.getString("track_name"));
        track.setApiId(jsonTrack.getLong("track_id"));
        track.setRating(jsonTrack.getInt("track_rating"));
        track.setAlbum(album);
        track.setDuration(jsonTrack.getInt("track_length"));

        track.setGenres(buildGenres(jsonTrack));
        track.setWordFrequencies(buildLyrics(jsonTrack.getLong("track_id")));

        buildStatistics(track);

        return track;
    }

    private void buildStatistics(Music music) {
        var statistics = analyser.generateStatistics(music); // Returns an array list with three entries

        music.setUniqueWordCount((int) statistics.get(0)); // Unique word count
        music.setWordCount((int) statistics.get(1)); // Total word count
        music.setUniqueWordDensity((float) statistics.get(2)); // Unique word density
    }

    private Map<String, Integer> buildLyrics(long track_id) throws StatusCodeException, UnirestException {
        var rawLyrics = consumer.getTrackLyrics(track_id);

        return analyser.parseTrackLyrics(rawLyrics);
    }

    private ArrayList<String> buildGenres(JSONObject jsonArtist) {
        var genres = new ArrayList<String>();

        var primaryGenreArray = jsonArtist
                .getJSONObject("primary_genres")
                .getJSONArray("music_genre_list");

        var secondaryGenreArray = jsonArtist
                .getJSONObject("secondary_genres")
                .getJSONArray("music_genre_list");

        for (int i = 0; i < primaryGenreArray.length(); i++) {
            genres.add(primaryGenreArray
                    .getJSONObject(i)
                    .getJSONObject("music_genre")
                    .getString("music_genre_name"));
        }

        for (int i = 0; i < secondaryGenreArray.length(); i++) {
            genres.add(secondaryGenreArray
                    .getJSONObject(i)
                    .getJSONObject("music_genre")
                    .getString("music_genre_name"));
        }

        return genres;
    }
}
