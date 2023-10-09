package data_extractor.web;

import data_extractor.model.NodeDecorator;
import data_extractor.service.ExtractDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@CrossOrigin(value = "http://localhost:4200")
@RequestMapping("/admin/extract-data")
public class ExtractDataController {

    private final ExtractDataService extractDataService;

    /**
     * Extracts selectable data from given url.
     *
     * @param url given url
     * @return selectable nodes -> list of {@link data_extractor.model.SelectableNode}
     */
    @PostMapping("/selectable")
    public List<NodeDecorator> extractSelectableDataFrom(@RequestBody String url) {
        return extractDataService.makeAllNodesSelectable(url);
    }
}
