package data_extractor.model;

import lombok.Getter;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Node;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Node decorator.
 */
@Getter
public abstract class NodeDecorator {

    protected Node node;
    protected List<NodeDecorator> childNodes;

    public NodeDecorator(Node node) {
        this.node = node;
        childNodes = new ArrayList<>();
        setChildNodes(node.childNodes());
    }

    abstract void setChildNodes(List<Node> childNodes);

    public String outerHtml() {
        return this.node.outerHtml();
    }
    public List<Attribute> attributes() {
        return this.node.attributes().asList();
    }
}
