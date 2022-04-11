import apis.Deepl;

import java.io.IOException;

public class TestTranslateApis {

    public static void main(final String... args) throws IOException {
//        final var translator = new LibreTranslate(ENGLISH, SPANISH);
        final var translator = new Deepl();
        final var text = translator.translate("Hello, world!");
        System.out.println(text);
    }

}
