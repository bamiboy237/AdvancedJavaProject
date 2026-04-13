/*
 * Guy-robert Bogning
 * Welcome panel for local profile sign in and sign up navigation.
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class WelcomePanel extends JPanel {
    private JLabel welcomeLabel;
    private JButton signInButton;
    private JButton signUpButton;
    private JPanel buttonPanel;

    public WelcomePanel() {
        setLayout(new BorderLayout());
        
        welcomeLabel = new JLabel("MiniLMS: Your Local Course Management System");
        add(welcomeLabel, BorderLayout.TOP);
        
        
        buttonPanel = new JPanel();
        
        signInButton = new JButton("Sign In");
        signUpButton = new JButton("Sign Up");
        

    }
}
