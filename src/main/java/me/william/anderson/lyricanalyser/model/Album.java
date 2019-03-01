package me.william.anderson.lyricanalyser.model;

import java.util.Collection;
import java.util.Map;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import static me.william.anderson.lyricanalyser.model.Constants.*;

@Entity
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class Album extends Music implements Comparable<Album> {

    @NotNull
    @Getter
    @Setter
    private String releaseDate;

    @NotNull
    @OneToMany(mappedBy = ALBUM, cascade = CascadeType.ALL)
    @OrderColumn
    @JsonIgnore
    @Getter
    @Setter
    private Collection<Track> tracks;

    @NotNull
    @ManyToOne
    @JoinColumn(name = ARTIST_ID)
    @JsonIgnore
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Getter
    @Setter
    private Artist artist;

    @NotNull
    @Getter
    @Setter
    private boolean duplicate = DUPLICATE_FALSE;

    @NotNull
    @ElementCollection
    @OrderColumn
    @Getter
    @Setter
    private Map<String, Float> trends;

    @Override
    public int compareTo(Album album) {
        return getReleaseDate().compareTo(album.getReleaseDate());
    }
}
