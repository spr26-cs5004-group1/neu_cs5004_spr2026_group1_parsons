package com.parsons.controller.student;
import com.parsons.controller.DummyData;
import javax.swing.*;

public class SolverViewTest {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() ->
                new SolverView(DummyData.getProblems().get(0))
        );
    }
}
