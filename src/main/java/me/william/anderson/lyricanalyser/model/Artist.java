package me.william.anderson.lyricanalyser.model;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import java.util.Collection;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

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
