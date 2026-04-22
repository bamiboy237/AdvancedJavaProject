/*
 * Guy-robert Bogning
 * Student record object for enrolled course data and related student LMS state.
 */

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

public class StudentRecord implements Serializable {

    private static final long serialVersionUID = 1L;
    private ArrayList<Course> enrolledCourses;
    private ArrayList<CalendarEvent> calendarEvents;

    public StudentRecord() {
        this.enrolledCourses = new ArrayList<>();
        this.calendarEvents = new ArrayList<>();
    }

    private ArrayList<CalendarEvent> collectCourseDeadlines() {
        ArrayList<CalendarEvent> deadlines = new ArrayList<>();
        try {
            for (Course course : enrolledCourses) {
                if (course == null) {
                    continue;
                }
                deadlines.addAll(course.getDeadlines());
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to collect course deadlines", e);
        }
        Collections.sort(deadlines);
        return deadlines;
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

    public void syncCoursesFromCatalog(CourseCatalog courseCatalog) {
        if (courseCatalog == null) {
            throw new IllegalArgumentException("Course catalog cannot be null");
        }
        try {
            ArrayList<Course> syncedCourses = new ArrayList<>();
            for (Course enrolledCourse : enrolledCourses) {
                if (enrolledCourse == null) {
                    continue;
                }
                Course catalogCourse = courseCatalog.getCourseById(
                    enrolledCourse.getCourseId()
                );
                syncedCourses.add(
                    catalogCourse == null ? enrolledCourse : catalogCourse
                );
            }
            enrolledCourses.clear();
            enrolledCourses.addAll(syncedCourses);
        } catch (Exception e) {
            throw new RuntimeException("Failed to sync enrolled courses", e);
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

    public ArrayList<CalendarEvent> getImportedCalendarEvents() {
        try {
            return new ArrayList<>(calendarEvents);
        } catch (Exception e) {
            throw new RuntimeException(
                "Failed to retrieve imported calendar events",
                e
            );
        }
    }

    public ArrayList<CalendarEvent> getCalendarEvents() {
        try {
            ArrayList<CalendarEvent> events = getImportedCalendarEvents();
            events.addAll(collectCourseDeadlines());
            Collections.sort(events);
            return events;
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
