import managers.*;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;


public class LinuxMock {

    public static void main(String[] args) throws IOException {

        // Exception in method signature for testing purposes

        // Save all jsons before exit
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                UserManager.saveFile();
                FileManager.saveFile();
                DirectoryManager.saveFile();
                Linker.saveFile();
                System.out.println("Files Saved");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }));

         // ---------- Reset all files back to just root -------------
    }

    static void run(boolean loggedIn) {
        Scanner input = new Scanner(System.in);

        //  ---------- Add a particular user -------------
//        try {
//            UserManager.addUser("monkey", "1234");
//        } catch (IOException | RuntimeException e) {
//            System.out.println(e.getMessage());
//        }

        // ------------ Simple login loop ------------
        while (!loggedIn) {
                try {
                    System.out.println("Enter your username:");
                    loggedIn = Authenticate.login(input.next());
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }

    }

    static void resetFiles() {
        DirectoryManager.removeDirectories("root");
        FileManager.removeFiles("root");
    }
}

