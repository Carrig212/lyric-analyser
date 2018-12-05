package me.william.anderson.lyricanalyser.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import java.util.Collection;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor // For JPA purposes
@Entity
public class Artist extends Music {
    @NotNull
    @OneToMany(mappedBy = "artist")
    private Collection<Album> albums;

    @NotNull
    private String country;
}
