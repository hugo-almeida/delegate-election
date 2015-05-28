package core;

import java.util.Date;

public abstract class Period {

    private Date start;
    private Date end;

    public Period(Date start, Date end) {
        if (end.before(start)) {
            //Throws a nice exception saying that nothing ends before starting
        }
        this.start = start;
        this.end = end;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public boolean conflictsWith(Period p) {
        if (end.before(p.getStart()) || start.after(p.getEnd())) {
            return false;
        }
        return true;
    }
}
