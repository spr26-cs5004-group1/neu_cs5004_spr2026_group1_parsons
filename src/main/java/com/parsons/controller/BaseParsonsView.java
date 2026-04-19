package com.parsons.controller;

import com.parsons.model.CodeBlock;
import com.parsons.model.ParsonsProblem;

import java.awt.*;
import javax.swing.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.parsons.controller.Utils.*;

/**
 * Abstract base class for Parsons problem views (solver and editor).
 * Contains the shared UI structure: nav bar, title, instructions,
 * split pane with draggable code blocks, and a response label.
 * Subclasses add their own south-panel buttons and business logic,
 * then call setVisible(true) when ready.
 */
public abstract class BaseParsonsView extends JFrame {

    /** The Parsons problem being displayed. Not final so EditorView can replace it after a file upload. */
    private ParsonsProblem problem;

    /** Left panel holding the shuffled code blocks available to the student. */
    private final JPanel blocksPanelLeft;

    /** Right panel where the student drags code blocks to build their answer. */
    private final JPanel answerPanelRight;

    /** Label shown below the split pane to display correct/incorrect feedback. */
    private final JLabel responseLabel;

    /** Center panel holding topPanel, splitPane, and the subclass south panel. */
    private final JPanel centerPanel;

    /** Top panel inside centerPanel, holding title and instructions rows. */
    private final JPanel topPanel;

    /**
     * Constructs the shared base UI: frame chrome, nav bar, top panel,
     * split pane, and response label. Subclasses call super() first, then
     * append their own controls via getCenterPanel() and getTopPanel(),
     * and finally call setVisible(true).
     *
     * @param problem the Parsons problem to display; may be null in EditorView
     *                when no problem has been loaded yet
     * @param title   the window title string, composed by the subclass
     */
    protected BaseParsonsView(ParsonsProblem problem, String title) {

        /* Store problem and configure frame chrome. */
        this.problem = problem;
        this.setTitle(title);
        this.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        /* Add navigation bar. */
        JPanel navBar = Utils.createNavBar(true, true, this);
        navBar.setBorder(BorderFactory.createEmptyBorder(PANEL_PAD, PANEL_PAD, PANEL_PAD, PANEL_PAD));
        this.add(navBar, BorderLayout.NORTH);

        /* Build centerPanel which holds topPanel, splitPane, and subclass south content. */
        centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBorder(BorderFactory.createEmptyBorder(PANEL_PAD, PANEL_PAD, PANEL_PAD, PANEL_PAD));

        /* Build topPanel: subclasses may prepend rows (e.g. setter instructions) before the title. */
        topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));

        /* Title row. */
        String problemTitle = (problem != null) ? problem.getTitle() : "";
        topPanel.add(new JLabel(problemTitle));

        /* Instructions row. */
        String instr = (problem != null) ? problem.getInstructions() : "";
        JTextArea instrArea = new JTextArea(instr);
        instrArea.setEditable(false);
        topPanel.add(instrArea);

        centerPanel.add(topPanel, BorderLayout.NORTH);

        /* Build split pane with drag-and-drop enabled panels. */
        blocksPanelLeft = new JPanel(new GridLayout(20, 1, TIGHT_GAP, TIGHT_GAP));
        answerPanelRight = new JPanel(new GridLayout(20, 1, TIGHT_GAP, TIGHT_GAP));
        Utils.makeDropTarget(blocksPanelLeft);
        Utils.makeDropTarget(answerPanelRight);
        JSplitPane splitPane = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                new JScrollPane(blocksPanelLeft),
                new JScrollPane(answerPanelRight));
        splitPane.setDividerLocation(DIVIDER_LOCATION);
        centerPanel.add(splitPane, BorderLayout.CENTER);

        /* Response label sits above subclass-supplied south buttons. */
        responseLabel = new JLabel(" ");
        responseLabel.setHorizontalAlignment(JLabel.CENTER);

        this.add(centerPanel, BorderLayout.CENTER);
    }

    /**
     * Returns the problem currently loaded in this view.
     *
     * @return the current ParsonsProblem, or null if none is loaded
     */
    protected ParsonsProblem getProblem() {
        return problem;
    }

    /**
     * Replaces the current problem. Used by EditorView when a new file is uploaded.
     *
     * @param problem the new ParsonsProblem to load into this view
     */
    protected void setProblem(ParsonsProblem problem) {
        this.problem = problem;
    }

    /**
     * Returns the center panel so subclasses can attach their south-area controls.
     *
     * @return the center BorderLayout panel
     */
    protected JPanel getCenterPanel() {
        return centerPanel;
    }

    /**
     * Returns the top panel so subclasses can prepend extra rows above the title.
     *
     * @return the top BoxLayout panel
     */
    protected JPanel getTopPanel() {
        return topPanel;
    }

    /**
     * Returns the response label so subclasses can update feedback text on submit or retry.
     *
     * @return the response JLabel
     */
    protected JLabel getResponseLabel() {
        return responseLabel;
    }

    /**
     * Returns the answer panel so subclasses can read or clear it on submit or retry.
     *
     * @return the right-side answer JPanel
     */
    protected JPanel getAnswerPanelRight() {
        return answerPanelRight;
    }

    /**
     * Populates blocksPanelLeft with shuffled, non-null code blocks from the current problem.
     * Returns immediately if the problem or its code list is null.
     * Called on construction and again when the student clicks Retry.
     */
    protected void populateBlocks() {
        if (blocksPanelLeft == null || problem == null || problem.getCode() == null) {
            return;
        }
        blocksPanelLeft.removeAll();
        List<CodeBlock> shuffled = new ArrayList<>(problem.getCode());
        System.out.println("Block count: " + problem.getCode().size());
        Collections.shuffle(shuffled);
        for (CodeBlock block : shuffled) {
            if (block != null && block.getCodeContent() != null) {
                blocksPanelLeft.add(Utils.makeCodeBlock(block.getCodeContent()));
            }
        }
        blocksPanelLeft.revalidate();
        blocksPanelLeft.repaint();
    }

    /**
     * Extracts the student's current answer from answerPanelRight as an ordered
     * list of CodeBlocks, one per JLabel component found in the panel.
     *
     * @return list of CodeBlocks in the order the student arranged them;
     *         empty list if the panel contains no JLabel components
     */
    protected List<CodeBlock> extractAnswer() {
        List<CodeBlock> answer = new ArrayList<>();
        for (Component c : answerPanelRight.getComponents()) {
            if (c instanceof JLabel labelBlock) {
                answer.add(new CodeBlock(labelBlock.getText(), false, null));
            }
        }
        return answer;
    }

    /**
     * Resets the answer panel and response label to their initial state,
     * then reshuffles the blocks on the left. Called by the Retry button
     * in both subclasses.
     */
    protected void resetForRetry() {
        answerPanelRight.setBorder(null);
        answerPanelRight.removeAll();
        answerPanelRight.revalidate();
        answerPanelRight.repaint();
        populateBlocks();
        responseLabel.setText(" ");
    }
}
