package core;

import java.util.Set;

public class Degree {
    private final String name;
    private final String acronym;
    private final String id;
    private final String type;
    private final Set<DegreeYear> years;

    public Degree(String name, String acronym, String id, String type, Set<DegreeYear> years) {
        super();
        this.name = name;
        this.acronym = acronym;
        this.id = id;
        this.type = type;
        this.years = years;
        initDegreeYears();
    }

    private void initDegreeYears() {
        if (type.equals("BOLONHA_MASTER_DEGREE")) {
            for (int i = 0; i < 2; i++) {
                years.add(new DegreeYear(i));
            }
        } else if (type.equals("BOLONHA_DEGREE")) {
            for (int i = 0; i < 3; i++) {
                years.add(new DegreeYear(i));
            }
        } else if (type.equals("BOLONHA_INTEGRATED_MASTER_DEGREE")) {
            for (int i = 0; i < 5; i++) {
                years.add(new DegreeYear(i));
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

    public Set<DegreeYear> getYears() {
        return years;
    }

}
