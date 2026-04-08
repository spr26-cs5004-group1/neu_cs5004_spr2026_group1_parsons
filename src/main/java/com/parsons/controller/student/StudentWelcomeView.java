package com.parsons.controller.student;
import com.parsons.controller.GuiConstants;
import com.parsons.model.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * ref: https://docs.oracle.com/javase/tutorial/uiswing/events/intro.html
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
        JTable problemsTable = new JTable(tableModel);

    }
}

