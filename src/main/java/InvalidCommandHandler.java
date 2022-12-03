import picocli.CommandLine;
import picocli.CommandLine.*;

import java.io.PrintWriter;

public class InvalidCommandHandler implements IParameterExceptionHandler {

    public int handleParseException(ParameterException ex, String[] args) {
        CommandLine cmd = ex.getCommandLine();
        PrintWriter writer = cmd.getErr();

        writer.println(ex.getMessage());
        UnmatchedArgumentException.printSuggestions(ex, writer);

        Model.CommandSpec spec = cmd.getCommandSpec();
        writer.printf("Try '-h or --help' for more information.%n");

        return cmd.getExitCodeExceptionMapper() != null
                ? cmd.getExitCodeExceptionMapper().getExitCode(ex)
                : spec.exitCodeOnInvalidInput();
    }
}
