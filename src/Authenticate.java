import Managers.FileManager;
import Managers.UserManager;
import Roles.User;

import java.util.Objects;
import java.util.Scanner;

public class Authenticate {

    static boolean login(String username) {
        Scanner input = new Scanner(System.in);

        if (!UserManager.hasUser(username)) {
            throw new RuntimeException("User doesn't exist!");
        }

        System.out.println("Enter the password:");

        if (!Objects.equals(input.next(), UserManager.getPassword(username))) {
            throw new RuntimeException("Incorrect password");
        }

        System.out.printf("Welcome %s!\n", username);

        return true;
    }

    static boolean access(User user, FileManager target, String opType) {
        System.out.println("Authenticating user");

        // Get file and user permissions ( Overloading )

        // return whether the user permissions are sufficient
        return true;
    }

}
