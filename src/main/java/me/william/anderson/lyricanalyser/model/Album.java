package me.william.anderson.lyricanalyser.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor // For JPA purposes
@Entity
public class Album extends Music {
    @NotNull
    @OneToMany(mappedBy = "album")
    private Set<Track> tracks;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "artistId")
    private Artist artist;

    @NotNull
    private String releaseType;

    @NotNull
    private Date releaseDate;

    @NotNull
    private String label;
}
