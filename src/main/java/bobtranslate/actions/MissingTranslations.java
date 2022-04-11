package bobtranslate.actions;

import com.google.gson.Gson;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static bobtranslate.util.Functions.loadTranslationsMap;
import static java.util.stream.Collectors.toList;

public enum MissingTranslations {;

    public static int checkValuesInDir(final Path dir, final Consumer<String> reporter) throws IOException {
        int returnCode = 0;

        final var gson = new Gson();
        for (final var name : Files.list(dir).collect(toList())) {
            final var missingTranslations = checkTranslationsInFile(gson, name);
            if (!missingTranslations.isEmpty()) {
                reporter.accept("File " + name + " is missing " + missingTranslations.size() + " translations: " + missingTranslations);
                returnCode++;
            }
        }

        return returnCode;
    }

    private static List<String> checkTranslationsInFile(final Gson gson, final Path path) throws IOException {
        final var missingTranslations = new ArrayList<String>();

        for (final var entry : loadTranslationsMap(gson, path).entrySet()) {
            if (entry.getValue().isEmpty()) {
                missingTranslations.add(entry.getKey());
            }
        }

        return missingTranslations;
    }

}
