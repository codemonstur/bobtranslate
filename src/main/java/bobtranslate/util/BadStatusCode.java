package bobtranslate.util;

import java.io.IOException;

public final class BadStatusCode extends IOException {
    public BadStatusCode(final String apiName, final int statusCode, final String expected) {
        super(apiName + " returned incorrect status code " + statusCode + ", expected " + expected);
    }
}
