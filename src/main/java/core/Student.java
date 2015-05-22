package core;


public class Student {
    private final String name;
    private final String username;
    private final String email;
    private boolean applied;
    private final String photoType;
    private final byte[] photoBytes;

    public Student(String name, String username, String email, String photoType, byte[] photoBytes) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.applied = false;
        this.photoType = photoType;
        this.photoBytes = photoBytes;
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

    public String getPhotoType() {
        return photoType;
    }

    public byte[] getPhotoBytes() {
        return photoBytes;
    }

    public boolean hasApplied() {
        return applied;
    }

    public void apply() {
        applied = true;
    }
}
