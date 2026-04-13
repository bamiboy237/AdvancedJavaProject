/*
 * Guy-robert Bogning
 * Calendar event object for imported .ics deadlines and schedule items.
 */

import java.io.Serializable;
import java.time.LocalDateTime;

// import java.time.format.DateTimeFormatter;

public class CalendarEvent implements Serializable {

    private static final long serialVersionUID = 1L;
    private String title;
    private String description;
    private LocalDateTime dueDate;

    public CalendarEvent(
        String title,
        String description,
        LocalDateTime dueDateTime
    ) {
        this.title = title;
        this.description = description;
        this.dueDate = dueDateTime;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }
}
