package com.parsons.controller.student;
import com.parsons.controller.GuiConstants;
import com.parsons.model.CodeBlock;
import com.parsons.model.ParsonsProblem;
import com.parsons.service.ParsonsProblemsService;

import javax.swing.*;

import java.awt.*;
import java.util.ArrayList;

import static com.parsons.controller.GuiConstants.*;

public class SolverView extends JFrame{
    public SolverView(ParsonsProblem problem, ParsonsProblemsService service) {
        /* Set frame title and size. */
        String title = problem.getTitle();
        this.setTitle("Solving Parson's Problem: " + title);
        this.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        /* Add navigation bar using helper functions. */
        this.add(GuiConstants.createNavBar(true,true, this), BorderLayout.NORTH);

        /* Make a centerPanel which will hold topPanel (title, instructions), SplitPane, and submit button. */
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBorder(BorderFactory.createEmptyBorder(PANEL_PAD, PANEL_PAD, PANEL_PAD, PANEL_PAD));

        /* Create topPanel that holds title and instructions */
        JPanel topPanel = new JPanel(new GridLayout(2, 1));
        topPanel.add(new JLabel(title));
        String instr = problem.getInstructions();
        topPanel.add(new JTextArea(instr));
        /* add it to centerPanel */
        centerPanel.add(topPanel, BorderLayout.NORTH);

        /* Create the Split Pane */
        JPanel blocksPanelLeft = new JPanel(new GridLayout(20, 1, TIGHT_GAP, TIGHT_GAP));
        JPanel answerPanelRight = new JPanel(new GridLayout(20, 1, TIGHT_GAP, TIGHT_GAP));
        GuiConstants.makeDropTarget(blocksPanelLeft);
        GuiConstants.makeDropTarget(answerPanelRight);
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
        submitButton.addActionListener(e -> {
            // TODO: check answer
            // TODO: change frame color
            // TODO: update responseLabelResult
        });
        southPanel.add(submitButton);

        /* Create response label */
        JLabel responseLabel = new JLabel(" ");  // empty before submit
        responseLabel.setHorizontalAlignment(JLabel.CENTER);
        southPanel.add(responseLabel);

        /* Add southPanel to centerPanel */
        centerPanel.add(southPanel, BorderLayout.SOUTH);
        
        /* Add centerPanel to this frame */
        this.add(centerPanel, BorderLayout.CENTER);

        /* Business Logic */
//        List<CodeBlock> answer = new ArrayList<>();
//        for (Component c : answerPanelRight.getComponents()) {
//            if (c instanceof JLabel labelBlock) {
//                CodeBlock blockAnswer = new CodeBlock(labelBlock.getText(), false, null);
//                answer.add(blockAnswer);
//            }
//        }

        setVisible(true);
    }
}
