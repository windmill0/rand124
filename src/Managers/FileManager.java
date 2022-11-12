package managers;

import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.Iterator;

public class FileManager {

    static FileReader reader;
    static FileWriter fileWriter;
    static File file;

    static JSONParser parser = new JSONParser();
    static JSONObject files;

    static {
        try {
            file = new File("src/Database/files.json");
            reader = new FileReader("src/Database/files.json");
            Object obj = parser.parse(reader);
            files = new JSONObject(obj.toString());
            fileWriter = new FileWriter(file, false);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    public static void makeFile(String name, String path) throws IOException {
        JSONObject directoryFiles = getDirFiles(path);
        if (directoryFiles.has(name)) {
            throw new RuntimeException("File name taken!");
        }
        new File(path + '/' + name).createNewFile();
        directoryFiles.put(name, new JSONObject(fileFrom(name)));
    }

    public static void removeFile(String name, String path) {
        JSONObject directoryFiles = getDirFiles(path);
        new File(path + '/' + name).delete();
        directoryFiles.remove(name);
    }

    public static void removeFiles(String dirPath) {
        Iterator<String> iterator = FileManager.getDirFiles(dirPath).keys();
        while (iterator.hasNext()) {
            removeFile(iterator.next(), dirPath);
        }
    }

    public static JSONObject getDirFiles(String path) {
        String dirId = DirectoryManager.getDirId(path);
        return files.getJSONObject(dirId).getJSONObject("files");
    }

    public static void saveFile() throws IOException {
        fileWriter.flush();
        if (isEmpty()) {
            fileWriter.write(files.toString());
        }
        fileWriter.close();
    }

    public static JSONObject getFiles() {
        return files;
    }

    protected static String getPath(String dirId) {
        return files.getJSONObject(dirId).getString("path");
    }

    private static boolean isEmpty() throws IOException {
        return reader.read() == -1;
    }

    private static String fileFrom(String name) {
        return String.format("{'name': %s,'owner': '','group':'','permissions':777,'facl':[],'links':[]}", name);
    }

}
