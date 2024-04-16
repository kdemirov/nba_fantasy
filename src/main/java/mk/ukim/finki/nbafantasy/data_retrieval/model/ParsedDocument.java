package mk.ukim.finki.nbafantasy.data_retrieval.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.jsoup.nodes.Node;

import java.util.List;

/**
 * Parsed document
 */
@AllArgsConstructor
@Builder
@Getter
public class ParsedDocument {
    List<Node> cssNodes;
    Node parsedBody;
}
