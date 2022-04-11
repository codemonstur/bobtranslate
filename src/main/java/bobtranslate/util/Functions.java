package bobtranslate.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.util.Map;

import static java.nio.file.Files.readString;

public enum Functions {;

    private static final Type MAP_TYPE = new TypeToken<Map<String, String>>() {}.getType();
    public static Map<String, String> loadTranslationsMap(final Gson gson, final Path path) throws IOException {
        return gson.fromJson(readString(path), MAP_TYPE);
    }

    public static boolean isSuccessFul(final HttpResponse<?> response) {
        return response.statusCode() >= 200 && response.statusCode() <= 299;
    }

}
