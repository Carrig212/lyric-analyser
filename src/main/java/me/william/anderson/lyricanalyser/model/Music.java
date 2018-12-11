package me.william.anderson.lyricanalyser.model;

import javax.persistence.ElementCollection;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Date;

import lombok.Data;
import lombok.NoArgsConstructor;

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
