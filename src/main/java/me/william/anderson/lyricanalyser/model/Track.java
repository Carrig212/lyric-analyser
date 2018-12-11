package me.william.anderson.lyricanalyser.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor // For JPA purposes
@Entity
public class Track extends Music {
    @NotNull
    @ManyToOne
    @JoinColumn(name = "albumId")
    private Album album;

    @NotNull
    private int duration;
}
