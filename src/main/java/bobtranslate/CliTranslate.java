package bobtranslate;

import jcli.annotations.CliCommand;
import jcli.annotations.CliOption;

import java.nio.file.Path;

public class CliTranslate {

    @CliOption(name = 'p', longName = "path", isMandatory = true, description = "Path to file or directory")
    public Path path;

    @CliOption(name = 'l', longName = "log-level", description = "Log level to use for reporting", defaultValue = "2")
    public int logLevel;

    @CliCommand
    public TranslateCommand command;

    public enum TranslateCommand {
        keys, values, translate
    }

}
