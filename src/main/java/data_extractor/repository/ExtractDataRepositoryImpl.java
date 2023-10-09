package data_extractor.repository;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.net.URL;

@Repository
public class ExtractDataRepositoryImpl implements ExtractDataRepository {

    @Override
    public Elements extractData(String url) {
        Document document = null;
        try {
            document = Jsoup.parse(new URL(url), 5000);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return document.getElementsByTag("body");
    }
}
