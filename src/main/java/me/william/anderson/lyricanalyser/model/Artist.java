package me.william.anderson.lyricanalyser.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.Set;

@Data
@NoArgsConstructor // For JPA purposes
@Entity
public class Artist extends Music {
    @OneToMany(mappedBy = "artist")
    private Set<Album> albums;

    private String country;
}
