package commands.file;

import picocli.CommandLine;

@CommandLine.Command(name = "dir",
        description = "list all files and directories",
        mixinStandardHelpOptions = true,
        descriptionHeading = "%nDescription:%n",
        parameterListHeading = "%nParameters:%n",
        optionListHeading = "Options:%n"
)
public class ListAll extends ListEntries implements Runnable {

    @Override
    public void run() {
        if (!isFile) {
            if (!isDirPermitted(commander.user, path + fullFile, isAbs, 4)) {
                throw new RuntimeException("Permissions to directory denied!");
            }
        } else {
            if (!isDirPermitted(commander.user, path, isAbs,4)) {
                throw new RuntimeException("Permission to directory denied!");
            }
        }
        try {
            listFiles(inode);
        } catch (Exception ignored) {}
        try {
            listDirectories(inode);
        } catch (Exception ignored) {}
    }
}
