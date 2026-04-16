/*
 * Guy-robert Bogning
 * Shared minimal Swing theme helpers.
 */

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

public final class UITheme {
    public static final Color BACKGROUND = Color.WHITE;
    public static final Color SURFACE = Color.WHITE;
    public static final Color TEXT = new Color(34, 34, 34);
    public static final Color MUTED_TEXT = new Color(93, 93, 93);
    public static final Color BORDER = new Color(226, 226, 226);
    public static final Color PRIMARY = new Color(56, 63, 81);
    public static final Color PRIMARY_HOVER = new Color(44, 50, 66);
    public static final Color SECONDARY = new Color(255, 255, 255);

    public static final Font TITLE_FONT = new Font("Georgia", Font.BOLD, 28);
    public static final Font SECTION_FONT = new Font("Georgia", Font.BOLD, 22);
    public static final Font BODY_FONT = new Font("Georgia", Font.PLAIN, 15);
    public static final Font BUTTON_FONT = new Font("Georgia", Font.BOLD, 14);

    private UITheme() {
    }

    public static Border panelPadding(int top, int left, int bottom, int right) {
        return new EmptyBorder(top, left, bottom, right);
    }

    public static void stylePrimaryButton(JButton button) {
        styleButton(button, PRIMARY, Color.WHITE);
    }

    public static void styleSecondaryButton(JButton button) {
        styleButton(button, SECONDARY, TEXT);
        button.setBorder(BorderFactory.createLineBorder(BORDER));
    }

    public static void styleTextField(JTextField field, int columns) {
        field.setColumns(columns);
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        field.setFont(BODY_FONT);
        field.setForeground(TEXT);
        field.setBackground(SURFACE);
        field.setCaretColor(TEXT);
        field.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
            )
        );
    }

    private static void styleButton(JButton button, Color background, Color foreground) {
        button.setFont(BUTTON_FONT);
        button.setBackground(background);
        button.setForeground(foreground);
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setBorder(BorderFactory.createEmptyBorder(10, 18, 10, 18));
        button.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    }
}
