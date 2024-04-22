package mk.ukim.finki.nbafantasy.data_retrieval.utils;

import mk.ukim.finki.nbafantasy.config.Constants;
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
        if (node.nodeName().contains(Constants.LINK_HEAD_ELEMENT)
                && node.attr(Constants.LINK_HEAD_ELEMENT_REL_ATTR).contains(Constants.LINK_HEAD_ELEMENT_REL_ATTR_VALUE)) {
            String cssUrl = node.attr(Constants.HREF_ATTR);
            cssUrl = Constants.NBA_URL + cssUrl;
            node.attr(Constants.HREF_ATTR, cssUrl);
            cssNodes.add(node);
        }
    }

    @Override
    public void tail(Node node, int i) {

    }
}
