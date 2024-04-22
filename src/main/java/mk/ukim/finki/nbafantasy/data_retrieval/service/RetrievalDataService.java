package mk.ukim.finki.nbafantasy.data_retrieval.service;

import mk.ukim.finki.nbafantasy.data_retrieval.model.ParsedDocument;

public interface RetrievalDataService {

    /**
     * Parses source page source into {@link ParsedDocument}
     *
     * @param url            given url
     * @param includeScripts boolean value whether to include scripts into the parsed document
     * @return {@link ParsedDocument}
     */
    ParsedDocument retrieveDataFromUrl(String url, Boolean includeScripts);
}
