/*
 * Guy-robert Bogning
 * Main application frame for profile navigation, LMS screens, and shared window controls.
 */

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

public class MiniLMSFrame extends JFrame {

    private static final String WELCOME_CARD = "welcome";
    private static final String PROFILE_SELECTION_CARD = "profileSelection";
    private static final String PROFILE_CREATION_CARD = "profileCreation";
    private static final String HOME_CARD = "home";
    private static final String COURSE_MANAGEMENT_CARD = "courseManagement";
    private static final String COURSE_CONTENT_CARD = "courseContent";
    private static final String CALENDAR_CARD = "calendar";

    private static final int WIDTH = 1200;
    private static final int HEIGHT = 800;

    private final CardLayout cardLayout;
    private final JPanel centerPanel;
    private final WelcomePanel welcomePanel;
    private final ProfileSelectionPanel profileSelectionPanel;
    private final ProfileCreationPanel profileCreationPanel;
    private final HomePanel homePanel;
    private final CourseManagementPanel courseManagementPanel;
    private final CourseContentPanel courseContentPanel;
    private final CalendarPanel calendarPanel;
    private final PersistenceManager persistenceManager;
    private final ProfileIndex profileIndex;
    private final ProfileService profileService;
    private final Path profileIndexPath;
    private final CourseCatalog courseCatalog;
    private final ICSParser icsParser;

    private StudentProfile currentProfile;
    private Course selectedCourse;

    public MiniLMSFrame() {
        super("MiniLMS");
        Path dataDirectory = Paths.get("data");
        profileIndexPath = dataDirectory.resolve("profiles.ser");
        persistenceManager = new PersistenceManager(dataDirectory);
        profileIndex = persistenceManager.loadProfileIndex(profileIndexPath);
        profileService = new ProfileService(persistenceManager, profileIndexPath);
        courseCatalog = new CourseCatalog();
        icsParser = new ICSParser();

        cardLayout = new CardLayout();
        centerPanel = new JPanel(cardLayout);
        welcomePanel = new WelcomePanel();
        profileSelectionPanel = new ProfileSelectionPanel();
        profileCreationPanel = new ProfileCreationPanel();
        homePanel = new HomePanel(this);
        courseManagementPanel = new CourseManagementPanel(this);
        courseContentPanel = new CourseContentPanel(this);
        calendarPanel = new CalendarPanel(this);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(UITheme.BACKGROUND);

        centerPanel.add(welcomePanel, WELCOME_CARD);
        centerPanel.add(profileSelectionPanel, PROFILE_SELECTION_CARD);
        centerPanel.add(profileCreationPanel, PROFILE_CREATION_CARD);
        centerPanel.add(homePanel, HOME_CARD);
        centerPanel.add(courseManagementPanel, COURSE_MANAGEMENT_CARD);
        centerPanel.add(courseContentPanel, COURSE_CONTENT_CARD);
        centerPanel.add(calendarPanel, CALENDAR_CARD);
        centerPanel.setBackground(UITheme.BACKGROUND);

        welcomePanel.getSignInButton().addActionListener(showProfileSelection());
        welcomePanel.getSignUpButton().addActionListener(showProfileCreation());
        profileSelectionPanel.getBackButton().addActionListener(showWelcome());
        profileCreationPanel.getBackButton().addActionListener(showWelcome());
        profileCreationPanel.getCreateProfileButton().addActionListener(createProfile());
        profileSelectionPanel.getSelectButton().addActionListener(selectProfile());

        buildMenuBar();

        add(centerPanel, BorderLayout.CENTER);
        profileSelectionPanel.refreshProfiles(profileIndex);
        cardLayout.show(centerPanel, WELCOME_CARD);
    }

    private void buildMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        JMenuItem signOutItem = new JMenuItem("Sign Out");
        signOutItem.addActionListener(e -> signOut());
        JMenuItem saveProfileItem = new JMenuItem("Save Profile");
        saveProfileItem.addActionListener(e -> saveCurrentProfile());
        JMenuItem importIcsItem = new JMenuItem("Import ICS");
        importIcsItem.addActionListener(e -> handleImportIcs());
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> handleExit());

        fileMenu.add(signOutItem);
        fileMenu.add(saveProfileItem);
        fileMenu.add(importIcsItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        JMenu viewMenu = new JMenu("View");
        JMenuItem homeItem = new JMenuItem("Home");
        homeItem.addActionListener(e -> showCard(HOME_CARD));
        JMenuItem courseMgmtItem = new JMenuItem("Course Management");
        courseMgmtItem.addActionListener(e -> showCard(COURSE_MANAGEMENT_CARD));
        JMenuItem calendarItem = new JMenuItem("Calendar");
        calendarItem.addActionListener(e -> showCard(CALENDAR_CARD));

        viewMenu.add(homeItem);
        viewMenu.add(courseMgmtItem);
        viewMenu.add(calendarItem);

        JMenu helpMenu = new JMenu("Help");
        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.addActionListener(e -> showAboutDialog());
        helpMenu.add(aboutItem);

        menuBar.add(fileMenu);
        menuBar.add(viewMenu);
        menuBar.add(helpMenu);

        setJMenuBar(menuBar);
    }

    public void showCard(String cardName) {
        cardLayout.show(centerPanel, cardName);
        if (HOME_CARD.equals(cardName)) {
            refreshHomePanel();
        } else if (COURSE_MANAGEMENT_CARD.equals(cardName)) {
            refreshCourseManagement();
        } else if (COURSE_CONTENT_CARD.equals(cardName) && selectedCourse != null) {
            courseContentPanel.displayCourse(selectedCourse);
        }
    }

    public void signOut() {
        if (currentProfile != null) {
            saveCurrentProfile();
        }
        currentProfile = null;
        selectedCourse = null;
        cardLayout.show(centerPanel, WELCOME_CARD);
    }

    public void signInAndShowHome(StudentProfile profile) {
        currentProfile = profile;
        homePanel.setProfile(currentProfile);
        showCard(HOME_CARD);
    }

    public void saveCurrentProfile() {
        if (currentProfile != null) {
            try {
                persistCurrentProfile();
                JOptionPane.showMessageDialog(
                    this,
                    "Profile saved successfully.",
                    "Save",
                    JOptionPane.INFORMATION_MESSAGE
                );
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(
                    this,
                    "Failed to save profile.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }

    public void persistCurrentProfile() {
        if (currentProfile != null) {
            try {
                persistenceManager.saveProfileIndex(profileIndex, profileIndexPath);
            } catch (Exception ex) {
                throw new RuntimeException("Failed to save profile.", ex);
            }
        }
    }

    public void handleImportIcs() {
        if (currentProfile == null) {
            JOptionPane.showMessageDialog(
                this,
                "Please sign in first.",
                "Import ICS",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("ICS Files", "ics"));
        fileChooser.setDialogTitle("Import ICS File");

        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try {
                ArrayList<CalendarEvent> events = icsParser.parse(selectedFile);
                if (events.isEmpty()) {
                    JOptionPane.showMessageDialog(
                        this,
                        "No events found in the file.",
                        "Import ICS",
                        JOptionPane.INFORMATION_MESSAGE
                    );
                } else {
                    currentProfile.getRecord().setCalendarEvents(events);
                    saveCurrentProfile();
                    homePanel.refreshDisplay();
                    calendarPanel.refreshEvents();
                    JOptionPane.showMessageDialog(
                        this,
                        "Imported " + events.size() + " event(s) successfully.",
                        "Import ICS",
                        JOptionPane.INFORMATION_MESSAGE
                    );
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(
                    this,
                    "Failed to parse ICS file: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }

    private void handleExit() {
        int choice = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to exit?",
            "Exit",
            JOptionPane.YES_NO_OPTION
        );
        if (choice == JOptionPane.YES_OPTION) {
            if (currentProfile != null) {
                saveCurrentProfile();
            }
            System.exit(0);
        }
    }

    private void showAboutDialog() {
        JOptionPane.showMessageDialog(
            this,
            "MiniLMS v1.0\n\nA mini Learning Management System\nbuilt with Java Swing.",
            "About MiniLMS",
            JOptionPane.INFORMATION_MESSAGE
        );
    }

    public CourseCatalog getCourseCatalog() {
        return courseCatalog;
    }

    public void openCourse(Course course) {
        selectedCourse = course;
        courseContentPanel.displayCourse(course);
        showCard(COURSE_CONTENT_CARD);
    }

    public StudentProfile getCurrentProfile() {
        return currentProfile;
    }

    public void refreshHomePanel() {
        homePanel.refreshDisplay();
    }

    public void refreshCourseManagement() {
        courseManagementPanel.refreshCourseLists();
    }

    public void refreshCalendarPanel() {
        calendarPanel.refreshEvents();
    }

    private ActionListener showWelcome() {
        return event -> cardLayout.show(centerPanel, WELCOME_CARD);
    }

    private ActionListener showProfileSelection() {
        return event -> cardLayout.show(centerPanel, PROFILE_SELECTION_CARD);
    }

    private ActionListener showProfileCreation() {
        return event -> cardLayout.show(centerPanel, PROFILE_CREATION_CARD);
    }

    private ActionListener createProfile() {
        return event -> {
            try {
                profileCreationPanel.clearStatusMessage();
                StudentProfile newProfile = profileService.createProfile(
                    profileIndex,
                    profileCreationPanel.getStudentFirstName(),
                    profileCreationPanel.getStudentLastName()
                );
                profileCreationPanel.clearFields();
                profileSelectionPanel.refreshProfiles(profileIndex);
                JOptionPane.showMessageDialog(
                    this,
                    "Profile created successfully!\nStudent ID: " + newProfile.getStudentId(),
                    "Sign Up",
                    JOptionPane.INFORMATION_MESSAGE
                );
                cardLayout.show(centerPanel, PROFILE_SELECTION_CARD);
            } catch (IllegalArgumentException ex) {
                profileCreationPanel.setStatusMessage(ex.getMessage());
            } catch (RuntimeException ex) {
                profileCreationPanel.setStatusMessage("Could not create profile right now.");
            }
        };
    }

    private ActionListener selectProfile() {
        return event -> {
            StudentProfile selectedProfile = profileSelectionPanel.getSelectedProfile();
            if (selectedProfile == null) {
                JOptionPane.showMessageDialog(
                    this,
                    "Please select a profile first.",
                    "Sign In",
                    JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            String enteredId = JOptionPane.showInputDialog(
                this,
                "Enter the student ID for " + selectedProfile.getStudentName() + ":",
                "Sign In",
                JOptionPane.PLAIN_MESSAGE
            );

            if (enteredId == null) {
                return;
            }

            enteredId = enteredId.trim();
            if (enteredId.isEmpty()) {
                JOptionPane.showMessageDialog(
                    this,
                    "Student ID cannot be empty.",
                    "Sign In",
                    JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            if (!enteredId.equals(selectedProfile.getStudentId())) {
                JOptionPane.showMessageDialog(
                    this,
                    "Invalid student ID.",
                    "Sign In",
                    JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            JOptionPane.showMessageDialog(
                this,
                "Signed in as " + selectedProfile.getStudentName() + ".",
                "Sign In",
                JOptionPane.INFORMATION_MESSAGE
            );
            signInAndShowHome(selectedProfile);
        };
    }
}
