package com.parsons.controller;

import com.parsons.model.CodeBlock;
import com.parsons.model.ParsonsProblem;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Shared constants and UI helper methods for all views in the Parsons Problems app.
 * Not instantiable -- all members are static.
 */
public class Utils {

    /** Utility class -- do not instantiate */
    private Utils() {}

    /* Shared Constants
     derived based on the two panel drag and drop
     each panel is 40 cols of ~13px each (font size 10)
     40 cols is narrow but good enough for beginner level problems
     maintain 16:9 ratio
     */

    /** Height for launcher window */
    public static final int LAUNCHER_WIDTH = 500;
    public static final int LAUNCHER_HEIGHT = 500;


    /** Width for single-panel views such as StudentWelcomeView */
    public static final int NARROW_FRAME_WIDTH = 720;

    /** Full width for two-panel views such as SolverView */
    public static final int FRAME_WIDTH = 1280;

    /** Frame height shared across all views (16:9 ratio) */
    public static final int FRAME_HEIGHT = 720;

    /** Width of one code block panel (MAX_COLS * ~13px per char) */
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
    makeCodeBlock(String code)
    makeDropTarget(JPanel panel)
     */

    /** Tracks which label is currently being dragged -- shared across all drag-and-drop views */
    private static JLabel draggedLabel = null;

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

        /** Top-level panel using BorderLayout to split left and right sections */
        JPanel navPanel = new JPanel(new BorderLayout());

        // -- LEFT: Home as button or label --
        if (showHomeButton) {
            /** Home button shown in SolverView and EditorView -- navigates back to welcome */
            JButton homeButton = new JButton("Home");
            homeButton.addActionListener(e -> {
                frameParam.dispose();
                // TODO: open StudentWelcomeView or SetterWelcomeView here
            });
            navPanel.add(homeButton, BorderLayout.WEST);
        } else {
            /** Home label shown in welcome views -- indicates current location */
            JLabel homeLabel = new JLabel("Home");
            navPanel.add(homeLabel, BorderLayout.WEST);
        }

        // -- RIGHT: Quit (always) + Close (optional) --
        /** Panel to hold right-side buttons, laid out right to left */
        JPanel rightButtonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        if (showClose) {
            /** Close button shown in SolverView and EditorView -- closes this window only */
            JButton closeButton = new JButton("Close Window");
            closeButton.addActionListener(e -> frameParam.dispose());
            rightButtonsPanel.add(closeButton);
        }

        /** Quit button shown in all views -- exits the entire application */
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
        /** Panel centered horizontally to display team credits */
        JPanel creditsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        /** Label listing all team members */
        JLabel creditsLabel = new JLabel("Developed by: Parker McKillop, " +
                "Michael O'Bannon, " +
                "Oksana Pooley, " +
                "and Arsh Singh");

        creditsPanel.add(creditsLabel);
        return creditsPanel;
    }

    /**
     * Creates a draggable code block label with monospace font and blue border.
     *
     * @param code the code string to display in the block
     * @return a styled draggable JLabel
     */
    public static JLabel makeCodeBlock(String code) {
        JLabel labelCodeBlock = new JLabel(code, JLabel.LEFT);
        labelCodeBlock.setFont(new Font("Courier New", Font.PLAIN, 13));
        labelCodeBlock.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLUE),
                BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));
        labelCodeBlock.setOpaque(true);
        labelCodeBlock.setBackground(Color.WHITE);

        /** TransferHandler exports the label text as a string on drag */
        labelCodeBlock.setTransferHandler(new TransferHandler() {
            @Override
            public int getSourceActions(JComponent c) {
                return TransferHandler.MOVE;
            }

            @Override
            protected Transferable createTransferable(JComponent c) {
                return new StringSelection(((JLabel) c).getText());
            }
        });

        /** Remember which label is being dragged when mouse is pressed */
        labelCodeBlock.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                JLabel l = (JLabel) e.getSource();
                draggedLabel = l;
                l.getTransferHandler().exportAsDrag(l, e, TransferHandler.MOVE);
            }
        });

        return labelCodeBlock;
    }

    /**
     * Makes a JPanel a drop target that accepts dragged code blocks.
     * Removes the block from its source panel and adds it to this panel.
     *
     * @param panelParam the panel to make a drop target
     */
    public static void makeDropTarget(JPanel panelParam) {
        panelParam.setTransferHandler(new TransferHandler() {
            @Override
            public boolean canImport(TransferSupport support) {
                return support.isDataFlavorSupported(DataFlavor.stringFlavor);
            }

            @Override
            public boolean importData(TransferSupport support) {
                try {
                    String text = (String) support.getTransferable()
                            .getTransferData(DataFlavor.stringFlavor);

                    /** Remove block from source panel */
                    if (draggedLabel != null) {
                        Container sourceParent = draggedLabel.getParent();
                        if (sourceParent != null) {
                            sourceParent.remove(draggedLabel);
                            sourceParent.revalidate();
                            sourceParent.repaint();
                        }
                        draggedLabel = null;
                    }

                    /** Add new block to target panel */
                    panelParam.add(makeCodeBlock(text));
                    panelParam.revalidate();
                    panelParam.repaint();
                    return true;

                } catch (Exception e) {
                    return false;
                }
            }
        });
    }

    /**
     * File parser helper method for extracting new parsons problem from a .txt file.
     *
     * @param filepath the user provided path to the new problems file.
     * @return A list of parsed ParsonsProblem objects.
     * @throws IOException Occurs when the input file cannot be found or read.
     */
    public static List<ParsonsProblem> parseFile(String filepath) throws IOException {
        //TODO: if time allows abstract this out to its own parser class.
        int errorCount = 0;
        java.util.List<String> errorLines = new ArrayList<>();

        java.util.List<ParsonsProblem> problemList = new ArrayList<>();
        // Note: Caller is responsible for error catching/reporting
        java.util.List<String> lines = Files.readAllLines(Path.of(filepath));
        java.util.List<java.util.List<String>> problemChunks = new ArrayList<>();
        java.util.List<String> current = new ArrayList<>();


        for (String line : lines) {
            if (line.trim().isEmpty() || line.trim().startsWith("//")) continue;
            if (line.trim().equals("---")) {
                problemChunks.add(current);
                current = new ArrayList<>();
            } else {
                current.add(line);
            }
        }

        problemChunks.add(current);

        for (java.util.List<String> chunk : problemChunks) {
            try {
                int id = Integer.parseInt(chunk.getFirst());
                String title = chunk.get(1);
                String instructions = chunk.get(2);
                List<CodeBlock> codeBlocks = new ArrayList<>();

                for (int i = 3; i < chunk.size(); i++) {
                    String[] parts = chunk.get(i).split("\\|");
                    boolean isDistractor = parts[0].trim().toLowerCase(Locale.ROOT).startsWith("t");
                    Integer orderIndex = Integer.parseInt(parts[1].trim());
                    String codeContent = parts[2].stripTrailing();
                    codeBlocks.add(new CodeBlock(codeContent, isDistractor, orderIndex));
                }
                ParsonsProblem problem = new ParsonsProblem(title, instructions, codeBlocks);
                problem.setId(id);
                problemList.add(problem);
            } catch (Exception e) {
                // TODO: instantiate these variables as fields instead of local so they can
                //  be reported in error catch under the run method.
                errorCount++;
                errorLines.add("Failed to parse chunk starting at: '" + (chunk.isEmpty() ?
                        "(empty)" : chunk.getFirst()) + "' \n" + e.getMessage());
            }
        }
        return problemList;
    }
}
