package com.parsons.controller.setter;

import com.parsons.controller.BaseParsonsView;
import com.parsons.controller.Utils;
import com.parsons.model.CodeBlock;
import com.parsons.model.ParsonsProblem;
import com.parsons.service.ParsonsProblemsService;

import java.awt.*;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.parsons.controller.Utils.*;

/**
 * View for a setter (instructor) to create, preview, edit, or delete a Parsons problem.
 * Extends BaseParsonsView with a file browser, setter instructions, save, and delete buttons.
 * The problem field may be null on open when the setter intends to create a new problem,
 * in which case submit, save, and delete are disabled until a file is loaded.
 */
public final class EditorView extends BaseParsonsView {

    /** Account name of the current user, used for refresh after save or delete. */
    private final String name;

    /**
     * Constructs the EditorView, wires all button listeners, populates code blocks
     * if a problem is provided, and makes the frame visible.
     *
     * @param problem the Parsons problem to edit, or null when creating a new problem
     * @param service the service used to save, update, and delete problems
     * @param name    the account name of the current setter user
     * @param parent  the SetterWelcomeView to dispose and refresh after a save or delete;
     *                may be null
     */
    public EditorView(ParsonsProblem problem,
                      ParsonsProblemsService service,
                      String name,
                      SetterWelcomeView parent) {
        super(problem, buildTitle(problem));
        this.name = name;

        /* Prefix setter instructions and file browser to the shared top panel. */
        buildTopExtras();

        /* Get references shared via superclass getters. */
        JPanel topPanel = getTopPanel();
        JPanel centerPanel = getCenterPanel();
        JPanel answerPanelRight = getAnswerPanelRight();

        /* File status label, referenced by the browse listener below. */
        JLabel fileStatusLabel = new JLabel("No file selected");

        /* Instructions text area, referenced by the browse listener to refresh on file load. */
        String instr = (problem != null) ? problem.getInstructions() : "";
        JTextArea instrArea = new JTextArea(instr);
        instrArea.setEditable(false);
        topPanel.add(instrArea);

        /* South panel holds submit, response label, and retry. */
        JPanel southPanel = new JPanel(new GridLayout(3, 1));

        /* Submit button: disabled when no problem is loaded. */
        JButton submitButton = new JButton("Submit");
        submitButton.setEnabled(problem != null);
        southPanel.add(submitButton);

        /* Response label from superclass. */
        southPanel.add(getResponseLabel());

        /* Retry button. */
        JButton retryButton = new JButton("Retry");
        southPanel.add(retryButton);

        centerPanel.add(southPanel, BorderLayout.SOUTH);

        /* Bottom button strip for save and delete. */
        JPanel southButtonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        /* Save button: disabled when no problem is loaded. */
        JButton saveButton = new JButton("Save This Problem");
        saveButton.setEnabled(problem != null);
        southButtonsPanel.add(saveButton);

        /* Delete button: red to signal danger; disabled when no problem is loaded. */
        JButton deleteButton = new JButton("Delete Problem");
        deleteButton.setForeground(Color.RED);
        deleteButton.setEnabled(problem != null);
        southButtonsPanel.add(deleteButton);

        this.add(southButtonsPanel, BorderLayout.SOUTH);

        /* ******************/
        /* Business Logic. */
        /* ******************/

        /* File browser panel wired here since it references submitButton and saveButton. */
        JPanel fileBrowserPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton browseButton = new JButton("Browse .txt file");
        fileBrowserPanel.add(browseButton);
        fileBrowserPanel.add(fileStatusLabel);
        /* Insert file browser before other top content by adding at index 0. */
        topPanel.add(fileBrowserPanel, 0);

        /* Browse: parse the selected file and load the first problem found. */
        browseButton.addActionListener(e -> {
            if (getProblem() != null && getProblem().getId() > 0) {
                int confirm = JOptionPane.showConfirmDialog(this,
                        "Uploading a file will overwrite the existing problem. Are you sure?",
                        "Confirm Overwrite", JOptionPane.YES_NO_OPTION);
                if (confirm != JOptionPane.YES_OPTION) {
                    return;
                }
            }
            int originalId = (getProblem() != null) ? getProblem().getId() : -1;
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new File("src/main/resources"));
            // limit the types of files that can be uploaded to .txt
            fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                    "Text files (*.txt)", "txt")
            );
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                try {
                    List<ParsonsProblem> parsed = Utils.parseFile(file.getAbsolutePath());
                    if (!parsed.isEmpty()) {
                        setProblem(parsed.get(0));
                        getProblem().setId(originalId);
                        populateBlocks();
                        fileStatusLabel.setText("Loaded: " + file.getName());
                        submitButton.setEnabled(true);
                        saveButton.setEnabled(true);
                        instrArea.setText(getProblem().getInstructions());
                    } else {
                        fileStatusLabel.setText("Error: invalid file format");
                    }
                } catch (IOException ex) {
                    fileStatusLabel.setText("Error reading file: " + ex.getMessage());
                }
            }
        });

        /* Populate blocks if a problem was passed in at construction time. */
        this.populateBlocks();

        /* Submit: check answer locally since the problem may not yet be saved. */
        submitButton.addActionListener(e -> {
            List<CodeBlock> answer = extractAnswer();
            boolean correct = checkAnswerLocally(answer);
            if (correct) {
                getResponseLabel().setText("Correct! Well done!");
                answerPanelRight.setBorder(BorderFactory.createLineBorder(Color.GREEN, 3));
            } else {
                getResponseLabel().setText("Incorrect. Try again!");
                answerPanelRight.setBorder(BorderFactory.createLineBorder(Color.RED, 3));
            }
        });

        /* Retry: clear the answer panel and reshuffle blocks. */
        retryButton.addActionListener(e -> resetForRetry());

        /* Save: insert new problem or overwrite existing one after confirmation. */
        saveButton.addActionListener(e -> {
            if (getProblem() == null) {
                JOptionPane.showMessageDialog(this, "No problem loaded yet.");
            } else if (getProblem().getId() > 0) {
                int confirm = JOptionPane.showConfirmDialog(this,
                        "This will overwrite the existing problem. Are you sure?",
                        "Confirm Save", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    service.updateProblem(getProblem().getId(), getProblem());
                    JOptionPane.showMessageDialog(this, "Problem updated successfully!");
                    refreshAndClose(parent, service);
                }
            } else {
                service.saveProblem(getProblem());
                JOptionPane.showMessageDialog(this, "Problem saved successfully!");
                refreshAndClose(parent, service);
            }
        });

        /* Delete: remove the saved problem after confirmation. */
        deleteButton.addActionListener(e -> {
            if (getProblem() == null || getProblem().getId() <= 0) {
                JOptionPane.showMessageDialog(this, "No saved problem to delete.");
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete this problem?",
                    "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                service.deleteProblem(getProblem().getId());
                JOptionPane.showMessageDialog(this, "Problem deleted.");
                refreshAndClose(parent, service);
            }
        });

        setVisible(true);
    }

    /**
     * Builds the window title string based on whether a problem is provided.
     *
     * @param problem the problem being edited, or null for a new problem
     * @return the title string for the JFrame
     */
    private static String buildTitle(ParsonsProblem problem) {
        if (problem != null) {
            return "Testing Parson's Problem: " + problem.getTitle();
        }
        return "Adding A New Parson's Problem";
    }

    /**
     * Prepends the setter instructions scroll pane to the shared top panel.
     * Called once during construction before other top content is added.
     */
    private void buildTopExtras() {
        JTextArea setterInstr = new JTextArea(
        """
        SETTER INSTRUCTIONS:
        - Upload a .txt file to add a new problem (id assigned automatically)
        - WARNING: if a file is uploaded for an existing problem, it will be overwritten
        - TXT File format:
        - line 1 = title, line 2 = instructions,
        - remaining = code blocks. Code block format: isDistractor | orderIndex | codeContent
        - isDistractor: f/false = solution block, t/true = distractor
        - orderIndex is expected to be 0-indexed and complete
        - Use \\t for indentation (converted to 4 spaces)
        - Problems separated by ---
        """);
        setterInstr.setEditable(false);
        setterInstr.setBackground(null);
        JScrollPane setterInstrScroll = new JScrollPane(setterInstr);
        setterInstrScroll.setPreferredSize(new Dimension(FRAME_WIDTH, 120));
        getTopPanel().add(setterInstrScroll);
    }

    /**
     * Checks the student's answer locally against the problem solution.
     * Used instead of the service method because the problem may not yet be saved,
     * so it has no repository id to pass to the service.
     *
     * @param answer the list of CodeBlocks the student arranged in the answer panel
     * @return true if the answer matches the solution in order, false otherwise
     */
    private boolean checkAnswerLocally(List<CodeBlock> answer) {
        List<CodeBlock> solution = getProblem().getCode().stream()
                .filter(b -> !b.getIsDistractor())
                .sorted(Comparator.comparing(CodeBlock::getOrderIndex))
                .collect(Collectors.toList());
        if (answer.size() != solution.size()) {
            return false;
        }
        for (int i = 0; i < solution.size(); i++) {
            if (!solution.get(i).getCodeContent().equals(answer.get(i).getCodeContent())) {
                return false;
            }
        }
        return true;
    }

    /**
     * Disposes the parent SetterWelcomeView if present, opens a fresh one to reflect
     * the latest problem list, then closes this EditorView.
     *
     * @param parent  the parent SetterWelcomeView to close and reopen; may be null
     * @param service the service passed to the new SetterWelcomeView
     */
    private void refreshAndClose(SetterWelcomeView parent, ParsonsProblemsService service) {
        if (parent != null) {
            parent.dispose();
        }
        new SetterWelcomeView(service, name);
        this.dispose();
    }
}
