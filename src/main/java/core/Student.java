package core;

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
    @JoinColumns({ @JoinColumn(name = "DegreeDegreeYearPK_DegreeName", insertable = false, updatable = false),
            @JoinColumn(name = "DegreeYearPK_DegreeYear", insertable = false, updatable = false),
            @JoinColumn(name = "DegreeYearPK_CalendarYear", insertable = false, updatable = false) })
    private DegreeYear degreeYear;

    private String name;
    private String email;
    private boolean applied;
    private String photoType;
    private byte[] photoBytes;

    Student() {

    }

    public Student(String name, String username, String email, String photoType, byte[] photoBytes, Period p, DegreeYear d) {
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
