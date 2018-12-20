package me.william.anderson.lyricanalyser.exception;

public class MalformedRequestException extends Exception {
    public MalformedRequestException(String url, String expected) {
        super("The url " + url + " is not valid. " +
                "Expected to start with '" + expected + "', " +
                "but found '" + url.substring(0, expected.length()) + "'");
    }
}
