package commands.misc;

import commands.Command;
import managers.GroupManager;
import picocli.CommandLine;

@CommandLine.Command(name = "group",
        description = "add/remove user/group",
        mixinStandardHelpOptions = true,
        descriptionHeading = "%nDescription:%n",
        parameterListHeading = "%nParameters:%n",
        optionListHeading = "Options:%n"
)
public class GroupOp extends Command {

    @CommandLine.Parameters(paramLabel = "<group name>", arity = "1", description = "group to add",scope = CommandLine.ScopeType.INHERIT)
    protected String group;

    @CommandLine.Command(
            name = "-ag",
            description = "add group",
            mixinStandardHelpOptions = true
    )
    void addGroup() {
        GroupManager.addGroup(group);
        System.out.println("Group added!");
    }

    @CommandLine.Command(
            name = "-rg",
            description = "remove group"
    )
    void removeGroup() {
        if (!commander.getName().matches("root|" + group)) {
            throw new RuntimeException("Operation denied!");
        }
        GroupManager.removeGroup(group);
    }

    @CommandLine.Command(
            name = "-au",
            description = "add member to group",
            mixinStandardHelpOptions = true
    )
    void addMember(@CommandLine.Parameters(paramLabel = "<user name>", arity = "1", description = "user to add") String username) {
        if (!commander.getName().matches("root|" + group)) {
            throw new RuntimeException("Operation denied!");
        }
        GroupManager.addToGroup(username, group);
        System.out.println("Member added!");
    }

    @CommandLine.Command(
            name = "-rm",
            description = "remove member from group",
            mixinStandardHelpOptions = true
    )
    void removeMember(@CommandLine.Parameters(paramLabel = "<user name>", arity = "1", description = "user to remove") String username) {
        if (!commander.getName().matches("root|" + group)) {
            throw new RuntimeException("Operation denied!");
        }
        GroupManager.kickMember(username, group);
        System.out.println("Member kicked");
    }

}
