/*
 * Guy-robert Bogning
 * Course catalog for built-in LMS classes available for enrollment.
 */

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CourseCatalog implements Serializable {

    private static final long serialVersionUID = 1L;

    private final List<Course> courses;

    public CourseCatalog() {
        courses = new ArrayList<>();
    }

    public void addCourse(Course course) {
        if (course != null) {
            courses.add(course);
        }
    }

    public List<Course> getCourses() {
        return Collections.unmodifiableList(courses);
    }
}
