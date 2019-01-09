package me.william.anderson.lyricanalyser.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import java.util.Collection;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
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
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Getter
    @Setter
    private Artist artist;
}
