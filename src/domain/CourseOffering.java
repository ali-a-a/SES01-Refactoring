package domain;

import domain.exceptions.EnrollmentRulesViolationException;

import java.util.Date;
import java.util.List;

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

    public boolean hasOverlapWith(CourseOffering o) {
        return this.getExamTime().equals(o.getExamTime());
    }

    public boolean hasSameCourseWith(CourseOffering o) {
        return this.getCourse().equals(o.getCourse());
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

    static int sumOfUnits(List<CourseOffering> courseOfferings) {
        int units = 0;
        for (CourseOffering o : courseOfferings)
            units += o.getCourse().getUnits();
        return units;
    }
}
