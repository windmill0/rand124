package commands.file;

import commands.FileCommand;
import picocli.CommandLine;

@CommandLine.Command(name = "cd",
        description = "change current directory",
        mixinStandardHelpOptions = true,
        descriptionHeading = "%nDescription:%n",
        parameterListHeading = "%nParameters:%n",
        optionListHeading = "Options:%n"
)
public class ChangeDirectory extends FileCommand implements Runnable {

    @CommandLine.Command(name = "..", description = "navigate back one directory")
    private void moveBack() {
        int idx = path.substring(0, path.length() - 1).lastIndexOf('/');
        commander.user.setCurrentPath(path.substring(0, idx == -1 ? 5 : idx + 1));
    }


    @Override
    public void run() {
        if (isAbs || pathFile.length() != 0) {
            if (!isDirPermitted(commander.user, path + fullFile, isAbs,4)) {
                throw new RuntimeException("Permission to directory denied!");
            }
            commander.user.setCurrentPath(path + fullFile);
        }
    }
}
