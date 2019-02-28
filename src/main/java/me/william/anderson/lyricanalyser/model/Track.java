package me.william.anderson.lyricanalyser.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Collection;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import lombok.*;

@Entity
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown=true)
public class Track extends Music {

    @NotNull
    @ManyToOne
    @JoinColumn(name = "albumId")
    @JsonIgnore
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
    @JsonIgnore
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Getter
    @Setter
    private String lyrics;
}
