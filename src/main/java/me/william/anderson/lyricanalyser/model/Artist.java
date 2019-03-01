package me.william.anderson.lyricanalyser.model;

import java.util.Collection;
import java.util.Map;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import static me.william.anderson.lyricanalyser.model.Constants.ARTIST;

@Entity
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class Artist extends Music {

    @NotNull
    @OneToMany(mappedBy = ARTIST, cascade = CascadeType.ALL)
    @OrderColumn
    @JsonIgnore
    @Getter
    @Setter
    private Collection<Album> albums;

    @NotNull
    @ElementCollection
    @OrderColumn
    @Getter
    @Setter
    private Map<String, Float> trends;
}
