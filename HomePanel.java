/*
 * Guy-robert Bogning
 * Home panel for the active student profile dashboard.
 */

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;

public class HomePanel extends JPanel {

    private MiniLMSFrame parentFrame;
    private StudentProfile currentProfile;

    private JLabel appTitleLabel;
    private JLabel signedInInfoLabel;
    private JLabel welcomeLabel;
    private JLabel enrollmentCountLabel;
    private JList<Course> enrolledCoursesList;
    private JButton manageCoursesButton;
    private JButton openCourseButton;

    private DefaultListModel<Course> coursesListModel;

    public HomePanel(MiniLMSFrame parentFrame) {
        this.parentFrame = parentFrame;
        setLayout(new BorderLayout(0, 0));
        setBackground(UITheme.BACKGROUND);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel headerPanel = new JPanel(new BorderLayout(10, 0));
        headerPanel.setBackground(UITheme.PRIMARY);
        headerPanel.setBorder(new EmptyBorder(15, 20, 15, 20));

        appTitleLabel = new JLabel("MiniLMS");
        appTitleLabel.setFont(new Font("Georgia", Font.BOLD, 24));
        appTitleLabel.setForeground(Color.WHITE);

        signedInInfoLabel = new JLabel();
        signedInInfoLabel.setFont(new Font("Georgia", Font.PLAIN, 14));
        signedInInfoLabel.setForeground(Color.WHITE);
        signedInInfoLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        headerPanel.add(appTitleLabel, BorderLayout.WEST);
        headerPanel.add(signedInInfoLabel, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        JPanel welcomePanel = new JPanel();
        welcomePanel.setLayout(new BoxLayout(welcomePanel, BoxLayout.Y_AXIS));
        welcomePanel.setBackground(UITheme.SURFACE);
        welcomePanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UITheme.BORDER),
            new EmptyBorder(20, 20, 20, 20)
        ));

        welcomeLabel = new JLabel("Welcome back!");
        welcomeLabel.setFont(UITheme.SECTION_FONT);
        welcomeLabel.setForeground(UITheme.TEXT);
        welcomeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        enrollmentCountLabel = new JLabel("You are not enrolled in any courses");
        enrollmentCountLabel.setFont(UITheme.BODY_FONT);
        enrollmentCountLabel.setForeground(UITheme.MUTED_TEXT);
        enrollmentCountLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        welcomePanel.add(welcomeLabel);
        welcomePanel.add(Box.createVerticalStrut(10));
        welcomePanel.add(enrollmentCountLabel);
        welcomePanel.add(Box.createVerticalStrut(20));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBackground(UITheme.SURFACE);
        buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        manageCoursesButton = new JButton("Manage Courses");
        UITheme.stylePrimaryButton(manageCoursesButton);
        manageCoursesButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        manageCoursesButton.setMaximumSize(new Dimension(200, 40));
        manageCoursesButton.addActionListener(e -> parentFrame.showCard("courseManagement"));

        buttonPanel.add(manageCoursesButton);

        welcomePanel.add(buttonPanel);

        add(welcomePanel, BorderLayout.WEST);

        JPanel coursesPanel = new JPanel(new BorderLayout(0, 10));
        coursesPanel.setBackground(UITheme.BACKGROUND);

        JLabel sectionTitle = new JLabel("Your Enrolled Courses");
        sectionTitle.setFont(UITheme.SECTION_FONT);
        sectionTitle.setForeground(UITheme.TEXT);

        coursesListModel = new DefaultListModel<>();
        enrolledCoursesList = new JList<>(coursesListModel);
        enrolledCoursesList.setFont(UITheme.BODY_FONT);
        enrolledCoursesList.setForeground(UITheme.TEXT);
        enrolledCoursesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        enrolledCoursesList.setCellRenderer(new CourseListCellRenderer());

        JScrollPane coursesScrollPane = new JScrollPane(enrolledCoursesList);
        coursesScrollPane.setBorder(BorderFactory.createLineBorder(UITheme.BORDER));
        coursesScrollPane.getViewport().setBackground(UITheme.SURFACE);

        JPanel coursesButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        coursesButtonPanel.setBackground(UITheme.BACKGROUND);

        openCourseButton = new JButton("Open Course");
        UITheme.stylePrimaryButton(openCourseButton);
        openCourseButton.addActionListener(e -> openSelectedCourse());

        coursesButtonPanel.add(openCourseButton);

        coursesPanel.add(sectionTitle, BorderLayout.NORTH);
        coursesPanel.add(coursesScrollPane, BorderLayout.CENTER);
        coursesPanel.add(coursesButtonPanel, BorderLayout.SOUTH);

        add(coursesPanel, BorderLayout.CENTER);

        JPanel eventsPanel = new JPanel(new BorderLayout(0, 10));
        eventsPanel.setBackground(UITheme.BACKGROUND);

        JLabel eventsTitleLabel = new JLabel("Upcoming Deadlines");
        eventsTitleLabel.setFont(UITheme.SECTION_FONT);
        eventsTitleLabel.setForeground(UITheme.TEXT);

        JLabel placeholderLabel = new JLabel("Calendar integration - to be implemented");
        placeholderLabel.setFont(UITheme.BODY_FONT);
        placeholderLabel.setForeground(UITheme.MUTED_TEXT);
        placeholderLabel.setBorder(new EmptyBorder(20, 10, 20, 10));

        eventsPanel.add(eventsTitleLabel, BorderLayout.NORTH);
        eventsPanel.add(placeholderLabel, BorderLayout.CENTER);

        add(eventsPanel, BorderLayout.SOUTH);
    }

    public void setProfile(StudentProfile profile) {
        this.currentProfile = profile;
        refreshDisplay();
    }

    public void refreshDisplay() {
        if (currentProfile == null) {
            signedInInfoLabel.setText("");
            welcomeLabel.setText("Welcome!");
            enrollmentCountLabel.setText("No profile selected");
            coursesListModel.clear();
            return;
        }

        String fullName = currentProfile.getStudentName();
        String studentId = currentProfile.getStudentId();
        signedInInfoLabel.setText("Signed in as: " + fullName + " (" + studentId + ")");
        welcomeLabel.setText("Welcome, " + fullName + "!");

        StudentRecord record = currentProfile.getRecord();
        if (record != null) {
            ArrayList<Course> enrolledCourses = record.getEnrolledCourses();
            int courseCount = enrolledCourses.size();

            if (courseCount == 0) {
                enrollmentCountLabel.setText("You are not enrolled in any courses");
                coursesListModel.clear();
            } else if (courseCount == 1) {
                enrollmentCountLabel.setText("You are enrolled in 1 course");
                coursesListModel.clear();
                for (Course course : enrolledCourses) {
                    coursesListModel.addElement(course);
                }
            } else {
                enrollmentCountLabel.setText("You are enrolled in " + courseCount + " courses");
                coursesListModel.clear();
                for (Course course : enrolledCourses) {
                    coursesListModel.addElement(course);
                }
            }
        }
    }

    private void openSelectedCourse() {
        Course selectedCourse = enrolledCoursesList.getSelectedValue();
        if (selectedCourse == null) {
            JOptionPane.showMessageDialog(
                parentFrame,
                "Please select a course to open.",
                "Open Course",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }
        parentFrame.openCourse(selectedCourse);
    }

    private static class CourseListCellRenderer extends JPanel implements ListCellRenderer<Course> {
        private JLabel courseIdLabel;
        private JLabel courseNameLabel;
        private JLabel instructorLabel;
        private JLabel modulesLabel;

        public CourseListCellRenderer() {
            setLayout(new GridLayout(1, 4, 10, 0));
            setBorder(new EmptyBorder(10, 10, 10, 10));
            setBackground(UITheme.SURFACE);

            courseIdLabel = new JLabel();
            courseIdLabel.setFont(new Font("Georgia", Font.BOLD, 13));
            courseIdLabel.setForeground(UITheme.PRIMARY);

            courseNameLabel = new JLabel();
            courseNameLabel.setFont(UITheme.BODY_FONT);
            courseNameLabel.setForeground(UITheme.TEXT);

            instructorLabel = new JLabel();
            instructorLabel.setFont(UITheme.BODY_FONT);
            instructorLabel.setForeground(UITheme.MUTED_TEXT);

            modulesLabel = new JLabel();
            modulesLabel.setFont(new Font("Georgia", Font.PLAIN, 12));
            modulesLabel.setForeground(UITheme.MUTED_TEXT);
            modulesLabel.setHorizontalAlignment(SwingConstants.RIGHT);

            add(courseIdLabel);
            add(courseNameLabel);
            add(instructorLabel);
            add(modulesLabel);
        }

        @Override
        public Component getListCellRendererComponent(JList<? extends Course> list, Course course,
                int index, boolean isSelected, boolean cellHasFocus) {
            courseIdLabel.setText(course.getCourseId());
            courseNameLabel.setText(course.getTitle());
            instructorLabel.setText(course.getInstructor());

            String[] topics = course.getTopics();
            if (topics != null) {
                modulesLabel.setText(topics.length + " topics");
            } else {
                modulesLabel.setText("0 topics");
            }

            if (isSelected) {
                setBackground(UITheme.PRIMARY);
                courseIdLabel.setForeground(Color.WHITE);
                courseNameLabel.setForeground(Color.WHITE);
                instructorLabel.setForeground(Color.WHITE);
                modulesLabel.setForeground(Color.WHITE);
            } else {
                setBackground(UITheme.SURFACE);
                courseIdLabel.setForeground(UITheme.PRIMARY);
                courseNameLabel.setForeground(UITheme.TEXT);
                instructorLabel.setForeground(UITheme.MUTED_TEXT);
                modulesLabel.setForeground(UITheme.MUTED_TEXT);
            }

            return this;
        }
    }
}
