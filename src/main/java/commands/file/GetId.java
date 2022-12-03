package commands.file;

import commands.FileCommand;
import managers.DirectoryManager;
import managers.FileManager;
import org.json.JSONObject;
import picocli.CommandLine;

@CommandLine.Command(
        name = "geteid",
        description = "get file effective user/group id",
        mixinStandardHelpOptions = true
)
public class GetId extends FileCommand implements Runnable{

    @CommandLine.Option(names = {"-g", "--g"}, description = "get effective group id")
    boolean group;


    @Override
    public void run() {
        JSONObject target = isFile ? FileManager.getFile(pathFile, path) : DirectoryManager.getDirectory(path + fullFile);

        System.out.println(group ? target.getString("egid") : target.getString("eid"));
    }
}
