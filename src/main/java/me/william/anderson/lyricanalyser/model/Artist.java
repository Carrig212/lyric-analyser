package me.william.anderson.lyricanalyser.model;

import java.util.Collection;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Entity
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Artist extends Music {

    @NotNull
    @OneToMany(mappedBy = "artist", cascade = CascadeType.ALL)
    @Getter
    @Setter
    private Collection<Album> albums;
}
