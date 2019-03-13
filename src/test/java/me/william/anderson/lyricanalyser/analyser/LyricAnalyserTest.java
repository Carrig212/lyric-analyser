package me.william.anderson.lyricanalyser.analyser;

import java.util.Arrays;
import java.util.LinkedHashMap;

import lombok.val;
import org.junit.jupiter.api.Test;

import static me.william.anderson.lyricanalyser.analyser.AnalyserTestHelper.*;
import static me.william.anderson.lyricanalyser.analyser.LyricAnalyser.*;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("WeakerAccess")
public class LyricAnalyserTest {

    // cleanLyrics() tests
    @Test
    public void cleanCleanLyrics() {
        assertEquals(Arrays.asList(CLEAN_LYRICS_ARRAY), cleanLyrics(CLEAN_LYRICS));
    }

    @Test
    public void cleanLyrics_QuotationMarks() {
        assertEquals(Arrays.asList(CLEAN_LYRICS_ARRAY), cleanLyrics(LYRICS_WITH_QUOTES));
    }

    @Test
    public void cleanLyrics_SongMarkers() {
        assertEquals(Arrays.asList(CLEAN_LYRICS_ARRAY), cleanLyrics(LYRICS_WITH_SONG_MARKERS));
    }

    @Test
    public void cleanLyrics_Multipliers() {
        assertEquals(Arrays.asList(CLEAN_LYRICS_ARRAY), cleanLyrics(LYRICS_WITH_MULTIPLIERS));
    }

    @Test
    public void cleanLyrics_Punctuation() {
        assertEquals(Arrays.asList(CLEAN_LYRICS_ARRAY), cleanLyrics(LYRICS_WITH_PUNCTUATION));
    }

    @Test
    public void cleanLyrics_Capitals() {
        assertEquals(Arrays.asList(CLEAN_LYRICS_ARRAY), cleanLyrics(LYRICS_WITH_CAPITALS));
    }

    @Test
    public void cleanLyrics_Whitespace() {
        assertEquals(Arrays.asList(CLEAN_LYRICS_ARRAY), cleanLyrics(LYRICS_WITH_WHITESPACE));
    }

    // mergeWordFrequencies() tests
    @Test
    public void mergeTwoEmptyHashMaps() {
        val wordFrequencies = new LinkedHashMap<String, Integer>();
        val totalWordFrequencies = new LinkedHashMap<String, Integer>();

        mergeWordFrequencies(wordFrequencies, totalWordFrequencies);

        assertEquals(totalWordFrequencies, EMPTY_HASH_MAP);
        assertEquals(wordFrequencies, EMPTY_HASH_MAP);
    }

    @Test
    public void mergeEmptyTotalWordFrequencies() {
        val wordFrequencies = createWordFrequencies();
        val totalWordFrequencies = new LinkedHashMap<String, Integer>();

        mergeWordFrequencies(wordFrequencies, totalWordFrequencies);

        assertEquals(totalWordFrequencies, wordFrequencies);
    }

    @Test
    public void mergeEmptyWordFrequencies() {
        val wordFrequencies = new LinkedHashMap<String, Integer>();
        val totalWordFrequencies = createWordFrequencies();

        mergeWordFrequencies(wordFrequencies, totalWordFrequencies);

        assertEquals(totalWordFrequencies, createWordFrequencies());
        assertEquals(wordFrequencies, EMPTY_HASH_MAP);
    }

    @Test
    public void mergeDoubleWordFrequencies() {
        val wordFrequencies = createWordFrequencies();
        val totalWordFrequencies = createWordFrequencies();

        mergeWordFrequencies(wordFrequencies, totalWordFrequencies);

        assertEquals(totalWordFrequencies, createDoubleWordFrequencies());
        assertEquals(wordFrequencies, createWordFrequencies());
    }

    // generateArtistTrends() tests
    @Test
    public void generateArtistTrends_NoAlbums() {
        assertEquals(LyricAnalyser.generateArtistTrends(createArtist_EmptyAlbums()), EMPTY_HASH_MAP);
    }

    @Test
    public void generateArtistTrends_Albums_IncreasingDensities() {
        assertEquals(LyricAnalyser.generateArtistTrends(createArtist_Albums_IncreasingDensities()), createIncreasingTrends());
    }

    @Test
    public void generateArtistTrends_Albums_DecreasingDensities() {
        assertEquals(LyricAnalyser.generateArtistTrends(createArtist_Albums_DecreasingDensities()), createDecreasingTrends());
    }

    @Test
    public void generateArtistTrends_Albums_SameDensities() {
        assertEquals(LyricAnalyser.generateArtistTrends(createArtist_Albums_SameDensities()), createSameDensityTrends());
    }

    // generateAlbumTrends() tests
    @Test
    public void generateAlbumTrends_NoTracks() {
        assertEquals(LyricAnalyser.generateAlbumTrends(createAlbum_EmptyTracks()), EMPTY_HASH_MAP);
    }

    @Test
    public void generateAlbumTrends_Tracks_IncreasingDensities() {
        assertEquals(LyricAnalyser.generateAlbumTrends(createAlbum_Tracks_IncreasingDensities()), createIncreasingTrends());
    }

    @Test
    public void generateAlbumTrends_Tracks_DecreasingDensities() {
        assertEquals(LyricAnalyser.generateAlbumTrends(createAlbum_Tracks_DecreasingDensities()), createDecreasingTrends());
    }

    @Test
    public void generateAlbumTrends_Tracks_SameDensities() {
        assertEquals(LyricAnalyser.generateAlbumTrends(createAlbum_Tracks_SameDensities()), createSameDensityTrends());
    }

    // generateStatistics() tests
    @Test
    public void generateStatistics_EmptyWordFrequencies() {
        assertEquals(LyricAnalyser.generateStatistics(createMusic_EmptyWordFrequencies()), createEmptyStatisticsModel());
    }

    @Test
    public void generateStatistics_NonEmptyWordFrequencies() {
        assertEquals(LyricAnalyser.generateStatistics(createMusic_NonEmptyWordFrequencies()), createNonEmptyStatisticsModel());
    }
}
