package domain;

import java.util.List;
import java.util.Map;

import domain.exceptions.EnrollmentRulesViolationException;

public class EnrollCtrl {
    public void enroll(Student s, List<CourseOffering> courseOfferings) throws EnrollmentRulesViolationException {
        StringBuilder errorMessage = new StringBuilder();
        for (CourseOffering o : courseOfferings) {
            Course offeredCourse = o.getCourse();
            if (s.isPassed(offeredCourse))
                errorMessage.append(String.format("The student has already passed %s\n", o.getCourse().getName()));

            List<Course> prerequisites = offeredCourse.getPrerequisites();
            for (Course pre : prerequisites)
                if (!s.isPassed(pre))
                    errorMessage.append(String.format("The student has not passed %s as a prerequisite of %s\n", pre.getName(), o.getCourse().getName()));

            for (CourseOffering o2 : courseOfferings) {
                if (o == o2)
                    continue;
                if (o.hasOverlapWith(o2))
                    errorMessage.append(String.format("Two offerings %s and %s have the same exam time\n", o, o2));
                if (o.hasSameCourseWith(o2))
                    errorMessage.append(String.format("%s is requested to be taken twice\n", o.getCourse().getName()));
            }
        }

        double gpa = s.GPA();
        int unitsRequested = CourseOffering.sumOfUnits(courseOfferings);
        if (unitsRequested > maxAllowedUnits(gpa))
            errorMessage.append(String.format("Number of units (%d) requested does not match GPA of %f\n", unitsRequested, gpa));

        if (errorMessage.isEmpty())
            s.takeCourses(courseOfferings);
        else
            throw new EnrollmentRulesViolationException(errorMessage.toString());
    }

    static int maxAllowedUnits(double gpa) {
        if (gpa < 12)
            return 14;
        if (gpa < 16)
            return 16;
        return 20;
    }
}