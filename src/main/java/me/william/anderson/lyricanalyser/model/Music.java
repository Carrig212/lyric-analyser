package me.william.anderson.lyricanalyser.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Date;

@Data
@NoArgsConstructor // For JPA purposes
@MappedSuperclass // We don't want any Music objects in the database
abstract class Music {
    @Id
    @GeneratedValue
    private long id;

    @NotNull
    private String name;

    @NotNull
    private long apiId;

    private int wordCount;
    private int uniqueWordCount;
    private float uniqueWordDensity;

    @NotNull
    @ElementCollection
    private Collection<String> genres;

    @NotNull
    private int rating;

    @NotNull
    private Date updated;

    // We want the objects to maintain a timestamp for when they were last updated
    @PrePersist
    @PreUpdate
    private void updateTimestamp() {
        this.updated = new Date();
    }
}
