package commands.file;

import commands.FileCommand;
import managers.DirectoryManager;
import managers.FileManager;
import org.json.JSONArray;
import org.json.JSONObject;
import picocli.CommandLine;


@CommandLine.Command(name = "ls",
        description = "list directory entries",
        mixinStandardHelpOptions = true,
        descriptionHeading = "%nDescription:%n",
        parameterListHeading = "%nParameters:%n",
        optionListHeading = "Options:%n"
)
public class ListEntries extends FileCommand implements Runnable {

    @CommandLine.Option(names = {"-d", "--dir"}, description = "list directories only")
    private boolean dirs;

    @CommandLine.Option(names = {"-l", "--perms"}, description = "list permissions")
    private boolean perm;

    @CommandLine.Option(names = {"-i", "--inode"}, description = "list inodes")
    protected boolean inode;

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
        if (dirs) {
            listDirectories(inode);
        }  else {
            listFiles(inode);
        }
    }

    protected Integer listFiles(boolean inode) {
        if (FileManager.getDirFiles(path).has(pathFile)) {
            printEntry(FileManager.getFile(pathFile, path), inode, perm);
            return null;
        }

        JSONObject files = FileManager.getDirFiles(path + fullFile);
        JSONArray entries = files.toJSONArray(files.names());

        if (entries == null) {
            throw new RuntimeException("Directory has no files");
        }

        for (int i = 0; i < entries.length(); i++) {
            printEntry(entries.getJSONObject(i), inode, perm);
        }
        return null;
    }

    protected void listDirectories(boolean inode) {
        JSONArray childDirectories = DirectoryManager.getChildDirectories(path + fullFile);
        if (childDirectories.length() < 1) {
            throw new RuntimeException("No subdirectories");
        }
        for (int i = 0; i < childDirectories.length(); i++) {
            printEntry(FileManager.getDirectory(childDirectories.getInt(i)), inode, perm);
        }
    }

     protected static void printEntry(JSONObject file, boolean inode, boolean perm) {
        System.out.printf("%-15s", file.getString("name"));
        if (inode) {
            System.out.printf("%-6d", file.getInt("inode"));
        }
        if (perm) {
            System.out.printf("%03d %-8s %-8s", file.getInt("permissions"), file.getString("owner"), file.getString("group"));
        }
        System.out.println();
    }
}
