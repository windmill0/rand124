package commands.file;

import commands.FileCommand;
import managers.DirectoryManager;
import managers.FileManager;
import org.json.JSONArray;
import org.json.JSONObject;
import picocli.CommandLine;

@CommandLine.Command(
        name = "getfacl",
        description = "get file/directory facl",
        mixinStandardHelpOptions = true,
        descriptionHeading = "%nDescription:%n",
        parameterListHeading = "%nParameters:%n",
        optionListHeading = "Options:%n"
)
public class GetAcl extends FileCommand implements Runnable{

    @Override
    public void run() {
        JSONObject facl = isFile ? FileManager.getFacl(pathFile, path) : DirectoryManager.getFacl(path + fullFile);
        JSONArray iter = facl.names();
        if (iter == null) {
            throw new RuntimeException("Target has no facl!");
        }
        for (int i = 0; i < iter.length(); i++) {
            System.out.printf("%s %03d\n", iter.getString(i), facl.getInt(iter.getString(i)));
        }
    }

}
