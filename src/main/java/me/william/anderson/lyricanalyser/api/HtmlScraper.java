package me.william.anderson.lyricanalyser.api;

import java.io.IOException;
import java.util.ArrayList;

import me.william.anderson.lyricanalyser.exception.MalformedRequestException;
import me.william.anderson.lyricanalyser.exception.MalformedResponseException;
import me.william.anderson.lyricanalyser.exception.StatusCodeException;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HtmlScraper {

    private static final Logger logger = LoggerFactory.getLogger(HtmlScraper.class);

    // Constants for scraping artist IDs
    private static final String NAME_ATTR_KEY = "name"; // The name attribute key in the meta tags
    private static final String NAME_ATTR_VALUE = "newrelic-resource-path"; // The name value we want to find
    private static final String CONTENT_ATTR_KEY = "content"; // The content attribute key
    private static final String ITEMPROP_ATTR_KEY = "itemprop"; // The itemprop attribute key in the meta tags

    // Constants for scraping the album IDs
    private static final String ITEMPROP_ATTR_VALUE = "page_data"; // The itemprop value we want to find
    private static final String ALBUM_ID_START = "album_ids\":\"["; // The start of the album_ids element
    private static final String ALBUM_ID_END = "]"; // The end of the album_ids element

    // Constants for scraping album lists
    private static final String HREF_ATTR_KEY = "href"; // The href attribute key in the anchor tags
    private static final String ALBUM_LINK_CLASS = "album_link"; // The class of the album links

    // Constants for scraping track lists
    private static final String TRACK_LINK_CLASS = "u-display_block"; // The class of the track links

    // Constants for connection validation
    private static final int STATUS_CODE_200 = 200; // Status code 200 OK
    private static final String BASE_URL = "https://genius.com"; // The base url we want all requests to go to.

    // Link list scraper method
    public static ArrayList<String> scrapeLinkList(String url, LinkClass linkClass) throws StatusCodeException, MalformedResponseException, MalformedRequestException {
        // Get the list of links from the body
        var linkListHtml = getHtmlDocument(url).body().getElementsByClass(linkClass.toString());
        var linkStringList = new ArrayList<String>();

        for (var link : linkListHtml) {
            linkStringList.add(link.attr(HREF_ATTR_KEY));
        }

        return linkStringList;
    }

    // ID scraper methods
    public static String scrapeArtistId(String url) throws StatusCodeException, MalformedResponseException, MalformedRequestException {
        // Retrieve the value of the meta tag with the appropriate name from the head
        var metaTagContent = getHtmlDocument(url)
                .head()
                .getElementsByAttributeValueContaining(NAME_ATTR_KEY, NAME_ATTR_VALUE)
                .first()
                .attr(CONTENT_ATTR_KEY);

        // Return the value of the content attribute, sans the "/artists/"
        return metaTagContent.substring(metaTagContent.lastIndexOf('/') + 1);
    }

    public static String scrapeAlbumId(String url) throws StatusCodeException, MalformedResponseException, MalformedRequestException {
        // Extract the content string from the meta tag with the appropriate name from the head
        var contentString = getHtmlDocument(url)
                .head()
                .getElementsByAttributeValueContaining(ITEMPROP_ATTR_KEY, ITEMPROP_ATTR_VALUE)
                .attr(CONTENT_ATTR_KEY);

        // Extract and return the album ID from the content string
        return contentString.substring(
                contentString.indexOf(ALBUM_ID_START) + ALBUM_ID_START.length(),
                contentString.indexOf(ALBUM_ID_END)
        );
    }

    // Enum for link classes
    enum LinkClass {
        TRACK_LINK_CLASS("u-display_block"),
        ALBUM_LINK_CLASS("album_link");

        private final String linkClass;

        LinkClass(String linkClass) {
            this.linkClass = linkClass;
        }

        public String getLinkClass() {
            return this.linkClass;
        }
    }

    // Private request helper methods
    private static Document getHtmlDocument(String url) throws MalformedResponseException, StatusCodeException, MalformedRequestException {
        checkConnection(url);

        try {
            return Jsoup.connect(url).get(); // Return the HTML from the site
        } catch (IOException e) { // An IOException is thrown if the response is not valid HTML
            throw new MalformedResponseException(url, e);
        }
    }

    private static void checkConnection(String url) throws StatusCodeException, MalformedResponseException, MalformedRequestException {
        validateUrl(url);

        try {
            Jsoup.connect(url).execute(); // Test the connection, without retrieving the HTML document
        } catch (HttpStatusException e) { // A HttpStatusException is thrown if we receive a 404 Not Found status code
            throw new StatusCodeException(STATUS_CODE_200, e);
        } catch (IOException e) { // An IOException is thrown if the response is not valid HTTP
            throw new MalformedResponseException(url, e);
        }
    }

    private static void validateUrl(String url) throws MalformedRequestException {
        if (!url.substring(0, BASE_URL.length()).equals(BASE_URL)) { // Make sure the url starts with the correct string
            throw new MalformedRequestException(url, BASE_URL);
        }
    }
}
