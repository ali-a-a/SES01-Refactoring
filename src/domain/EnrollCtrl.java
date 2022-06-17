package domain;

import java.util.List;
import java.util.Map;

import domain.exceptions.EnrollmentRulesViolationException;

public class EnrollCtrl {
    public void enroll(Student s, List<CourseOffering> courseOfferings) throws EnrollmentRulesViolationException {
        Map<Term, Map<Course, Double>> transcript = s.getTranscript();
        for (CourseOffering o : courseOfferings) {
            Course offeredCourse = o.getCourse();
            if(s.isPassed(offeredCourse))
                throw new EnrollmentRulesViolationException(String.format("The student has already passed %s", o.getCourse().getName()));

            List<Course> prerequisites = offeredCourse.getPrerequisites();
            for (Course pre : prerequisites)
                if(!s.isPassed(pre))
                    throw new EnrollmentRulesViolationException(String.format("The student has not passed %s as a prerequisite of %s", pre.getName(), o.getCourse().getName()));

            for (CourseOffering o2 : courseOfferings) {
                if (o == o2)
                    continue;
                if (o.hasOverlapWith(o2))
                    throw new EnrollmentRulesViolationException(String.format("Two offerings %s and %s have the same exam time", o, o2));
                if (o.hasSameCourseWith(o2))
                    throw new EnrollmentRulesViolationException(String.format("%s is requested to be taken twice", o.getCourse().getName()));
            }
        }

        int unitsRequested = CourseOffering.sumOfUnits(courseOfferings);

        double points = 0;
        int totalUnits = 0;
        for (Map.Entry<Term, Map<Course, Double>> tr : transcript.entrySet()) {
            for (Map.Entry<Course, Double> r : tr.getValue().entrySet()) {
                points += r.getValue() * r.getKey().getUnits();
                totalUnits += r.getKey().getUnits();
            }
        }

        double gpa = points / totalUnits;
        if ((gpa < 12 && unitsRequested > 14) ||
                (gpa < 16 && unitsRequested > 16) ||
                (unitsRequested > 20))
            throw new EnrollmentRulesViolationException(String.format("Number of units (%d) requested does not match GPA of %f", unitsRequested, gpa));

        for (CourseOffering o : courseOfferings)
            s.takeCourse(o.getCourse(), o.getSection());
    }
}
