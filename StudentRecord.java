/*
 * Guy-robert Bogning
 * Student record object for enrolled course data and related student LMS state.
 */

import java.io.Serializable;
import java.util.ArrayList;

public class StudentRecord implements Serializable {

    private static final long serialVersionUID = 1L;
    private ArrayList<Course> enrolledCourses;
    private ArrayList<CalendarEvent> calendarEvents;

    public StudentRecord() {
        this.enrolledCourses = new ArrayList<>();
        this.calendarEvents = new ArrayList<>();
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
        if (enrolledCourses.contains(course)) {
            throw new IllegalArgumentException("Course already enrolled");
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
        if (!enrolledCourses.contains(course)) {
            throw new IllegalArgumentException("Course not enrolled");
        }
        try {
            enrolledCourses.remove(course);
        } catch (Exception e) {
            throw new RuntimeException("Failed to remove course", e);
        }
    }

    public boolean isEnrolled(Course course) {
        if (course == null) {
            return false;
        }
        try {
            return enrolledCourses.contains(course);
        } catch (Exception e) {
            throw new RuntimeException("Failed to check enrollment", e);
        }
    }

    public ArrayList<CalendarEvent> getCalendarEvents() {
        try {
            return new ArrayList<>(calendarEvents);
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve calendar events", e);
        }
    }

    public void setCalendarEvents(ArrayList<CalendarEvent> events) {
        if (events == null) {
            throw new IllegalArgumentException("Events cannot be null");
        }
        try {
            this.calendarEvents.clear();
            this.calendarEvents.addAll(events);
        } catch (Exception e) {
            throw new RuntimeException("Failed to set calendar events", e);
        }
    }
}
