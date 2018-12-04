package me.william.anderson.lyricanalyser.repository;

import me.william.anderson.lyricanalyser.model.Artist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtistRepository extends JpaRepository<Artist, Long> {
}
