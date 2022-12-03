package roles;

import managers.UserManager;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class User {
    private int umask;
    private String username;
    private String currentPath;
    private List<String[]> log;
    private JSONObject data;
    // Consider adding Sudoer flag

    public User(String name) {
        this.data = UserManager.getUser(name);
        this.username = name;
        this.currentPath = "root/";
        this.umask = 2;
        this.log = new ArrayList<>();
    }

    public User(String name, List<String[]> log) {
        this(name);
        this.log = log;
    }
    
    public String getUsername() {
        return this.username;
    }

    public void setUsername(String name) {
        this.username = name;
        this.data = UserManager.getUser(name);
    }

    public String getCurrentPath() {
        return currentPath;
    }

    public void setCurrentPath(String currentPath) {
        this.currentPath = currentPath;
    }

    public String[] getGroups() {
        JSONArray array = data.getJSONArray("groups");
        String[] groups = new String[array.length()];
        for(int i = 0; i < array.length(); i++){
            groups[i] = array.getString(i);
        }

        return groups;
    }

    public int getUmask() {
        return umask;
    }

    public void setUmask(int umask) {
        this.umask = umask;
    }

    public List<String[]> getLog() {
        return log;
    }
}
