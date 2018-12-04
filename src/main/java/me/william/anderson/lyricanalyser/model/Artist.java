package me.william.anderson.lyricanalyser.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor // For JPA purposes
@Entity
public class Artist extends Music {
    @NotNull
    @OneToMany(mappedBy = "artist")
    private Set<Album> albums;

    @NotNull
    private String country;
}
