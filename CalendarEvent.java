/*
 * Guy-robert Bogning
 * Calendar event object for imported .ics deadlines and schedule items.
 */

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

public class CalendarEvent implements Serializable, Comparable<CalendarEvent> {

    private static final long serialVersionUID = 1L;

    private final String title;
    private final String description;
    private final String location;
    private final LocalDateTime startDateTime;
    private final LocalDateTime dueDateTime;

    public CalendarEvent(
        String title,
        String description,
        String location,
        LocalDateTime startDateTime,
        LocalDateTime dueDateTime
    ) {
        this.title = normalize(title, "Untitled Event");
        this.description = normalize(description, "");
        this.location = normalize(location, "");
        this.startDateTime = startDateTime;
        this.dueDateTime = dueDateTime;
    }

    private static String normalize(String value, String defaultValue) {
        return (value == null || value.isBlank()) ? defaultValue : value;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getSortDateTime() {
        if (dueDateTime != null) {
            return dueDateTime;
        }
        return startDateTime;
    }

    @Override
    public int compareTo(CalendarEvent other) {
        if (other == null) {
            return -1;
        }

        LocalDateTime thisDate = getSortDateTime();
        LocalDateTime otherDate = other.getSortDateTime();

        if (thisDate == null && otherDate == null) {
            return title.compareToIgnoreCase(other.title);
        }
        if (thisDate == null) {
            return 1;
        }
        if (otherDate == null) {
            return -1;
        }

        int dateCompare = thisDate.compareTo(otherDate);
        if (dateCompare != 0) {
            return dateCompare;
        }
        return title.compareToIgnoreCase(other.title);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(title);
        LocalDateTime sortDateTime = getSortDateTime();
        if (sortDateTime != null) {
            builder.append(" - ").append(sortDateTime);
        }
        if (!location.isBlank()) {
            builder.append(" @ ").append(location);
        }
        return builder.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CalendarEvent)) {
            return false;
        }
        CalendarEvent that = (CalendarEvent) o;
        return (
            Objects.equals(title, that.title) &&
            Objects.equals(description, that.description) &&
            Objects.equals(location, that.location) &&
            Objects.equals(startDateTime, that.startDateTime) &&
            Objects.equals(dueDateTime, that.dueDateTime)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            title,
            description,
            location,
            startDateTime,
            dueDateTime
        );
    }
}
