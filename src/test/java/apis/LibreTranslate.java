package apis;

import bobtranslate.util.BadStatusCode;
import bobtranslate.util.MultiPartBodyPublisher;
import com.jayway.jsonpath.JsonPath;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.List;

import static bobthebuildtool.services.Constants.HTTP_CLIENT;
import static bobthebuildtool.services.Functions.encodeUrl;
import static bobtranslate.util.Functions.isSuccessFul;

public final class LibreTranslate implements Translator {

    public enum Language {
        ENGLISH("en"), SPANISH("es");

        private final String lang;
        Language(final String lang) {
            this.lang = lang;
        }
    }

    private final Language sourceLanguage;
    private final Language targetLanguage;

    public LibreTranslate(final Language sourceLanguage, final Language targetLanguage) {
        this.sourceLanguage = sourceLanguage;
        this.targetLanguage = targetLanguage;
    }

    @Override public List<String> translate(final String text) throws IOException {
        return submitFormToApi(text, sourceLanguage.lang, targetLanguage.lang);
    }

    private static List<String> submitFormToApi(final String text, final String sourceLanguage, final String targetLanguage) throws IOException {
        final var referer = "source=" + encodeUrl(sourceLanguage) + "&target=" + encodeUrl(targetLanguage) + "&q=" + encodeUrl(text);

        final var body = new MultiPartBodyPublisher().addPart("q", text)
            .addPart("source", sourceLanguage).addPart("target", targetLanguage)
            .addPart("format", "text");
        final var request = HttpRequest.newBuilder()
            .uri(URI.create("https://libretranslate.com/translate"))
            .header("Content-Type", "multipart/form-data; boundary=" + body.getBoundary())
            .header("Referer", "https://libretranslate.com/?" + referer)
            .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/99.0.4844.74 Safari/537.36")
            .header("Origin", "https://libretranslate.com")
            .header("Cache-Control", "no-cache")
            .header("Accept", "*/*")
            .header("Accept-Language", "en-US,en;q=0.9,nl;q=0.8")
            .method("POST", body.build())
            .build();
        try {
            final var response = HTTP_CLIENT.send(request, BodyHandlers.ofString());
            if (!isSuccessFul(response)) throw new BadStatusCode("Deepl", response.statusCode(), "2XX");
            return List.of(JsonPath.read(response.body(), "$.translatedText"));
        } catch (InterruptedException e) {
            throw new IOException(e);
        }
    }

}
