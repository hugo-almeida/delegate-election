package core;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "student")
public class Student {

    @Transient
    private String username;

    @EmbeddedId
    private StudentPK studentpk = null;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "student_period", joinColumns = { @JoinColumn(name = "username", referencedColumnName = "username"),
            @JoinColumn(name = "degree_name", referencedColumnName = "degree_name"),
            @JoinColumn(name = "degree_year", referencedColumnName = "degree_year"),
            @JoinColumn(name = "calendar_year", referencedColumnName = "calendar_year") }, inverseJoinColumns = { @JoinColumn(
            name = "period_id", referencedColumnName = "period_id") })
    private Set<Period> applicationPeriod;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumns({
//            @JoinColumn(name = "degree_name", referencedColumnName = "degree_year_pk_degree_name", insertable = true,
//                    updatable = false),
//            @JoinColumn(name = "degree_year", referencedColumnName = "degree_year_pk_degree_year", insertable = true,
//                    updatable = false),
//            @JoinColumn(name = "calendar_year", referencedColumnName = "degree_year_pk_calendar_year", insertable = true,
//                    updatable = false) })
//    private DegreeYear degreeYear;

    private String name;
    private String email;

    private String photoType = null;
    @Column(length = 100000)
    private String photoBytes = null;

    Student() {

    }

    public Student(String name, String username, String email, String photoType, String photoBytes) {
        this.name = name;
        this.username = username;
        this.studentpk = new StudentPK(username);
        this.email = email;
        this.photoType = photoType;
        this.photoBytes = photoBytes;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return studentpk.getUsername();
    }

    public String getEmail() {
        return email;
    }

    public String getPhotoType() {
        return photoType;
    }

    public String getPhotoBytes() {
        return photoBytes;
    }

    public DegreeYear getDegreeYear() {
        return studentpk.getDegreeYear();
    }

    public void setDegreeYear(DegreeYear degreeYear) {
        if (studentpk == null) {
            this.studentpk = new StudentPK(username);
        }
        studentpk.setDegreeYear(degreeYear);
    }

    public void addPeriod(Period p) {
        this.applicationPeriod.add(p);
    }

    public void removePeriod(Period p) {
        if (this.applicationPeriod.contains(p)) {
            this.applicationPeriod.remove(p);
        }
    }

    public void setEmail(String e) {
        email = e;
    }

    public void setPhotoType(String type) {
        photoType = type;
    }

    public void setPhotoBytes(String bytes) {
        photoBytes = bytes;
    }

}
