/*
 * Guy-robert Bogning
 * Course content panel for displaying the selected course materials.
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;

public class CourseContentPanel extends JPanel {

    private MiniLMSFrame parentFrame;
    private JLabel screenTitleLabel;
    private JLabel helperLabel;
    private JLabel courseTitleLabel;
    private JLabel instructorLabel;
    private JTextArea descriptionArea;
    private JList<String> topicsList;
    private JList<String> assignmentsList;
    private DefaultListModel<String> topicsModel;
    private DefaultListModel<String> assignmentsModel;
    private JButton backHomeButton;
    private JButton backCoursesButton;
    private Course currentCourse;

    public CourseContentPanel(MiniLMSFrame parentFrame) {
        this.parentFrame = parentFrame;
        setLayout(new BorderLayout(0, 0));
        setBackground(UITheme.BACKGROUND);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new javax.swing.BoxLayout(headerPanel, javax.swing.BoxLayout.Y_AXIS));
        headerPanel.setOpaque(false);

        screenTitleLabel = new JLabel("Course Content");
        screenTitleLabel.setFont(UITheme.SECTION_FONT);
        screenTitleLabel.setForeground(UITheme.TEXT);
        screenTitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        helperLabel = new JLabel("View the selected course title, instructor, description, topics, and assignments.");
        helperLabel.setFont(UITheme.BODY_FONT);
        helperLabel.setForeground(UITheme.MUTED_TEXT);
        helperLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        headerPanel.add(screenTitleLabel);
        headerPanel.add(new javax.swing.Box.Filler(new Dimension(0, 6), new Dimension(0, 6), new Dimension(0, 6)));
        headerPanel.add(helperLabel);

        add(headerPanel, BorderLayout.NORTH);

        JPanel bodyPanel = new JPanel(new BorderLayout(0, 16));
        bodyPanel.setOpaque(false);

        JPanel summaryPanel = new JPanel();
        summaryPanel.setLayout(new javax.swing.BoxLayout(summaryPanel, javax.swing.BoxLayout.Y_AXIS));
        summaryPanel.setOpaque(false);

        courseTitleLabel = new JLabel("No course selected");
        courseTitleLabel.setFont(new Font("Georgia", Font.BOLD, 24));
        courseTitleLabel.setForeground(UITheme.TEXT);
        courseTitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        instructorLabel = new JLabel("Select a course to view its details.");
        instructorLabel.setFont(UITheme.BODY_FONT);
        instructorLabel.setForeground(UITheme.MUTED_TEXT);
        instructorLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel descriptionLabel = new JLabel("Description");
        descriptionLabel.setFont(UITheme.BODY_FONT.deriveFont(Font.BOLD, 16f));
        descriptionLabel.setForeground(UITheme.TEXT);
        descriptionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        descriptionArea = new JTextArea();
        descriptionArea.setEditable(false);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setFont(UITheme.BODY_FONT);
        descriptionArea.setForeground(UITheme.TEXT);
        descriptionArea.setBackground(UITheme.SURFACE);
        descriptionArea.setBorder(new EmptyBorder(10, 10, 10, 10));

        JScrollPane descriptionScrollPane = new JScrollPane(descriptionArea);
        descriptionScrollPane.setBorder(BorderFactory.createLineBorder(UITheme.BORDER));
        descriptionScrollPane.setPreferredSize(new Dimension(0, 110));

        summaryPanel.add(courseTitleLabel);
        summaryPanel.add(new javax.swing.Box.Filler(new Dimension(0, 6), new Dimension(0, 6), new Dimension(0, 6)));
        summaryPanel.add(instructorLabel);
        summaryPanel.add(new javax.swing.Box.Filler(new Dimension(0, 12), new Dimension(0, 12), new Dimension(0, 12)));
        summaryPanel.add(descriptionLabel);
        summaryPanel.add(new javax.swing.Box.Filler(new Dimension(0, 8), new Dimension(0, 8), new Dimension(0, 8)));
        summaryPanel.add(descriptionScrollPane);

        JPanel materialsPanel = new JPanel(new GridLayout(1, 2, 16, 0));
        materialsPanel.setOpaque(false);

        JPanel topicsPanel = new JPanel(new BorderLayout(0, 10));
        topicsPanel.setOpaque(false);

        JLabel topicsLabel = new JLabel("Topics");
        topicsLabel.setFont(UITheme.BODY_FONT.deriveFont(Font.BOLD, 16f));
        topicsLabel.setForeground(UITheme.TEXT);

        topicsModel = new DefaultListModel<>();
        topicsList = new JList<>(topicsModel);
        topicsList.setFont(UITheme.BODY_FONT);
        topicsList.setForeground(UITheme.TEXT);
        topicsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane topicsScrollPane = new JScrollPane(topicsList);
        topicsScrollPane.setBorder(BorderFactory.createLineBorder(UITheme.BORDER));
        topicsScrollPane.getViewport().setBackground(UITheme.SURFACE);

        topicsPanel.add(topicsLabel, BorderLayout.NORTH);
        topicsPanel.add(topicsScrollPane, BorderLayout.CENTER);

        JPanel assignmentsPanel = new JPanel(new BorderLayout(0, 10));
        assignmentsPanel.setOpaque(false);

        JLabel assignmentsLabel = new JLabel("Assignments");
        assignmentsLabel.setFont(UITheme.BODY_FONT.deriveFont(Font.BOLD, 16f));
        assignmentsLabel.setForeground(UITheme.TEXT);

        assignmentsModel = new DefaultListModel<>();
        assignmentsList = new JList<>(assignmentsModel);
        assignmentsList.setFont(UITheme.BODY_FONT);
        assignmentsList.setForeground(UITheme.TEXT);
        assignmentsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane assignmentsScrollPane = new JScrollPane(assignmentsList);
        assignmentsScrollPane.setBorder(BorderFactory.createLineBorder(UITheme.BORDER));
        assignmentsScrollPane.getViewport().setBackground(UITheme.SURFACE);

        assignmentsPanel.add(assignmentsLabel, BorderLayout.NORTH);
        assignmentsPanel.add(assignmentsScrollPane, BorderLayout.CENTER);

        materialsPanel.add(topicsPanel);
        materialsPanel.add(assignmentsPanel);

        bodyPanel.add(summaryPanel, BorderLayout.NORTH);
        bodyPanel.add(materialsPanel, BorderLayout.CENTER);

        add(bodyPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        buttonPanel.setOpaque(false);

        backHomeButton = new JButton("Back to Home");
        UITheme.stylePrimaryButton(backHomeButton);
        backHomeButton.addActionListener(e -> parentFrame.showCard("home"));

        backCoursesButton = new JButton("Course Management");
        UITheme.styleSecondaryButton(backCoursesButton);
        backCoursesButton.addActionListener(e -> parentFrame.showCard("courseManagement"));

        buttonPanel.add(backHomeButton);
        buttonPanel.add(new javax.swing.Box.Filler(new Dimension(10, 0), new Dimension(10, 0), new Dimension(10, 0)));
        buttonPanel.add(backCoursesButton);

        add(buttonPanel, BorderLayout.SOUTH);

        refreshDisplay();
    }

    public void displayCourse(Course course) {
        currentCourse = course;
        refreshDisplay();
    }

    public void refreshDisplay() {
        topicsModel.clear();
        assignmentsModel.clear();

        if (currentCourse == null) {
            courseTitleLabel.setText("No course selected");
            instructorLabel.setText("Select a course to view its details.");
            descriptionArea.setText("Select a course from Home or Course Management.");
            return;
        }

        courseTitleLabel.setText(currentCourse.getTitle());
        instructorLabel.setText("Instructor: " + currentCourse.getInstructor());
        descriptionArea.setText(currentCourse.getDescription());
        descriptionArea.setCaretPosition(0);

        for (String topic : currentCourse.getTopics()) {
            topicsModel.addElement(topic);
        }

        for (String assignment : currentCourse.getAssignments()) {
            assignmentsModel.addElement(assignment);
        }
    }

    public JButton getBackHomeButton() {
        return backHomeButton;
    }

    public JButton getBackCoursesButton() {
        return backCoursesButton;
    }
}
