package me.william.anderson.lyricanalyser.model;

import java.util.Date;
import java.util.Map;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@MappedSuperclass
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class Music {

    @NotNull
    @Id
    @GeneratedValue
    @Getter
    private long id;

    @NotNull
    @Getter
    @Setter
    private long apiId;

    @NotNull
    @Getter
    @Setter
    private String name;

    @NotNull
    @Getter
    @Setter
    private String geniusUrl;

    @NotNull
    @Getter
    @Setter
    private String imageUrl;

    @NotNull
    @ElementCollection
    @OrderColumn
    @Getter
    @Setter
    private Map<String, Integer> wordFrequencies;

    @NotNull
    @Getter
    @Setter
    private int wordCount;

    @NotNull
    @Getter
    @Setter
    private int uniqueWordCount;

    @NotNull
    @Getter
    @Setter
    private float uniqueWordDensity;

    @NotNull
    @Getter
    private Date created;

    @NotNull
    @Getter
    private Date lastUpdated;

    @PrePersist
    private void setCreated() {
        this.created = new Date();
        this.lastUpdated = new Date();
    }

    @PreUpdate
    private void setLastUpdated() {
        this.lastUpdated = new Date();
    }
}
