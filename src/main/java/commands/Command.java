package commands;

import managers.DirectoryManager;
import managers.FileManager;
import org.json.JSONObject;
import picocli.CommandLine;
import roles.User;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@CommandLine.Command
public class Command {

    @CommandLine.ParentCommand
    public Commander commander;

    public boolean isDirPermitted(User user, String path, boolean isAbs, int requiredPerms) {
        if (user.getUsername().matches("root")) {
            return true;
        }
        Pattern pattern = Pattern.compile(".*\\/(?=.*\\/)", Pattern.MULTILINE);
        Matcher matcher;

        String base = isAbs ? "root/" : user.getCurrentPath();

        while (!path.matches(base)) {
            matcher = pattern.matcher(path);
            System.out.println("Checking permissions for " + path);
            if ((getActivePermission(user, DirectoryManager.getDirectory(path))
                    & requiredPerms) != requiredPerms) {
                return false;
            }
            if (matcher.find()) {
                path = matcher.group(0);
            }
            requiredPerms = 4;
        }
        return true;
    }

    public boolean isFilePermitted(User user, String name, String path, boolean isAbs, int requiredPerms) {
        if (user.getUsername().matches("root")) {
            return true;
        }
        JSONObject file = FileManager.getFile(name, path);
        int activePerms = getActivePermission(user, file);
        return ((activePerms & requiredPerms) == requiredPerms) && isDirPermitted(user, path, isAbs,4);
    }
    
    public boolean isOwner(User user, String name, String path, boolean isFile) {
        if (isFile) {
            return commander.getName().matches("root|" + FileManager.getOwner(name, path));
        }
        name += '/';
        return commander.getName().matches("root|" + DirectoryManager.getOwner(path + name));
    }

    public static int getActivePermission(User user, JSONObject target) {
        int activePerms = target.getInt("permissions");
        JSONObject facl = target.getJSONObject("facl");
        if (!Objects.equals(user.getUsername(), target.getString("owner"))) {
            for (String group : user.getGroups()) {
                if (group.matches(target.getString("group"))) {
                    activePerms = activePerms / 10;
                    break;
                }
            }
            activePerms %= 10;
        } else {
            activePerms /= 100;
        }
        return facl.has(user.getUsername()) ? activePerms | facl.getInt(user.getUsername()) : activePerms;
    }
}
