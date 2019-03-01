package me.william.anderson.lyricanalyser.controller;

import java.io.IOException;
import java.util.stream.Collectors;

import me.william.anderson.lyricanalyser.api.HtmlScraper;
import me.william.anderson.lyricanalyser.exception.MalformedRequestException;
import me.william.anderson.lyricanalyser.model.Album;
import me.william.anderson.lyricanalyser.model.Artist;
import me.william.anderson.lyricanalyser.model.data.ArtistSubmissionModel;
import me.william.anderson.lyricanalyser.model.manager.DuplicateRemover;
import me.william.anderson.lyricanalyser.model.manager.MusicEntityBuilder;
import me.william.anderson.lyricanalyser.repository.AlbumRepository;
import me.william.anderson.lyricanalyser.repository.ArtistRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.val;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static me.william.anderson.lyricanalyser.controller.Constants.*;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@SuppressWarnings("Duplicates")
@RestController
@RequestMapping(ARTISTS_ROUTE)
public class ArtistController {

    private static final Logger logger = LoggerFactory.getLogger(ArtistController.class);

    private final ArtistRepository artistRepository;
    private final AlbumRepository albumRepository;
    private final MusicEntityBuilder builder;
    private final DuplicateRemover duplicateRemover;

    @Autowired
    ArtistController(ArtistRepository artistRepository, AlbumRepository albumRepository, MusicEntityBuilder builder, DuplicateRemover duplicateRemover) {
        this.artistRepository = artistRepository;
        this.albumRepository = albumRepository;
        this.builder = builder;
        this.duplicateRemover = duplicateRemover;
    }

    @GetMapping
    public ResponseEntity<Resources<Resource<Artist>>> findAll() {

        val artists = artistRepository.findAll().stream()
                .map(artist -> new Resource<>(artist,
                        linkTo(methodOn(ArtistController.class).findOne(artist.getId())).withSelfRel(),
                        linkTo(methodOn(ArtistController.class).findAlbums(artist.getId())).withRel(ALBUMS_REL),
                        linkTo(methodOn(ArtistController.class).findAll()).withRel(ARTISTS_REL)))
                .collect(Collectors.toList());

        return ResponseEntity.ok(
                new Resources<>(artists,
                        linkTo(methodOn(ArtistController.class).findAll()).withSelfRel()));
    }

    @GetMapping(SEARCH_ROUTE)
    public ResponseEntity<Resources<Resource<Artist>>> search(@RequestParam(NAME_PARAM) String name) {
        val artists = artistRepository.findAllByNameIsLike(name).stream()
                .map(artist -> new Resource<>(artist,
                        linkTo(methodOn(ArtistController.class).findOne(artist.getId())).withSelfRel(),
                        linkTo(methodOn(ArtistController.class).findAlbums(artist.getId())).withRel(ALBUMS_REL),
                        linkTo(methodOn(ArtistController.class).findAll()).withRel(ARTISTS_REL)))
                .collect(Collectors.toList());

        return ResponseEntity.ok(
                new Resources<>(artists,
                        linkTo(methodOn(ArtistController.class).search(name)).withSelfRel()));
    }

    @GetMapping(FIND_ONE_ROUTE)
    public ResponseEntity<Resource<Artist>> findOne(@PathVariable long id) {

        return artistRepository.findById(id)
                .map(artist -> new Resource<>(artist,
                        linkTo(methodOn(ArtistController.class).findOne(artist.getId())).withSelfRel(),
                        linkTo(methodOn(ArtistController.class).findAlbums(artist.getId())).withRel(ALBUMS_REL),
                        linkTo(methodOn(ArtistController.class).findAll()).withRel(ARTISTS_REL)))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping(FIND_ALBUMS_ROUTE)
    public ResponseEntity<Resources<Resource<Album>>> findAlbums(@PathVariable long id) {
        val albums = albumRepository.findAllByArtistId(id).stream()
                .map(album -> new Resource<>(album,
                        linkTo(methodOn(AlbumController.class).findOne(album.getId())).withSelfRel(),
                        linkTo(methodOn(AlbumController.class).findAll()).withRel(ALBUMS_REL),
                        linkTo(methodOn(ArtistController.class).findOne(id)).withRel(ARTIST_REL),
                        linkTo(methodOn(AlbumController.class).findTracks(album.getId())).withRel(TRACKS_REL)))
                .collect(Collectors.toList());

        return ResponseEntity.ok(
                new Resources<>(albums,
                        linkTo(methodOn(ArtistController.class).findAlbums(id)).withSelfRel(),
                        linkTo(methodOn(ArtistController.class).findOne(id)).withRel(ARTIST_REL)));
    }

    @PostMapping(path = SUBMIT_ARTIST_ROUTE, consumes = ACCEPT_JSON)
    public ResponseEntity<Resource<Artist>> create(@RequestBody ArtistSubmissionModel submission) {
        val url = submission.getUrl();
        var apiId = 0L;

        try {
            apiId = HtmlScraper.scrapeArtistId(url);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (MalformedRequestException e) {
            return ResponseEntity.badRequest().build();
        }

        var artist = artistRepository.findByApiId(apiId);

        if (artist != null) {
            return findOne(artist.getId()); // If the artist already exists in the database, return that artist
        }

        try {
            artist = builder.buildArtist(url); // Otherwise, analyse the artist
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        artistRepository.save(artist);

        return ResponseEntity.created(
                linkTo(methodOn(ArtistController.class).findOne(artist.getId())).toUri()).build();

    }

    @PutMapping(UPDATE_ROUTE)
    public ResponseEntity<Resource<Artist>> update(@PathVariable long id) {
        val artist = artistRepository.getOne(id);

        if (artist == null) {
            return ResponseEntity.notFound().build();
        }

        duplicateRemover.removeDuplicates(artist);
        artistRepository.save(artist);

        return ResponseEntity.noContent().build();
    }
}