package me.william.anderson.lyricanalyser.repository;

import me.william.anderson.lyricanalyser.model.Track;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrackRepository extends JpaRepository<Track, Long> {
}
