import Managers.UserManager;

import java.io.IOException;
import java.util.Scanner;


public class LinuxMock {

    public static void main(String[] args) {

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                System.out.println("Saving files"); // Save all jsons before exit
                UserManager.saveFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }));

        run(false);

    }

    static void run(boolean loggedIn) {
        Scanner input = new Scanner(System.in);

        //  ---------- Add a particular user -------------
//        try {
//            UserManager.addUser("monkey", "1234");
//        } catch (IOException | RuntimeException e) {
//            System.out.println(e.getMessage());
//        }

        // simple login loop...
        do {
            try {
                System.out.println("Enter your username:");
                loggedIn = Authenticate.login(input.next());
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        } while (!loggedIn);

        loggedIn = true;
    }
}

