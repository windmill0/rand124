package commands.misc;

import commands.Command;
import managers.UserManager;
import picocli.CommandLine;

import java.io.IOException;
import java.util.Scanner;

@CommandLine.Command(
        name = "user",
        description = "add/delete/switch users",
        mixinStandardHelpOptions = true,
        descriptionHeading = "%nDescription:%n",
        parameterListHeading = "%nParameters:%n",
        optionListHeading = "Options:%n"
)
public class UserOp extends Command {

    Scanner input;

    UserOp() {
        input = new Scanner(System.in);
    }

    @CommandLine.Parameters(arity = "1", scope = CommandLine.ScopeType.INHERIT)
    String username;


    @CommandLine.Command(name = "-a", description = "add user", mixinStandardHelpOptions = true)
    void registerUser() throws IOException {
        if (!commander.getName().matches("root")) {
            throw new RuntimeException("Only root can add users!");
        }
        System.out.println("Enter a password");
        UserManager.addUser(username, input.nextLine());
    }

    @CommandLine.Command(name = "-r", description = "delete user", mixinStandardHelpOptions = true)
    void removeUser() {
        if (!commander.getName().matches("root")) {
            throw new RuntimeException("Only root delete users!");
        }
        UserManager.removeUser(username);
    }

    @CommandLine.Command(name = "-s", description = "switch user", mixinStandardHelpOptions = true)
    void switchUser() {
        System.out.printf("Enter %s's password\n", username);
        if (!UserManager.isPassword(username, input.nextLine())) {
            throw new RuntimeException("Incorrect password!");
        }
        commander.user.setUsername(username);
        System.out.println("Welcome " + username);
    }

}
