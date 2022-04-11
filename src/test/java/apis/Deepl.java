package apis;

import bobtranslate.util.BadStatusCode;
import com.jayway.jsonpath.JsonPath;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.List;

import static apis.Deepl.Language.ENGLISH;
import static apis.Deepl.Language.GERMAN;
import static bobthebuildtool.services.Constants.HTTP_CLIENT;
import static bobtranslate.util.Functions.isSuccessFul;
import static java.net.http.HttpRequest.BodyPublishers.ofString;

public final class Deepl implements Translator {

    public enum Language {
        ENGLISH("EN"), BULGARIAN("BG"), CHINESE("ZH"), CZECH("CS"), DANISH("DA"), DUTCH("NL"),
        ESTONIAN("ET"), FINNISH("FI"), FRENCH("FR"), GERMAN("DE"), GREEK("EL"), HUNGARIAN("HU"),
        ITALIAN("IT"), JAPANESE("JA"), LATVIAN("LV"), LITHUANIAN("LT"), POLISH("PL"),
        PORTUGUESE("PT"), ROMANIAN("RO"), RUSSIAN("RU"), SLOVAK("SK"), SLOVENIAN("SL"),
        SPANISH("ES"), SWEDISH("SV");

        private final String lang;
        Language(final String lang) {
            this.lang = lang;
        }
    }

    @Override public List<String> translate(final String text) throws IOException {
        return translateWithJson(text, ENGLISH.lang, GERMAN.lang);
    }

    private static final String REQUEST = """
            {"jsonrpc":"2.0","method": "LMT_handle_jobs","params":{"jobs":[{"kind":"default","sentences":[{
            "text":"%s","id":0,"prefix":""}],"raw_en_context_before":[],"raw_en_context_after":[],
            "preferred_num_beams":4,"quality":"fast"}],"lang":{"preference":{"weight":{},"default":"default"},
            "source_lang_computed":"%s","target_lang":"%s"},"priority":-1,"commonJobParams":{"browserType":1},
            "timestamp":1647696683909},"id":49040020}
            """;

    private static List<String> translateWithJson(final String text, final String sourceLanguage, final String targetLanguage) throws IOException {
        final var body = String.format(REQUEST, text, sourceLanguage, targetLanguage);
        final var request = HttpRequest.newBuilder()
                .uri(URI.create("https://www2.deepl.com/jsonrpc?method=LMT_handle_jobs"))
                .header("Content-Type", "application/json")
                .header("Origin", "https://www.deepl.com")
                .method("POST", ofString(body))
                .build();
        try {
            final var response = HTTP_CLIENT.send(request, BodyHandlers.ofString());
            if (!isSuccessFul(response)) throw new BadStatusCode("Deepl", response.statusCode(), "2XX");
            return JsonPath.read(response.body(), "$.result.translations[*].beams[*].sentences[*].text");
        } catch (InterruptedException e) {
            throw new IOException(e);
        }
    }

}
