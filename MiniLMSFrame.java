/*
 * Guy-robert Bogning
 * Main application frame for profile navigation, LMS screens, and shared window controls.
 */

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.event.ActionListener;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class MiniLMSFrame extends JFrame {

    private static final String WELCOME_CARD = "welcome";
    private static final String PROFILE_SELECTION_CARD = "profileSelection";
    private static final String PROFILE_CREATION_CARD = "profileCreation";

    private static final int WIDTH = 1200;
    private static final int HEIGHT = 800;

    private final CardLayout cardLayout;
    private final JPanel centerPanel;
    private final WelcomePanel welcomePanel;
    private final ProfileSelectionPanel profileSelectionPanel;
    private final ProfileCreationPanel profileCreationPanel;
    private final PersistenceManager persistenceManager;
    private final ProfileIndex profileIndex;
    private final ProfileService profileService;
    private final Path profileIndexPath;

    public MiniLMSFrame() {
        super("MiniLMS");
        Path dataDirectory = Paths.get("data");
        profileIndexPath = dataDirectory.resolve("profiles.ser");
        persistenceManager = new PersistenceManager(dataDirectory);
        profileIndex = persistenceManager.loadProfileIndex(profileIndexPath);
        profileService = new ProfileService(
            persistenceManager,
            profileIndexPath
        );

        cardLayout = new CardLayout();
        centerPanel = new JPanel(cardLayout);
        welcomePanel = new WelcomePanel();
        profileSelectionPanel = new ProfileSelectionPanel();
        profileCreationPanel = new ProfileCreationPanel();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(UITheme.BACKGROUND);

        centerPanel.add(welcomePanel, WELCOME_CARD);
        centerPanel.add(profileSelectionPanel, PROFILE_SELECTION_CARD);
        centerPanel.add(profileCreationPanel, PROFILE_CREATION_CARD);
        centerPanel.setBackground(UITheme.BACKGROUND);

        welcomePanel
            .getSignInButton()
            .addActionListener(showProfileSelection());
        welcomePanel.getSignUpButton().addActionListener(showProfileCreation());
        profileSelectionPanel.getBackButton().addActionListener(showWelcome());
        profileCreationPanel.getBackButton().addActionListener(showWelcome());
        profileCreationPanel
            .getCreateProfileButton()
            .addActionListener(createProfile());
        profileSelectionPanel
            .getSelectButton()
            .addActionListener(selectProfile());

        add(centerPanel, BorderLayout.CENTER);
        profileSelectionPanel.refreshProfiles(profileIndex);
        cardLayout.show(centerPanel, WELCOME_CARD);
    }

    public JPanel getCenterPanel() {
        return centerPanel;
    }

    public CardLayout getCardLayout() {
        return cardLayout;
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
                profileService.createProfile(
                    profileIndex,
                    profileCreationPanel.getStudentFirstName(),
                    profileCreationPanel.getStudentLastName()
                );
                profileCreationPanel.clearFields();
                profileSelectionPanel.refreshProfiles(profileIndex);
                cardLayout.show(centerPanel, PROFILE_SELECTION_CARD);
            } catch (IllegalArgumentException ex) {
                profileCreationPanel.setStatusMessage(ex.getMessage());
            } catch (RuntimeException ex) {
                profileCreationPanel.setStatusMessage(
                    "Could not create profile right now."
                );
            }
        };
    }

    private ActionListener selectProfile() {
        return event -> {
            StudentProfile selectedProfile =
                profileSelectionPanel.getSelectedProfile();
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
                "Enter the student ID for " +
                    selectedProfile.getStudentName() +
                    ":",
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
            
            
        };
    }
}
