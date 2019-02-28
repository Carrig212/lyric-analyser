package me.william.anderson.lyricanalyser.model;

import java.util.Collection;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Entity
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Album extends Music {

    @NotNull
    @Getter
    @Setter
    private String releaseDate;

    @NotNull
    @OneToMany(mappedBy = "album", cascade = CascadeType.ALL)
    @Getter
    @Setter
    private Collection<Track> tracks;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "artistId")
    @JsonIgnore
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Getter
    @Setter
    private Artist artist;
}
