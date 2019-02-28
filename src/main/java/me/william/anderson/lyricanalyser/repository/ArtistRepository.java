package me.william.anderson.lyricanalyser.repository;

import me.william.anderson.lyricanalyser.model.Artist;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArtistRepository extends JpaRepository<Artist, Long> {
    List<Artist> findAllByNameIsLike(String name); // Search method on artist controller
    Artist findByApiId(long apiId); // Submit method on artist controller
}
