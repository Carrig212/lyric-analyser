package me.william.anderson.lyricanalyser.analyser;

import java.util.*;

import me.william.anderson.lyricanalyser.model.Album;
import me.william.anderson.lyricanalyser.model.Artist;
import me.william.anderson.lyricanalyser.model.Music;
import me.william.anderson.lyricanalyser.model.Track;
import me.william.anderson.lyricanalyser.model.data.StatisticsModel;

import lombok.val;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static me.william.anderson.lyricanalyser.analyser.Constants.*;

public class LyricAnalyser {

    private static final Logger logger = LoggerFactory.getLogger(LyricAnalyser.class);

    private static Set<Long> apiIds = new HashSet<>();

    public static LinkedHashMap<String, Integer> parseTrackLyrics(Track track) {
        logger.debug("Parsing lyrics for Track \"" + track.getName() + "\".");

        val words = cleanLyrics(track.getLyrics());
        var wordFrequencies = new LinkedHashMap<String, Integer>();

        for (val word : words) {
            wordFrequencies.put(word, !wordFrequencies.containsKey(word) ? 1 : wordFrequencies.get(word) + 1); // Remove and count duplicates
        }

        wordFrequencies = Sorter.sortHashMapByValueDescending(wordFrequencies);

        logger.debug("Lyrics have been successfully parsed for Track \"" + track.getName() + "\".");

        return wordFrequencies;
    }

    public static LinkedHashMap<String, Integer> parseAlbumLyrics(Album album) {
        logger.debug("Parsing lyrics for Album \"" + album.getName() + "\".");

        var wordFrequencies = new LinkedHashMap<String, Integer>();

        for (val track : album.getTracks()) {
            if (track.isDuplicate() || apiIds.contains(track.getApiId())) {
                continue;
            }

            apiIds.add(track.getApiId());
            mergeWordFrequencies(track.getWordFrequencies(), wordFrequencies);
        }

        wordFrequencies = Sorter.sortHashMapByValueDescending(wordFrequencies);

        logger.debug("Lyrics have been successfully parsed for Album \"" + album.getName() + "\".");

        return wordFrequencies;
    }

    public static LinkedHashMap<String, Integer> parseArtistLyrics(Artist artist) {
        logger.debug("Parsing lyrics for Artist \"" + artist.getName() + "\".");

        var wordFrequencies = new LinkedHashMap<String, Integer>();

        for (val album : artist.getAlbums()) {
            if (album.isDuplicate() || apiIds.contains(album.getApiId())) {
                continue;
            }

            apiIds.add(album.getApiId());
            mergeWordFrequencies(album.getWordFrequencies(), wordFrequencies);
        }

        wordFrequencies = Sorter.sortHashMapByValueDescending(wordFrequencies);
        apiIds = new HashSet<>();

        logger.debug("Lyrics have been successfully parsed for Artist \"" + artist.getName() + "\".");

        return wordFrequencies;
    }

    public static StatisticsModel generateStatistics(Music music) {
        logger.debug("Generating statistics for " + music.getClass().getSimpleName() + " \"" + music.getName() + "\".");

        val uniqueWordCount = music.getWordFrequencies().size();
        var wordCount = 0;

        for (val entry : music.getWordFrequencies().entrySet()) { // Iterate over the word frequencies
            wordCount += entry.getValue();
        }

        val uniqueWordDensity = wordCount == 0 ? 0.0f : (((float) uniqueWordCount / (float) wordCount) * 100.0f);

        logger.debug("Statistics have been generated successfully for " + music.getClass().getSimpleName() + " \"" + music.getName() + "\".");

        return new StatisticsModel(wordCount, uniqueWordCount, uniqueWordDensity);
    }

    public static LinkedHashMap<String, Float> generateArtistTrends(Artist artist) {
        logger.debug("Generating trends for Artist \"" + artist.getName() + "\".");

        val trends = new LinkedHashMap<String, Float>();
        var previous = 0.0f;

        for (val album : artist.getAlbums()) {
            val current = album.getUniqueWordDensity();
            trends.put(album.getName(), (current - previous));

            previous = current;
        }

        logger.debug("Trends have been generated successfully for Artist \"" + artist.getName() + "\".");

        return trends;
    }

    public static LinkedHashMap<String, Float> generateAlbumTrends(Album album) {
        logger.debug("Generating trends for Album \"" + album.getName() + "\".");

        val trends = new LinkedHashMap<String, Float>();
        var previous = 0.0f;

        for (val track : album.getTracks()) {
            val current = track.getUniqueWordDensity();
            trends.put(track.getName(), (current - previous));

            previous = current;
        }

        logger.debug("Trends have been generated successfully for Album \"" + album.getName() + "\".");

        return trends;
    }

    static void mergeWordFrequencies(Map<String, Integer> wordFrequencies, Map<String, Integer> totalWordFrequencies) {
        for (val entry : wordFrequencies.entrySet()) { // Iterate over the word frequencies
            if (totalWordFrequencies.containsKey(entry.getKey())) { // Remove and count duplicates
                totalWordFrequencies.put(entry.getKey(), (totalWordFrequencies.get(entry.getKey()) + entry.getValue()));
            } else { // Add unique entries
                totalWordFrequencies.put(entry.getKey(), entry.getValue());
            }
        }
    }

    static ArrayList<String> cleanLyrics(String rawString) {

        rawString = rawString.toLowerCase(); // Convert the lyrics to lower case
        rawString = SONG_MARKERS.matcher(rawString).replaceAll(REPLACEMENT); // Remove all song structure markers
        rawString = MULTIPLIERS.matcher(rawString).replaceAll(REPLACEMENT);  // Remove all multipliers
        rawString = OPEN_QUOTES.matcher(rawString).replaceAll(REPLACEMENT);  // Remove all opening quotes
        rawString = CLOSE_QUOTES.matcher(rawString).replaceAll(REPLACEMENT); // Remove all closing quotes
        rawString = PUNCTUATION.matcher(rawString).replaceAll(REPLACEMENT);  // Remove all other punctuation
        rawString = rawString.strip(); // Strip all leading and trailing whitespace

        return new ArrayList<>(Arrays.asList(rawString.split(WHITE_SPACE))); // Split on one or more whitespace
    }
}
