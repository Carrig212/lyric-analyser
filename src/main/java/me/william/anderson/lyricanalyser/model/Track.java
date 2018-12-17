package me.william.anderson.lyricanalyser.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
public class Track extends Music {
    @NotNull
    @ManyToOne
    @JoinColumn(name = "albumId")
    private Album album;

    @NotNull
    private int duration;

    public Track() {

    }

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Track)) return false;
        if (!super.equals(o)) return false;
        Track track = (Track) o;
        return duration == track.duration &&
                album.equals(track.album);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), album, duration);
    }

    @Override
    public String toString() {
        return "Track{" +
                "duration=" + duration +
                "} " + super.toString();
    }
}
