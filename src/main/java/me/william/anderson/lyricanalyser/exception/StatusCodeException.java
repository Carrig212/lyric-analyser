package me.william.anderson.lyricanalyser.exception;

public class StatusCodeException extends Exception {
    public StatusCodeException(int expected, Exception cause) {
        super("Expected status code " + expected, cause);
    }

    public StatusCodeException(int expected, int actual) {
        super("Expected status code " + expected + " but found " + actual);
    }
}
