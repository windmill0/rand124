import commands.Commander;
import picocli.CommandLine;
import picocli.CommandLine.IExecutionExceptionHandler;
import roles.User;


public class CommandHandler {

    private final CommandLine handler;
    private final Commander commander;
    IExecutionExceptionHandler errorHandler;

    public CommandHandler(User user) {
        this.errorHandler = (ex, commandLine, parseResult) -> {
            System.out.println(ex.getMessage());
            return commandLine.getCommandSpec().exitCodeOnExecutionException();
        };
        commander = new Commander(user);
        this.handler = new CommandLine(commander)
                .setExecutionExceptionHandler(errorHandler)
                .setParameterExceptionHandler(new InvalidCommandHandler());
        this.handler.getSubcommands()
                .get("sudo").getCommandSpec().parser()
                .unmatchedOptionsArePositionalParams(true);
    }

    public void runCommand(String... args) {
        if (handler.execute(args) == 0) {
            commander.getLog().add(new String[] {commander.getName(), String.join(" ", args)});
        }
    }
}
