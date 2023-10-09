package data_extractor.service;

import data_extractor.model.NodeDecorator;
import data_extractor.model.SelectableNode;
import data_extractor.repository.ExtractDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ExtractDataServiceImpl implements ExtractDataService {

    private final ExtractDataRepository extractDataRepository;

    @Override
    public List<NodeDecorator> makeAllNodesSelectable(String url) {
        return extractDataRepository.extractData(url)
                .stream()
                .map(SelectableNode::new)
                .collect(Collectors.toList());
    }
}
