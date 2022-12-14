package managers;

import roles.User;

import java.util.Scanner;

public class Authenticate {

    public static User login(String username) {
        Scanner input = new Scanner(System.in);

        if (!UserManager.hasUser(username)) {
            throw new RuntimeException("User doesn't exist!");
        }

        System.out.println("Enter the password:");

        if (!UserManager.isPassword(username, input.next())) {
            throw new RuntimeException("Incorrect password");
        }

        System.out.printf("Welcome %s!\n", username);

        return new User(username);
    }

}
