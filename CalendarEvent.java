/*
 * Guy-robert Bogning
 * Calendar event object for imported .ics deadlines and schedule items.
 */

import java.io.Serializable;
import java.time.LocalDateTime;

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
        this.title =
            title == null || title.isBlank() ? "Untitled Event" : title;
        this.description = description == null ? "" : description;
        this.location = location == null ? "" : location;
        this.startDateTime = startDateTime;
        this.dueDateTime = dueDateTime;
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
}
