package me.william.anderson.lyricanalyser.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Data
@NoArgsConstructor // For JPA purposes
@MappedSuperclass // We don't want any Music objects in the database
abstract class Music {
    @Id
    @GeneratedValue
    private long id;

    private String name;
    private String apiId;
    private int wordCount;
    private int uniqueWordCount;
    private float uniqueWordDensity;

    @ElementCollection
    private Set<String> genres;

    private int rating;
    private Date updated;

    // We want the objects to maintain a timestamp for when they were last updated
    @PrePersist
    @PreUpdate
    private void updateTimestamp() {
        this.updated = new Date();
    }
}
