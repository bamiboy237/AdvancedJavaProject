# MiniLMS

MiniLMS is a local desktop learning management system built with Java Swing.

## What it does

- Create and sign in with local profiles
- Enroll in and drop courses
- View course content
- Track deadlines from built-in courses and imported `.ics` files
- See upcoming items in a calendar view

## Project structure

Key files:

- `MiniLMSApp.java` — application entry point
- `MiniLMSFrame.java` — main window and screen navigation
- `WelcomePanel.java`, `ProfileSelectionPanel.java`, `ProfileCreationPanel.java` — profile flow
- `HomePanel.java` — dashboard
- `CourseManagementPanel.java`, `CourseContentPanel.java` — course screens
- `CalendarPanel.java` — calendar and upcoming items
- `PersistenceManager.java` — local profile storage in `data/`
- `ICSParser.java` — imports `.ics` calendar files
- `UITheme.java` — shared styling

## Requirements

- Java installed
- Third-party JARs in `lib/`

The `ICSParser` depends on ical4j and its transitive dependencies.

## Get dependencies

Run the helper script from the project root:

```bash
./scripts/fetch_libs.sh
```

This creates `lib/` if needed and downloads the required JARs from Maven Central.

## Compile and run

From the project root:

```bash
javac -cp .:lib/* *.java
java -cp .:lib/* MiniLMSApp
```

On Windows, replace `:` with `;` in the classpath.

## Notes

- Data is stored locally in `data/`
- The app uses a simple CardLayout-based flow
- Imported `.ics` events are combined with course deadlines in the calendar
