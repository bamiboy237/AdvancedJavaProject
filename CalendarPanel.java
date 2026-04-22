/*
 * Guy-robert Bogning
 * Calendar panel for upcoming event display.
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

public class CalendarPanel extends JPanel {

    private enum FilterMode {
        THIS_WEEK,
        THIS_MONTH,
        ALL,
    }

    private final MiniLMSFrame parentFrame;
    private final ICSParser icsParser;
    private final Map<LocalDate, List<CalendarEvent>> eventsByDate;
    private final DefaultListModel<CalendarEvent> sidebarModel;
    private final JList<CalendarEvent> sidebarList;
    private final JPanel gridPanel;
    private final JLabel monthLabel;
    private final JLabel statusLabel;
    private final JToggleButton weekFilterButton;
    private final JToggleButton monthFilterButton;
    private final JToggleButton allFilterButton;
    private final JButton prevButton;
    private final JButton nextButton;
    private final JButton todayButton;
    private final JButton importButton;
    private final JButton backButton;

    private YearMonth visibleMonth;
    private LocalDate selectedDate;
    private FilterMode filterMode;

    public CalendarPanel(MiniLMSFrame parentFrame) {
        this.parentFrame = parentFrame;
        this.icsParser = new ICSParser();
        this.eventsByDate = new HashMap<>();
        this.sidebarModel = new DefaultListModel<>();
        this.sidebarList = new JList<>(sidebarModel);
        this.gridPanel = new JPanel();
        this.monthLabel = new JLabel();
        this.statusLabel = new JLabel(
            "Import an .ics file or enroll in a course to view deadlines."
        );
        this.weekFilterButton = new JToggleButton("This Week");
        this.monthFilterButton = new JToggleButton("This Month");
        this.allFilterButton = new JToggleButton("All");
        this.prevButton = new JButton("‹");
        this.nextButton = new JButton("›");
        this.todayButton = new JButton("Today");
        this.importButton = new JButton("Import .ics");
        this.backButton = new JButton("Back");

        this.visibleMonth = YearMonth.now();
        this.selectedDate = LocalDate.now();
        this.filterMode = FilterMode.THIS_MONTH;

        setLayout(new BorderLayout(14, 14));
        setBackground(UITheme.BACKGROUND);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel leftPanel = new JPanel(new BorderLayout(0, 12));
        leftPanel.setOpaque(false);
        leftPanel.setPreferredSize(new Dimension(820, 0));
        leftPanel.add(buildHeader(), BorderLayout.NORTH);
        leftPanel.add(buildCalendarGridShell(), BorderLayout.CENTER);

        JPanel rightPanel = new JPanel(new BorderLayout(0, 12));
        rightPanel.setOpaque(false);
        rightPanel.setPreferredSize(new Dimension(340, 0));
        rightPanel.add(buildSidebarHeader(), BorderLayout.NORTH);
        rightPanel.add(buildSidebarList(), BorderLayout.CENTER);

        JPanel split = new JPanel(new BorderLayout(14, 0));
        split.setOpaque(false);
        split.add(leftPanel, BorderLayout.CENTER);
        split.add(rightPanel, BorderLayout.EAST);

        add(split, BorderLayout.CENTER);

        styleControls();
        bindActions();
        rebuildCalendar();
        refreshSidebar();
    }

    public void refreshEvents() {
        StudentProfile profile = parentFrame.getCurrentProfile();
        eventsByDate.clear();

        if (profile == null || profile.getRecord() == null) {
            sidebarModel.clear();
            gridPanel.removeAll();
            gridPanel.revalidate();
            gridPanel.repaint();
            statusLabel.setText(
                "Sign in to view course and calendar deadlines."
            );
            return;
        }

        ArrayList<CalendarEvent> events = profile
            .getRecord()
            .getCalendarEvents();
        for (CalendarEvent event : events) {
            LocalDate date = toLocalDate(event.getSortDateTime());
            if (date == null) {
                continue;
            }
            eventsByDate
                .computeIfAbsent(date, this::createEventList)
                .add(event);
        }

        for (List<CalendarEvent> list : eventsByDate.values()) {
            Collections.sort(list);
        }

        if (events.isEmpty()) {
            statusLabel.setText("No course or calendar deadlines yet.");
        } else {
            statusLabel.setText(
                "Showing " + events.size() + " course and calendar event(s)."
            );
        }

        if (!events.isEmpty() && !eventsByDate.containsKey(selectedDate)) {
            selectedDate = eventsByDate
                .keySet()
                .stream()
                .sorted()
                .findFirst()
                .orElse(LocalDate.now());
        }

        rebuildCalendar();
        refreshSidebar();
    }

    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout(10, 10));
        header.setOpaque(false);

        JLabel titleLabel = new JLabel("Calendar");
        titleLabel.setFont(UITheme.SECTION_FONT);
        titleLabel.setForeground(UITheme.TEXT);

        statusLabel.setFont(UITheme.BODY_FONT.deriveFont(Font.PLAIN, 14f));
        statusLabel.setForeground(UITheme.MUTED_TEXT);

        JPanel topLine = new JPanel(new BorderLayout());
        topLine.setOpaque(false);
        topLine.add(titleLabel, BorderLayout.WEST);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        actions.setOpaque(false);
        actions.add(backButton);
        actions.add(importButton);

        topLine.add(actions, BorderLayout.EAST);

        header.add(topLine, BorderLayout.NORTH);
        header.add(statusLabel, BorderLayout.SOUTH);
        return header;
    }

    private JPanel buildCalendarGridShell() {
        JPanel shell = new JPanel(new BorderLayout(0, 10));
        shell.setOpaque(false);

        JPanel nav = new JPanel(new BorderLayout(8, 0));
        nav.setOpaque(false);

        JPanel navLeft = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        navLeft.setOpaque(false);
        navLeft.add(prevButton);
        navLeft.add(Box.createHorizontalStrut(8));
        navLeft.add(todayButton);
        navLeft.add(Box.createHorizontalStrut(8));
        navLeft.add(nextButton);

        monthLabel.setFont(UITheme.SECTION_FONT);
        monthLabel.setHorizontalAlignment(SwingConstants.CENTER);
        monthLabel.setForeground(UITheme.TEXT);

        nav.add(navLeft, BorderLayout.WEST);
        nav.add(monthLabel, BorderLayout.CENTER);

        gridPanel.setOpaque(false);
        gridPanel.setLayout(new GridLayout(0, 7, 8, 8));

        shell.add(nav, BorderLayout.NORTH);
        shell.add(gridPanel, BorderLayout.CENTER);
        return shell;
    }

    private JPanel buildSidebarHeader() {
        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));

        JLabel heading = new JLabel("Upcoming Deadlines");
        heading.setFont(UITheme.SECTION_FONT);
        heading.setForeground(UITheme.TEXT);

        JPanel filters = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        filters.setOpaque(false);
        filters.add(weekFilterButton);
        filters.add(monthFilterButton);
        filters.add(allFilterButton);

        header.add(heading);
        header.add(Box.createVerticalStrut(8));
        header.add(filters);
        return header;
    }

    private JScrollPane buildSidebarList() {
        sidebarList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        sidebarList.setCellRenderer(new DeadlineCardRenderer());
        sidebarList.addListSelectionListener(
            this::handleSidebarSelectionChanged
        );

        JScrollPane scrollPane = new JScrollPane(sidebarList);
        scrollPane.setBorder(BorderFactory.createLineBorder(UITheme.BORDER));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        return scrollPane;
    }

    private void styleControls() {
        UITheme.styleSecondaryButton(prevButton);
        UITheme.styleSecondaryButton(nextButton);
        UITheme.styleSecondaryButton(todayButton);
        UITheme.styleSecondaryButton(backButton);
        UITheme.stylePrimaryButton(importButton);
        styleToggleButton(weekFilterButton);
        styleToggleButton(monthFilterButton);
        styleToggleButton(allFilterButton);

        weekFilterButton.setSelected(false);
        monthFilterButton.setSelected(true);
        allFilterButton.setSelected(false);
        applyFilterButtonStyles();

        prevButton.setPreferredSize(new Dimension(42, 42));
        nextButton.setPreferredSize(new Dimension(42, 42));

        monthLabel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    }

    private void bindActions() {
        prevButton.addActionListener(this::handlePreviousMonth);
        nextButton.addActionListener(this::handleNextMonth);
        todayButton.addActionListener(this::handleToday);
        importButton.addActionListener(this::handleImportICS);
        backButton.addActionListener(this::handleBackToHome);
        weekFilterButton.addActionListener(this::handleWeekFilter);
        monthFilterButton.addActionListener(this::handleMonthFilter);
        allFilterButton.addActionListener(this::handleAllFilter);

        monthLabel.addMouseListener(
            new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 1) {
                        visibleMonth = YearMonth.now();
                        rebuildCalendar();
                    }
                }
            }
        );
    }

    private void setFilterMode(FilterMode mode) {
        filterMode = mode;
        weekFilterButton.setSelected(mode == FilterMode.THIS_WEEK);
        monthFilterButton.setSelected(mode == FilterMode.THIS_MONTH);
        allFilterButton.setSelected(mode == FilterMode.ALL);
        applyFilterButtonStyles();
        refreshSidebar();
    }

    private void applyFilterButtonStyles() {
        weekFilterButton.setBackground(
            weekFilterButton.isSelected() ? UITheme.PRIMARY : UITheme.SECONDARY
        );
        weekFilterButton.setForeground(
            weekFilterButton.isSelected() ? Color.WHITE : UITheme.TEXT
        );
        monthFilterButton.setBackground(
            monthFilterButton.isSelected() ? UITheme.PRIMARY : UITheme.SECONDARY
        );
        monthFilterButton.setForeground(
            monthFilterButton.isSelected() ? Color.WHITE : UITheme.TEXT
        );
        allFilterButton.setBackground(
            allFilterButton.isSelected() ? UITheme.PRIMARY : UITheme.SECONDARY
        );
        allFilterButton.setForeground(
            allFilterButton.isSelected() ? Color.WHITE : UITheme.TEXT
        );
    }

    private void styleToggleButton(JToggleButton button) {
        button.setFont(UITheme.BUTTON_FONT);
        button.setBackground(UITheme.SECONDARY);
        button.setForeground(UITheme.TEXT);
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setBorder(BorderFactory.createLineBorder(UITheme.BORDER));
        button.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    }

    private void rebuildCalendar() {
        monthLabel.setText(formatMonthLabel(visibleMonth));
        gridPanel.removeAll();

        for (DayOfWeek dayOfWeek : DayOfWeek.values()) {
            JLabel dayHeader = new JLabel(
                dayName(dayOfWeek),
                SwingConstants.CENTER
            );
            dayHeader.setOpaque(true);
            dayHeader.setBackground(UITheme.SURFACE);
            dayHeader.setForeground(UITheme.MUTED_TEXT);
            dayHeader.setFont(UITheme.BODY_FONT.deriveFont(Font.BOLD, 12f));
            dayHeader.setBorder(
                BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(UITheme.BORDER),
                    new EmptyBorder(8, 4, 8, 4)
                )
            );
            gridPanel.add(dayHeader);
        }

        LocalDate firstOfMonth = visibleMonth.atDay(1);
        int startOffset = firstOfMonth.getDayOfWeek().getValue() % 7;
        LocalDate cursor = firstOfMonth.minusDays(startOffset);

        for (int i = 0; i < 42; i++) {
            gridPanel.add(createDayCell(cursor));
            cursor = cursor.plusDays(1);
        }

        gridPanel.revalidate();
        gridPanel.repaint();
    }

    private JPanel createDayCell(LocalDate date) {
        JPanel cell = new JPanel(new BorderLayout(0, 4));
        cell.setPreferredSize(new Dimension(100, 88));
        cell.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                    date.equals(selectedDate)
                        ? UITheme.PRIMARY
                        : UITheme.BORDER,
                    date.equals(selectedDate) ? 2 : 1
                ),
                new EmptyBorder(6, 6, 6, 6)
            )
        );
        cell.setBackground(
            date.getMonth().equals(visibleMonth.getMonth())
                ? UITheme.SURFACE
                : new Color(248, 248, 248)
        );
        cell.setOpaque(true);

        if (date.equals(LocalDate.now())) {
            cell.setBackground(new Color(237, 248, 246));
        }

        JLabel dayNumber = new JLabel(String.valueOf(date.getDayOfMonth()));
        dayNumber.setFont(UITheme.BODY_FONT.deriveFont(Font.BOLD, 14f));
        dayNumber.setForeground(
            date.equals(LocalDate.now()) ? new Color(0, 124, 128) : UITheme.TEXT
        );

        JPanel topRow = new JPanel(new BorderLayout());
        topRow.setOpaque(false);
        topRow.add(dayNumber, BorderLayout.WEST);

        JPanel dotsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        dotsPanel.setOpaque(false);
        List<CalendarEvent> events = eventsByDate.getOrDefault(
            date,
            Collections.emptyList()
        );
        for (CalendarEvent event : events) {
            JLabel dot = new JLabel(" ");
            dot.setOpaque(true);
            dot.setPreferredSize(new Dimension(10, 10));
            dot.setBackground(urgencyColor(event));
            dot.setToolTipText(event.getTitle());
            dotsPanel.add(dot);
        }

        cell.add(topRow, BorderLayout.NORTH);
        cell.add(dotsPanel, BorderLayout.SOUTH);

        cell.setToolTipText(buildTooltip(date, events));
        cell.addMouseListener(
            new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    selectedDate = date;
                    rebuildCalendar();
                    showDayPopup(date);
                    refreshSidebar();
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    cell.setBorder(
                        BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(
                                UITheme.PRIMARY,
                                date.equals(selectedDate) ? 2 : 1
                            ),
                            new EmptyBorder(6, 6, 6, 6)
                        )
                    );
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    cell.setBorder(
                        BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(
                                date.equals(selectedDate)
                                    ? UITheme.PRIMARY
                                    : UITheme.BORDER,
                                date.equals(selectedDate) ? 2 : 1
                            ),
                            new EmptyBorder(6, 6, 6, 6)
                        )
                    );
                }
            }
        );

        return cell;
    }

    private void refreshSidebar() {
        sidebarModel.clear();
        List<CalendarEvent> filtered = collectFilteredEvents();
        for (CalendarEvent event : filtered) {
            sidebarModel.addElement(event);
        }
    }

    private List<CalendarEvent> collectFilteredEvents() {
        List<CalendarEvent> allEvents = eventsByDate
            .entrySet()
            .stream()
            .flatMap(entry -> entry.getValue().stream())
            .sorted()
            .collect(Collectors.toList());

        LocalDate now = LocalDate.now();
        switch (filterMode) {
            case THIS_WEEK:
                LocalDate startOfWeek = now.minusDays(
                    now.getDayOfWeek().getValue() % 7
                );
                LocalDate endOfWeek = startOfWeek.plusDays(6);
                return allEvents
                    .stream()
                    .filter(event -> {
                        LocalDate eventDate = toLocalDate(
                            event.getSortDateTime()
                        );
                        return (
                            eventDate != null &&
                            !eventDate.isBefore(startOfWeek) &&
                            !eventDate.isAfter(endOfWeek)
                        );
                    })
                    .collect(Collectors.toList());
            case THIS_MONTH:
                YearMonth month = visibleMonth;
                return allEvents
                    .stream()
                    .filter(event ->
                        month.equals(
                            YearMonth.from(toLocalDate(event.getSortDateTime()))
                        )
                    )
                    .collect(Collectors.toList());
            case ALL:
            default:
                return allEvents;
        }
    }

    private void showDayPopup(LocalDate date) {
        List<CalendarEvent> events = eventsByDate.getOrDefault(
            date,
            Collections.emptyList()
        );
        if (events.isEmpty()) {
            JOptionPane.showMessageDialog(
                this,
                "No deadlines for " + date,
                "Calendar",
                JOptionPane.INFORMATION_MESSAGE
            );
            return;
        }

        JDialog dialog = new JDialog(parentFrame, date.toString(), true);
        dialog.setSize(420, 300);
        dialog.setLocationRelativeTo(this);

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(new EmptyBorder(16, 16, 16, 16));
        content.setBackground(UITheme.BACKGROUND);

        for (CalendarEvent event : events) {
            JLabel title = new JLabel(event.getTitle());
            title.setFont(UITheme.BODY_FONT.deriveFont(Font.BOLD, 14f));
            title.setForeground(UITheme.TEXT);

            JLabel time = new JLabel(formatEventTime(event));
            time.setFont(UITheme.BODY_FONT.deriveFont(Font.PLAIN, 12f));
            time.setForeground(UITheme.MUTED_TEXT);

            JPanel row = new JPanel(new BorderLayout());
            row.setOpaque(false);
            row.add(title, BorderLayout.NORTH);
            row.add(time, BorderLayout.SOUTH);
            row.setBorder(new EmptyBorder(0, 0, 12, 0));
            content.add(row);
        }

        dialog.add(new JScrollPane(content), BorderLayout.CENTER);
        dialog.setVisible(true);
    }

    private void handleImportICS(ActionEvent event) {
        StudentProfile profile = parentFrame.getCurrentProfile();
        if (profile == null || profile.getRecord() == null) {
            JOptionPane.showMessageDialog(
                this,
                "Please sign in first.",
                "Import ICS",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(
            new FileNameExtensionFilter("ICS Files", "ics")
        );

        if (fileChooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File selectedFile = fileChooser.getSelectedFile();
        try {
            ArrayList<CalendarEvent> events = icsParser.parse(selectedFile);
            if (events.isEmpty()) {
                JOptionPane.showMessageDialog(
                    this,
                    "No events found in the selected .ics file.",
                    "Import ICS",
                    JOptionPane.INFORMATION_MESSAGE
                );
                return;
            }

            profile.getRecord().setCalendarEvents(events);
            parentFrame.saveCurrentProfile();
            refreshEvents();

            JOptionPane.showMessageDialog(
                this,
                "Imported " + events.size() + " event(s).",
                "Import ICS",
                JOptionPane.INFORMATION_MESSAGE
            );
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                this,
                "Failed to import ICS file: " + ex.getMessage(),
                "Import ICS",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private ArrayList<CalendarEvent> createEventList(LocalDate date) {
        return new ArrayList<>();
    }

    private void handleSidebarSelectionChanged(
        javax.swing.event.ListSelectionEvent event
    ) {
        if (!event.getValueIsAdjusting()) {
            CalendarEvent selected = sidebarList.getSelectedValue();
            if (selected != null) {
                selectedDate = toLocalDate(selected.getSortDateTime());
                rebuildCalendar();
                showDayPopup(selectedDate);
            }
        }
    }

    private void handlePreviousMonth(ActionEvent event) {
        visibleMonth = visibleMonth.minusMonths(1);
        rebuildCalendar();
    }

    private void handleNextMonth(ActionEvent event) {
        visibleMonth = visibleMonth.plusMonths(1);
        rebuildCalendar();
    }

    private void handleToday(ActionEvent event) {
        visibleMonth = YearMonth.now();
        selectedDate = LocalDate.now();
        rebuildCalendar();
        refreshSidebar();
    }

    private void handleBackToHome(ActionEvent event) {
        parentFrame.showCard("home");
    }

    private void handleWeekFilter(ActionEvent event) {
        setFilterMode(FilterMode.THIS_WEEK);
    }

    private void handleMonthFilter(ActionEvent event) {
        setFilterMode(FilterMode.THIS_MONTH);
    }

    private void handleAllFilter(ActionEvent event) {
        setFilterMode(FilterMode.ALL);
    }

    private String buildTooltip(LocalDate date, List<CalendarEvent> events) {
        if (events.isEmpty()) {
            return date.toString();
        }
        return date + "\n" + events.get(0).getTitle();
    }

    private String formatMonthLabel(YearMonth month) {
        String monthName = month
            .getMonth()
            .getDisplayName(TextStyle.FULL, Locale.getDefault());
        return monthName + " " + month.getYear();
    }

    private String dayName(DayOfWeek dayOfWeek) {
        return dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault());
    }

    private Color urgencyColor(CalendarEvent event) {
        LocalDate due = toLocalDate(event.getSortDateTime());
        if (due == null) {
            return new Color(140, 140, 140);
        }

        LocalDate now = LocalDate.now();
        if (due.isBefore(now)) {
            return new Color(214, 69, 65);
        }
        if (!due.isAfter(now.plusDays(7))) {
            return new Color(230, 140, 37);
        }
        return new Color(68, 160, 90);
    }

    private String formatEventTime(CalendarEvent event) {
        if (event.getSortDateTime() == null) {
            return "All day";
        }
        return event.getSortDateTime().toLocalTime().toString();
    }

    private LocalDate toLocalDate(java.time.LocalDateTime dateTime) {
        return dateTime == null ? null : dateTime.toLocalDate();
    }

    private final class DeadlineCardRenderer extends DefaultListCellRenderer {

        @Override
        public java.awt.Component getListCellRendererComponent(
            JList<?> list,
            Object value,
            int index,
            boolean isSelected,
            boolean cellHasFocus
        ) {
            CalendarEvent event = (CalendarEvent) value;
            JPanel card = new JPanel(new BorderLayout(10, 0));
            card.setBorder(new EmptyBorder(10, 10, 10, 10));
            card.setBackground(
                isSelected ? new Color(242, 245, 250) : UITheme.SURFACE
            );
            card.setOpaque(true);

            JLabel dot = new JLabel(" ");
            dot.setOpaque(true);
            dot.setPreferredSize(new Dimension(10, 10));
            dot.setBackground(urgencyColor(event));

            JPanel left = new JPanel(new BorderLayout());
            left.setOpaque(false);
            left.add(dot, BorderLayout.NORTH);

            JLabel title = new JLabel(event.getTitle());
            title.setFont(UITheme.BODY_FONT.deriveFont(Font.BOLD, 14f));
            title.setForeground(UITheme.TEXT);

            JLabel date = new JLabel(formatSidebarDate(event));
            date.setFont(UITheme.BODY_FONT.deriveFont(Font.PLAIN, 12f));
            date.setForeground(UITheme.MUTED_TEXT);

            JPanel center = new JPanel();
            center.setOpaque(false);
            center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
            center.add(title);
            center.add(date);

            card.add(left, BorderLayout.WEST);
            card.add(center, BorderLayout.CENTER);

            return card;
        }

        private String formatSidebarDate(CalendarEvent event) {
            LocalDate date = toLocalDate(event.getSortDateTime());
            if (date == null) {
                return "";
            }
            return (
                date
                    .getMonth()
                    .getDisplayName(TextStyle.SHORT, Locale.getDefault()) +
                " " +
                date.getDayOfMonth()
            );
        }
    }
}
