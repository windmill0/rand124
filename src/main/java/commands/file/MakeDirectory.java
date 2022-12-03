package commands.file;


import commands.FileCommand;

import managers.DirectoryManager;
import picocli.CommandLine;

import java.util.concurrent.Callable;

@CommandLine.Command(name = "mkdir",
        description = "create directory",
        mixinStandardHelpOptions = true,
        descriptionHeading = "%nDescription:%n",
        parameterListHeading = "%nParameters:%n",
        optionListHeading = "Options:%n"
)
public class MakeDirectory extends FileCommand implements Callable<Integer> {

    @Override
    public Integer call() {
        if(!isDirPermitted(commander.user, path, isAbs,6)) {
            throw new RuntimeException("Permission denied!");
        }

        DirectoryManager.makeDirectory(pathFile, path, commander.getName(), commander.getMask());

        System.out.println(pathFile + " created");
        return null;
    }
}
