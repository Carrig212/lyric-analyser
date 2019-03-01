package me.william.anderson.lyricanalyser.controller;

abstract class Constants {

    // Artist routes
    static final String ARTISTS_ROUTE = "/artists";
    static final String FIND_ALBUMS_ROUTE = "/{id}/albums";
    static final String SUBMIT_ARTIST_ROUTE = "/submit";

    // Album routes
    static final String ALBUMS_ROUTE = "/albums";
    static final String FIND_TRACKS_ROUTE = "/{id}/tracks";

    // Track routes
    static final String TRACKS_ROUTE = "/tracks";

    // General routes
    static final String SEARCH_ROUTE = "/search";
    static final String FIND_ONE_ROUTE = "/{id}";
    static final String UPDATE_ROUTE = "/{id}/update";

    // Aggregate rels
    static final String ARTISTS_REL = "artists";
    static final String ALBUMS_REL = "albums";
    static final String TRACKS_REL = "tracks";

    // Parent rels
    static final String ARTIST_REL = "artist";
    static final String ALBUM_REL = "album";

    // Request parameters
    static final String NAME_PARAM = "name";

    // Header values
    static final String ACCEPT_JSON = "application/json";
}
