package me.william.anderson.lyricanalyser.model;

import javax.persistence.ElementCollection;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Map;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@MappedSuperclass
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString
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
    }

    @PreUpdate
    private void setLastUpdated() {
        this.lastUpdated = new Date();
    }
}
