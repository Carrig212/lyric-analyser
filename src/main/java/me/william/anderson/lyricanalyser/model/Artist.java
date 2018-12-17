package me.william.anderson.lyricanalyser.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Objects;

@Entity
public class Artist extends Music {
    @NotNull
    @OneToMany(mappedBy = "artist", cascade = CascadeType.ALL)
    private Collection<Album> albums;

    @NotNull
    private String country;

    public Artist() {

    }

    public Collection<Album> getAlbums() {
        return albums;
    }

    public void setAlbums(Collection<Album> albums) {
        this.albums = albums;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Artist)) return false;
        if (!super.equals(o)) return false;
        Artist artist = (Artist) o;
        return albums.equals(artist.albums) &&
                country.equals(artist.country);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), albums, country);
    }

    @Override
    public String toString() {
        return "Artist{" +
                "albums=" + albums +
                ", country='" + country + '\'' +
                "} " + super.toString();
    }
}
