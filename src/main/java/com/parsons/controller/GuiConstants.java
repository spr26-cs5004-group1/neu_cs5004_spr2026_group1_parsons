package com.parsons.controller;

import javax.swing.*;
import java.awt.*;

public class GuiConstants {
    /* Shared Constants
     derived based on the two panel drag and drop
     each panel is 40 cols of ~13px each (font size 10)
     40 cols is narrow but good enough for beginner level problems
     maintain 16:9 ratio
     */
    /** Width for single-panel views such as StudentWelcomeView */
    public static final int NARROW_FRAME_WIDTH = 720;

    /** Full width for two-panel views such as SolverView */
    public static final int FRAME_WIDTH = 1280;

    /** Frame height shared across all views (16:9 ratio) */
    public static final int FRAME_HEIGHT = 720;

    /** Width of one code block panel (MAX_COLS * ~7.8px per char) */
    public static final int BLOCK_PANEL_WIDTH = 624;

    /** Maximum number of characters per line in a code block */
    public static final int MAX_COLS = 40;

    /** Default divider position splitting the frame into two equal halves */
    public static final int DIVIDER_LOCATION = FRAME_WIDTH / 2;

    /** Gap between tightly related elements such as code blocks in a list */
    public static final int TIGHT_GAP = 5;

    /** Padding around panels */
    public static final int PANEL_PAD = 10;

    /** Margin around the outer edges of the frame */
    public static final int FRAME_MARGIN = 20;

    /** Utility class -- do not instantiate */
    private GuiConstants() {}

    /* Shared Components
    DESIGN
    Component     | Type    | Position      | Shown in
    --------------|---------|---------------|---------------------------
    Credits       | JLabel  | SOUTH         | StudentWelcomeView, SetterWelcomeView
    HomeLabel     | JLabel  | NORTH / WEST  | StudentWelcomeView, SetterWelcomeView
    HomeButton    | JButton | NORTH / WEST  | SolverView, EditorView
    Quit          | JButton | NORTH / EAST  | all views
    Close         | JButton | NORTH / EAST  | SolverView, EditorView

    METHODS
    createNavBar(boolean showHomeButton, boolean showClose, JFrame frameParam)
    createCreditsPanel()
     */

    /**
     * Creates a navigation bar panel for the top of each view.
     *
     * Layout:  | Home (label or button)  ...  Quit  [Close] |
     *
     * @param showHomeButton  true = Home is a JButton (SolverView, EditorView)
     *                        false = Home is a JLabel (WelcomeViews)
     * @param showClose       true = Close Window button is shown
     * @param frameParam      the parent frame, used for dispose() and home navigation
     * @return                a JPanel to place in BorderLayout.NORTH
     */
    public static JPanel createNavBar(boolean showHomeButton, boolean showClose, JFrame frameParam) {

        /* Top-level panel using BorderLayout to split left and right sections */
        JPanel navPanel = new JPanel(new BorderLayout());

        // -- LEFT: Home as button or label --
        if (showHomeButton) {
            /* Home button shown in SolverView and EditorView -- navigates back to welcome */
            JButton homeButton = new JButton("Home");
            homeButton.addActionListener(e -> {
                frameParam.dispose();
                // TODO: open StudentWelcomeView or SetterWelcomeView here
            });
            navPanel.add(homeButton, BorderLayout.WEST);
        } else {
            /* Home label shown in welcome views -- indicates current location */
            JLabel homeLabel = new JLabel("Parsons Problems");
            navPanel.add(homeLabel, BorderLayout.WEST);
        }

        // -- RIGHT: Quit (always) + Close (optional) --
        /* Panel to hold right-side buttons, laid out right to left */
        JPanel rightButtonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        if (showClose) {
            /* Close button shown in SolverView and EditorView -- closes this window only */
            JButton closeButton = new JButton("Close Window");
            closeButton.addActionListener(e -> frameParam.dispose());
            rightButtonsPanel.add(closeButton);
        }

        /* Quit button shown in all views -- exits the entire application */
        JButton quitButton = new JButton("Quit App");
        quitButton.addActionListener(e -> System.exit(0));
        rightButtonsPanel.add(quitButton);

        navPanel.add(rightButtonsPanel, BorderLayout.EAST);

        return navPanel;
    }


    /**
     * Creates a credits panel for the bottom of the welcome views.
     *
     * Layout:  | Developed by: [comma separated names] |
     *
     * @return a JPanel to place in BorderLayout.SOUTH
     */
    public static JPanel createCreditsPanel() {
        /* Panel centered horizontally to display team credits */
        JPanel creditsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        /* Label listing all team members */
        JLabel creditsLabel = new JLabel("Developed by: Parker McKillop, " +
                "Michael O'Bannon, " +
                "Oksana Pooley, " +
                "and Arsh Singh");

        creditsPanel.add(creditsLabel);
        return creditsPanel;
    }
}
