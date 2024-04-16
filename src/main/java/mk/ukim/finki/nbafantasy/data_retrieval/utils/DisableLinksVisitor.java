package mk.ukim.finki.nbafantasy.data_retrieval.utils;

import org.jsoup.nodes.Node;
import org.jsoup.select.NodeVisitor;

import java.security.SecureRandom;

/**
 * Disables links and adds on click function on each visited node.
 */
public class DisableLinksVisitor implements NodeVisitor {

    private final SecureRandom secureRandom = new SecureRandom();

    @Override
    public void head(Node node, int i) {
        JsoupUtils.cleanUpLinks(node);
        Integer id = Math.abs(secureRandom.nextInt());
        node.attr("id", String.valueOf(id));
        node.attr("onclick", String.format("select(%s)", id));
    }

    @Override
    public void tail(Node node, int i) {

    }
}
