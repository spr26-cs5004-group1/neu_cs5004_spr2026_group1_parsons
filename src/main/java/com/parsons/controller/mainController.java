package com.parsons.controller;

import com.parsons.model.ParsonsProblem;
import com.parsons.service.ParsonsProblemsService;
import com.parsons.controller.student.*;
import com.parsons.controller.setter.*;
import static com.parsons.controller.GuiConstants.*;
import javax.swing.*;
import java.awt.*;


public class MainController extends JFrame {
    /*
    +----------------------------------+
    |   Parsons Problems               |
    |                                  |
    |  Account Name: [__________]      |
    |                                  |
    |  [ Setter ]      [ Student ]     |
    +----------------------------------+
     */
    public MainController(ParsonsProblemsService service) {
        /* Set up JFrame */
        this.setTitle("Parson's Problem App: Choose Your Role");
        this.setSize(LAUNCHER_WIDTH, LAUNCHER_HEIGHT);

        /* Create a centerPanel that will hold accountName, and buttonPanel (setterButton, and studentButton) */
        JPanel centerPanel = new JPanel(new GridLayout(3, 1, TIGHT_GAP, TIGHT_GAP));

        /* AccountName field: accountName may be used to save personalized XML: new/maybe feature */
        JTextField accountNameField = new JTextField(20);
        JPanel namePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        namePanel.add(new JLabel("Account Name:"));
        namePanel.add(accountNameField);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        /* Buttons */
        JButton setterButton = new JButton("Setter");
        JButton studentButton = new JButton("Student");

        buttonPanel.add(setterButton);
        buttonPanel.add(studentButton);

        /* Populate centerPanel */
        centerPanel.add(new JLabel("Parsons Problems", JLabel.CENTER));
        centerPanel.add(namePanel);
        centerPanel.add(buttonPanel);

        /* add centerPanel to this */
        this.add(centerPanel, BorderLayout.CENTER);

        /* BUSINESS LOGIC */

        /* studentButton actionListener */
        studentButton.addActionListener(e -> {
            String name = accountNameField.getText().trim();
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter an account name.");
                return;
            }
            new StudentWelcomeView(service.getAllProblems(), service);
        });
        setVisible(true);
    }
}
