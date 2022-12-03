package managers;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.Iterator;
import java.util.Objects;

public class FileManager {

    static FileReader reader;
    static FileWriter fileWriter;
    static File file;

    static JSONParser parser = new JSONParser();
    static JSONObject files;

    static {
        try {
            file = new File("Database/files.json");
            reader = new FileReader("Database/files.json");
            Object obj = parser.parse(reader);
            files = new JSONObject(obj.toString());
            fileWriter = new FileWriter(file, false);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    public static void makeFile(String name, String path, String owner, int permissions) throws IOException {
        makeFileRecord(name, path, owner, permissions);
        new File(path + name).createNewFile();
    }

    public static void removeFile(String name, String path) {
        JSONObject directoryFiles = getDirFiles(path);
        JSONObject file = getFile(name, path);
        new File(path + name).delete();

        if (file.has("linkIdx")) {
                String inode = String.valueOf(file.getInt("inode"));
                JSONArray jsonArray = LinkManager.links.getJSONArray(inode);

                if (jsonArray.length() < 3) {
                    LinkManager.links.remove(inode);
                    for (int i = 0; i < 2; i++) {
                        String[] args = parsePath(jsonArray.getString(i));
                        getFile(args[0], args[1] + '/').remove("linkIdx");
                    }

                } else {
                    for (int i = 0; i < jsonArray.length(); ++i) {
                        if (Objects.equals(jsonArray.getString(i), path + name)) {
                            jsonArray.remove(i);
                            break;
                        }
                    }
                }
        }
        directoryFiles.remove(name);
    }

    public static void removeFiles(String dirPath) {
        Iterator<String> iterator = new JSONObject(FileManager.getDirFiles(dirPath).toString()).keys();
        while (iterator.hasNext()) {
            removeFile(iterator.next(), dirPath);
        }
    }

    public static JSONObject getFile(String name, String path) {
        JSONObject dirFiles = getDirFiles(path);
        if (!dirFiles.has(name)) {
            throw new RuntimeException("File doesn't exist");
        }

        return dirFiles.getJSONObject(name);
    }

    public static JSONObject getDirFiles(String path) {
        if (!DirectoryManager.directories.has(path)) {
            throw new RuntimeException("Directory doesn't exist");
        }
        int inode = DirectoryManager.getInode(path);
        return files.getJSONObject(String.valueOf(inode)).getJSONObject("files");
    }

    public static String getOwner(String file, String path) {
        return getFile(file, path).getString("owner");
    }

    public static String getDirPath(String inode) {
        return files.getJSONObject(inode).getString("path");
    }

    public static JSONObject getDirectory(int inode) {
        return DirectoryManager.getDirectory(getDirPath(String.valueOf(inode)));
    }

    public static String[] parsePath(String path) {
        int index1 = path.lastIndexOf('/');
        String path1 = index1 == -1 ? "" : path.substring(0, index1);
        String file1 = path.substring(index1 + 1);
        return new String[] {file1, path1};
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

    public static JSONObject getFacl(String name, String path) {
        return getFile(name, path).getJSONObject("facl");
    }

    protected static JSONObject makeFileRecord(String name, String path, String owner, int umask) {
        JSONObject directoryFiles = getDirFiles(path);
        if (directoryFiles.has(name)) {
            throw new RuntimeException("File name taken!");
        }
        return directoryFiles.put(name, new JSONObject(fileFrom(name, owner, nextInode(), umask))).getJSONObject(name);
    }

    protected static int nextInode() {
        int inode = files.getInt("lastInode") + 1;
        files.put("lastInode", inode);
        return inode;
    }

    private static boolean isEmpty() throws IOException {
        return reader.read() == -1;
    }

    private static String fileFrom(String name, String owner, int inode, int umask) {
        return String.format("{'name': %s,'owner': '%s','group':'%s'," +
                "'permissions':%d,'facl':{},'inode': %d, 'eid': %s, 'egid': %s}", name, owner, owner, 666 - umask, inode, owner, owner);
    }

}
