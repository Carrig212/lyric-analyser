package me.william.anderson.lyricanalyser.api;

final class ApiConstants {
    // Base URL for all requests
    static final String URL = "https://api.musixmatch.com/ws/1.1/";

    // API methods
    static final String ARTIST_GET = "artist.get";
    static final String ARTIST_ALBUMS_GET = "artist.albums.get";
    static final String ALBUM_TRACKS_GET = "album.tracks.get";
    static final String TRACK_LYRICS_GET = "track.lyrics.get";

    // Release Types
    static final String RELEASE_TYPE_ALBUM = "Album";
    static final String RELEASE_TYPE_SINGLE = "Single";
    static final String RELEASE_TYPE_EP = "Ep";

    // Pagination
    static final int PAGE_SIZE = 100;
}
