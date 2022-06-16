package domain;

import domain.exceptions.EnrollmentRulesViolationException;

import java.util.Date;

public class CourseOffering {
    private Course course;
    private int section;
    private Date examDate;

    public CourseOffering(Course course) {
        this.course = course;
        this.section = 1;
        this.examDate = null;
    }

    public CourseOffering(Course course, Date examDate) {
        this.course = course;
        this.section = 1;
        this.examDate = examDate;
    }

    public CourseOffering(Course course, Date examDate, int section) {
        this.course = course;
        this.section = section;
        this.examDate = examDate;
    }

    public void checkViolation(CourseOffering o) throws EnrollmentRulesViolationException {
        if (this == o)
            return;
        if (this.getExamTime().equals(o.getExamTime()))
            throw new EnrollmentRulesViolationException(String.format("Two offerings %s and %s have the same exam time", this, o));
        if (this.getCourse().equals(o.getCourse()))
            throw new EnrollmentRulesViolationException(String.format("%s is requested to be taken twice", this.getCourse().getName()));
    }

    public Course getCourse() {
        return course;
    }

    public String toString() {
        return course.getName() + " - " + section;
    }

    public Date getExamTime() {
        return examDate;
    }

    public int getSection() {
        return section;
    }
}
