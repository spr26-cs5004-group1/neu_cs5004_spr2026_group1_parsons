package com.parsons.controller.student;

import com.parsons.controller.BaseParsonsView;
import com.parsons.model.CodeBlock;
import com.parsons.model.ParsonsProblem;
import com.parsons.service.ParsonsProblemsService;

import java.awt.*;
import javax.swing.*;
import java.util.List;

/**
 * View for a student solving a single Parsons problem.
 * Extends BaseParsonsView with submit, retry, and next buttons.
 * On correct submission the next button is enabled, allowing the student
 * to advance to the following problem in the list.
 */
public final class SolverView extends BaseParsonsView {

    /**
     * Constructs the SolverView, wires button listeners, populates code blocks,
     * and makes the frame visible.
     *
     * @param problem the Parsons problem to solve
     * @param service the service used to check answers and fetch the problem list
     * @param index   the zero-based index of this problem in the full problem list,
     *                used by the Next button to load the following problem
     */
    public SolverView(ParsonsProblem problem, ParsonsProblemsService service, int index) {
        super(problem, "Solving Parson's Problem: " + problem.getTitle());

        /* Get reference shared via superclass getters. */
        JPanel topPanel = getTopPanel();

        /* Add instructions row to Top Panel of centerPanel. */
        String instr = problem.getInstructions(); // we always have a problem in SolverView, never null.
        JTextArea instrArea = new JTextArea(instr);
        instrArea.setEditable(false);
        topPanel.add(instrArea);

        /* South panel holds submit, retry, and next buttons plus the response label. */
        JPanel southPanel = new JPanel(new GridLayout(4, 1));

        /* Submit button. */
        JButton submitButton = new JButton("Submit");
        southPanel.add(submitButton);

        /* Response label from superclass. */
        southPanel.add(getResponseLabel());

        /* Retry button. */
        JButton retryButton = new JButton("Retry");
        southPanel.add(retryButton);

        /* Next button, disabled until the student submits a correct answer. */
        JButton nextButton = new JButton("Next");
        nextButton.setEnabled(false);
        southPanel.add(nextButton);

        getCenterPanel().add(southPanel, BorderLayout.SOUTH);

        /* ***************** */
        /* Business Logic.   */
        /* ***************** */

        /* Shuffle and populate code blocks on the left panel. */
        this.populateBlocks();

        /* Submit: check answer, colour the border, enable Next on correct. */
        submitButton.addActionListener(e -> {
            List<CodeBlock> answer = extractAnswer();
            boolean correct = service.checkAnswer(problem.getId(), answer);
            if (correct) {
                getResponseLabel().setText("Correct! Well done!");
                getAnswerPanelRight().setBorder(BorderFactory.createLineBorder(Color.GREEN, 3));
                nextButton.setEnabled(true);
            } else {
                getResponseLabel().setText("Incorrect. Try again!");
                getAnswerPanelRight().setBorder(BorderFactory.createLineBorder(Color.RED, 3));
            }
        });

        /* Retry: clear the answer panel and reshuffle blocks. */
        retryButton.addActionListener(e -> resetForRetry());

        /* Next: load the following problem, or congratulate when the list is exhausted. */
        nextButton.addActionListener(e -> {
            /* Fetch the newest problem list in case problems were added during the session. */
            List<ParsonsProblem> newestProblems = service.getAllProblems();
            if (index + 1 < newestProblems.size()) {
                this.dispose();
                new SolverView(newestProblems.get(index + 1), service, index + 1);
            } else {
                getResponseLabel().setText("Congrats! You have reached the end!");
            }
        });

        setVisible(true);
    }
}
