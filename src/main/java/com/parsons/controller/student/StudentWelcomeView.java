package com.parsons.controller.student;
import com.parsons.controller.GuiConstants;
import com.parsons.model.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * StudentWelcomeView displays a list of Parsons problems for the student.
 * The student can click a row to open the problem in a new window.
 *
 * Layout:
 *   NORTH  -- welcome label + instructions
 *   CENTER -- scrollable table of problems (ID, Title)
 */
public class StudentWelcomeView extends JFrame implements GuiConstants {
    public StudentWelcomeView(List<ParsonsProblem> problems) {
        setTitle("Parsons Problems: Student View");
        setSize(NARROW_FRAME_WIDTH, FRAME_HEIGHT);
        JLabel welcome = new JLabel("Welcome Learner!", JLabel.LEFT);
        JTextArea instr = new JTextArea("Select a parson's problem from the table below.\n" +
                "The problem will open in a new window.");
        instr.setEditable(false);

        // add welcome and instructions to a grid layout panel stacked vertically
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridLayout(2, 1));
        topPanel.add(welcome);
        topPanel.add(instr);

        // Add this panel to the NORTH of the frame
        add(topPanel, BorderLayout.NORTH);

        // create the datasheet that will be displayed in the JTable below the welcome message and instructions table
        String[] columns = {"ID", "Title"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0);
        // fill the tableModel. DefaultTableModel only accepts array of Object class
        for (ParsonsProblem p : problems) {
            tableModel.addRow(new Object[]{p.getId(), p.getTitle()});
        }

        // create a JTable object that displays the datasheet tableModel
        JTable problemsTable = new JTable(tableModel);

        // wrap the JTable in a scrolling pane and add to the center of frame
        add(new JScrollPane(problemsTable), BorderLayout.CENTER);

        // now to tackle making the mouse click open a new window
        // doing this for the first time so this will be learning.
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
                    new SolverView(selected);   // a new SolverView window opens and stays open
                }
            }
        });
    }
}

