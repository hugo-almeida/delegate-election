package core;

import java.util.Set;

public class Course {
    private final String name;
    private final String acronym;
    private final String id;
    private final String type;
    private final Set<CourseYear> years;

    public Course(String name, String acronym, String id, String type, Set<CourseYear> years) {
        super();
        this.name = name;
        this.acronym = acronym;
        this.id = id;
        this.type = type;
        this.years = years;
        initCourseYears();
    }

    private void initCourseYears() {
        if (type.equals("BOLONHA_MASTER_DEGREE")) {
            for (int i = 0; i < 2; i++) {
                years.add(new CourseYear(i));
            }
        } else if (type.equals("BOLONHA_DEGREE")) {
            for (int i = 0; i < 3; i++) {
                years.add(new CourseYear(i));
            }
        } else if (type.equals("BOLONHA_INTEGRATED_MASTER_DEGREE")) {
            for (int i = 0; i < 5; i++) {
                years.add(new CourseYear(i));
            }
        }
    }

    public String getName() {
        return name;
    }

    public String getAcronym() {
        return acronym;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public Set<CourseYear> getYears() {
        return years;
    }

}
