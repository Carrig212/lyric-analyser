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
        logger.debug("Removing duplicates for Artist \"" + artist.getName() + "\".");

        Collection<Album> albums = artist.getAlbums();

        for (Album album : albums) {
            album.setWordFrequencies(LyricAnalyser.parseAlbumLyrics(album));
            buildStatistics(album);
            album.setTrends(LyricAnalyser.generateAlbumTrends(album));
        }

        artist.setWordFrequencies(LyricAnalyser.parseArtistLyrics(artist));
        buildStatistics(artist);
        artist.setTrends(LyricAnalyser.generateArtistTrends(artist));

        logger.debug("Duplicates have been successfully removed for Artist \"" + artist.getName() + "\".");
    }

    private void buildStatistics(Music music) {
        logger.debug("Rebuilding statistics for " + music.getClass().getSimpleName() + " \"" + music.getName() + "\".");

        val statistics = LyricAnalyser.generateStatistics(music);

        music.setUniqueWordCount(statistics.getUniqueWordCount());
        music.setWordCount(statistics.getWordCount());
        music.setUniqueWordDensity(statistics.getUniqueWordDensity());

        logger.debug("Statistics have been rebuilt successfully for " + music.getClass().getSimpleName() + " \"" + music.getName() + "\".");
    }
}
