/*
 * Guy-robert Bogning
 * Course object for LMS catalog and enrollment data.
 */

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class Course implements Serializable {

    private static final long serialVersionUID = 1L;

    private String courseId;
    private String title;
    private String description;
    private String[] topics;
    private String[] assignments;
    private ArrayList<CalendarEvent> deadlines;
    private String instructor;

    public Course(
        String courseId,
        String title,
        String description,
        String[] topics,
        String[] assignments,
        String instructor
    ) {
        this(
            courseId,
            title,
            description,
            topics,
            assignments,
            new ArrayList<>(),
            instructor
        );
    }

    public Course(
        String courseId,
        String title,
        String description,
        String[] topics,
        String[] assignments,
        ArrayList<CalendarEvent> deadlines,
        String instructor
    ) {
        this.courseId = courseId;
        this.title = title;
        this.description = description;
        this.topics = topics == null ? new String[0] : topics.clone();
        this.assignments =
            assignments == null ? new String[0] : assignments.clone();
        this.deadlines =
            deadlines == null ? new ArrayList<>() : new ArrayList<>(deadlines);
        this.instructor = instructor;
    }

    public String getCourseId() {
        return courseId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String[] getTopics() {
        return topics.clone();
    }

    public String[] getAssignments() {
        return assignments.clone();
    }

    public ArrayList<CalendarEvent> getDeadlines() {
        if (deadlines == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(deadlines);
    }

    public String getInstructor() {
        return instructor;
    }

    @Override
    public String toString() {
        return courseId + " - " + title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Course)) {
            return false;
        }
        Course course = (Course) o;
        return Objects.equals(courseId, course.courseId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(courseId);
    }

    public String getTopicsAsText() {
        return Arrays.toString(topics);
    }

    public String getAssignmentsAsText() {
        return Arrays.toString(assignments);
    }
}
