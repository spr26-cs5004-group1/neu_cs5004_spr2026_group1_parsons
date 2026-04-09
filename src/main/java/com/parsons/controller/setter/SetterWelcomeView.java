package com.parsons.controller.setter;

import com.parsons.controller.Utils;
import com.parsons.model.ParsonsProblem;
import com.parsons.service.ParsonsProblemsService;

import java.awt.*;
import java.util.List;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;



import static com.parsons.controller.Utils.*;
import static com.parsons.controller.Utils.PANEL_PAD;

public class SetterWelcomeView extends JFrame {
    public SetterWelcomeView(ParsonsProblemsService service) {
        setTitle("Parsons Problems: Setter's View");
        setSize(NARROW_FRAME_WIDTH, FRAME_HEIGHT);
        JLabel welcome = new JLabel("Welcome Setter!", JLabel.LEFT);
        JTextArea instr = new JTextArea("""
                Add a new problem: click first row,\s
                or select a parson's problem from the table to edit below.
                The problem will open in a new window.""");
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
        /* First line is add new problem */
        tableModel.addRow(new Object[]{"NEW", "Add New Problem..."});
        /* fill the tableModel. DefaultTableModel only accepts array of Object class */
        List<ParsonsProblem> problems = service.getAllProblems();
        for (ParsonsProblem p : problems) {
            tableModel.addRow(new Object[]{p.getId(), p.getTitle()});
        }

        /* Create a JTable object that displays the datasheet tableModel */
        JTable problemsTable = new JTable(tableModel);

        /* Now to tackle making the mouse click open a new window */
        problemsTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = problemsTable.getSelectedRow();
                if (row != -1) {// protect against click anywhere but a row, which leads to deselection
                    String ids = (String) tableModel.getValueAt(row, 0);
                    if (ids.equals("NEW") || row == 0) {
                        // opens new editorView
                        new EditorView(null, service);
                    } else {
                        // find problem with id from problems
                        int id = (int) tableModel.getValueAt(row, 0); // extract id from row
                        ParsonsProblem selected = problems.stream()
                                .filter(p -> p.getId() == id)
                                .findFirst()
                                .orElse(null);
                        new EditorView(selected, service);
                    }
                }
            }
        });
        /* Add nav bar using helper functions. */
        JPanel navBar = Utils.createNavBar(false,false, this);
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
        add(Utils.createCreditsPanel(), BorderLayout.SOUTH);
        setVisible(true);
    }
}
