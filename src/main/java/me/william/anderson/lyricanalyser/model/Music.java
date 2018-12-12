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
import java.util.Map;
import java.util.Objects;

@MappedSuperclass // We don't want any Music objects in the database
public abstract class Music {
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

    @ElementCollection
    private Map<String, Integer> wordFrequencies;

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

    Music() {
        // Base constructor for JPA and Music subclasses
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getApiId() {
        return apiId;
    }

    public void setApiId(long apiId) {
        this.apiId = apiId;
    }

    public int getWordCount() {
        return wordCount;
    }

    public void setWordCount(int wordCount) {
        this.wordCount = wordCount;
    }

    public int getUniqueWordCount() {
        return uniqueWordCount;
    }

    public void setUniqueWordCount(int uniqueWordCount) {
        this.uniqueWordCount = uniqueWordCount;
    }

    public float getUniqueWordDensity() {
        return uniqueWordDensity;
    }

    public void setUniqueWordDensity(float uniqueWordDensity) {
        this.uniqueWordDensity = uniqueWordDensity;
    }

    public Map<String, Integer> getWordFrequencies() {
        return wordFrequencies;
    }

    public void setWordFrequencies(Map<String, Integer> wordFrequencies) {
        this.wordFrequencies = wordFrequencies;
    }

    public Collection<String> getGenres() {
        return genres;
    }

    public void setGenres(Collection<String> genres) {
        this.genres = genres;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public Date getUpdated() {
        return updated;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Music)) return false;
        Music music = (Music) o;
        return id == music.id &&
                apiId == music.apiId &&
                wordCount == music.wordCount &&
                uniqueWordCount == music.uniqueWordCount &&
                Float.compare(music.uniqueWordDensity, uniqueWordDensity) == 0 &&
                rating == music.rating &&
                name.equals(music.name) &&
                wordFrequencies.equals(music.wordFrequencies) &&
                genres.equals(music.genres) &&
                updated.equals(music.updated);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, apiId, wordCount, uniqueWordCount, uniqueWordDensity, wordFrequencies, genres, rating, updated);
    }

    @Override
    public String toString() {
        return "Music{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", apiId=" + apiId +
                ", wordCount=" + wordCount +
                ", uniqueWordCount=" + uniqueWordCount +
                ", uniqueWordDensity=" + uniqueWordDensity +
                ", wordFrequencies=" + wordFrequencies +
                ", genres=" + genres +
                ", rating=" + rating +
                ", updated=" + updated +
                '}';
    }
}
