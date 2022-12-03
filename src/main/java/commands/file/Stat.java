package commands.file;

import commands.FileCommand;
import managers.DirectoryManager;
import managers.FileManager;
import org.json.JSONObject;
import picocli.CommandLine;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;

@CommandLine.Command(
        name = "stat",
        description = "display file/directory information",
        mixinStandardHelpOptions = true,
        descriptionHeading = "%nDescription:%n",
        parameterListHeading = "%nParameters:%n",
        optionListHeading = "Options:%n"
)
public class Stat extends FileCommand implements Runnable {
    @Override
    public void run() {
        JSONObject target = isFile ? FileManager.getFile(pathFile, path) : DirectoryManager.getDirectory(path + fullFile);
        ListEntries.printEntry(target, true, true);
        BasicFileAttributes attr = null;
        try {
            attr = Files.readAttributes(Paths.get(path + pathFile), BasicFileAttributes.class);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        assert attr != null;
        System.out.println("Created on  = " + attr.creationTime());
        System.out.println("last Accessed = " + attr.lastAccessTime());
        System.out.println("last Modified = " + attr.lastModifiedTime());
        System.out.println("size = " + attr.size());
    }
}
