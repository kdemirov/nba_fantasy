package data_extractor.model;

import org.jsoup.nodes.Node;

/**
 * Selectable Node.
 */
public class SelectableNode extends NodeDecorator {
    private boolean selected = false;

    public SelectableNode(Node node) {
        super(node);
        this.selected = false;
    }

    public void selectNode() {
        this.selected = true;
    }

    public void deselectNode() {
        this.selected = false;
    }
}
