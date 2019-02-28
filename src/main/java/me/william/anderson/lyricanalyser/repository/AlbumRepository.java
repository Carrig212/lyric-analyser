package me.william.anderson.lyricanalyser.repository;

import me.william.anderson.lyricanalyser.model.Album;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlbumRepository extends JpaRepository<Album, Long> {
    List<Album> findAllByArtistId(long id); // find albums method on artist controller
    List<Album> findAllByNameIsLike(String name); // Search method on album controller
}
