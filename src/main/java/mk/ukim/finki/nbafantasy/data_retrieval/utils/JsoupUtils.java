package mk.ukim.finki.nbafantasy.data_retrieval.utils;

import mk.ukim.finki.nbafantasy.config.Constants;
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

    /**
     * Clean up links and buttons and changes the html element into div.
     *
     * @param node given node
     */
    public static void cleanUpLinks(Node node) {
        cleanUpTag(node, Constants.BUTTON_ELEMENT);
        cleanUpTag(node, Constants.LINK_ELEMENT);
    }

    private static void cleanUpTag(Node n, String tag) {
        if (n instanceof Element && ((Element) n).tagName().equals(tag)) {
            ((Element) n).tagName(Constants.DIV_ELEMENT);
        }
    }
}
