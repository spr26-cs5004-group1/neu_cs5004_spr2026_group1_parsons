package com.parsons.controller.setter;
import com.parsons.controller.Utils;
import com.parsons.model.CodeBlock;
import com.parsons.model.ParsonsProblem;
import com.parsons.service.ParsonsProblemsService;

import java.awt.*;
import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.parsons.controller.Utils.*;

public class EditorView extends JFrame{
    /**
     * The Parsons problem being solved by the student.
     * Stored as a field so it can be accessed by checkAnswer() outside the constructor.
     */
    private final ParsonsProblem problem;

    /**
     * Populates the blocks panel with shuffled code blocks from the problem.
     * Called on load and again when the student clicks Retry.
     *
     * @param blocksPanelLeft the panel to populate with code blocks
     */
    private void populateBlocks(JPanel blocksPanelLeft) {
        if (blocksPanelLeft == null || problem == null || problem.getCode() == null)
            return;
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
     * Extracts the student's answer from the answer panel as a list of CodeBlocks.
     *
     * @param answerPanel the panel containing the student's arranged code blocks
     * @return list of CodeBlocks in the order the student arranged them
     */
    private List<CodeBlock> extractAnswer(JPanel answerPanel) {
        List<CodeBlock> answer = new ArrayList<>();
        for (Component c : answerPanel.getComponents()) {
            if (c instanceof JLabel labelBlock) {
                answer.add(new CodeBlock(labelBlock.getText(), false, null));
            }
        }
        return answer;
    }


    // ADD JAVA DOC FOR CONSTRUCTOR
    public EditorView(ParsonsProblem problem, ParsonsProblemsService service) {
        /* Set problem */
        this.problem = problem;

        /* Set frame title and size. */
        String title = "";
        if (problem != null) {
            title = problem.getTitle();
            this.setTitle("Testing Parson's Problem: " + title);
        } else {

            this.setTitle("Adding A New Parson's Problem");
        }

        this.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        /* Add navigation bar using helper functions. */
        JPanel navBar = Utils.createNavBar(true,true, this);
        this.add(navBar, BorderLayout.NORTH);
        navBar.setBorder(BorderFactory.createEmptyBorder(PANEL_PAD, PANEL_PAD, PANEL_PAD, PANEL_PAD));

        /* Make a centerPanel which will hold topPanel (title, instructions), SplitPane, and submit button. */
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBorder(BorderFactory.createEmptyBorder(PANEL_PAD, PANEL_PAD, PANEL_PAD, PANEL_PAD));

        /* Make a fileBrowserPanel for centerPanel. */
        if (problem == null) {
            JPanel fileBrowserPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JButton browseButton = new JButton("Browse .txt file");
            JLabel fileStatusLabel = new JLabel("No file selected");
            fileBrowserPanel.add(browseButton);
            fileBrowserPanel.add(fileStatusLabel);
            centerPanel.add(fileBrowserPanel, BorderLayout.NORTH);
            /* Business Logic */
            browseButton.addActionListener(e -> {
                // TODO: file parsing logic -- being implemented by Parker
            });
        }

        /* Create topPanel that holds title and instructions */
        JPanel topPanel = new JPanel(new GridLayout(3, 1));
        topPanel.add(new JLabel(title));
        String instr = problem.getInstructions();
        topPanel.add(new JTextArea(instr));
        /* add it to centerPanel */
        centerPanel.add(topPanel, BorderLayout.NORTH);

        /* Create the Split Pane */
        JPanel blocksPanelLeft = new JPanel(new GridLayout(20, 1, TIGHT_GAP, TIGHT_GAP));
        JPanel answerPanelRight = new JPanel(new GridLayout(20, 1, TIGHT_GAP, TIGHT_GAP));
        Utils.makeDropTarget(blocksPanelLeft);
        Utils.makeDropTarget(answerPanelRight);
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                new JScrollPane(blocksPanelLeft),
                new JScrollPane(answerPanelRight));
        splitPane.setDividerLocation(DIVIDER_LOCATION);
        /* Add it to centerPanel */
        centerPanel.add(splitPane, BorderLayout.CENTER);

        /* Create a southPanel that will go to south of centerPanel. It will have submitButton and responseLabel. */
        JPanel southPanel = new JPanel(new GridLayout(2, 1));

        /* Create submit button */
        JButton submitButton = new JButton("Submit");
        southPanel.add(submitButton);

        /* Create response label */
        JLabel responseLabel = new JLabel(" ");  // empty before submit
        responseLabel.setHorizontalAlignment(JLabel.CENTER);
        southPanel.add(responseLabel);

        /* Create retry button */
        JButton retryButton = new JButton("Retry");
        southPanel.add(retryButton);

        /* Add southPanel to centerPanel. */
        centerPanel.add(southPanel, BorderLayout.SOUTH);

        /* Add centerPanel to this frame, */
        this.add(centerPanel, BorderLayout.CENTER);

        /* Create a Save problem button at panel South */
        JButton saveButton = new JButton("Save This Problem");
        add(saveButton, BorderLayout.SOUTH);

        /*******************/
        /* Business Logic. */
        /*******************/

        /* Scramble codeBlocks and add to blocksPanelLeft. */
        this.populateBlocks(blocksPanelLeft);

        /* Submit and check response */
        submitButton.addActionListener(e -> {
            List<CodeBlock> answer = extractAnswer(answerPanelRight);
            boolean correct = service.checkAnswer(problem.getId(), answer);
            if (correct) {
                responseLabel.setText("Correct! Well done!");
                answerPanelRight.setBorder(BorderFactory.createLineBorder(Color.GREEN, 3));
            } else {
                responseLabel.setText("Incorrect. Try again!");
                answerPanelRight.setBorder(BorderFactory.createLineBorder(Color.RED, 3));
            }
        });

        /* retryButton listener. */
        retryButton.addActionListener(e -> {
            answerPanelRight.setBorder(null);
            answerPanelRight.removeAll();
            answerPanelRight.revalidate();
            answerPanelRight.repaint();
            populateBlocks(blocksPanelLeft);
            responseLabel.setText(" ");
        });

        /* Save button logic */
        saveButton.addActionListener(e -> {
            if (problem == null) {
                JOptionPane.showMessageDialog(this, "No problem loaded yet.");
            } else {
                service.saveProblem(problem);
                JOptionPane.showMessageDialog(this, "Problem saved successfully!");
            }
        });

        setVisible(true);
    }
}
