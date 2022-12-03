package commands.file;

import commands.FileCommand;
import managers.FileManager;
import picocli.CommandLine;

@CommandLine.Command(name = "rm", description = "remove file",
        mixinStandardHelpOptions = true,
        descriptionHeading = "%nDescription:%n",
        parameterListHeading = "%nParameters:%n",
        optionListHeading = "Options:%n"
)
public class RemoveFile extends FileCommand implements Runnable {
    @Override
    public void run() {
        if (!isDirPermitted(commander.user, path, isAbs,7)) {
            throw new RuntimeException("Permission to file denied");
        }
        FileManager.removeFile(pathFile, path);
        System.out.printf("%s removed\n", pathFile);
    }
}
