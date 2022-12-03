package commands.file;

import commands.FileCommand;
import managers.FileManager;
import org.json.JSONArray;
import org.json.JSONObject;
import picocli.CommandLine;

@CommandLine.Command(
        name = "find",
        description = "find files with permissions",
        mixinStandardHelpOptions = true
)
public class Find extends FileCommand implements Runnable{

    @CommandLine.Option(names = {"perms"}, arity = "0..1", defaultValue = "-1")
    int permissions;

    @CommandLine.Option(names = {"id"}, arity = "0..1", defaultValue = "/")
    String id;

    @Override
    public void run() {
        JSONObject files = FileManager.getDirFiles(path + fullFile);
        JSONArray names = files.names();

        for (int i = 0; i < names.length(); i++) {
            String name = names.getString(i);
            if (permissions != -1 && !(files.getJSONObject(name).getInt("permissions") == permissions)) {
                continue;
            }

            if (!id.matches("/") && !(files.getJSONObject(name).getString("eid").matches(id))) {
                continue;
            }
            System.out.println(name);
        }
    }
}
