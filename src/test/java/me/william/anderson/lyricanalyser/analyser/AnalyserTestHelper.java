package me.william.anderson.lyricanalyser.analyser;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import me.william.anderson.lyricanalyser.model.Album;
import me.william.anderson.lyricanalyser.model.Artist;
import me.william.anderson.lyricanalyser.model.Track;
import me.william.anderson.lyricanalyser.model.data.StatisticsModel;

import lombok.val;

class AnalyserTestHelper {

    static final String CLEAN_LYRICS             = "hello' world hello world hello";
    static final String LYRICS_WITH_QUOTES       = "\"hello' world \"hello\" \"\" world hello\"";
    static final String LYRICS_WITH_SONG_MARKERS = "[Verse] hello' [Chorus][Bridge] world hello world hello";
    static final String LYRICS_WITH_MULTIPLIERS  = "hello' x1 world x2 hellox3 world x4x5 hello";
    static final String LYRICS_WITH_PUNCTUATION  = "hello'.!#$%&()*+,-./:;<=>?@[]^_`{|}~ world hello-world hello";
    static final String LYRICS_WITH_CAPITALS     = "hello' World heLLo wOrld hEllo";
    static final String LYRICS_WITH_WHITESPACE   = "   hello'    world   hello     world     hello    ";

    static final String [] CLEAN_LYRICS_ARRAY = {"hello'", "world", "hello", "world", "hello"};

    static final LinkedHashMap<String, Integer> EMPTY_HASH_MAP = new LinkedHashMap<>();

    private static final String HELLO = "hello";
    private static final String WORLD = "world";
    private static final String NAME_1 = "foo";
    private static final String NAME_2 = "bar";
    private static final int HELLO_COUNT = 3;
    private static final int WORLD_COUNT = 2;

    static Map<String, Integer> createWordFrequencies() {
        val wordFrequencies = new LinkedHashMap<String, Integer>();

        wordFrequencies.put(HELLO, HELLO_COUNT);
        wordFrequencies.put(WORLD, WORLD_COUNT);

        return wordFrequencies;
    }

    static Map<String, Integer> createDoubleWordFrequencies() {
        val wordFrequencies = new LinkedHashMap<String, Integer>();

        wordFrequencies.put(HELLO, HELLO_COUNT * 2);
        wordFrequencies.put(WORLD, WORLD_COUNT * 2);

        return wordFrequencies;
    }

    static Artist createArtist_EmptyAlbums() {
        val artist = new Artist();
        artist.setName(NAME_1);

        artist.setAlbums(new ArrayList<>());

        return artist;
    }

    static Artist createArtist_Albums_IncreasingDensities() {
        val artist = new Artist();
        artist.setName(NAME_1);

        val albums = new ArrayList<Album>();

        for (var i = 1; i < 6; i++) {
            val album = new Album();

            album.setName(NAME_2 + i);
            album.setUniqueWordDensity(i * 1.0f);

            albums.add(album);
        }

        artist.setAlbums(albums);

        return artist;
    }

    static Artist createArtist_Albums_DecreasingDensities() {
        val artist = new Artist();
        artist.setName(NAME_1);

        val albums = new ArrayList<Album>();

        for (int i = 1, j = 6; i < 6; i++, j--) {
            val album = new Album();

            album.setName(NAME_2 + i);
            album.setUniqueWordDensity(j * 1.0f);

            albums.add(album);
        }

        artist.setAlbums(albums);

        return artist;
    }

    static Artist createArtist_Albums_SameDensities() {
        val artist = new Artist();
        artist.setName(NAME_1);

        val albums = new ArrayList<Album>();

        for (int i = 1; i < 6; i++) {
            val album = new Album();

            album.setName(NAME_2 + i);
            album.setUniqueWordDensity(1.0f);

            albums.add(album);
        }

        artist.setAlbums(albums);

        return artist;
    }

    static Album createAlbum_EmptyTracks() {
        val album = new Album();
        album.setName(NAME_1);

        album.setTracks(new ArrayList<>());

        return album;
    }

    static Album createAlbum_Tracks_IncreasingDensities() {
        val album = new Album();
        album.setName(NAME_1);

        val tracks = new ArrayList<Track>();

        for (var i = 1; i < 6; i++) {
            val track = new Track();

            track.setName(NAME_2 + i);
            track.setUniqueWordDensity(i * 1.0f);

            tracks.add(track);
        }

        album.setTracks(tracks);

        return album;
    }

    static Album createAlbum_Tracks_DecreasingDensities() {
        val album = new Album();
        album.setName(NAME_1);

        val tracks = new ArrayList<Track>();

        for (int i = 1, j = 6; i < 6; i++, j--) {
            val track = new Track();

            track.setName(NAME_2 + i);
            track.setUniqueWordDensity(j * 1.0f);

            tracks.add(track);
        }

        album.setTracks(tracks);

        return album;
    }

    static Album createAlbum_Tracks_SameDensities() {
        val album = new Album();
        album.setName(NAME_1);

        val tracks = new ArrayList<Track>();

        for (int i = 1; i < 6; i++) {
            val track = new Track();

            track.setName(NAME_2 + i);
            track.setUniqueWordDensity(1.0f);

            tracks.add(track);
        }

        album.setTracks(tracks);

        return album;
    }

    static LinkedHashMap<String, Float> createIncreasingTrends() {
        val trends = new LinkedHashMap<String, Float>();

        for (var i = 1; i < 6; i++) {
            trends.put(NAME_2 + i, 1.0f);
        }

        return trends;
    }

    static LinkedHashMap<String, Float> createDecreasingTrends() {
        val trends = new LinkedHashMap<String, Float>();

        for (var i = 1; i < 6; i++) {
            trends.put(NAME_2 + i, (i == 1 ? 6.0f : -1.0f));
        }

        return trends;
    }

    static LinkedHashMap<String, Float> createSameDensityTrends() {
        val trends = new LinkedHashMap<String, Float>();

        for (var i = 1; i < 6; i++) {
            trends.put(NAME_2 + i, (i == 1 ? 1.0f : 0.0f));
        }

        return trends;
    }

    static Artist createMusic_EmptyWordFrequencies() {
        val artist = new Artist();
        artist.setName(NAME_1);

        artist.setWordFrequencies(EMPTY_HASH_MAP);

        return artist;
    }

    static Artist createMusic_NonEmptyWordFrequencies() {
        val artist = new Artist();
        artist.setName(NAME_1);

        val wordFrequencies = new LinkedHashMap<String, Integer>();

        wordFrequencies.put(HELLO, HELLO_COUNT);
        wordFrequencies.put(WORLD, WORLD_COUNT);

        artist.setWordFrequencies(wordFrequencies);

        return artist;
    }

    static StatisticsModel createEmptyStatisticsModel() {
        return new StatisticsModel(0, 0, 0.0f);
    }

    static StatisticsModel createNonEmptyStatisticsModel() {
        return new StatisticsModel(5, 2, 40.0f);
    }

}
