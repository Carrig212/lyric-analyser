package me.william.anderson.lyricanalyser.controller;

import me.william.anderson.lyricanalyser.exception.EntityNotFoundException;
import me.william.anderson.lyricanalyser.model.Album;
import me.william.anderson.lyricanalyser.repository.AlbumRepository;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AlbumController {

    private final AlbumRepository repository;

    AlbumController(AlbumRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/albums/{id}")
    public Album getAlbum(@PathVariable long id) {
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException(id, Album.class));
    }
}