package me.william.anderson.lyricanalyser.model.manager;

import java.util.Collection;

import me.william.anderson.lyricanalyser.analyser.LyricAnalyser;
import me.william.anderson.lyricanalyser.model.Album;
import me.william.anderson.lyricanalyser.model.Artist;
import me.william.anderson.lyricanalyser.model.Music;

import org.springframework.stereotype.Component;

import lombok.val;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class DuplicateRemover {

    private static final Logger logger = LoggerFactory.getLogger(DuplicateRemover.class);

    public void removeDuplicates(Artist artist) {
        Collection<Album> albums = artist.getAlbums();

        for (Album album : albums) {
            album.setWordFrequencies(LyricAnalyser.parseAlbumLyrics(album));
            buildStatistics(album);
            album.setTrends(LyricAnalyser.generateAlbumTrends(album));
        }

        artist.setWordFrequencies(LyricAnalyser.parseArtistLyrics(artist));
        buildStatistics(artist);
        artist.setTrends(LyricAnalyser.generateArtistTrends(artist));
    }

    private void buildStatistics(Music music) {
        val statistics = LyricAnalyser.generateStatistics(music);

        music.setUniqueWordCount(statistics.getUniqueWordCount());
        music.setWordCount(statistics.getWordCount());
        music.setUniqueWordDensity(statistics.getUniqueWordDensity());
    }
}
