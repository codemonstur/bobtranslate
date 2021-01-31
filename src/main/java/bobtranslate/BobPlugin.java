package bobtranslate;

import bobthebuildtool.pojos.buildfile.Project;
import bobthebuildtool.pojos.error.InvalidInput;
import bobthebuildtool.services.Log;
import jcli.errors.InvalidCommandLine;

import java.io.IOException;
import java.util.Map;
import java.util.function.Consumer;

import static bobtranslate.actions.MissingLanguageKeys.checkKeysInDir;
import static bobtranslate.actions.MissingTranslations.checkValuesInDir;
import static jcli.CliParserBuilder.newCliParser;

public enum BobPlugin {;

    private static final String DESCRIPTION_TRANSLATE = "Various tools for fixing language files";

    public static void installPlugin(final Project project) {
        project.addCommand("translate", DESCRIPTION_TRANSLATE, BobPlugin::translate);
    }

    private static int translate(final Project project, final Map<String, String> env, final String[] args)
            throws InvalidCommandLine, InvalidInput, IOException {
        final CliTranslate arguments = newCliParser(CliTranslate::new).parse(args);

        final var reporter = toReporter(arguments.logLevel);
        switch (arguments.command) {
            case keys:
                return checkKeysInDir(arguments.path, reporter);
            case values:
                return checkValuesInDir(arguments.path, reporter);
            case translate:
                throw new InvalidInput("Option 'translate' has not been implemented yet.");
        }

        return 0;
    }

    private static Consumer<String> toReporter(final int logLevel) throws InvalidInput {
        if (logLevel == 1) return Log::logInfo;
        if (logLevel == 2) return Log::logWarning;
        if (logLevel == 4) return Log::logError;
        throw new InvalidInput("Unknown log level: " + logLevel);
    }

}
