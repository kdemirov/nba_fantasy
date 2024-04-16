package mk.ukim.finki.nbafantasy.data_retrieval.utils;

import mk.ukim.finki.nbafantasy.data_retrieval.model.exceptions.PageSourceRetrievalException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.stream.Collectors;

/**
 * Url Utils.
 */
public class UrlUtils {

    /**
     * Returns page source from given url.
     *
     * @param link url
     * @return html representation
     */
    public static String getPageSource(String link) {
        String pageSource = null;
        try {
            URL url = new URL(link);
            url.openConnection().setConnectTimeout(5 * (60 * 1000));
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()));
            pageSource = bufferedReader.lines().collect(Collectors.joining());
        } catch (IOException e) {
            throw new PageSourceRetrievalException(e);
        }
        return pageSource;
    }
}
