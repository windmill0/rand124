package Roles;

public class User {
    private final int userId;
    private String userName;
    private Group[] groups; // Questionable
    // Consider adding Sudoer flag

    public User(int id, String name) {
        this.userId = id;
        this.userName = name;
    }

    int getUserId() {
        return this.userId;
    }

    String getUserName() {
        return this.userName;
    }

    public void setUserName(String name) {
        this.userName = name;
    }
}
