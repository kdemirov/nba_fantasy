package data_extractor.service;

import data_extractor.model.NodeDecorator;

import java.util.List;

public interface ExtractDataService {

    /**
     * Makes all nodes returned from the given url as selectable.
     *
     * @param url url
     * @return selectableNodes -> list of {@link data_extractor.model.SelectableNode}
     */
    List<NodeDecorator> makeAllNodesSelectable(String url);
}
