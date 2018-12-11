package me.william.anderson.lyricanalyser.repository;

import me.william.anderson.lyricanalyser.model.Album;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlbumRepository extends JpaRepository<Album, Long> {
}
