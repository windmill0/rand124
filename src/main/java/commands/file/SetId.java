package commands.file;

import commands.FileCommand;
import managers.DirectoryManager;
import managers.FileManager;
import managers.UserManager;
import org.json.JSONObject;
import picocli.CommandLine;

@CommandLine.Command(
        name = "seteid",
        description = "set effective user/group id",
        mixinStandardHelpOptions = true
)
public class SetId extends FileCommand implements Runnable{

    @CommandLine.Option(names = {"-g", "--group"})
    boolean group;

    @CommandLine.Parameters(arity = "1")
    String newId;

    @Override
    public void run() {
        if (!isOwner(commander.user, pathFile, path, isFile)) {
            throw new RuntimeException("Permission denied1");
        }
        if (!UserManager.hasUser(newId)) {
            throw new RuntimeException("User doesn't exist!");
        }
        JSONObject target = isFile ? FileManager.getFile(pathFile, path) : DirectoryManager.getDirectory(path + fullFile);

        if (group)
            target.put("egid", newId);
        else
            target.put("eid", newId);
    }
}
