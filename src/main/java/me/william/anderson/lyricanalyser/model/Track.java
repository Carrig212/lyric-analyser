package me.william.anderson.lyricanalyser.model;

import java.util.Collection;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import static me.william.anderson.lyricanalyser.model.Constants.ALBUM_ID;
import static me.william.anderson.lyricanalyser.model.Constants.DUPLICATE_FALSE;

@Entity
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class Track extends Music {

    @NotNull
    @ManyToOne
    @JoinColumn(name = ALBUM_ID)
    @JsonIgnore
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Getter
    @Setter
    private Album album;

    @NotNull
    @ElementCollection
    @OrderColumn
    @Getter
    @Setter
    private Collection<String> featuredArtists;

    @NotNull
    @Getter
    @Setter
    private String lyricsState;

    @Transient
    @JsonIgnore
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Getter
    @Setter
    private String lyrics;

    @NotNull
    @Getter
    @Setter
    private boolean duplicate = DUPLICATE_FALSE;
}
