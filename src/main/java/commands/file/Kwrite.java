package commands.file;

import commands.FileCommand;
import picocli.CommandLine;

import java.awt.*;
import java.io.File;
import java.util.concurrent.Callable;

@CommandLine.Command(
        name = "kwrite",
        description = "edit file using default editor",
        mixinStandardHelpOptions = true,
        descriptionHeading = "%nDescription:%n",
        parameterListHeading = "%nParameters:%n",
        optionListHeading = "Options:%n"
)
public class Kwrite extends FileCommand implements Callable<Integer> {

    @Override
    public Integer call() throws Exception {
        if (!isFilePermitted(commander.user, pathFile, path, isAbs,6)) {
            throw new RuntimeException("Permission denied");
        }
        File file = new File(path + pathFile);
        Desktop.getDesktop().edit(file);
        return null;
    }
}
