package commands;

import commands.file.*;
import commands.misc.*;
import picocli.CommandLine;
import roles.User;

import java.util.List;
import java.util.concurrent.Callable;


@CommandLine.Command(name="commander",
        mixinStandardHelpOptions = true,
        commandListHeading = "%nAvailable commands:%n",
        hidden = true,
        subcommands = {
                ChangeDirectory.class,
                ListEntries.class,
                RemoveFile.class,
                CreateFile.class,
                EditFile.class,
                LinkFile.class,
                MakeDirectory.class,
                ListAll.class,
                RemoveDirectory.class,
                ChangePermissions.class,
                GroupOp.class,
                Chown.class,
                Umask.class,
                UserOp.class,
                GetAcl.class,
                SetAcl.class,
                GetId.class,
                SetId.class,
                Stat.class,
                Kwrite.class,
                Sudo.class,
                WhoAmI.class,
                Find.class,
                Exit.class
        }
)
public class Commander implements Callable<Integer> {
    final Integer SUCCESS = 1;
    final Integer FAILURE = 0;

    public User user;

    public Commander(User user) {
        this.user = user;
    }

    public String getName() {
        return user.getUsername();
    }

    public int getMask() {
        return user.getUmask();
    }

    public List<String[]> getLog() {
        return user.getLog();
    }

    @Override
    public Integer call() throws Exception {
        return SUCCESS;
    }

    @CommandLine.Command(
            name = "ps",
            description = "show command history"
    )
    void printLog() {
        System.out.printf("%-15s%-40s\n", "UID", "CMD");
        getLog().forEach(log -> System.out.printf("%-15s%-40s\n", log[0], log[1]));
    }
}
