package me.william.anderson.lyricanalyser.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Objects;

@Entity
public class Album extends Music {
    @NotNull
    @OneToMany(mappedBy = "album", cascade = CascadeType.ALL)
    private Collection<Track> tracks;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "artistId")
    private Artist artist;

    @NotNull
    private String releaseType;

    @NotNull
    private String releaseDate;

    @NotNull
    private String label;

    public Album() {

    }

    public Collection<Track> getTracks() {
        return tracks;
    }

    public void setTracks(Collection<Track> tracks) {
        this.tracks = tracks;
    }

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    public String getReleaseType() {
        return releaseType;
    }

    public void setReleaseType(String releaseType) {
        this.releaseType = releaseType;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Album)) return false;
        if (!super.equals(o)) return false;
        Album album = (Album) o;
        return tracks.equals(album.tracks) &&
                artist.equals(album.artist) &&
                releaseType.equals(album.releaseType) &&
                releaseDate.equals(album.releaseDate) &&
                label.equals(album.label);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), tracks, artist, releaseType, releaseDate, label);
    }

    @Override
    public String toString() {
        return "Album{" +
                "tracks=" + tracks +
                ", releaseType='" + releaseType + '\'' +
                ", releaseDate='" + releaseDate + '\'' +
                ", label='" + label + '\'' +
                "} " + super.toString();
    }
}
