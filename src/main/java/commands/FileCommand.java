package commands;

import managers.FileManager;
import picocli.CommandLine;

@CommandLine.Command(name = "f_operator")
public class FileCommand extends Command {

    protected String path;
    protected String pathFile;
    protected String fullFile;
    protected boolean isFile;
    protected boolean isAbs;

    @CommandLine.Parameters(defaultValue = "",
            paramLabel = "<path>",
            scope = CommandLine.ScopeType.INHERIT,
            description = "target path"
    )
    public void setPath(String target) {
        if (target.length() > 0 && target.charAt(0) == '/') {
            isAbs = true;
            target = "root" + target;
        } else {
            target = commander.user.getCurrentPath() + target;
            isAbs = false;
        }

        int idx = target.lastIndexOf('/');
        path = target.substring(0, idx + 1);
        pathFile = target.substring(idx + 1);
        fullFile = pathFile + (pathFile.length() > 0 ? "/" : "");
        isFile = FileManager.getDirFiles(path).has(pathFile);
    }
}
