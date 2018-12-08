package me.william.anderson.lyricanalyser.model.builder;

import com.mashape.unirest.http.exceptions.UnirestException;
import me.william.anderson.lyricanalyser.analyser.LyricAnalyser;
import me.william.anderson.lyricanalyser.api.ApiConsumer;
import me.william.anderson.lyricanalyser.exception.StatusCodeException;
import me.william.anderson.lyricanalyser.model.Album;
import me.william.anderson.lyricanalyser.model.Artist;
import me.william.anderson.lyricanalyser.model.Track;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Map;

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

        artist.setGenres(parseGenres(jsonArtist));

        return artist;
    }

    public ArrayList<Album> buildAlbumList(long artistId) throws StatusCodeException, UnirestException {
        var albums = new ArrayList<Album>();
        var jsonAlbums = consumer.getArtistAlbums(artistId);

        for (JSONObject jsonAlbum : jsonAlbums) {
            albums.add(buildAlbum(jsonAlbum));
        }

        return albums;
    }

    private Album buildAlbum(JSONObject jsonAlbum) {
        var album = new Album();

        album.setName(jsonAlbum.getString("album_name"));
        album.setApiId(jsonAlbum.getLong("album_id"));
        album.setRating(jsonAlbum.getInt("album_rating"));
        album.setLabel(jsonAlbum.getString("album_label"));
        album.setReleaseType(jsonAlbum.getString("album_release_type"));
        album.setReleaseDate(jsonAlbum.getString("album_release_date"));

        album.setGenres(parseGenres(jsonAlbum));

        return album;
    }

    public ArrayList<Track> buildTrackList(long albumId) throws StatusCodeException, UnirestException {
        var tracks = new ArrayList<Track>();
        var jsonTracks = consumer.getAlbumTracks(albumId);

        for (JSONObject jsonTrack : jsonTracks) {
            tracks.add(buildTrack(jsonTrack));
        }

        return tracks;
    }

    private Track buildTrack(JSONObject jsonTrack) throws StatusCodeException, UnirestException {
        var track = new Track();

        track.setName(jsonTrack.getString("track_name"));
        track.setApiId(jsonTrack.getLong("track_id"));
        track.setRating(jsonTrack.getInt("track_rating"));
        track.setDuration(jsonTrack.getInt("track_length"));

        track.setGenres(parseGenres(jsonTrack));
        track.setWordFrequencies(buildLyrics(jsonTrack.getLong("track_id")));

        return track;
    }

    private Map<String, Integer> buildLyrics(long track_id) throws StatusCodeException, UnirestException {
        var rawLyrics = consumer.getTrackLyrics(track_id);

        return analyser.countUniqueWords(rawLyrics);
    }

    private ArrayList<String> parseGenres(JSONObject jsonArtist) {
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
