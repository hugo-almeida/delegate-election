package core;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "Student")
public class Student {

    @Id
    private String username;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({ @JoinColumn(name = "PeriodPK_DegreeName", insertable = false, updatable = false),
            @JoinColumn(name = "PeriodPK_DegreeYear", insertable = false, updatable = false),
            @JoinColumn(name = "PeriodPK_Id", insertable = false, updatable = false),
            @JoinColumn(name = "PeriodPK_CalendarYear", insertable = false, updatable = false) })
    private Period applicationPeriod;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({ @JoinColumn(name = "DegreeDegreeYearPK_DegreeName", insertable = true, updatable = false),
            @JoinColumn(name = "DegreeYearPK_DegreeYear", insertable = true, updatable = false),
            @JoinColumn(name = "DegreeYearPK_CalendarYear", insertable = true, updatable = false) })
    private DegreeYear degreeYear;

    private String name;
    private String email;
    private boolean applied;
    private String photoType;
    @Column(length = 100000)
    private String photoBytes;

    Student() {

    }

    public Student(String name, String username, String email, String photoType, String photoBytes) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.applied = false;
        this.photoType = photoType;
        this.photoBytes = photoBytes;
    }

    public Student(String name, String username, String email, String photoType, String photoBytes, Period p, DegreeYear d) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.applied = false;
        this.photoType = photoType;
        this.photoBytes = photoBytes;
        this.applicationPeriod = p;
        this.degreeYear = d;
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

    public String getPhotoBytes() {
        return photoBytes;
    }

    public boolean hasApplied() {
        return applied;
    }

    public void apply() {
        applied = true;
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

    public void setDegreeYear(DegreeYear dy) {
        degreeYear = dy;
    }
}
