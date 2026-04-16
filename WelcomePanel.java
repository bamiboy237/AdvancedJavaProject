/*
 * Guy-robert Bogning
 * Welcome panel for local profile sign in and sign up navigation.
 */

import java.awt.*;
import javax.swing.*;

public class WelcomePanel extends JPanel {

    private JLabel titleLabel;
    private JButton signInButton;
    private JButton signUpButton;

    public WelcomePanel() {
        setLayout(new GridBagLayout());
        setBackground(UITheme.BACKGROUND);

        titleLabel = new JLabel("MiniLMS");
        titleLabel.setFont(UITheme.TITLE_FONT);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setForeground(UITheme.TEXT);

        JLabel subtitleLabel = new JLabel("Local course management");
        subtitleLabel.setFont(UITheme.BODY_FONT);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitleLabel.setForeground(UITheme.MUTED_TEXT);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 12, 0));
        buttonPanel.setOpaque(false);

        signInButton = new JButton("Sign In");
        UITheme.stylePrimaryButton(signInButton);
        

        signUpButton = new JButton("Sign Up");
        UITheme.styleSecondaryButton(signUpButton);

        buttonPanel.add(signInButton);
        buttonPanel.add(signUpButton);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);
        contentPanel.add(titleLabel);
        contentPanel.add(Box.createVerticalStrut(8));
        contentPanel.add(subtitleLabel);
        contentPanel.add(Box.createVerticalStrut(28));
        contentPanel.add(buttonPanel);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.fill = GridBagConstraints.NONE;
        add(contentPanel, constraints);
    }

    public JButton getSignInButton() {
        return signInButton;
    }

    public JButton getSignUpButton() {
        return signUpButton;
    }

    public JLabel getTitleLabel() {
        return titleLabel;
    }
}
