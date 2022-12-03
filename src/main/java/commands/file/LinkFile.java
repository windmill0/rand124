package commands.file;

import commands.FileCommand;
import managers.LinkManager;
import picocli.CommandLine;

import java.util.concurrent.Callable;

@CommandLine.Command(name = "ln",
        description = "create a hardlink",
        mixinStandardHelpOptions = true,
        descriptionHeading = "%nDescription:%n",
        parameterListHeading = "%nParameters:%n",
        optionListHeading = "Options:%n"
)
public class LinkFile extends FileCommand implements Callable<Integer> {

    @CommandLine.Parameters(description = "new link", arity = "1")
    String newLink;

    @Override
    public Integer call() throws Exception {

        if (!isDirPermitted(commander.user, path, isAbs,1)) {
            throw new RuntimeException("Permission denied!");
        }
        LinkManager.makeLink(path, pathFile,
                commander.user.getCurrentPath() + newLink, commander.getName(), commander.getMask());

        return null;
    }

}
