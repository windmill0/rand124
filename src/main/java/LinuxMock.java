import managers.*;
import roles.User;

import java.io.IOException;
import java.util.Scanner;

public class LinuxMock {

    public static void main(String[] args) {
        preExit();

        System.out.println("Welcome to linux-mock, type -h or --help to list the available commands!");
        System.out.println("You can also type the command name followed up with -h for more information...");
        User user = run(false);
        CommandHandler commandHandler = new CommandHandler(user);

        Scanner input = new Scanner(System.in);
        while(true) {
            System.out.print(user.getCurrentPath() + ':');
            commandHandler.runCommand(input.nextLine().split(" "));
        }
    }

    static User run(boolean loggedIn) {
        Scanner input = new Scanner(System.in);

        while (!loggedIn) {
                try {
                    System.out.println("Enter your username:");
                    return Authenticate.login(input.next());
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        return null;
    }

    static void saveFiles() throws IOException {
        UserManager.saveFile();
        FileManager.saveFile();
        DirectoryManager.saveFile();
        LinkManager.saveFile();
        GroupManager.saveFile();
    }

    static void preExit() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                saveFiles();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }));
    }
}

