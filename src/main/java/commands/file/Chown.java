package commands.file;

import commands.FileCommand;
import managers.DirectoryManager;
import managers.FileManager;
import managers.GroupManager;
import managers.UserManager;

import org.json.JSONObject;
import picocli.CommandLine;

@CommandLine.Command(
        name = "chown",
        description = "change owner/group of file/directory",
        mixinStandardHelpOptions = true,
        descriptionHeading = "%nDescription:%n",
        parameterListHeading = "%nParameters:%n",
        optionListHeading = "Options:%n"
)
public class Chown extends FileCommand implements Runnable {

    String owner;
    String group;

    @CommandLine.Parameters(arity = "1", description = "owner:group")
    void setInput(String arg) {
        int idx = arg.indexOf(":");
        if (idx == -1) {
            throw new RuntimeException("Invalid input! Must contain ':'");
        }
        owner = arg.substring(0, idx);
        group = arg.substring(idx + 1);
    }

    @Override
    public void run() {
        if (!isOwner(commander.user, pathFile, path, isFile)) {
            throw new RuntimeException("Permission denied!");
        }
        JSONObject target = isFile ? FileManager.getFile(pathFile, path) : DirectoryManager.getDirectory(path + fullFile);
        if (UserManager.hasUser(owner)) {
            target.put("owner", owner);
        }
        if (GroupManager.hasGroup(group)) {
            target.put("group", group);
        }
        System.out.println("Operation done!");
    }
}
