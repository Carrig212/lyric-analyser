package me.william.anderson.lyricanalyser.controller;

import me.william.anderson.lyricanalyser.exception.EntityNotFoundException;
import me.william.anderson.lyricanalyser.model.Track;
import me.william.anderson.lyricanalyser.repository.TrackRepository;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TrackController {
    private final TrackRepository repository;

    TrackController(TrackRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/tracks/{id}")
    public Track getTrack(@PathVariable long id) {
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException(id, Track.class));
    }
}
