/*
 * Guy-robert Bogning
 * Profile creation panel for local student sign up.
 */

import java.awt.*;
import javax.swing.*;

public class ProfileCreationPanel extends JPanel {

    private JLabel screenTitleLabel;
    private JLabel studentFirstNameLabel;
    private JTextField studentFirstNameField;
    private JLabel studentLastNameLabel;
    private JTextField studentLastNameField;
    private JButton createProfileButton;
    private JButton backButton;
    private JLabel statusLabel;

    public ProfileCreationPanel() {
        setLayout(new GridBagLayout());
        setBackground(UITheme.BACKGROUND);

        screenTitleLabel = new JLabel("Create Profile");
        screenTitleLabel.setFont(UITheme.SECTION_FONT);
        screenTitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        screenTitleLabel.setForeground(UITheme.TEXT);

        JLabel helperLabel = new JLabel(
            "Enter a first and last name to continue"
        );
        helperLabel.setFont(UITheme.BODY_FONT);
        helperLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        helperLabel.setForeground(UITheme.MUTED_TEXT);

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setOpaque(false);

        studentFirstNameLabel = new JLabel("First Name:");
        studentFirstNameLabel.setFont(UITheme.BODY_FONT);
        studentFirstNameLabel.setForeground(UITheme.TEXT);
        studentFirstNameField = new JTextField();
        UITheme.styleTextField(studentFirstNameField, 24);

        formPanel.add(studentFirstNameLabel);
        formPanel.add(Box.createVerticalStrut(8));
        formPanel.add(studentFirstNameField);

        formPanel.add(Box.createVerticalStrut(16));

        studentLastNameLabel = new JLabel("Last Name:");
        studentLastNameLabel.setFont(UITheme.BODY_FONT);
        studentLastNameLabel.setForeground(UITheme.TEXT);
        studentLastNameField = new JTextField();
        UITheme.styleTextField(studentLastNameField, 24);

        formPanel.add(studentLastNameLabel);
        formPanel.add(Box.createVerticalStrut(8));
        formPanel.add(studentLastNameField);

        createProfileButton = new JButton("Create Profile");
        UITheme.stylePrimaryButton(createProfileButton);

        backButton = new JButton("Back");
        UITheme.styleSecondaryButton(backButton);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 12, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.add(createProfileButton);
        buttonPanel.add(backButton);

        statusLabel = new JLabel(" ");
        statusLabel.setFont(UITheme.BODY_FONT.deriveFont(Font.PLAIN, 12f));
        statusLabel.setForeground(new Color(160, 60, 60));
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);
        contentPanel.add(screenTitleLabel);
        contentPanel.add(Box.createVerticalStrut(8));
        contentPanel.add(helperLabel);
        contentPanel.add(Box.createVerticalStrut(24));
        contentPanel.add(formPanel);
        contentPanel.add(Box.createVerticalStrut(24));
        contentPanel.add(statusLabel);
        contentPanel.add(Box.createVerticalStrut(16));
        contentPanel.add(buttonPanel);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.CENTER;
        add(contentPanel, constraints);
    }

    public String getStudentFirstName() {
        return studentFirstNameField.getText().trim();
    }

    public String getStudentLastName() {
        return studentLastNameField.getText().trim();
    }

    public String getStudentName() {
        String firstName = getStudentFirstName();
        String lastName = getStudentLastName();
        return (firstName + " " + lastName).trim();
    }

    public void clearFields() {
        studentFirstNameField.setText("");
        studentLastNameField.setText("");
        clearStatusMessage();
    }

    public void setStatusMessage(String message) {
        statusLabel.setText(message == null || message.trim().isEmpty() ? " " : message.trim());
    }

    public void clearStatusMessage() {
        statusLabel.setText(" ");
    }

    public JButton getCreateProfileButton() {
        return createProfileButton;
    }

    public JButton getBackButton() {
        return backButton;
    }
}
