package Managers;

import Roles.User;
import org.json.simple.parser.JSONParser;
import org.json.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.*;

public class UserManager {

    static FileReader reader;
    static FileWriter fileWriter;
    static File file;

    static {
        try {
            file = new File("src/Database/users.json");
            reader = new FileReader("src/Database/users.json");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static JSONParser parser = new JSONParser();
    static JSONObject users;

    static {
        try {
            Object obj = parser.parse(reader);
            users = new JSONObject(obj.toString());
            fileWriter = new FileWriter(file, false);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    private static String userFrom(int id, String password) {
        return String.format("{\"id\":%d,\"groups\":[],\"password\":\"%s\"}", id, password);
    }

    public static boolean hasUser(String username) {
        return users.has(username);
    }

    public static User addUser(String name, String password) throws IOException {
        int id = (int) (Math.random() * 100);
        if (!hasUser(name)) {
            users.put(name, new JSONObject(userFrom(id, password)));
            fileWriter.write(users.toString());
        }   else {
            throw new RuntimeException("User already exists!");
        }
        return new User(id , name);
    }

    public static String getPassword(String username) {
        return users.getJSONObject(username).getString("password");
    }

    public static void saveFile() throws IOException {
        fileWriter.flush();
        if (isEmpty()) {
            System.out.println("No file modifications were made...");
            fileWriter.write(users.toString());
        }
        fileWriter.close();
    }

    public static boolean isEmpty() throws IOException {
        return reader.read() == -1;
    }
}
