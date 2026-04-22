/*
 * Guy-robert Bogning
 * Course management panel for enrolled courses and course enrollment actions.
 */

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;

public class CourseManagementPanel extends JPanel {

    private enum SelectionSource {
        NONE,
        AVAILABLE,
        ENROLLED,
    }

    private MiniLMSFrame parentFrame;
    private JLabel screenTitleLabel;
    private JLabel helperLabel;
    private JLabel availableCoursesLabel;
    private JLabel enrolledCoursesLabel;
    private JLabel statusLabel;
    private JList<Course> availableCoursesList;
    private JList<Course> enrolledCoursesList;
    private DefaultListModel<Course> availableCoursesModel;
    private DefaultListModel<Course> enrolledCoursesModel;

    private JLabel infoCourseIdValueLabel;
    private JLabel infoTitleValueLabel;
    private JLabel infoInstructorValueLabel;
    private JLabel infoTopicsValueLabel;
    private JLabel infoAssignmentsValueLabel;
    private JTextArea infoDescriptionArea;

    private JButton enrollButton;
    private JButton dropButton;
    private JButton openCourseButton;
    private JButton backButton;

    private SelectionSource activeSelectionSource;

    public CourseManagementPanel(MiniLMSFrame parentFrame) {
        this.parentFrame = parentFrame;
        activeSelectionSource = SelectionSource.NONE;

        setLayout(new BorderLayout(0, 0));
        setBackground(UITheme.BACKGROUND);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(
            new javax.swing.BoxLayout(headerPanel, javax.swing.BoxLayout.Y_AXIS)
        );
        headerPanel.setOpaque(false);

        screenTitleLabel = new JLabel("Course Management");
        screenTitleLabel.setFont(UITheme.SECTION_FONT);
        screenTitleLabel.setForeground(UITheme.TEXT);
        screenTitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        helperLabel = new JLabel(
            "Select a course to preview details, then enroll, drop, or open it."
        );
        helperLabel.setFont(UITheme.BODY_FONT);
        helperLabel.setForeground(UITheme.MUTED_TEXT);
        helperLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        headerPanel.add(screenTitleLabel);
        headerPanel.add(BoxUtil.spacer(0, 6));
        headerPanel.add(helperLabel);

        add(headerPanel, BorderLayout.NORTH);

        JPanel availablePanel = new JPanel(new BorderLayout(0, 10));
        availablePanel.setBackground(UITheme.BACKGROUND);

        availableCoursesLabel = new JLabel("Available Catalog Courses");
        availableCoursesLabel.setFont(
            UITheme.BODY_FONT.deriveFont(Font.BOLD, 16f)
        );
        availableCoursesLabel.setForeground(UITheme.TEXT);

        availableCoursesModel = new DefaultListModel<>();
        availableCoursesList = new JList<>(availableCoursesModel);
        availableCoursesList.setSelectionMode(
            ListSelectionModel.SINGLE_SELECTION
        );
        availableCoursesList.setFont(UITheme.BODY_FONT);
        availableCoursesList.setForeground(UITheme.TEXT);
        availableCoursesList.setCellRenderer(new CourseListCellRenderer());
        availableCoursesList.addListSelectionListener(
            this::handleAvailableCourseSelectionChanged
        );

        JScrollPane availableScrollPane = new JScrollPane(availableCoursesList);
        availableScrollPane.setBorder(
            BorderFactory.createLineBorder(UITheme.BORDER)
        );
        availableScrollPane.getViewport().setBackground(UITheme.SURFACE);

        availablePanel.add(availableCoursesLabel, BorderLayout.NORTH);
        availablePanel.add(availableScrollPane, BorderLayout.CENTER);

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(
            new javax.swing.BoxLayout(infoPanel, javax.swing.BoxLayout.Y_AXIS)
        );
        infoPanel.setBackground(UITheme.SURFACE);
        infoPanel.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UITheme.BORDER),
                new EmptyBorder(12, 12, 12, 12)
            )
        );

        JLabel infoTitleLabel = new JLabel("Course Info");
        infoTitleLabel.setFont(UITheme.BODY_FONT.deriveFont(Font.BOLD, 16f));
        infoTitleLabel.setForeground(UITheme.TEXT);
        infoTitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        infoCourseIdValueLabel = createInfoLineLabel();
        infoTitleValueLabel = createInfoLineLabel();
        infoInstructorValueLabel = createInfoLineLabel();
        infoTopicsValueLabel = createInfoLineLabel();
        infoAssignmentsValueLabel = createInfoLineLabel();

        infoDescriptionArea = new JTextArea();
        infoDescriptionArea.setEditable(false);
        infoDescriptionArea.setLineWrap(true);
        infoDescriptionArea.setWrapStyleWord(true);
        infoDescriptionArea.setFont(UITheme.BODY_FONT.deriveFont(14f));
        infoDescriptionArea.setForeground(UITheme.TEXT);
        infoDescriptionArea.setBackground(UITheme.SURFACE);
        infoDescriptionArea.setBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(UITheme.BORDER),
                "Description"
            )
        );

        JScrollPane descriptionScrollPane = new JScrollPane(
            infoDescriptionArea
        );
        descriptionScrollPane.setBorder(null);
        descriptionScrollPane.setPreferredSize(new Dimension(0, 150));

        infoPanel.add(infoTitleLabel);
        infoPanel.add(BoxUtil.spacer(0, 8));
        infoPanel.add(infoCourseIdValueLabel);
        infoPanel.add(BoxUtil.spacer(0, 4));
        infoPanel.add(infoTitleValueLabel);
        infoPanel.add(BoxUtil.spacer(0, 4));
        infoPanel.add(infoInstructorValueLabel);
        infoPanel.add(BoxUtil.spacer(0, 4));
        infoPanel.add(infoTopicsValueLabel);
        infoPanel.add(BoxUtil.spacer(0, 4));
        infoPanel.add(infoAssignmentsValueLabel);
        infoPanel.add(BoxUtil.spacer(0, 10));
        infoPanel.add(descriptionScrollPane);

        JPanel enrolledPanel = new JPanel(new BorderLayout(0, 10));
        enrolledPanel.setBackground(UITheme.BACKGROUND);

        enrolledCoursesLabel = new JLabel("Enrolled Courses");
        enrolledCoursesLabel.setFont(
            UITheme.BODY_FONT.deriveFont(Font.BOLD, 16f)
        );
        enrolledCoursesLabel.setForeground(UITheme.TEXT);

        enrolledCoursesModel = new DefaultListModel<>();
        enrolledCoursesList = new JList<>(enrolledCoursesModel);
        enrolledCoursesList.setSelectionMode(
            ListSelectionModel.SINGLE_SELECTION
        );
        enrolledCoursesList.setFont(UITheme.BODY_FONT);
        enrolledCoursesList.setForeground(UITheme.TEXT);
        enrolledCoursesList.setCellRenderer(new CourseListCellRenderer());
        enrolledCoursesList.addListSelectionListener(
            this::handleEnrolledCourseSelectionChanged
        );

        JScrollPane enrolledScrollPane = new JScrollPane(enrolledCoursesList);
        enrolledScrollPane.setBorder(
            BorderFactory.createLineBorder(UITheme.BORDER)
        );
        enrolledScrollPane.getViewport().setBackground(UITheme.SURFACE);

        enrolledPanel.add(enrolledCoursesLabel, BorderLayout.NORTH);
        enrolledPanel.add(enrolledScrollPane, BorderLayout.CENTER);

        JSplitPane rightSplitPane = new JSplitPane(
            JSplitPane.VERTICAL_SPLIT,
            infoPanel,
            enrolledPanel
        );
        rightSplitPane.setResizeWeight(0.62);
        rightSplitPane.setDividerLocation(330);
        rightSplitPane.setBorder(null);

        JSplitPane mainSplitPane = new JSplitPane(
            JSplitPane.HORIZONTAL_SPLIT,
            availablePanel,
            rightSplitPane
        );
        mainSplitPane.setResizeWeight(0.58);
        mainSplitPane.setDividerLocation(620);
        mainSplitPane.setBorder(null);

        add(mainSplitPane, BorderLayout.CENTER);

        JPanel footerPanel = new JPanel(new BorderLayout(0, 10));
        footerPanel.setOpaque(false);

        statusLabel = new JLabel(" ");
        statusLabel.setFont(UITheme.BODY_FONT.deriveFont(Font.PLAIN, 12f));
        statusLabel.setForeground(UITheme.MUTED_TEXT);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        buttonPanel.setOpaque(false);

        enrollButton = new JButton("Enroll Selected");
        UITheme.stylePrimaryButton(enrollButton);
        enrollButton.addActionListener(this::handleEnrollButtonClicked);

        dropButton = new JButton("Drop Selected");
        UITheme.styleSecondaryButton(dropButton);
        dropButton.addActionListener(this::handleDropButtonClicked);

        openCourseButton = new JButton("Open Course");
        UITheme.stylePrimaryButton(openCourseButton);
        openCourseButton.addActionListener(this::handleOpenCourseButtonClicked);

        backButton = new JButton("Back to Home");
        UITheme.styleSecondaryButton(backButton);
        backButton.addActionListener(this::handleBackButtonClicked);

        buttonPanel.add(enrollButton);
        buttonPanel.add(BoxUtil.spacer(10, 0));
        buttonPanel.add(dropButton);
        buttonPanel.add(BoxUtil.spacer(10, 0));
        buttonPanel.add(openCourseButton);
        buttonPanel.add(BoxUtil.spacer(10, 0));
        buttonPanel.add(backButton);

        footerPanel.add(statusLabel, BorderLayout.NORTH);
        footerPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(footerPanel, BorderLayout.SOUTH);

        showCourseInfo(null);
        refreshCourseLists();
    }

    public void refreshCourseLists() {
        Course preferredCourse = getActiveSelectedCourse();

        enrolledCoursesModel.clear();
        availableCoursesModel.clear();
        enrolledCoursesList.clearSelection();
        availableCoursesList.clearSelection();
        activeSelectionSource = SelectionSource.NONE;

        StudentProfile profile = parentFrame.getCurrentProfile();
        if (profile == null || profile.getRecord() == null) {
            statusLabel.setText("Please sign in to manage courses.");
            showCourseInfo(null);
            updateButtonStates();
            return;
        }

        StudentRecord record = profile.getRecord();
        ArrayList<Course> enrolledCourses = record.getEnrolledCourses();
        ArrayList<Course> catalogCourses = parentFrame
            .getCourseCatalog()
            .getCourses();

        for (Course course : enrolledCourses) {
            enrolledCoursesModel.addElement(course);
        }

        for (Course course : catalogCourses) {
            if (!record.isEnrolled(course)) {
                availableCoursesModel.addElement(course);
            }
        }

        if (preferredCourse != null && record.isEnrolled(preferredCourse)) {
            enrolledCoursesList.setSelectedValue(preferredCourse, true);
            activeSelectionSource = SelectionSource.ENROLLED;
            showCourseInfo(preferredCourse);
        } else if (preferredCourse != null) {
            availableCoursesList.setSelectedValue(preferredCourse, true);
            if (availableCoursesList.getSelectedValue() != null) {
                activeSelectionSource = SelectionSource.AVAILABLE;
                showCourseInfo(preferredCourse);
            }
        }

        if (activeSelectionSource == SelectionSource.NONE) {
            showCourseInfo(null);
        }

        statusLabel.setText(
            enrolledCoursesModel.size() +
                " enrolled course(s), " +
                availableCoursesModel.size() +
                " available course(s)."
        );
        updateButtonStates();
    }

    private void enrollSelectedCourse() {
        StudentProfile profile = parentFrame.getCurrentProfile();
        if (profile == null || profile.getRecord() == null) {
            JOptionPane.showMessageDialog(
                parentFrame,
                "Please sign in first.",
                "Enroll Course",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        Course selectedCourse = availableCoursesList.getSelectedValue();
        if (selectedCourse == null) {
            JOptionPane.showMessageDialog(
                parentFrame,
                "Please select an available course to enroll.",
                "Enroll Course",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        try {
            profile.getRecord().addCourse(selectedCourse);
            parentFrame.persistCurrentProfile();
            activeSelectionSource = SelectionSource.ENROLLED;
            refreshCourseLists();
            enrolledCoursesList.setSelectedValue(selectedCourse, true);
            showCourseInfo(selectedCourse);
            updateButtonStates();
            parentFrame.refreshHomePanel();
            JOptionPane.showMessageDialog(
                parentFrame,
                "Enrolled in " + selectedCourse.getTitle() + ".",
                "Enroll Course",
                JOptionPane.INFORMATION_MESSAGE
            );
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(
                parentFrame,
                ex.getMessage(),
                "Enroll Course",
                JOptionPane.WARNING_MESSAGE
            );
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(
                parentFrame,
                "Could not save the updated profile.",
                "Enroll Course",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void dropSelectedCourse() {
        StudentProfile profile = parentFrame.getCurrentProfile();
        if (profile == null || profile.getRecord() == null) {
            JOptionPane.showMessageDialog(
                parentFrame,
                "Please sign in first.",
                "Drop Course",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        Course selectedCourse = enrolledCoursesList.getSelectedValue();
        if (selectedCourse == null) {
            JOptionPane.showMessageDialog(
                parentFrame,
                "Please select an enrolled course to drop.",
                "Drop Course",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        try {
            profile.getRecord().removeCourse(selectedCourse);
            parentFrame.persistCurrentProfile();
            activeSelectionSource = SelectionSource.AVAILABLE;
            refreshCourseLists();
            availableCoursesList.setSelectedValue(selectedCourse, true);
            showCourseInfo(selectedCourse);
            updateButtonStates();
            parentFrame.refreshHomePanel();
            JOptionPane.showMessageDialog(
                parentFrame,
                "Dropped " + selectedCourse.getTitle() + ".",
                "Drop Course",
                JOptionPane.INFORMATION_MESSAGE
            );
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(
                parentFrame,
                ex.getMessage(),
                "Drop Course",
                JOptionPane.WARNING_MESSAGE
            );
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(
                parentFrame,
                "Could not save the updated profile.",
                "Drop Course",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void openSelectedCourse() {
        Course selectedCourse = enrolledCoursesList.getSelectedValue();
        if (selectedCourse == null) {
            JOptionPane.showMessageDialog(
                parentFrame,
                "Please select an enrolled course to open.",
                "Open Course",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }
        parentFrame.openCourse(selectedCourse);
    }

    private void showCourseInfo(Course course) {
        if (course == null) {
            infoCourseIdValueLabel.setText("Course ID: -");
            infoTitleValueLabel.setText("Title: -");
            infoInstructorValueLabel.setText("Instructor: -");
            infoTopicsValueLabel.setText("Topics: -");
            infoAssignmentsValueLabel.setText("Assignments: -");
            infoDescriptionArea.setText("Select a course to view details.");
            infoDescriptionArea.setCaretPosition(0);
            return;
        }

        infoCourseIdValueLabel.setText("Course ID: " + course.getCourseId());
        infoTitleValueLabel.setText("Title: " + course.getTitle());
        infoInstructorValueLabel.setText(
            "Instructor: " + course.getInstructor()
        );
        infoTopicsValueLabel.setText("Topics: " + course.getTopics().length);
        infoAssignmentsValueLabel.setText(
            "Assignments: " + course.getAssignments().length
        );
        infoDescriptionArea.setText(course.getDescription());
        infoDescriptionArea.setCaretPosition(0);
    }

    private void updateButtonStates() {
        StudentProfile profile = parentFrame.getCurrentProfile();
        boolean hasProfile = profile != null && profile.getRecord() != null;
        boolean hasAvailableSelection =
            availableCoursesList.getSelectedValue() != null;
        boolean hasEnrolledSelection =
            enrolledCoursesList.getSelectedValue() != null;

        enrollButton.setEnabled(hasProfile && hasAvailableSelection);
        dropButton.setEnabled(hasProfile && hasEnrolledSelection);
        openCourseButton.setEnabled(hasProfile && hasEnrolledSelection);
    }

    private void handleAvailableCourseSelectionChanged(
        javax.swing.event.ListSelectionEvent event
    ) {
        if (
            !event.getValueIsAdjusting() &&
            availableCoursesList.getSelectedValue() != null
        ) {
            activeSelectionSource = SelectionSource.AVAILABLE;
            enrolledCoursesList.clearSelection();
            showCourseInfo(availableCoursesList.getSelectedValue());
            updateButtonStates();
        }
    }

    private void handleEnrolledCourseSelectionChanged(
        javax.swing.event.ListSelectionEvent event
    ) {
        if (
            !event.getValueIsAdjusting() &&
            enrolledCoursesList.getSelectedValue() != null
        ) {
            activeSelectionSource = SelectionSource.ENROLLED;
            availableCoursesList.clearSelection();
            showCourseInfo(enrolledCoursesList.getSelectedValue());
            updateButtonStates();
        }
    }

    private void handleEnrollButtonClicked(java.awt.event.ActionEvent event) {
        enrollSelectedCourse();
    }

    private void handleDropButtonClicked(java.awt.event.ActionEvent event) {
        dropSelectedCourse();
    }

    private void handleOpenCourseButtonClicked(
        java.awt.event.ActionEvent event
    ) {
        openSelectedCourse();
    }

    private void handleBackButtonClicked(java.awt.event.ActionEvent event) {
        parentFrame.showCard("home");
    }

    private Course getActiveSelectedCourse() {
        if (activeSelectionSource == SelectionSource.ENROLLED) {
            return enrolledCoursesList.getSelectedValue();
        }
        if (activeSelectionSource == SelectionSource.AVAILABLE) {
            return availableCoursesList.getSelectedValue();
        }
        return null;
    }

    private JLabel createInfoLineLabel() {
        JLabel label = new JLabel();
        label.setFont(UITheme.BODY_FONT.deriveFont(14f));
        label.setForeground(UITheme.TEXT);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private static final class BoxUtil {

        private BoxUtil() {}

        private static Component spacer(int width, int height) {
            return new javax.swing.Box.Filler(
                new Dimension(width, height),
                new Dimension(width, height),
                new Dimension(width, height)
            );
        }
    }

    private static class CourseListCellRenderer
        extends JPanel
        implements javax.swing.ListCellRenderer<Course>
    {

        private JLabel courseIdLabel;
        private JLabel titleLabel;
        private JLabel instructorLabel;
        private JLabel topicsLabel;

        public CourseListCellRenderer() {
            setLayout(new GridLayout(1, 4, 10, 0));
            setBorder(new EmptyBorder(10, 10, 10, 10));
            setBackground(UITheme.SURFACE);

            courseIdLabel = new JLabel();
            courseIdLabel.setFont(new Font("Georgia", Font.BOLD, 13));
            courseIdLabel.setForeground(UITheme.PRIMARY);

            titleLabel = new JLabel();
            titleLabel.setFont(UITheme.BODY_FONT);
            titleLabel.setForeground(UITheme.TEXT);

            instructorLabel = new JLabel();
            instructorLabel.setFont(UITheme.BODY_FONT);
            instructorLabel.setForeground(UITheme.MUTED_TEXT);

            topicsLabel = new JLabel();
            topicsLabel.setFont(new Font("Georgia", Font.PLAIN, 12));
            topicsLabel.setForeground(UITheme.MUTED_TEXT);
            topicsLabel.setHorizontalAlignment(JLabel.RIGHT);

            add(courseIdLabel);
            add(titleLabel);
            add(instructorLabel);
            add(topicsLabel);
        }

        @Override
        public Component getListCellRendererComponent(
            JList<? extends Course> list,
            Course course,
            int index,
            boolean isSelected,
            boolean cellHasFocus
        ) {
            if (course == null) {
                courseIdLabel.setText("");
                titleLabel.setText("");
                instructorLabel.setText("");
                topicsLabel.setText("");
            } else {
                courseIdLabel.setText(course.getCourseId());
                titleLabel.setText(course.getTitle());
                instructorLabel.setText(course.getInstructor());
                topicsLabel.setText(course.getTopics().length + " topics");
            }

            if (isSelected) {
                setBackground(UITheme.PRIMARY);
                courseIdLabel.setForeground(java.awt.Color.WHITE);
                titleLabel.setForeground(java.awt.Color.WHITE);
                instructorLabel.setForeground(java.awt.Color.WHITE);
                topicsLabel.setForeground(java.awt.Color.WHITE);
            } else {
                setBackground(UITheme.SURFACE);
                courseIdLabel.setForeground(UITheme.PRIMARY);
                titleLabel.setForeground(UITheme.TEXT);
                instructorLabel.setForeground(UITheme.MUTED_TEXT);
                topicsLabel.setForeground(UITheme.MUTED_TEXT);
            }

            return this;
        }
    }
}
