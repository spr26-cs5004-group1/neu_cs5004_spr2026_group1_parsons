package com.parsons.controller.student;
import com.parsons.controller.DummyData;

import javax.swing.*;

public class StudentWelcomeViewTest {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() ->
                new StudentWelcomeView(DummyData.getProblems())
        );
    }
}
