package core;

public class Student {
    private final String name;
    private final int number;
    private final String email;

    //private whatever photo;

    public Student(String name, int number, String email /*whatever photo*/) {
        this.name = name;
        this.number = number;
        this.email = email;
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
}
