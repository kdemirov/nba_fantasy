package mk.ukim.finki.nbafantasy.data_retrieval.utils;

import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.NodeVisitor;

/**
 * Checks if node is text node and save its value to its parent node.
 */
public class TextNodeValueVisitor implements NodeVisitor {
    @Override
    public void head(Node node, int i) {
        if (node instanceof TextNode) {
            Node parent = node.parentNode();
            if (parent.attributes() != null) {
                parent.attr("textNodeValue", ((TextNode) node).text());
            }
        }
    }

    @Override
    public void tail(Node node, int i) {

    }
}
