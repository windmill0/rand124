package commands.file;

import commands.FileCommand;
import managers.FileManager;
import picocli.CommandLine;

import java.util.concurrent.Callable;

@CommandLine.Command(name = "touch",
        description = "create file",
        mixinStandardHelpOptions = true,
        descriptionHeading = "%nDescription:%n",
        parameterListHeading = "%nParameters:%n",
        optionListHeading = "Options:%n"
)
public class CreateFile extends FileCommand implements Callable<Integer> {

    @Override
    public Integer call() throws Exception {
        if (!isDirPermitted(commander.user, path, isAbs,6)) {
            throw new RuntimeException("Permission denied!");
        }
        FileManager.makeFile(pathFile, path, commander.getName(), commander.getMask());
        System.out.printf("%s created\n", pathFile);
        return null;
    }
}
