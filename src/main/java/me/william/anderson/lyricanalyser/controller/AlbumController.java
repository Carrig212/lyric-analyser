package me.william.anderson.lyricanalyser.controller;

import java.util.stream.Collectors;

import me.william.anderson.lyricanalyser.model.Album;
import me.william.anderson.lyricanalyser.model.Track;
import me.william.anderson.lyricanalyser.repository.AlbumRepository;
import me.william.anderson.lyricanalyser.repository.TrackRepository;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.val;

import static me.william.anderson.lyricanalyser.controller.Constants.*;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@SuppressWarnings("Duplicates")
@RestController
@RequestMapping(ALBUMS_ROUTE)
public class AlbumController {

    private final AlbumRepository albumRepository;
    private final TrackRepository trackRepository;

    AlbumController(AlbumRepository albumRepository, TrackRepository trackRepository) {
        this.albumRepository = albumRepository;
        this.trackRepository = trackRepository;
    }

    @GetMapping
    public ResponseEntity<Resources<Resource<Album>>> findAll() {

        val albums = albumRepository.findAll().stream()
                .map(album -> new Resource<>(album,
                        linkTo(methodOn(AlbumController.class).findOne(album.getId())).withSelfRel(),
                        linkTo(methodOn(ArtistController.class).findOne(album.getArtist().getId())).withRel(ARTIST_REL),
                        linkTo(methodOn(AlbumController.class).findTracks(album.getId())).withRel(TRACKS_REL),
                        linkTo(methodOn(AlbumController.class).findAll()).withRel(ALBUMS_REL)))
                .collect(Collectors.toList());

        return ResponseEntity.ok(
                new Resources<>(albums,
                        linkTo(methodOn(AlbumController.class).findAll()).withSelfRel()));
    }

    @GetMapping(SEARCH_ROUTE)
    public ResponseEntity<Resources<Resource<Album>>> search(@RequestParam(NAME_PARAM) String name) {
        val albums = albumRepository.findAllByNameIsLike(name).stream()
                .map(album -> new Resource<>(album,
                        linkTo(methodOn(AlbumController.class).findOne(album.getId())).withSelfRel(),
                        linkTo(methodOn(ArtistController.class).findOne(album.getArtist().getId())).withRel(ARTIST_REL),
                        linkTo(methodOn(AlbumController.class).findTracks(album.getId())).withRel(TRACKS_REL),
                        linkTo(methodOn(AlbumController.class).findAll()).withRel(ALBUMS_REL)))
                .collect(Collectors.toList());

        return ResponseEntity.ok(
                new Resources<>(albums,
                        linkTo(methodOn(AlbumController.class).search(name)).withSelfRel()));
    }

    @GetMapping(FIND_ONE_ROUTE)
    public ResponseEntity<Resource<Album>> findOne(@PathVariable long id) {

        return albumRepository.findById(id)
                .map(album -> new Resource<>(album,
                        linkTo(methodOn(AlbumController.class).findOne(album.getId())).withSelfRel(),
                        linkTo(methodOn(ArtistController.class).findOne(album.getArtist().getId())).withRel(ARTIST_REL),
                        linkTo(methodOn(AlbumController.class).findTracks(album.getId())).withRel(TRACKS_REL),
                        linkTo(methodOn(AlbumController.class).findAll()).withRel(ALBUMS_REL)))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping(FIND_TRACKS_ROUTE)
    public ResponseEntity<Resources<Resource<Track>>> findTracks(@PathVariable long id) {
        val tracks = trackRepository.findAllByAlbumId(id).stream()
                .map(track -> new Resource<>(track,
                        linkTo(methodOn(TrackController.class).findOne(track.getId())).withSelfRel(),
                        linkTo(methodOn(AlbumController.class).findOne(id)).withRel(ALBUM_REL),
                        linkTo(methodOn(TrackController.class).findAll()).withRel(TRACKS_REL)))
                .collect(Collectors.toList());

        return ResponseEntity.ok(
                new Resources<>(tracks,
                        linkTo(methodOn(AlbumController.class).findTracks(id)).withSelfRel(),
                        linkTo(methodOn(AlbumController.class).findOne(id)).withRel(ALBUM_REL),
                        linkTo(methodOn(TrackController.class).findAll()).withRel(TRACKS_REL)));
    }

    @PutMapping(UPDATE_ROUTE)
    public ResponseEntity<Resource<Album>> markDuplicate(@PathVariable long id) {
        val album = albumRepository.getOne(id);

        if (album == null) {
            return ResponseEntity.notFound().build();
        }

        album.setDuplicate(!album.isDuplicate());
        albumRepository.save(album);

        return ResponseEntity.noContent().build();
    }
}