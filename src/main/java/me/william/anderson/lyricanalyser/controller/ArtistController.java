package me.william.anderson.lyricanalyser.controller;

import me.william.anderson.lyricanalyser.exception.EntityNotFoundException;
import me.william.anderson.lyricanalyser.model.Artist;
import me.william.anderson.lyricanalyser.repository.ArtistRepository;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ArtistController {

    private final ArtistRepository repository;

    ArtistController(ArtistRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/artists/{id}")
    public Artist getArtist(@PathVariable long id) {
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException(id, Artist.class));
    }
}
