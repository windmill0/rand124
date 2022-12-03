package commands.misc;

import commands.Command;
import picocli.CommandLine;

@CommandLine.Command(
        name = "umask",
        description = "change default permissions",
        mixinStandardHelpOptions = true,
        descriptionHeading = "%nDescription:%n",
        parameterListHeading = "%nParameters:%n",
        optionListHeading = "Options:%n"
)
public class Umask extends Command implements Runnable {

    @CommandLine.Parameters(arity = "0..1", defaultValue = "-1")
    int newMask;

    @Override
    public void run() {
        if (!(777 < newMask || newMask < 0)) {
            commander.user.setUmask(newMask);
        }
        System.out.printf("%04d\n", commander.getMask());
    }
}
