package me.william.anderson.lyricanalyser.controller;

import me.william.anderson.lyricanalyser.api.HtmlScraper;
import me.william.anderson.lyricanalyser.exception.MalformedRequestException;
import me.william.anderson.lyricanalyser.model.Album;
import me.william.anderson.lyricanalyser.model.Artist;
import me.william.anderson.lyricanalyser.model.builder.MusicEntityBuilder;
import me.william.anderson.lyricanalyser.model.data.ArtistSubmission;
import me.william.anderson.lyricanalyser.repository.AlbumRepository;
import me.william.anderson.lyricanalyser.repository.ArtistRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.val;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@SuppressWarnings("Duplicates")
@RestController
@RequestMapping("/artists")
public class ArtistController {

    private static final Logger logger = LoggerFactory.getLogger(ArtistController.class);

    private final ArtistRepository artistRepository;
    private final AlbumRepository albumRepository;
    private final MusicEntityBuilder builder;

    @Autowired
    ArtistController(ArtistRepository artistRepository, AlbumRepository albumRepository, MusicEntityBuilder builder) {
        this.artistRepository = artistRepository;
        this.albumRepository = albumRepository;
        this.builder = builder;
    }

    @GetMapping("")
    public ResponseEntity<Resources<Resource<Artist>>> findAll() {

        List<Resource<Artist>> artists = artistRepository.findAll().stream()
                .map(artist -> new Resource<>(artist,
                        linkTo(methodOn(ArtistController.class).findOne(artist.getId())).withSelfRel(),
                        linkTo(methodOn(ArtistController.class).findAlbums(artist.getId())).withRel("albums"),
                        linkTo(methodOn(ArtistController.class).findAll()).withRel("artists")))
                .collect(Collectors.toList());

        return ResponseEntity.ok(
                new Resources<>(artists,
                        linkTo(methodOn(ArtistController.class).findAll()).withSelfRel()));
    }

    @GetMapping("/search")
    public ResponseEntity<Resources<Resource<Artist>>> search(@RequestParam("name") String name) {
        List<Resource<Artist>> artists = artistRepository.findAllByNameIsLike(name).stream()
                .map(artist -> new Resource<>(artist,
                        linkTo(methodOn(ArtistController.class).findOne(artist.getId())).withSelfRel(),
                        linkTo(methodOn(ArtistController.class).findAlbums(artist.getId())).withRel("albums"),
                        linkTo(methodOn(ArtistController.class).findAll()).withRel("artists")))
                .collect(Collectors.toList());

        return ResponseEntity.ok(
                new Resources<>(artists,
                        linkTo(methodOn(ArtistController.class).search(name)).withSelfRel())
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Resource<Artist>> findOne(@PathVariable long id) {

        return artistRepository.findById(id)
                .map(artist -> new Resource<>(artist,
                        linkTo(methodOn(ArtistController.class).findOne(artist.getId())).withSelfRel(),
                        linkTo(methodOn(ArtistController.class).findAlbums(artist.getId())).withRel("albums"),
                        linkTo(methodOn(ArtistController.class).findAll()).withRel("artists")))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/albums")
    public ResponseEntity<Resources<Resource<Album>>> findAlbums(@PathVariable long id) {
        List<Resource<Album>> albums = albumRepository.findAllByArtistId(id).stream()
                .map(album -> new Resource<>(album,
                        linkTo(methodOn(AlbumController.class).findOne(album.getId())).withSelfRel(),
                        linkTo(methodOn(AlbumController.class).findAll()).withRel("albums"),
                        linkTo(methodOn(ArtistController.class).findOne(id)).withRel("artist"),
                        linkTo(methodOn(AlbumController.class).findTracks(album.getId())).withRel("tracks")))
                .collect(Collectors.toList());

        return ResponseEntity.ok(
                new Resources<>(albums,
                        linkTo(methodOn(ArtistController.class).findAlbums(id)).withSelfRel(),
                        linkTo(methodOn(ArtistController.class).findOne(id)).withRel("artist"))
        );
    }

    @PostMapping(path = "/submit", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<Resource<Artist>> create(@RequestBody ArtistSubmission submission) {
        val url = submission.getUrl();
        long apiId;

        try {
            apiId = HtmlScraper.scrapeArtistId(url);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (MalformedRequestException e) {
            return ResponseEntity.badRequest().build();
        }

        Artist savedArtist = artistRepository.findByApiId(apiId);

        if (savedArtist != null) {
            return findOne(savedArtist.getId());
        }

        Artist artist;

        try {
            artist = builder.buildArtist(url);
        } catch (Exception e) {
            logger.error("An exception was encountered while trying to build artist " + url, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        artistRepository.save(artist);

        return findOne(artist.getId());
    }
}