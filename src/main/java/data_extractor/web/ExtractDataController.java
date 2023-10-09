package data_extractor.web;

import data_extractor.model.NodeDecorator;
import data_extractor.service.ExtractDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/extract-data")
public class ExtractDataController {

    private final ExtractDataService extractDataService;

    /**
     * Extracts selectable data from given url.
     *
     * @param url given url
     * @return selectable nodes -> list of {@link data_extractor.model.SelectableNode}
     */
    @GetMapping
    public List<NodeDecorator> extractSelectableDataFrom(@RequestParam String url) {
        return extractDataService.makeAllNodesSelectable(url);
    }
}
