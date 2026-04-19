package com.parsons.controller.student;

import com.parsons.controller.WelcomeView;
import com.parsons.model.ParsonsProblem;
import com.parsons.service.ParsonsProblemsService;

/**
 * StudentWelcomeView displays the list of Parsons problems for a learner.
 * Selecting a row opens the problem in a SolverView.
 */
public class StudentWelcomeView extends WelcomeView {

    /**
     * Constructs a StudentWelcomeView with the given service.
     *
     * @param service the service used to load Parsons problems for the student.
     */
    public StudentWelcomeView(ParsonsProblemsService service) {
        super(
            service,
            "Parsons Problems: Student View",
            "Welcome Learner!",
            "Select a parson's problem from the table below.\n"
            + "The problem will open in a new window."
        );
    }

    @Override
    protected void populateTable() {
        for (ParsonsProblem p : getParsonsProblems()) {
            getTableModel().addRow(new Object[]{p.getId(), p.getTitle()});
        }
    }

    @Override
    protected void onRowSelected(int row) {
        int id = (int) getTableModel().getValueAt(row, 0);
        ParsonsProblem selected = getParsonsProblems().stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .orElse(null);
        int index = getParsonsProblems().indexOf(selected);
        new SolverView(selected, getService(), index);
    }
}
