package mk.ukim.finki.nbafantasy.data_retrieval.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;

/**
 * Jsoup Utils.
 */
public class JsoupUtils {

    /**
     * Parses given html page source into {@link Document}.
     *
     * @param pageSource html
     * @return {@link Document}
     */
    public static Document parseDocument(String pageSource) {
        return Jsoup.parse(pageSource);
    }

    public static void cleanUpLinks(Node node) {
        cleanUpTag(node, "button");
        cleanUpTag(node, "a");
    }

    private static void cleanUpTag(Node n, String tag) {
        if (n instanceof Element && ((Element) n).tagName().equals(tag)) {
            ((Element) n).tagName("div");
        }
    }
}
