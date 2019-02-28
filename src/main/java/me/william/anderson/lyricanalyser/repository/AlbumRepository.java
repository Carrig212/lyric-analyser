package me.william.anderson.lyricanalyser.repository;

import java.util.List;

import me.william.anderson.lyricanalyser.model.Album;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlbumRepository extends JpaRepository<Album, Long> {
    List<Album> findAllByArtistId(long id); // find albums method on artist controller

    List<Album> findAllByNameIsLike(String name); // Search method on album controller
}
