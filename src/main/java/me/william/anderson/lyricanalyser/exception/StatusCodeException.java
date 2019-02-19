package me.william.anderson.lyricanalyser.exception;

public class StatusCodeException extends Exception {
    public StatusCodeException(int expected, int actual, String url) {
        super(url + " responded with status code " + actual + " instead of the expected " + expected);
    }
}
