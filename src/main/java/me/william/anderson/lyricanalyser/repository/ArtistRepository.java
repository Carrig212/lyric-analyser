package me.william.anderson.lyricanalyser.repository;

import java.util.List;

import me.william.anderson.lyricanalyser.model.Artist;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtistRepository extends JpaRepository<Artist, Long> {
    List<Artist> findAllByNameIsLike(String name); // Search method on artist controller

    Artist findByApiId(long apiId); // Submit method on artist controller
}
