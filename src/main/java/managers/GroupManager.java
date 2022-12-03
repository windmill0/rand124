package managers;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class GroupManager {

    static FileReader reader;
    static FileWriter fileWriter;
    static File file;

    static JSONParser parser = new JSONParser();
    static JSONObject groups;

    static {
        try {
            file = new File("Database/groups.json");
            reader = new FileReader("Database/groups.json");
            Object obj = parser.parse(reader);
            groups = new JSONObject(obj.toString());
            fileWriter = new FileWriter(file, false);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    public static boolean hasGroup(String name) {
        return groups.has(name);
    }

    public static JSONArray getGroup(String name) {
        if (!groups.has(name)) {
            throw new RuntimeException("Group doesn't exist");
        }
        return groups.getJSONArray(name);
    }

    public static void addGroup(String name) {
        if (groups.has(name)) {
            throw new RuntimeException("Group already exists!");
        }
        groups.put(name, new JSONArray());
    }

    public static void removeGroup(String name) {
        JSONArray members = groups.getJSONArray(name);
        for (int i = 0; i < members.length(); i++) {
            UserManager.deleteGroup(members.getString(i), name);
        }
        groups.remove(name);
    }

    public static boolean isMember(String user, String group) {
        JSONArray members = getGroup(group);
        for (int i = 0; i < members.length(); i++) {
            if (user.matches(members.getString(i))) {
                return true;
            }
        }
        return false;
    }

    public static void addToGroup(String user, String group) {
        if (isMember(user, group)) {
            throw new RuntimeException("User already in group!");
        }
        UserManager.getUser(user).getJSONArray("groups").put(group);
        getGroup(group).put(user);
    }

    public static void kickMember(String user, String group) {
        JSONArray members = getGroup(group);
        for (int i = 0; i < members.length(); i++) {
            if (user.matches(members.getString(i))) {
                UserManager.deleteGroup(user, group);
                members.remove(i);
            }
        }
        if (members.length() == 0) {
            removeGroup(group);
        }
    }

    public static void saveFile() throws IOException {
        fileWriter.flush();
        if (isEmpty()) {
            fileWriter.write(groups.toString());
        }
        fileWriter.close();
    }

    private static boolean isEmpty() throws IOException {
        return reader.read() == -1;
    }

}
