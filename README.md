# MiniLMS

MiniLMS is a local desktop learning management system built with Java Swing.

## What it does

- Create and sign in with local profiles
- Enroll in and drop courses
- View course content
- Track deadlines from built-in courses and imported `.ics` files
- See upcoming items in a calendar view

## Screenshots

### 1. Welcome screen
![Welcome screen](media/Screenshot%20from%202026-04-23%2016-35-28.png)

### 2. Profile creation
![Profile creation](media/Screenshot%20from%202026-04-23%2016-36-07.png)

### 3. Profile selection
![Profile selection](media/Screenshot%20from%202026-04-23%2016-36-16.png)

### 4. Sign-in prompt
![Sign-in prompt](media/Screenshot%20from%202026-04-23%2016-36-22.png)

### 5. Signed-in dashboard
![Dashboard](media/Screenshot%20from%202026-04-23%2016-36-28.png)

### 6. Course management
![Course management](media/Screenshot%20from%202026-04-23%2016-36-37.png)

### 7. Course content
![Course content](media/Screenshot%20from%202026-04-23%2016-36-43.png)

### 8. Calendar view
![Calendar view](media/Screenshot%20from%202026-04-23%2016-36-54.png)

### 9. Upcoming deadlines
![Upcoming deadlines](media/Screenshot%20from%202026-04-23%2016-37-05.png)

### 10. Import confirmation
![Import confirmation](media/Screenshot%20from%202026-04-23%2016-37-54.png)

### 11. Final dashboard state
![Final dashboard state](media/Screenshot%20from%202026-04-23%2016-38-05.png)

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
