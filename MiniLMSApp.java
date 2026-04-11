/*
 * Guy-robert Bogning
 * Application entry point for launching the Mini LMS Swing interface.
 */

import javax.swing.SwingUtilities;

public final class MiniLMSApp {
    private MiniLMSApp() {
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MiniLMSFrame frame = new MiniLMSFrame();
            frame.setVisible(true);
        });
    }
}
