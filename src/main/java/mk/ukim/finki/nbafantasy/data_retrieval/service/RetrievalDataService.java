package mk.ukim.finki.nbafantasy.data_retrieval.service;

import mk.ukim.finki.nbafantasy.data_retrieval.model.ParsedDocument;

public interface RetrievalDataService {

    /**
     * Parses source page source into {@link ParsedDocument}
     *
     * @param url
     * @return {@link ParsedDocument}
     */
    ParsedDocument retrieveDataFromUrl(String url);
}
