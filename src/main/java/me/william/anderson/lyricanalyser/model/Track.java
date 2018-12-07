package me.william.anderson.lyricanalyser.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor // For JPA purposes
@Entity
public class Track extends Music {
    @NotNull
    @ManyToOne
    @JoinColumn(name = "albumId")
    private Album album;

    @ElementCollection
    private Map<String, Integer> wordFrequencies;

    @NotNull
    private int duration;
}
