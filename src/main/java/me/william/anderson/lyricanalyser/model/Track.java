package me.william.anderson.lyricanalyser.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import java.time.Duration;
import java.util.Map;

@Data
@NoArgsConstructor // For JPA purposes
@Entity
public class Track extends Music {
    @JoinColumn(name = "albumId")
    private Album album;

    @ElementCollection
    private Map<String, Integer> wordFrequencies;

    private Duration duration;
}
