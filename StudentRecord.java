/*
 * Guy-robert Bogning
 * Student record object for enrolled course data and related student LMS state.
 */

import java.io.Serializable;
import java.util.ArrayList;

public class StudentRecord implements Serializable {

    private static final long serialVersionUID = 1L;
    private ArrayList<Course> enrolledCourses;

    public StudentRecord() {
        this.enrolledCourses = new ArrayList<>();
    }

    public ArrayList<Course> getEnrolledCourses() {
        try {
            return new ArrayList<>(enrolledCourses);
        } catch (Exception e) {
            throw new RuntimeException(
                "Failed to retrieve enrolled courses",
                e
            );
        }
    }

    public void addCourse(Course course) {
        if (course == null) {
            throw new IllegalArgumentException("Course cannot be null");
        }
        try {
            enrolledCourses.add(course);
        } catch (Exception e) {
            throw new RuntimeException("Failed to add course", e);
        }
    }

    public void removeCourse(Course course) {
        if (course == null) {
            throw new IllegalArgumentException("Course cannot be null");
        }
        try {
            enrolledCourses.remove(course);
        } catch (Exception e) {
            throw new RuntimeException("Failed to remove course", e);
        }
    }
}
