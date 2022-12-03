package commands.file;

import commands.FileCommand;
import managers.DirectoryManager;
import managers.FileManager;
import managers.GroupManager;
import managers.UserManager;
import org.json.JSONObject;
import picocli.CommandLine;

@CommandLine.Command(
        name = "setfacl",
        description = "set file/directory facl",
        mixinStandardHelpOptions = true,
        descriptionHeading = "%nDescription:%n",
        parameterListHeading = "%nParameters:%n",
        optionListHeading = "Options:%n"
)
public class SetAcl extends FileCommand {

    String target;
    JSONObject facl;
    int permission;

    @CommandLine.Parameters(arity = "1", description = "type:name:permissions  e.g. u:someuser:4", scope = CommandLine.ScopeType.INHERIT)
    void setPermission(String input) {
        String[] arg = input.split(":");
        if (!isOwner(commander.user, pathFile, path, isFile)) {
            throw new RuntimeException("Permission denied!");
        }

        if (arg.length != 3 || !arg[0].matches("u|g|o")) {
            throw new RuntimeException("Incorrect format <type:name:permission>");
        }

        permission = Integer.parseInt(arg[2]);
        if (0 > permission || permission > 7) {
            throw new RuntimeException("Invalid facl!");
        }
        facl = isFile ? FileManager.getFacl(pathFile, path) : DirectoryManager.getFacl(path + fullFile);

        switch (arg[0]) {
            case "u" -> {
                if (!UserManager.hasUser(arg[1])) {
                    throw new RuntimeException("User doesn't exist");
                }
            }
            case "g" -> {
                if (!GroupManager.hasGroup(arg[1])) {
                    throw new RuntimeException("Group doesn't exist");
                }
            }
        }
        target = arg[1];
    }

    @CommandLine.Command(
            name = "-m",
            description = "modify facl"
    )
    void setFacl() {
        facl.put(target, permission);
        System.out.println("Permission set");
    }

    @CommandLine.Command(
            name = "-x",
            description = "remove facl"
    )
    void removeFacl() {
        int newPerm = facl.getInt(target) & ~permission;
        if (newPerm > 0)
            facl.put(target, newPerm);
        else
            facl.remove(target);
        System.out.println("Permission removed");
    }
}
