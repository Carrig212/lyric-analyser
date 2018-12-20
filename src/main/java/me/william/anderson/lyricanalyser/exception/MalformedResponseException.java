package me.william.anderson.lyricanalyser.exception;

public class MalformedResponseException extends Exception {
    public MalformedResponseException(String url, Exception cause) {
        super("The server at " + url + "returned a malformed HTTP response", cause);
    }
}
