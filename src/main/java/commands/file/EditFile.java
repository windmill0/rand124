package commands.file;

import commands.FileCommand;
import picocli.CommandLine;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;
import java.util.Scanner;
import java.util.concurrent.Callable;

@CommandLine.Command(name = "cat",
        description = "read/write files",
        mixinStandardHelpOptions = true,
        descriptionHeading = "%nDescription:%n",
        parameterListHeading = "%nParameters:%n",
        optionListHeading = "Options:%n",
        commandListHeading = "Subcommands:%n"
)
public class EditFile extends FileCommand implements Callable<Integer> {

    @Override
    public Integer call() throws Exception {
        Scanner input = new Scanner(new File(path + pathFile));

        if (!isFilePermitted(commander.user, pathFile, path, isAbs,4)) {
            throw new RuntimeException("Permission denied!");
        }

        System.out.println("File content:");
        while (input.hasNextLine()) {
            System.out.println(input.nextLine());
        }
        return null;
    }

    @CommandLine.Command(name = ">", description = "overwrite file")
    private void overWrite() throws IOException {
        if (!isFilePermitted(commander.user, pathFile, path, isAbs,6)) {
            throw new RuntimeException("Permission denied!");
        }

        FileWriter writer = new FileWriter(path + pathFile);
        Scanner output = new Scanner(System.in);

        while(output.hasNextLine()) {
            String line = output.nextLine();
            if (Objects.equals(line, ":q")) {
                break;
            }
            writer.write(line + '\n');
        }
        System.out.println("File overwritten!");
        writer.close();
    }

}
