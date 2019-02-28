package me.william.anderson.lyricanalyser.repository;

import me.william.anderson.lyricanalyser.model.Track;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrackRepository extends JpaRepository<Track, Long> {
    List<Track> findAllByAlbumId(long id); // Find tracks method on album controller
    List<Track> findAllByNameIsLike(String name); // Search method on track controller
}
