package data_extractor.model;

import org.jsoup.nodes.Node;

/**
 * Node decorator.
 */
public abstract class NodeDecorator {

    protected Node node;

    public NodeDecorator(Node node) {
        this.node = node;
    }

}
