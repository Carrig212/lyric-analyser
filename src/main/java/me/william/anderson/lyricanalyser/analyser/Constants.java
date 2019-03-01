package me.william.anderson.lyricanalyser.analyser;

import java.util.regex.Pattern;

abstract class Constants {

    // Lyric Analyser constants
    static final Pattern SONG_MARKERS = Pattern.compile("\\[[^\\[]*]");       // Matches [anything]
    static final Pattern PUNCTUATION = Pattern.compile("(?!['])\\p{Punct}"); // Matches all punctuation
    static final Pattern MULTIPLIERS = Pattern.compile("[x][0-9]+");         // Matches x1, x2, etc...
    static final Pattern CLOSE_QUOTES = Pattern.compile("\\p{Pf}+");          // Matches all opening quotes
    static final Pattern OPEN_QUOTES = Pattern.compile("\\p{Pi}+");          // Matches all closing quotes

    static final String WHITE_SPACE = "\\p{Space}+"; // Matches one or more consecutive whitespace characters
    static final String REPLACEMENT = " ";           // Whitespace for replacing matched characters
}
