package commands.file;

import commands.FileCommand;
import managers.DirectoryManager;
import picocli.CommandLine;

import java.util.Objects;

@CommandLine.Command(name = "rmdir",
        description = "remove directory",
        mixinStandardHelpOptions = true,
        descriptionHeading = "%nDescription:%n",
        parameterListHeading = "%nParameters:%n",
        optionListHeading = "Options:%n"
)
public class RemoveDirectory extends FileCommand implements Runnable {

    @Override
    public void run() {
        if (!isDirPermitted(commander.user, path + fullFile, isAbs,6)) {
            throw new RuntimeException("Permission denied");
        }
        if (Objects.equals(pathFile, "")) {
            throw new RuntimeException("directory must be specified!");
        }

        DirectoryManager.removeDirectory(pathFile, path);
    }
}
