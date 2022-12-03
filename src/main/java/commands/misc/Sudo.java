package commands.misc;

import commands.Command;
import managers.UserManager;
import picocli.CommandLine;

import java.util.Arrays;
import java.util.Scanner;

@CommandLine.Command(
        name = "sudo",
        description = "run command as root",
        mixinStandardHelpOptions = true
)
public class Sudo extends Command implements Runnable {

    String[] args;

    @CommandLine.Parameters(arity = "1..*")
    void setArgs(String[] cmd) {
        Scanner input = new Scanner(System.in);

        if (cmd[0].matches("sudo")) {
            throw new RuntimeException("Sudo can only be called once!");
        }

        System.out.println("Enter password:");

        if (!UserManager.isPassword(commander.getName(), input.nextLine())) {
            throw new RuntimeException("Incorrect password!");
        }

        args = cmd;
    }

    @Override
    public void run() {
        String name = commander.getName();
        if (!Arrays.asList(commander.user.getGroups()).contains("sudo")) {
            throw new RuntimeException("User not in sudo group");
        }
        commander.user.setUsername("root");
        new CommandLine(commander).setExecutionExceptionHandler((ex, commandLine, parseResult) -> {
            System.out.println(ex.getMessage());
            return commandLine.getCommandSpec().exitCodeOnExecutionException();
        }).execute(args);
        commander.user.setUsername(name);
    }
}
