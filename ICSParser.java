/*
 * Guy-robert Bogning
 * ICS parser for reading .ics calendar files and extracting upcoming events.
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.time.temporal.Temporal;
import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Component;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.Description;
import net.fortuna.ical4j.model.property.Location;
import net.fortuna.ical4j.model.property.Summary;

public class ICSParser {

    public ArrayList<CalendarEvent> parse(File file) throws IOException {
        if (file == null || !file.exists()) {
            throw new IOException("File does not exist");
        }

        ArrayList<CalendarEvent> events = new ArrayList<>();
        CalendarBuilder builder = new CalendarBuilder();

        try (FileInputStream inputStream = new FileInputStream(file)) {
            Calendar calendar = builder.build(inputStream);
            List<VEvent> vevents = calendar.getComponents(Component.VEVENT);

            for (VEvent vevent : vevents) {
                CalendarEvent event = toCalendarEvent(vevent);
                if (event != null) {
                    events.add(event);
                }
            }
        } catch (Exception ex) {
            throw new IOException("Failed to parse ICS file", ex);
        }

        Collections.sort(events);
        return events;
    }

    private CalendarEvent toCalendarEvent(VEvent vevent) {
        String title = readSummary(vevent);
        String description = readDescription(vevent);
        String location = readLocation(vevent);
        LocalDateTime startDateTime = readDateTime(vevent.getDateTimeStart());
        LocalDateTime dueDateTime = readDateTime(vevent.getDateTimeEnd());

        if (dueDateTime == null) {
            dueDateTime = readDateTime(vevent.getDateTimeDue());
        }

        if (dueDateTime == null) {
            dueDateTime = startDateTime;
        }

        return new CalendarEvent(title, description, location, startDateTime, dueDateTime);
    }

    private String readSummary(VEvent vevent) {
        Summary summary = vevent.getSummary();
        return summary == null ? "Untitled Event" : summary.getValue();
    }

    private String readDescription(VEvent vevent) {
        Description description = vevent.getDescription();
        return description == null ? "" : description.getValue();
    }

    private String readLocation(VEvent vevent) {
        Location location = vevent.getLocation();
        return location == null ? "" : location.getValue();
    }

    private LocalDateTime readDateTime(net.fortuna.ical4j.model.property.DateProperty<?> property) {
        if (property == null || property.getDate() == null) {
            return null;
        }
        return toLocalDateTime(property.getDate());
    }

    private LocalDateTime toLocalDateTime(Temporal temporal) {
        if (temporal instanceof LocalDateTime localDateTime) {
            return localDateTime;
        }
        if (temporal instanceof LocalDate localDate) {
            return localDate.atStartOfDay();
        }
        if (temporal instanceof java.time.OffsetDateTime offsetDateTime) {
            return offsetDateTime.toLocalDateTime();
        }
        if (temporal instanceof ZonedDateTime zonedDateTime) {
            return zonedDateTime.toLocalDateTime();
        }
        if (temporal instanceof java.time.Instant instant) {
            return ZonedDateTime.ofInstant(instant, ZoneId.systemDefault()).toLocalDateTime();
        }
        return LocalDateTime.ofInstant(java.time.Instant.from(temporal), ZoneId.systemDefault());
    }
}
