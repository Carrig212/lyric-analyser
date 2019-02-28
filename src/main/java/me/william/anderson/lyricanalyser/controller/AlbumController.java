package me.william.anderson.lyricanalyser.controller;

import me.william.anderson.lyricanalyser.model.Album;
import me.william.anderson.lyricanalyser.model.Artist;
import me.william.anderson.lyricanalyser.model.Track;
import me.william.anderson.lyricanalyser.repository.AlbumRepository;
import me.william.anderson.lyricanalyser.repository.TrackRepository;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@SuppressWarnings("Duplicates")
@RestController
@RequestMapping("/albums")
public class AlbumController {

    private final AlbumRepository albumRepository;
    private final TrackRepository trackRepository;

    AlbumController(AlbumRepository albumRepository, TrackRepository trackRepository) {
        this.albumRepository = albumRepository;
        this.trackRepository = trackRepository;
    }

    @GetMapping("")
    public ResponseEntity<Resources<Resource<Album>>> findAll() {

        List<Resource<Album>> albums = albumRepository.findAll().stream()
                .map(album -> new Resource<>(album,
                        linkTo(methodOn(AlbumController.class).findOne(album.getId())).withSelfRel(),
                        linkTo(methodOn(ArtistController.class).findOne(album.getArtist().getId())).withRel("artist"),
                        linkTo(methodOn(AlbumController.class).findTracks(album.getId())).withRel("tracks"),
                        linkTo(methodOn(AlbumController.class).findAll()).withRel("albums")))
                .collect(Collectors.toList());

        return ResponseEntity.ok(
                new Resources<>(albums,
                        linkTo(methodOn(AlbumController.class).findAll()).withSelfRel()));
    }

    @GetMapping("/search")
    public ResponseEntity<Resources<Resource<Album>>> search(@RequestParam("name") String name) {
        List<Resource<Album>> albums = albumRepository.findAllByNameIsLike(name).stream()
                .map(album -> new Resource<>(album,
                        linkTo(methodOn(AlbumController.class).findOne(album.getId())).withSelfRel(),
                        linkTo(methodOn(ArtistController.class).findOne(album.getArtist().getId())).withRel("artist"),
                        linkTo(methodOn(AlbumController.class).findTracks(album.getId())).withRel("tracks"),
                        linkTo(methodOn(AlbumController.class).findAll()).withRel("albums")))
                .collect(Collectors.toList());

        return ResponseEntity.ok(
                new Resources<>(albums,
                        linkTo(methodOn(AlbumController.class).search(name)).withSelfRel())
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Resource<Album>> findOne(@PathVariable long id) {

        return albumRepository.findById(id)
                .map(album -> new Resource<>(album,
                        linkTo(methodOn(AlbumController.class).findOne(album.getId())).withSelfRel(),
                        linkTo(methodOn(ArtistController.class).findOne(album.getArtist().getId())).withRel("artist"),
                        linkTo(methodOn(AlbumController.class).findTracks(album.getId())).withRel("tracks"),
                        linkTo(methodOn(AlbumController.class).findAll()).withRel("albums")))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/tracks")
    public ResponseEntity<Resources<Resource<Track>>> findTracks(@PathVariable long id) {
        List<Resource<Track>> tracks = trackRepository.findAllByAlbumId(id).stream()
                .map(track -> new Resource<>(track,
                        linkTo(methodOn(TrackController.class).findOne(track.getId())).withSelfRel(),
                        linkTo(methodOn(AlbumController.class).findOne(id)).withRel("album")))
                .collect(Collectors.toList());

        return ResponseEntity.ok(
                new Resources<>(tracks,
                        linkTo(methodOn(AlbumController.class).findTracks(id)).withSelfRel(),
                        linkTo(methodOn(AlbumController.class).findOne(id)).withRel("album"))
        );
    }
}