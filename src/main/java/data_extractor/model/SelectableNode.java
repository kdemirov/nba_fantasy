package data_extractor.model;

import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.NodeVisitor;

import java.util.Iterator;
import java.util.List;

/**
 * Selectable Node.
 */
public class SelectableNode extends NodeDecorator {
    private boolean selected = false;

    public SelectableNode(Node node) {
        super(node);
        this.selected = false;
    }

    @Override
    void setChildNodes(List<Node> childNodes) {
        Iterator<Node> i = childNodes.iterator();
        while (i.hasNext()) {
            Node n = i.next();
            SelectableNode selectableNode = new SelectableNode(n);
            this.childNodes.add(new SelectableNode(n));
            selectableNode.setChildNodes(n.childNodes());
        }
    }

    public void selectNode() {
        this.selected = true;
    }

    public void deselectNode() {
        this.selected = false;
    }
}
