package commands.file;

import commands.FileCommand;
import managers.DirectoryManager;
import managers.FileManager;
import org.json.JSONObject;
import picocli.CommandLine;

@CommandLine.Command(name = "chmod",
        description = "change file/directory permissions",
        mixinStandardHelpOptions = true,
        descriptionHeading = "%nDescription:%n",
        parameterListHeading = "%nParameters:%n",
        optionListHeading = "Options:%n"
)
public class ChangePermissions extends FileCommand implements Runnable {

    int newPerm;
    JSONObject target;

    @CommandLine.Parameters(arity = "1", paramLabel = "<new permission>")
    void setNewPerm(int permissions) {
        if (777 < permissions || permissions < 0) {
            throw new RuntimeException("Invalid permissions");
        }
        newPerm = permissions;
        target = isFile ? FileManager.getFile(pathFile, path) : DirectoryManager.getDirectory(path + fullFile);
    }

    @Override
    public void run() {
        if (!isOwner(commander.user, pathFile, path, isFile)) {
            throw new RuntimeException("Permission denied!");
        }
        target.put("permissions", newPerm);
        System.out.println("Permission set!");
    }
}
