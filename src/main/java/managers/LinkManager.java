package managers;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class LinkManager {

    static FileReader reader;
    static FileWriter fileWriter;
    static File file;

    static JSONParser parser = new JSONParser();
    static JSONObject links;

    static {
        try {
            file = new File("Database/links.json");
            reader = new FileReader("Database/links.json");
            Object obj = parser.parse(reader);
            links = new JSONObject(obj.toString());
            fileWriter = new FileWriter(file, false);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    public static void makeLink(String path1, String file1, String newLink, String owner, int umask) throws IOException {

        int index1 = newLink.lastIndexOf('/');
        String path2 = index1 == -1 ? "" : newLink.substring(0, index1 + 1);
        String file2 = newLink.substring(index1 + 1);

        JSONObject fileData = FileManager.getFile(file1, path1);
        int inode = fileData.getInt("inode");

        Files.createLink(Paths.get(newLink), Paths.get(path1 + file1));

        if (!hasLink(inode)) {
            fileData.put("linkIdx", 0);
            links.put(String.valueOf(inode), new JSONArray().put(path1 + file1));
        }

        JSONArray array = links.getJSONArray(String.valueOf(inode)).put(newLink);
        FileManager.makeFileRecord(file2, path2, owner, umask).put("inode", inode).put("linkIdx", array.length() - 1);

    }

    public static void saveFile() throws IOException {
        fileWriter.flush();
        if (isEmpty()) {
            fileWriter.write(links.toString());
        }
        fileWriter.close();
    }

    protected static boolean hasLink(int inode) {
        return links.has((String.format("%d", inode)));
    }

    private static boolean isEmpty() throws IOException {
        return reader.read() == -1;
    }

}
