package me.william.anderson.lyricanalyser.api;

abstract class Constants {

    // HTML Scraper constants
    static final String GENIUS_COM = "https://genius.com";

    static final String ARTIST_ID_KEY = "name";
    static final String ARTIST_ID_VALUE = "newrelic-resource-path";
    static final String ARTIST_ID_START = "/";
    static final String ARTIST_ALBUMS = "/artists/albums?for_artist_page=";

    static final String ALBUM_LINK_CLASS = "album_link";
    static final String ALBUM_LINK_ATTRIBUTE = "href";
    static final String ALBUM_ID_KEY = "itemprop";
    static final String ALBUM_ID_VALUE = "page_data";
    static final String ALBUM_ID_ATTRIBUTE = "content";
    static final String ALBUM_ID_START = "album_ids\":\"[";
    static final String ALBUM_ID_END = "]\",\"artist\"";

    static final String TRACK_LINK_CLASS = "u-display_block";
    static final String TRACK_LYRICS_CLASS = "lyrics";
    static final String TRACK_ID_ATTRIBUTE = "content";
    static final String TRACK_ID_START = "/";

    // API Consumer constants
    static final int STATUS = 200;
    static final String API_GENIUS_COM = "https://api.genius.com/";
    static final String RESPONSE = "response";

    static final String ARTISTS = "artists/";
    static final String ARTIST = "artist";

    static final String ALBUMS = "albums/";
    static final String ALBUM = "album";

    static final String SONGS = "songs/";
    static final String SONG = "song";

    static final String ACCEPT = "Accept";
    static final String JSON = "application/json";
    static final String AUTH = "Authorization";
    static final String BEARER = "Bearer ";
    static final String FORMAT = "text_format";
    static final String PLAIN = "plain";
}
