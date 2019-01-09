package me.william.anderson.lyricanalyser.model;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
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
public class Track extends Music {

    @NotNull
    @ManyToOne
    @JoinColumn(name = "albumId")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Getter
    @Setter
    private Album album;

    @NotNull
    @ElementCollection
    @Getter
    @Setter
    private Collection<String> featuredArtists;

    @NotNull
    @Getter
    @Setter
    private String lyricsState;

    @Transient
    @Getter
    @Setter
    private String lyrics;
}
