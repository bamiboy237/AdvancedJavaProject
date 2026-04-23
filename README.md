# MiniLMS Java Swing Project

MiniLMS is a local desktop learning management system built with Java Swing. It lets a student:

- create and select a local profile
- sign in with password
- view enrolled courses
- enroll in and drop courses
- open course content
- import `.ics` calendar files
- see combined course deadlines and imported calendar events

## Application Flow

```text
+-------------------+
|   Launch App      |
|  MiniLMSApp.main  |
+---------+---------+
          |
          v
+-------------------+
|    Welcome UI     |
| Sign In / Sign Up |
+---------+---------+
          |
          v
+-------------------+
| Profile Selection |
|  or Creation UI   |
+---------+---------+
          |
          v
+-------------------+
|   Main LMS Frame  |
|  MiniLMSFrame     |
+----+----+----+----+
     |    |    | 
     |    |    +-------------------+
     |    |                        |
     |    v                        v
     |  Course Content         Calendar View
     |  Panel                  - imported .ics events
     |  - course title         - course deadlines
     |  - topics               - upcoming list
     |  - assignments
     |  - deadlines
     |
     v
+-------------------+
| Course Management |
| enroll / drop /   |
| open course       |
+-------------------+

Home panel shows enrolled courses and upcoming deadlines.
Saving happens automatically on important actions and exit.
```

## How to Run

From the `AdvancedJavaProject` directory:

```text
javac -cp .:lib/* *.java
java -cp .:lib/* MiniLMSApp
```

If your environment already has compiled `.class` files, you can run the app directly with the `java` command above.

## Project Files

### Main application
- `MiniLMSApp.java` — application entry point; launches the Swing UI.
- `MiniLMSFrame.java` — main window, navigation hub, menus, save/load coordination.

### Data/model classes
- `StudentProfile.java` — local student identity and saved record container.
- `StudentRecord.java` — enrolled courses and calendar events.
- `Course.java` — course data, assignments, and deadlines.
- `CalendarEvent.java` — one calendar/deadline item.
- `ProfileIndex.java` — list of stored profiles.
- `StudentIdGenerator.java` — creates new student IDs.

### Course and calendar logic
- `CourseCatalog.java` — built-in catalog of sample courses with deadlines.
- `ICSParser.java` — parses imported `.ics` files into calendar events.

### Persistence
- `PersistenceManager.java` — saves and loads profiles and profile index using serialization.
- `data/` — stored profile and index files.

### UI panels
- `WelcomePanel.java` — first screen with sign-in and sign-up choices.
- `ProfileSelectionPanel.java` — lets the user pick an existing profile.
- `ProfileCreationPanel.java` — creates a new local profile.
- `HomePanel.java` — dashboard showing enrolled courses and upcoming deadlines.
- `CourseManagementPanel.java` — enroll, drop, and inspect courses.
- `CourseContentPanel.java` — view selected course details, topics, assignments, and deadlines.
- `CalendarPanel.java` — calendar UI for course deadlines and imported `.ics` events.

### Shared styling
- `UITheme.java` — colors, fonts, and common Swing styling helpers.
