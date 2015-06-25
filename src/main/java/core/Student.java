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
@Table(name = "student")
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
    @JoinColumns({
            @JoinColumn(name = "degree_name", referencedColumnName = "degree_year_pk_degree_name", insertable = true,
                    updatable = false),
            @JoinColumn(name = "degree_year", referencedColumnName = "degree_year_pk_degree_year", insertable = true,
                    updatable = false),
            @JoinColumn(name = "calendar_year", referencedColumnName = "degree_year_pk_calendar_year", insertable = true,
                    updatable = false) })
    private DegreeYear degreeYear;

    private String name;
    private String email;
    private boolean applied;
    private boolean voted;
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

    public DegreeYear getDegreeYear() {
        return degreeYear;
    }

    public void setDegreeYear(DegreeYear degreeYear) {
        this.degreeYear = degreeYear;
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

    public boolean hasVoted() {
        return voted;
    }

    public void vote() {
        voted = true;
    }
}
