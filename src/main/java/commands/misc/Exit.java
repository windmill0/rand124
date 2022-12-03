package commands.misc;

import commands.Command;
import picocli.CommandLine;

@CommandLine.Command(name = "exit", description = "Exit program")
public class Exit extends Command implements Runnable {
    @Override
    public void run() {
        System.exit(0);
    }
}
