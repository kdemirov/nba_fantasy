package data_extractor.repository;

import org.jsoup.select.Elements;

/**
 * Extract Data Repository.
 */
public interface ExtractDataRepository {

    /**
     * Extract data from given url.
     *
     * @param url url
     * @return {@link Elements} list of nodes
     */
    Elements extractData(String url);
}
