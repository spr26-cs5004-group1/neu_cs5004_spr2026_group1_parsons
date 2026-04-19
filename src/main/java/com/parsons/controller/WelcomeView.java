package com.parsons.controller;

import com.parsons.model.ParsonsProblem;
import com.parsons.service.ParsonsProblemsService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

import static com.parsons.controller.Utils.*;

/**
 * Abstract base class for the student and setter welcome views.
 * <p>
 * Both views share the same frame layout:
 *   NORTH  -- nav bar
 *   CENTER -- welcome label + instructions + scrollable problems table
 *   SOUTH  -- credits
 * </p>
 * Subclasses supply the frame title, welcome text, instructions, and control
 * how rows are added to the table and how a row selection is handled.
 */
public abstract class WelcomeView extends JFrame {

    protected final ParsonsProblemsService service;
    protected final List<ParsonsProblem> problems;
    protected final DefaultTableModel tableModel;

    protected WelcomeView(ParsonsProblemsService service, String frameTitle, String welcomeText, String instrText) {
        
        /** Stores the service object passed to this. */
        this.service = service;

        /** Stores the List<ParsonsProblem> of the repo. */
        this.problems = service.getAllProblems();

        setTitle(frameTitle);
        setSize(NARROW_FRAME_WIDTH, FRAME_HEIGHT);

        /* top panel: welcome label + instructions */
        JLabel welcome = new JLabel(welcomeText, JLabel.LEFT);
        JTextArea instr = new JTextArea(instrText);
        instr.setEditable(false);

        JPanel topPanel = new JPanel(new GridLayout(2, 1));
        topPanel.add(welcome);
        topPanel.add(instr);
        topPanel.setBorder(BorderFactory.createEmptyBorder(PANEL_PAD, PANEL_PAD, PANEL_PAD, PANEL_PAD));

        /* table model: subclass populates rows */
        String[] columns = {"ID", "Title"};
        tableModel = new DefaultTableModel(columns, 0);
        populateTable();

        /* JTable with selection listener delegated to subclass */
        JTable problemsTable = new JTable(tableModel);
        problemsTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = problemsTable.getSelectedRow();
                if (row != -1) {
                    onRowSelected(row);
                }
            }
        });

        /* nav bar  */
        JPanel navBar = Utils.createNavBar(false, false, this);
        navBar.setBorder(BorderFactory.createEmptyBorder(PANEL_PAD, PANEL_PAD, PANEL_PAD, PANEL_PAD));
        add(navBar, BorderLayout.NORTH);

        /* center panel */
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBorder(BorderFactory.createEmptyBorder(PANEL_PAD, PANEL_PAD, PANEL_PAD, PANEL_PAD));
        centerPanel.add(topPanel, BorderLayout.NORTH);
        centerPanel.add(new JScrollPane(problemsTable), BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);

        /* credits footer */
        add(Utils.createCreditsPanel(), BorderLayout.SOUTH);

        setVisible(true);
    }

    /**
     * Populate tableModel with whatever rows this view needs.
     * Called once during construction, before the table is displayed.
     * The base list of problems is available via problems.
     */
    protected abstract void populateTable();

    /**
     * React to the user selecting a row in the problems table.
     *
     * @param row the zero-based index of the selected row in tableModel
     */
    protected abstract void onRowSelected(int row);
}
