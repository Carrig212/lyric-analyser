package me.william.anderson.lyricanalyser.exception;

import me.william.anderson.lyricanalyser.model.Music;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(long id, Class<? extends Music> type) {
        super("No " + type.getSimpleName() + " with ID " + id + " could be found");
    }
}
