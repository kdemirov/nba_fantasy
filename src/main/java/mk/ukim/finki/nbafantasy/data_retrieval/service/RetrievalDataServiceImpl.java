package mk.ukim.finki.nbafantasy.data_retrieval.service;

import lombok.RequiredArgsConstructor;
import mk.ukim.finki.nbafantasy.data_retrieval.model.ParsedDocument;
import mk.ukim.finki.nbafantasy.data_retrieval.utils.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.openqa.selenium.WebDriver;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@RequiredArgsConstructor
@Service
public class RetrievalDataServiceImpl implements RetrievalDataService {

    private final HashMap<String, String> visitedUrls = new HashMap();
    private final WebDriver webDriver;

    @Override
    public ParsedDocument retrieveDataFromUrl(String url, Boolean includeScripts) {
        setWebDriver();
        visitedUrls.putIfAbsent(url, UrlUtils.getPageSource(url, includeScripts));
        String pageSource = visitedUrls.get(url);

        Document document = JsoupUtils.parseDocument(pageSource);
        Element head = document.head();
        Node body = document.body();
        head.traverse(new StylesheetNodeCollector());
        body = body.traverse(new DisableLinksVisitor());
        body = body.traverse(new TextNodeValueVisitor());

        return ParsedDocument.builder()
                .cssNodes(StylesheetNodeCollector.cssNodes)
                .parsedBody(body)
                .build();
    }

    private void setWebDriver() {
        UrlUtils.setWebDriver(webDriver);
    }
}
