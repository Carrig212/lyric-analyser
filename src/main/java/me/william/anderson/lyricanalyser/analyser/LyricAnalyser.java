package me.william.anderson.lyricanalyser.analyser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import me.william.anderson.lyricanalyser.model.Album;
import me.william.anderson.lyricanalyser.model.Artist;
import me.william.anderson.lyricanalyser.model.Music;
import me.william.anderson.lyricanalyser.model.Track;

import org.springframework.stereotype.Component;

@Component
public class LyricAnalyser {

    private static final Pattern REGEX = Pattern.compile("[a-z']+");
    private static final String DISCLAIMER = "\n\n******* This Lyrics is NOT for Commercial use *******";

    public HashMap<String, Integer> parseTrackLyrics(String rawString) {
        var uniqueWords = new HashMap<String, Integer>();
        var words = cleanLyrics(rawString);

        for (var word : words) {
            uniqueWords.put(word, !uniqueWords.containsKey(word) ? 1 : uniqueWords.get(word) + 1); // Remove and count duplicates
        }

        return uniqueWords;
    }

    public HashMap<String, Integer> parseAlbumLyrics(Album album) {
        var wordFrequencies = new HashMap<String, Integer>();

        for (Track track : album.getTracks()) {
            mergeWordFrequencies(track.getWordFrequencies(), wordFrequencies);
        }

        return wordFrequencies;
    }

    public HashMap<String, Integer> parseArtistLyrics(Artist artist) {
        var wordFrequencies = new HashMap<String, Integer>();

        for (var album : artist.getAlbums()) {
            mergeWordFrequencies(album.getWordFrequencies(), wordFrequencies);
        }

        return wordFrequencies;
    }

    public ArrayList<Object> generateStatistics(Music music) {
        var uniqueWordCount = music.getWordFrequencies().size();
        var wordCount = 0;

        for (Entry<String, Integer> entry : music.getWordFrequencies().entrySet()) { // Iterate over the word frequencies
            wordCount += entry.getValue();
        }

        var statistics = new ArrayList<>();

        statistics.add(uniqueWordCount);
        statistics.add(wordCount);
        statistics.add((wordCount == 0 ? 0.0F : (((float) uniqueWordCount / (float) wordCount) * 100.0F)));

        return statistics;
    }

    private void mergeWordFrequencies(Map<String, Integer> wordFrequencies, Map<String, Integer> totalWordFrequencies) {
        for (Entry<String, Integer> entry : wordFrequencies.entrySet()) { // Iterate over the word frequencies
            if (totalWordFrequencies.containsKey(entry.getKey())) { // Remove and count duplicates
                totalWordFrequencies.put(entry.getKey(), (totalWordFrequencies.get(entry.getKey()) + entry.getValue()));
            } else { // Add unique entries
                totalWordFrequencies.put(entry.getKey(), entry.getValue());
            }
        }
    }

    private ArrayList<String> cleanLyrics(String rawString) {
        var words = new ArrayList<String>();

        if (!rawString.equals("")) {
            rawString = rawString.substring(0, rawString.indexOf(DISCLAIMER)); // Remove the disclaimer from the lyrics
            rawString = rawString.toLowerCase();
        }

        var matcher = REGEX.matcher(rawString);

        while (matcher.find()) {
            words.add(matcher.group()); // Get all of the individual words from the lyrics
        }

        return words;
    }
}
