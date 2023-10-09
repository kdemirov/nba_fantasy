package data_extractor.service;

import data_extractor.model.NodeDecorator;
import data_extractor.model.SelectableNode;
import data_extractor.repository.ExtractDataRepository;
import data_extractor.repository.ExtractDataRepositoryImpl;
import org.jsoup.select.Elements;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ExtractDataServiceImplTest {

    private final ExtractDataRepository extractDataRepository = new ExtractDataRepositoryImpl();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void makeAllNodesSelectable() {
        ExtractDataService extractDataService = new ExtractDataServiceImpl(extractDataRepository);

        List<NodeDecorator> nodeDecorators = extractDataService.makeAllNodesSelectable("https://www.google.com");

        nodeDecorators.forEach(nodeDecorator -> {
            Assert.assertEquals(true, nodeDecorator instanceof SelectableNode);
            assertChildren(nodeDecorator.getChildNodes());
        });
    }

    private void assertChildren(List<NodeDecorator> childNodes) {
        childNodes.forEach(childNode -> {
            Assert.assertEquals(true, childNode instanceof SelectableNode);
            assertChildren(childNode.getChildNodes());
        });
    }
}