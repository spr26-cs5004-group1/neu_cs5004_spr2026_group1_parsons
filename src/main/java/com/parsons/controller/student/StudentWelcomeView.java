package com.parsons.controller.student;
import com.parsons.controller.GuiConstants;
import com.parsons.model.*;
import com.parsons.service.ParsonsProblemsService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

import static com.parsons.controller.GuiConstants.*;
import static com.parsons.controller.GuiConstants.PANEL_PAD;

/**
 * StudentWelcomeView displays a list of Parsons problems for the student.
 * The student can click a row to open the problem in a new window.
 *
 * Frame Layout:
 *   NORTH  -- nav bar
 *   CENTER -- welcome + instructions + table
 *   SOUTH  -- credits
 */

public class StudentWelcomeView extends JFrame {
    public StudentWelcomeView(List<ParsonsProblem> problems, ParsonsProblemsService service) {
        setTitle("Parsons Problems: Student View");
        setSize(NARROW_FRAME_WIDTH, FRAME_HEIGHT);
        JLabel welcome = new JLabel("Welcome Learner!", JLabel.LEFT);
        JTextArea instr = new JTextArea("Select a parson's problem from the table below.\n" +
                "The problem will open in a new window.");
        instr.setEditable(false);

        /* Add welcome and instructions to a grid layout panel stacked vertically. */
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridLayout(2, 1));
        topPanel.add(welcome);
        topPanel.add(instr);
        topPanel.setBorder(BorderFactory.createEmptyBorder(PANEL_PAD, PANEL_PAD, PANEL_PAD, PANEL_PAD));

        /* create the datasheet that will be displayed in the JTable below the welcome message and instructions table */
        String[] columns = {"ID", "Title"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0);
        /* fill the tableModel. DefaultTableModel only accepts array of Object class */
        for (ParsonsProblem p : problems) {
            tableModel.addRow(new Object[]{p.getId(), p.getTitle()});
        }

        /* Create a JTable object that displays the datasheet tableModel */
        JTable problemsTable = new JTable(tableModel);

        /* Now to tackle making the mouse click open a new window */
        problemsTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = problemsTable.getSelectedRow();
                if (row != -1) {    // protect against click anywhere but a row, which leads to deselection
                    int id = (int) tableModel.getValueAt(row, 0); // extract id from row
                    // find problem with id from problems
                    ParsonsProblem selected = problems.stream()
                            .filter(p -> p.getId() == id)
                            .findFirst()
                            .orElse(null);
                    new SolverView(selected, service);
                }
            }
        });
        /* Add nav bar using helper functions. */
        JPanel navBar = GuiConstants.createNavBar(false,false, this);
        navBar.setBorder(BorderFactory.createEmptyBorder(PANEL_PAD, PANEL_PAD, PANEL_PAD, PANEL_PAD));
        this.add(navBar, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBorder(BorderFactory.createEmptyBorder(PANEL_PAD, PANEL_PAD, PANEL_PAD, PANEL_PAD));
        centerPanel.add(topPanel, BorderLayout.NORTH);
        /* Wrap the JTable in a scrolling pane and add to the center of panel */
        centerPanel.add(new JScrollPane(problemsTable), BorderLayout.CENTER);
        /* Add center panel in center of frame */
        add(centerPanel, BorderLayout.CENTER);

        /* Add credit footer using helper functions. */
        add(GuiConstants.createCreditsPanel(), BorderLayout.SOUTH);
        setVisible(true);
    }

}

