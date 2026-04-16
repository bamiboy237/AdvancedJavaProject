/*
 * Guy-robert Bogning
 * Profile selection panel for choosing an existing student profile.
 */

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class ProfileSelectionPanel extends JPanel {

    private JLabel screenTitleLabel;
    private JList<StudentProfile> profileList;
    private DefaultListModel<StudentProfile> listModel;
    private JButton selectButton;
    private JButton backButton;
    private JLabel statusLabel;

    public ProfileSelectionPanel() {
        setLayout(new GridBagLayout());
        setBackground(UITheme.BACKGROUND);

        screenTitleLabel = new JLabel("Select a Profile");
        screenTitleLabel.setFont(UITheme.SECTION_FONT);
        screenTitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        screenTitleLabel.setForeground(UITheme.TEXT);

        JLabel helperLabel = new JLabel("Choose an existing local profile");
        helperLabel.setFont(UITheme.BODY_FONT);
        helperLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        helperLabel.setForeground(UITheme.MUTED_TEXT);

        listModel = new DefaultListModel<>();
        profileList = new JList<>(listModel);
        profileList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        profileList.setFont(UITheme.BODY_FONT);
        profileList.setForeground(UITheme.TEXT);
        profileList.setBackground(UITheme.SURFACE);
        profileList.setSelectionBackground(UITheme.PRIMARY);
        profileList.setSelectionForeground(Color.WHITE);
        profileList.setCellRenderer(
            (list, value, index, isSelected, cellHasFocus) -> {
                JLabel label = new JLabel(
                    value == null ? "" : value.getStudentName()
                );
                label.setOpaque(true);
                label.setFont(UITheme.BODY_FONT);
                label.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));
                if (isSelected) {
                    label.setBackground(UITheme.PRIMARY);
                    label.setForeground(Color.WHITE);
                } else {
                    label.setBackground(UITheme.SURFACE);
                    label.setForeground(UITheme.TEXT);
                }
                return label;
            }
        );

        JScrollPane scrollPane = new JScrollPane(profileList);
        scrollPane.setBorder(BorderFactory.createLineBorder(UITheme.BORDER, 1));
        scrollPane.setPreferredSize(new Dimension(420, 240));
        scrollPane.setAlignmentX(Component.CENTER_ALIGNMENT);
        scrollPane.setMaximumSize(new Dimension(420, 240));

        selectButton = new JButton("Open Profile");
        UITheme.stylePrimaryButton(selectButton);
        selectButton.setEnabled(false);

        backButton = new JButton("Back");
        UITheme.styleSecondaryButton(backButton);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 12, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.add(selectButton);
        buttonPanel.add(backButton);
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        statusLabel = new JLabel("");
        statusLabel.setFont(UITheme.BODY_FONT.deriveFont(Font.PLAIN, 12f));
        statusLabel.setForeground(UITheme.MUTED_TEXT);
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        profileList.addListSelectionListener(e -> {
            selectButton.setEnabled(profileList.getSelectedIndex() >= 0);
        });

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);
        contentPanel.add(screenTitleLabel);
        contentPanel.add(Box.createVerticalStrut(8));
        contentPanel.add(helperLabel);
        contentPanel.add(Box.createVerticalStrut(20));
        contentPanel.add(scrollPane);
        contentPanel.add(Box.createVerticalStrut(16));
        contentPanel.add(statusLabel);
        contentPanel.add(Box.createVerticalStrut(20));
        contentPanel.add(buttonPanel);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.CENTER;
        add(contentPanel, constraints);
    }

    public void refreshProfiles(ProfileIndex index) {
        listModel.clear();
        if (
            index == null ||
            index.getProfiles() == null ||
            index.getProfiles().isEmpty()
        ) {
            statusLabel.setText("No profiles found. Please sign up first.");
            return;
        }
        statusLabel.setText(
            index.getProfiles().size() + " profile(s) available"
        );
        for (StudentProfile profile : index.getProfiles()) {
            listModel.addElement(profile);
        }
    }

    public StudentProfile getSelectedProfile() {
        int selectedIndex = profileList.getSelectedIndex();
        if (selectedIndex >= 0) {
            return listModel.get(selectedIndex);
        }
        return null;
    }

    public int getSelectedIndex() {
        return profileList.getSelectedIndex();
    }

    public JButton getSelectButton() {
        return selectButton;
    }

    public JButton getBackButton() {
        return backButton;
    }
}
