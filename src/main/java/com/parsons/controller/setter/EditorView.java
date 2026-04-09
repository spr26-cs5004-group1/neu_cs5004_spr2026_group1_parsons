package com.parsons.controller.setter;
import com.parsons.controller.Utils;
import com.parsons.model.CodeBlock;
import com.parsons.model.ParsonsProblem;
import com.parsons.service.ParsonsProblemsService;

import java.awt.*;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.parsons.controller.Utils.*;

public class EditorView extends JFrame{
    /** Account name of the current user, used for personalized messages and refresh. */
    private final String name;

    /**
     * The Parsons problem being solved by the student.
     * Stored as a field so it can be accessed by checkAnswer() outside the constructor.
     * It can be null when "NEW" row is selected, and then setter can set it, no not final.
     */
    private ParsonsProblem problem;

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

    /**
     * Checks the answer locally against a new problem loaded from text file.
     * Seemingly violates DRY but used in EditorView since the problem may not yet be saved to the repo.
     * Service needs id, we need to test before saving.
     *
     * @param answer the list of CodeBlocks the student arranged in the answer panel
     * @return true if the answer matches the solution, false otherwise
     */
    private boolean checkAnswer(List<CodeBlock> answer) {
        List<CodeBlock> solution = problem.getCode().stream()
                .filter(b -> !b.getIsDistractor())
                .sorted(Comparator.comparing(CodeBlock::getOrderIndex))
                .collect(Collectors.toList());
        if (answer.size() != solution.size()) return false;
        for (int i = 0; i < solution.size(); i++) {
            if (!solution.get(i).getCodeContent().equals(answer.get(i).getCodeContent())) {
                return false;
            }
        }
        return true;
    }

    // ADD JAVA DOC FOR CONSTRUCTOR
    public EditorView(ParsonsProblem problem, ParsonsProblemsService service, String name, SetterWelcomeView parent) {

        // TODO: The names of the panels are very confusing, south center etc. Find a reasonable Naming pattern.

        /* Set problem */
        this.problem = problem;

        /* Set name */
        this.name = name;


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

        /* Make a centerFramePanel which will hold topPanel (title, instructions), SplitPane, and submit button. */
        JPanel centerFramePanel = new JPanel(new BorderLayout());
        centerFramePanel.setBorder(BorderFactory.createEmptyBorder(PANEL_PAD, PANEL_PAD, PANEL_PAD, PANEL_PAD));

        /* Create topPanel that holds title, instructions for setter, student instructions from problem. */
        //JPanel topPanel = new JPanel(new GridLayout(4, 1));
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));

        /* setterInstr textArea, will put in JScrollPanel */
        JTextArea setterInstr = new JTextArea("""
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
        topPanel.add(setterInstrScroll);

        /* Make a fileBrowserPanel for centerFramePanel. */
        JPanel fileBrowserPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton browseButton = new JButton("Browse .txt file");
        JLabel fileStatusLabel = new JLabel("No file selected");
        fileBrowserPanel.add(browseButton);
        fileBrowserPanel.add(fileStatusLabel);
        topPanel.add(fileBrowserPanel);

        /* Add title and problem instructions to topPanel. */
        topPanel.add(new JLabel(title));
        String instr = (problem != null) ? problem.getInstructions() : "";
        JTextArea instrArea = new JTextArea(instr);
        instrArea.setEditable(false);
        topPanel.add(instrArea);

        /* add topPanel to centerFramePanel of Frame wrapped in a JScrollPanel. */
        centerFramePanel.add(topPanel, BorderLayout.NORTH);

        /* Create the Split Pane */
        JPanel blocksPanelLeft = new JPanel(new GridLayout(20, 1, TIGHT_GAP, TIGHT_GAP));
        JPanel answerPanelRight = new JPanel(new GridLayout(20, 1, TIGHT_GAP, TIGHT_GAP));
        Utils.makeDropTarget(blocksPanelLeft);
        Utils.makeDropTarget(answerPanelRight);
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                new JScrollPane(blocksPanelLeft),
                new JScrollPane(answerPanelRight));
        splitPane.setDividerLocation(DIVIDER_LOCATION);
        /* Add it to centerFramePanel */
        centerFramePanel.add(splitPane, BorderLayout.CENTER);

        /* Create a southPanel that will go to south of centerFramePanel. It will have submitButton and responseLabel. */
        JPanel southPanel = new JPanel(new GridLayout(2, 1));

        /* Create submit button */
        JButton submitButton = new JButton("Submit");
        if (problem == null) {
            submitButton.setEnabled(false);  // disable at start since there is no problem loaded
        }
        southPanel.add(submitButton);

        /* Create response label */
        JLabel responseLabel = new JLabel(" ");  // empty before submit
        responseLabel.setHorizontalAlignment(JLabel.CENTER);
        southPanel.add(responseLabel);

        /* Create retry button */
        JButton retryButton = new JButton("Retry");
        southPanel.add(retryButton);

        /* Add southPanel to centerFramePanel. */
        centerFramePanel.add(southPanel, BorderLayout.SOUTH);

        /* Add centerFramePanel to this frame, */
        this.add(centerFramePanel, BorderLayout.CENTER);

        /* Create a southButtonsPanel */
        JPanel southButtonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        /* Create a Save problem button at southButtonsPanel*/
        JButton saveButton = new JButton("Save This Problem");
        if (problem == null) {
            saveButton.setEnabled(false);  // disable at start since there is no problem loaded
        }
        southButtonsPanel.add(saveButton);

        /* Create a delete problem button at southButtonsPanel*/
        JButton deleteButton = new JButton("Delete Problem");
        deleteButton.setForeground(Color.RED);  // red to signal danger
        if (problem == null) {
            deleteButton.setEnabled(false);  // disable at start since there is no problem loaded
        }
        southButtonsPanel.add(deleteButton);

        /* Add southButtonsPanel to this */
        this.add(southButtonsPanel, BorderLayout.SOUTH);

        /*******************/
        /* Business Logic. */
        /*******************/

        /* Browser Button Logic to create new problems, or update existing ones. */
        browseButton.addActionListener(e -> {
            if (this.problem != null && this.problem.getId() > 0) {
                int confirm = JOptionPane.showConfirmDialog(this,
                        "Uploading a file will overwrite the existing problem. Are you sure?",
                        "Confirm Overwrite", JOptionPane.YES_NO_OPTION);
                if (confirm != JOptionPane.YES_OPTION) return;
            }
            JFileChooser fileChooser = new JFileChooser();
            /* file browser should open resource dir not home dir */
            // TODO: update path for production -- use System.getProperty("user.dir") when packaged
            fileChooser.setCurrentDirectory(new File("src/main/resources"));
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                try {
                    List<ParsonsProblem> parsed = Utils.parseFile(file.getAbsolutePath());
                    if (parsed != null && !parsed.isEmpty()) {
                        this.problem = parsed.get(0);
                        populateBlocks(blocksPanelLeft);
                        fileStatusLabel.setText("Loaded: " + file.getName());
                        submitButton.setEnabled(true);
                        saveButton.setEnabled(true);
                        instrArea.setText(this.problem.getInstructions());
                        topPanel.add(instrArea);
                    } else {
                        fileStatusLabel.setText("Error: invalid file format");
                    }
                } catch (IOException ex) {
                    fileStatusLabel.setText("Error reading file: " + ex.getMessage());
                }
            }
        });

        /* Scramble codeBlocks and add to blocksPanelLeft. */
        this.populateBlocks(blocksPanelLeft);

        /* Submit and check response */
        submitButton.addActionListener(e -> {
            List<CodeBlock> answer = extractAnswer(answerPanelRight);
            boolean correct = this.checkAnswer(answer);
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
            if (this.problem == null) {
                JOptionPane.showMessageDialog(this, "No problem loaded yet.");
            } else if (this.problem.getId() > 0) {
                int confirm = JOptionPane.showConfirmDialog(this,
                        "This will overwrite the existing problem. Are you sure?",
                        "Confirm Save", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    service.updateProblem(this.problem.getId(), this.problem);
                    JOptionPane.showMessageDialog(this, "Problem updated successfully!");
                    new SetterWelcomeView(service, name); // refresh the SetterWelcomeView
                }
            } else { // NOT DRY
                service.saveProblem(this.problem);
                JOptionPane.showMessageDialog(this, "Problem saved successfully!");
                new SetterWelcomeView(service, name); // refresh the SetterWelcomeView
            }
        });

        /* deleteButton Business Logic.
        Meaning for overall design:
        * We are letting user add duplicate problems.
        * If they have two problems with same titles, they can delete one.
        * id is handled internally.
        */
        deleteButton.addActionListener(e -> {
            if (this.problem == null || this.problem.getId() <= 0) {
                JOptionPane.showMessageDialog(this, "No saved problem to delete.");
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete this problem?",
                    "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                service.deleteProblem(this.problem.getId());
                JOptionPane.showMessageDialog(this, "Problem deleted.");
                this.dispose(); // close this window
                new SetterWelcomeView(service, name); // refresh the SetterWelcomeView
            }
        });

        setVisible(true);
    }
}
