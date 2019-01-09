package me.william.anderson.lyricanalyser.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
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
public class Artist extends Music {

    @NotNull
    @OneToMany(mappedBy = "artist", cascade = CascadeType.ALL)
    @Getter
    @Setter
    private Collection<Album> albums;
}
