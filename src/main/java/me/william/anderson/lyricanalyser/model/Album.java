package me.william.anderson.lyricanalyser.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.Date;
import java.util.Set;

@Data
@NoArgsConstructor // For JPA purposes
@Entity
public class Album extends Music {
    @OneToMany(mappedBy = "album")
    private Set<Track> tracks;

    @ManyToOne
    @JoinColumn(name = "artistId")
    private Artist artist;

    private String releaseType;
    private Date releaseDate;
    private String label;
}
