package managers;

import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;

public class DirectoryManager {

    static FileReader reader;
    static FileWriter fileWriter;
    static File file;

    static JSONParser parser = new JSONParser();
    static JSONObject directories;

    static {
        try {
            file = new File("src/Database/directories.json");
            reader = new FileReader("src/Database/directories.json");
            Object obj = parser.parse(reader);
            directories = new JSONObject(obj.toString());
            fileWriter = new FileWriter(file, false);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    public static void saveFile() throws IOException {
        fileWriter.flush();
        if (isEmpty()) {
            fileWriter.write(directories.toString());
        }
        fileWriter.close();
    }

    public static void makeDirectory(String name, String path) {

        String dirName = path + "/" + name;
        if (directories.has(dirName)) {
            throw new RuntimeException("Directory already exists!");
        }

        // Build directory record - Need to add permissions here
        JSONObject parentDirectory = directories.getJSONObject(path);
        int parentCnt = parentDirectory.getInt("childCnt");
        String parentId = parentDirectory.getString("dirId");
        parentDirectory.put("childCnt", parentCnt + 1);

        // Create directory and push record
        new File(path + '/' + name).mkdirs();
        directories.put(dirName, new JSONObject(directoryFrom(parentId, parentCnt + 1, name)));
        FileManager.files.put(getDirId(dirName), new JSONObject(String.format("{'path': \"%s\", 'files': {}}", dirName)));
    }

    public static void removeDirectory(String name, String path) {

        JSONObject files = FileManager.getFiles();
        String dirPath = path + "/" + name;

        // Check if directory exists in path
        if (!directories.has(dirPath)) {
            throw new RuntimeException("Directory doesn't exist!");
        }
        String parentDirId = getDirId(path);
        String[] siblings = getChildDirectories(path);

        // Initiate recursive directory deletion
        directories.getJSONObject(path).put("childCnt", siblings.length - 1);
        int id = getId(dirPath);
        deleteDirectory(dirPath);

        for (int i = id + 1; i <= siblings.length; i++) {

            // Temporarily store directory data and create new id
            JSONObject temp = files.getJSONObject(getChildDirId(parentDirId, i));
            String newDirId = getChildDirId(parentDirId, i - 1);

            // Shift directories once (e.g delete"1;3;1" so 1;3;2 -> 1;3;1 etc)
            directories.getJSONObject(temp.getString("path")).put("dirId", newDirId).put("id", i - 1);
            files.put(newDirId, temp);
        }

        // Remove duplicate directory
        files.remove(getChildDirId(parentDirId, siblings.length));
    }

    public static void removeDirectories(String path) {
        for (String directory: getChildDirectories(path)) {
            removeDirectory(getName(directory), path);
        }
    }

    protected static String getDirId(String path) {
        return directories.getJSONObject(path).getString("dirId");
    }

    private static boolean isEmpty() throws IOException {
        return reader.read() == -1;
    }

    private static String getName(String directory) {
        return directories.getJSONObject(directory).getString("name");
    }

    private static int getId(String path) {
        return directories.getJSONObject(path).getInt("id");
    }

    private static String getChildDirId(String parentDirId, int num) {
        return String.format("%s;%d", parentDirId, num);
    }

    private static int getChildCnt(String path) {
        return directories.getJSONObject(path).getInt("childCnt");
    }

    private static String[] getChildDirectories(String directory) {
        String[] childDirs = new String[getChildCnt(directory)];
        for (int i = 0; i < childDirs.length; i++) {
            childDirs[i] = FileManager.getPath(String.format("%s;%d", getDirId(directory), i + 1));
        }
        return childDirs;
    }

    private static void deleteDirectory(String dirPath) {
        FileManager.removeFiles(dirPath);

        // Loop over directories recursively
        for (String child: getChildDirectories(dirPath)) {
            deleteDirectory(child);
        }
        System.out.printf("Removing %s\n", dirPath);

        // Delete the directory and its record
        directories.remove(dirPath);
        new File(dirPath).delete();
    }

    private static String directoryFrom(String parentId, int id, String name) {
        return String.format("{\"name\":%s,\"id\": %d,\"childCnt\":0, \"dirId\": \"%s\"}", name, id, parentId + ";" + id);
    }

}
