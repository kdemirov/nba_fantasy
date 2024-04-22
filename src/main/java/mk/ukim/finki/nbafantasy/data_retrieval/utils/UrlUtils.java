package mk.ukim.finki.nbafantasy.data_retrieval.utils;

import org.openqa.selenium.WebDriver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Url Utils.
 */
public class UrlUtils {

    private static WebDriver webDriver;

    /**
     * Returns page source from given url.
     *
     * @param link url
     * @return html representation
     */
    public static String getPageSource(String link, Boolean includeScript) {
        String pageSource = null;
        URL url = null;
        if (link.endsWith("/")) {
            int i = link.lastIndexOf("/");
            link = link.substring(0, i);
        }
        try {
            url = new URL(link);
            if (includeScript) {
                webDriver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
                webDriver.get(link);
                pageSource = webDriver.getPageSource();
            } else {
                url.openConnection().setConnectTimeout(5 * (60 * 1000));
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()));
                pageSource = bufferedReader.lines().collect(Collectors.joining());
            }
        } catch (IOException o_O) {
            throw new RuntimeException(o_O);
        }

        return pageSource;
    }

    public static void setWebDriver(WebDriver webDriver) {
        UrlUtils.webDriver = webDriver;
    }
}
