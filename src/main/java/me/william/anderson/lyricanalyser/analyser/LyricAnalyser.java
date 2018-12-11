package me.william.anderson.lyricanalyser.analyser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

@Component
public class LyricAnalyser {

    private static final Pattern REGEX = Pattern.compile("[a-z']+");
    private static final String DISCLAIMER = "\n\n******* This Lyrics is NOT for Commercial use *******";

    public HashMap<String, Integer> countUniqueWords(String rawString) {
        var words = cleanLyrics(rawString);
        var uniqueWords = new HashMap<String, Integer>();

        for (var word : words) {
            uniqueWords.put(word, !uniqueWords.containsKey(word) ? 1 : uniqueWords.get(word) + 1);
        }

        return uniqueWords;
    }

    private ArrayList<String> cleanLyrics(String rawString) {
        var words = new ArrayList<String>();

        rawString = rawString.substring(0, rawString.indexOf(DISCLAIMER));
        rawString = rawString.toLowerCase();
        var matcher = REGEX.matcher(rawString);

        while (matcher.find()) {
            words.add(matcher.group());
        }

        return words;
    }
}
