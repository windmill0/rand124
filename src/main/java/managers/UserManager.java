package managers;

import org.json.JSONArray;
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
            file = new File("Database/users.json");
            reader = new FileReader("Database/users.json");
            Object obj = parser.parse(reader);
            users = new JSONObject(obj.toString());
            fileWriter = new FileWriter(file, false);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    public static JSONObject getUser(String username) {
        if (!users.has(username)) {
            throw new RuntimeException("User doesn't exist!");
        }
        return users.getJSONObject(username);
    }

    public static boolean hasUser(String username) {
        return users.has(username);
    }

    public static void addUser(String name, String password) throws IOException {
        int id = (int) (Math.random() * 100);
        if (hasUser(name)) {
            throw new RuntimeException("User already exists!");
        }
        users.put(name, new JSONObject(userFrom(id, name, password)));
        fileWriter.write(users.toString());
        GroupManager.addGroup(name);
        GroupManager.addToGroup(name, name);
    }

    public static void removeUser(String name) {
        JSONObject user = getUser(name);
        JSONArray groups = user.getJSONArray("groups");

        for (int i = 0; i < groups.length(); i++) {
            GroupManager.kickMember(name, groups.getString(i));
        }
        users.remove(name);
    }

    public static void deleteGroup(String user, String group) {
        JSONArray groups = getUser(user).getJSONArray("groups");
        for (int i = 0; i < groups.length(); i++) {
            if (groups.getString(i).matches(group)) {
                groups.remove(i);
            }
        }
    }

    public static void saveFile() throws IOException {
        fileWriter.flush();
        if (isEmpty()) {
            fileWriter.write(users.toString());
        }
        fileWriter.close();
    }

    public static boolean isPassword(String username, String password) {
        return Objects.equals(password, UserManager.getPassword(username));
    }

    private static String getPassword(String username) {
        return getUser(username).getString("password");
    }


    private static boolean isEmpty() throws IOException {
        return reader.read() == -1;
    }

    private static String userFrom(int id, String name, String password) {
        return String.format("{\"id\":%d,\"groups\":[\"%s\"],\"password\":\"%s\"}", id, name, password);
    }
}
