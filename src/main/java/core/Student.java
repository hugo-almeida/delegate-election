package core;

public class Student {
    private final String name;
    private final int number;
    private final String email;
    private boolean applied;

    //private whatever photo;

    public Student(String name, int number, String email /*whatever photo*/) {
        this.name = name;
        this.number = number;
        this.email = email;
        this.applied = false;
        //this.photo = photo;
    }

    public String getName() {
        return name;
    }

    public int getNumber() {
        return number;
    }

    public String getEmail() {
        return email;
    }

    public boolean hasApplied() {
        return applied;
    }

    public void apply() {
        applied = true;
    }
}
