package managers;

import org.json.JSONArray;
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
            file = new File("Database/directories.json");
            reader = new FileReader("Database/directories.json");
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

    public static void makeDirectory(String name, String path, String owner, int umask) {

        String dirName = path + name + "/";
        if (directories.has(dirName)) {
            throw new RuntimeException("Directory already exists!");
        }

        // Build directory record - Need to add permissions here
        JSONObject parentDirectory = getDirectory(path);
        int nextInode = FileManager.nextInode();

        // Create directory and push record
        new File(path + name).mkdirs();
        directories.put(dirName, new JSONObject(directoryFrom(name, owner, getInode(path), nextInode, umask)));
        parentDirectory.getJSONArray("childDirs").put(nextInode);
        FileManager.files.put(String.valueOf(nextInode), new JSONObject(String.format("{'path': \"%s\", 'files': {}}", dirName)));
    }

    public static void removeDirectory(String name, String path) {
        String dirPath = path + name + "/";

        // Check if directory exists in path
        if (!directories.has(dirPath)) {
            throw new RuntimeException("Directory doesn't exist!");
        }

        JSONArray children = getChildDirectories(dirPath);
        JSONArray siblings = directories.getJSONObject(path).getJSONArray("childDirs");
        int inode = getInode(dirPath);

        for (int i = 0; i < siblings.length(); i++) {
            if (siblings.getInt(i) == inode) {
                siblings.remove(i);
                break;
            }
        }

        for (int i = 0; i < children.length(); i++) {
            deleteDirectory(FileManager.getDirPath(String.valueOf(children.get(i))));
            children.remove(i);
        }
        deleteDirectory(dirPath);
    }

    public static JSONObject getDirectory(String path) {
        if (!directories.has(path)) {
            throw new RuntimeException("Directory doesn't exist");
        }
        return directories.getJSONObject(path);
    }

    public static String getOwner(String path) {
        return getDirectory(path).getString("owner");
    }

    public static JSONArray getChildDirectories(String directory) {
        return getDirectory(directory).getJSONArray("childDirs");
    }

    public static JSONObject getFacl(String path) {
        return getDirectory(path).getJSONObject("facl");
    }

    private static boolean isEmpty() throws IOException {
        return reader.read() == -1;
    }

    private static void deleteDirectory(String dirPath) {
        // Loop over directories recursively
        JSONArray children = getChildDirectories(dirPath);
        for (int i = 0; i < children.length(); i++) {
            deleteDirectory(FileManager.getDirPath(String.valueOf(children.get(i))));
        }
        FileManager.removeFiles(dirPath);
        System.out.printf("Removing %s\n", dirPath);

        // Delete the directory and its record
        FileManager.files.remove(String.valueOf(getInode(dirPath)));
        directories.remove(dirPath);
        new File(dirPath).delete();
    }

    protected static int getInode(String path) {
        return directories.getJSONObject(path).getInt("inode");
    }

    private static String directoryFrom(String name, String owner, int parent, int inode, int umask) {
        return String.format("{\"name\":\"%s\",\"parent\":%d," +
                " \"owner\": \"%s\", \"group\": \"%s\"," +
                " \"permissions\": %d, \"inode\": %d, \"facl\": {}," +
                " \"childDirs\": [], \"eid\": %s, \"egid\": %s}", name, parent, owner, owner, 666 - umask, inode, owner, owner);
    }

}
