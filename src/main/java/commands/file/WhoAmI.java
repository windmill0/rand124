package commands.file;

import commands.Command;
import picocli.CommandLine;

@CommandLine.Command(
        name = "whoami", description = "display your id/gid"

)
public class WhoAmI extends Command implements Runnable{

    @CommandLine.Option(names = {"-g", "--g"}, description = "display group")
    boolean group;

    @Override
    public void run() {
        System.out.println(commander.getName());
    }
}
