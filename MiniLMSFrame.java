/*
 * Guy-robert Bogning
 * Main application frame for profile navigation, LMS screens, and shared window controls.
 */

import java.awt.BorderLayout;
import java.awt.CardLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class MiniLMSFrame extends JFrame {
    private static final int WIDTH = 1200;
    private static final int HEIGHT = 800;

    private final CardLayout cardLayout;
    private final JPanel centerPanel;

    public MiniLMSFrame() {
        super("MiniLMS");

        cardLayout = new CardLayout();
        centerPanel = new JPanel(cardLayout);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        add(centerPanel, BorderLayout.CENTER);
    }

    public JPanel getCenterPanel() {
        return centerPanel;
    }

    public CardLayout getCardLayout() {
        return cardLayout;
    }
}
