package me.william.anderson.lyricanalyser.analyser;

import java.util.*;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import me.william.anderson.lyricanalyser.model.Album;
import me.william.anderson.lyricanalyser.model.Artist;
import me.william.anderson.lyricanalyser.model.Music;
import me.william.anderson.lyricanalyser.model.Track;
import me.william.anderson.lyricanalyser.model.data.Statistics;

import lombok.val;

public class LyricAnalyser {

    private static final Pattern BRACKET_REGEX = Pattern.compile("\\[[^\\[]*]");
    private static final Pattern WORD_REGEX = Pattern.compile("[a-z']+");
    private static final Pattern PUNCTUATION = Pattern.compile("(?!['])\\p{Punct}");

    public static HashMap<String, Integer> parseTrackLyrics(String rawString) {
        var wordFrequencies = new HashMap<String, Integer>();
        val words = cleanLyrics(rawString);

        for (var word : words) {
            wordFrequencies.put(word, !wordFrequencies.containsKey(word) ? 1 : wordFrequencies.get(word) + 1); // Remove and count duplicates
        }

        wordFrequencies = Sorter.sortHashMapByValueDescending(wordFrequencies);

        return wordFrequencies;
    }

    public static HashMap<String, Integer> parseAlbumLyrics(Album album) {
        var wordFrequencies = new HashMap<String, Integer>();

        for (Track track : album.getTracks()) {
            mergeWordFrequencies(track.getWordFrequencies(), wordFrequencies);
        }

        wordFrequencies = Sorter.sortHashMapByValueDescending(wordFrequencies);

        return wordFrequencies;
    }

    public static HashMap<String, Integer> parseArtistLyrics(Artist artist) {
        var wordFrequencies = new HashMap<String, Integer>();

        for (var album : artist.getAlbums()) {
            mergeWordFrequencies(album.getWordFrequencies(), wordFrequencies);
        }

        wordFrequencies = Sorter.sortHashMapByValueDescending(wordFrequencies);

        return wordFrequencies;
    }

    public static Statistics generateStatistics(Music music) {
        val uniqueWordCount = music.getWordFrequencies().size();
        var wordCount = 0;

        for (Entry<String, Integer> entry : music.getWordFrequencies().entrySet()) { // Iterate over the word frequencies
            wordCount += entry.getValue();
        }

        val uniqueWordDensity = wordCount == 0 ? 0.0F : (((float) uniqueWordCount / (float) wordCount) * 100.0F);

        return new Statistics(wordCount, uniqueWordCount, uniqueWordDensity);
    }

    private static void mergeWordFrequencies(Map<String, Integer> wordFrequencies, Map<String, Integer> totalWordFrequencies) {
        for (Entry<String, Integer> entry : wordFrequencies.entrySet()) { // Iterate over the word frequencies
            if (totalWordFrequencies.containsKey(entry.getKey())) { // Remove and count duplicates
                totalWordFrequencies.put(entry.getKey(), (totalWordFrequencies.get(entry.getKey()) + entry.getValue()));
            } else { // Add unique entries
                totalWordFrequencies.put(entry.getKey(), entry.getValue());
            }
        }
    }

    private static ArrayList<String> cleanLyrics(String rawString) {

        rawString = BRACKET_REGEX.matcher(rawString).replaceAll(" ");
        rawString = PUNCTUATION.matcher(rawString).replaceAll(" ");
        rawString = rawString.toLowerCase();

        /*val wordMatcher = WORD_REGEX.matcher(rawString);

        while (wordMatcher.find()) {
            words.add(wordMatcher.group()); // Get all of the individual words from the lyrics
        }*/

        return new ArrayList<>(Arrays.asList(rawString.split("[ ]+")));
    }
}
