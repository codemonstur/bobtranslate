import io.github.bonigarcia.wdm.managers.ChromeDriverManager;

import java.io.IOException;
import java.net.URLEncoder;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static io.github.bonigarcia.wdm.config.DriverManagerType.CHROME;
import static java.nio.charset.StandardCharsets.UTF_8;

public class Scraping {

    // Doesn't work. Direct 403 Forbidden. Probably detected by cloudflare using the HTTPS fingerprinting method.
    public static String scrapeUsingJsoup(final String url) throws IOException {
        return Jsoup.connect(url).get().html();
    }

    // Works but pops up the browser itself, which will make it fail in linux cli. When headless mode is
    // enabled it fails because you don't get past the cloudflare javascript challenge
    public static String scrapeUsingSelenide(final String url) {
        // Configuration.headless = true;
        // Configuration.startMaximized = true;
        ChromeDriverManager.getInstance(CHROME).setup();
        open(url);
        return $("html").innerHtml();
    }

    // Returns the cloudflare javascript challenge, maybe if I wait a little bit and allow running of JavaScript?
    public static String scrapeUsingPlaywright(final String url) {
        try (final var playwright = Playwright.create();
             final var browser = playwright.chromium().launch()) {
            final Page page = browser.newPage();
            page.navigate(url);
            return page.innerHTML("html");
        }
    }

    // Works but my free plan will run out after a while, and really slow
    record Response(String content, int status_code) {}
    public static String scrapeUsingAPI(final String url, final String apiKey) throws IOException {
        return newHttpCall()
            .scheme("https").hostname("api.scrapingant.com")
            .get("/v1/general?url=" + encodeUrl(url))
            .header("x-api-key", apiKey)
            .execute()
            .verifyNotServerError()
            .verifySuccess()
            .fetchBodyInto(Response.class)
            .content;
    }

    private static String encodeUrl(final String value) {
        return value != null ? URLEncoder.encode(value, UTF_8) : "";
    }

}
