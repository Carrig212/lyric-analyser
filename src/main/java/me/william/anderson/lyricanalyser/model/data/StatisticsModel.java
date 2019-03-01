package me.william.anderson.lyricanalyser.model.data;

import lombok.Data;

@Data
public class StatisticsModel {
    private final int wordCount;
    private final int uniqueWordCount;
    private final float uniqueWordDensity;
}
