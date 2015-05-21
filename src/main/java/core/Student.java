package core;

public class Student {
    private final String name;
    private final String username;
    private final String email;
    private boolean applied;

    private final byte[] photo;

    public Student(String name, String username, String email, byte[] photo) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.applied = false;
        this.photo = photo;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public boolean hasApplied() {
        return applied;
    }

    public void apply() {
        applied = true;
    }
}
