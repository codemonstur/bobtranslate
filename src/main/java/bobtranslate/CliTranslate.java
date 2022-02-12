package bobtranslate;

import jcli.annotations.CliCommand;
import jcli.annotations.CliOption;
import jcli.annotations.CliPositional;
import jcli.annotations.CliUnderscoreIsDash;

import java.nio.file.Path;

@CliCommand
public class CliTranslate {

    @CliOption(name = 'p', longName = "path", isMandatory = true, description = "Path to file or directory")
    public Path path;

    @CliOption(name = 'l', longName = "log-level", description = "Log level to use for reporting", defaultValue = "2")
    public int logLevel;

    @CliPositional
    public TranslateCommand command;

    @CliUnderscoreIsDash
    public enum TranslateCommand {
        check_keys, check_values, translate
    }

}
