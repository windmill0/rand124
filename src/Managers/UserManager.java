package managers;

import org.json.simple.parser.JSONParser;
import org.json.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.Objects;

public class UserManager {

    static FileReader reader;
    static FileWriter fileWriter;
    static File file;

    static JSONParser parser = new JSONParser();
    static JSONObject users;

    static {
        try {
            file = new File("src/Database/users.json");
            reader = new FileReader("src/Database/users.json");
            Object obj = parser.parse(reader);
            users = new JSONObject(obj.toString());
            fileWriter = new FileWriter(file, false);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    public static boolean hasUser(String username) {
        return users.has(username);
    }

    public static void addUser(String name, String password) throws IOException {
        int id = (int) (Math.random() * 100);
        if (hasUser(name)) {
            throw new RuntimeException("User already exists!");
        }
        users.put(name, new JSONObject(userFrom(id, password)));
        fileWriter.write(users.toString());
    }

    private static String getPassword(String username) {
        return users.getJSONObject(username).getString("password");
    }

    protected static boolean isPassword(String username, String password) {
        return !Objects.equals(password, UserManager.getPassword(username));
    }

    public static void saveFile() throws IOException {
        fileWriter.flush();
        if (isEmpty()) {
            fileWriter.write(users.toString());
        }
        fileWriter.close();
    }

    private static boolean isEmpty() throws IOException {
        return reader.read() == -1;
    }

    private static String userFrom(int id, String password) {
        return String.format("{\"id\":%d,\"groups\":[],\"password\":\"%s\"}", id, password);
    }
}
