package me.william.anderson.lyricanalyser.analyser;

import me.william.anderson.lyricanalyser.api.ApiConstants;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

@Component
public class LyricAnalyser {

    private static final Pattern regex = Pattern.compile("[a-z']+");

    public HashMap<String, Integer> countUniqueWords(String rawLyrics) {
        var words = cleanLyrics(rawLyrics);
        var uniqueWords = new HashMap<String, Integer>();

        for (var word : words) {
            if (!uniqueWords.containsKey(word)) {
                uniqueWords.put(word, 1);
            } else {
                uniqueWords.put(word, uniqueWords.get(word) + 1);
            }
        }

        return uniqueWords;
    }

    private ArrayList<String> cleanLyrics(String rawString) {
        var words = new ArrayList<String>();

        rawString = rawString.substring(0, rawString.indexOf(ApiConstants.DISCLAIMER));
        rawString = rawString.toLowerCase();
        var matcher = regex.matcher(rawString);

        while (matcher.find()) {
            words.add(matcher.group());
        }

        return words;
    }
}
