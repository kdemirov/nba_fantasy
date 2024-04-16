package mk.ukim.finki.nbafantasy.data_retrieval.utils;

import org.jsoup.nodes.Node;
import org.jsoup.select.NodeVisitor;

import java.util.ArrayList;
import java.util.List;

/**
 * Css stylesheet collector
 */
public class StylesheetNodeCollector implements NodeVisitor {

    public static List<Node> cssNodes = new ArrayList<>();

    @Override
    public void head(Node node, int i) {
        if (node.nodeName().contains("link") & node.attr("rel").contains("stylesheet")) {
            String cssUrl = node.attr("href");
            cssUrl = "https://www.nba.com" + cssUrl;
            node.attr("href", cssUrl);
            cssNodes.add(node);
        }
    }

    @Override
    public void tail(Node node, int i) {

    }
}
