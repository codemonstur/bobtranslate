package bobtranslate.actions;

import com.google.gson.Gson;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

import static bobtranslate.util.IO.loadTranslationsMap;
import static java.util.stream.Collectors.toList;

public enum MissingLanguageKeys {;

    public static int checkKeysInDir(final Path dir, final Consumer<String> reporter) throws IOException {
        final var keysPerFile = new HashMap<Path, Set<String>>();
        final var allKeys = new HashSet<String>();

        final var names = Files.list(dir).collect(toList());
        final var gson = new Gson();
        for (final var name : names) {
            final var keys = loadTranslationsMap(gson, name);
            keysPerFile.put(name, keys.keySet());
            allKeys.addAll(keys.keySet());
        }

        int returnCode = 0;
        for (final var name : names) {
            final var missingKeys = subtractSet(allKeys, keysPerFile.get(name));
            if (!missingKeys.isEmpty()) {
                reporter.accept("File " + dir + "/" + name + " is missing " + missingKeys.size() + " keys: " + missingKeys);
                returnCode++;
            }
        }

        return returnCode;
    }

    private static <T> Set<T> subtractSet(final Set<T> set, final Set<T> remove) {
        final var newSet = new HashSet<T>(set);
        newSet.removeAll(remove);
        return newSet;
    }

}
