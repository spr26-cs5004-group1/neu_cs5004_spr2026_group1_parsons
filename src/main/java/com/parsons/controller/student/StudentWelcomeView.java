package com.parsons.controller.student;

import com.parsons.controller.WelcomeView;
import com.parsons.model.ParsonsProblem;
import com.parsons.service.ParsonsProblemsService;

/**
 * StudentWelcomeView displays the list of Parsons problems for a learner.
 * Selecting a row opens the problem in a SolverView.
 */
public class StudentWelcomeView extends WelcomeView {

    public StudentWelcomeView(ParsonsProblemsService service) {
        super(
            service,
            "Parsons Problems: Student View",
            "Welcome Learner!",
            "Select a parson's problem from the table below.\n" +
            "The problem will open in a new window."
        );
    }

    @Override
    protected void populateTable() {
        for (ParsonsProblem p : problems) {
            tableModel.addRow(new Object[]{p.getId(), p.getTitle()});
        }
    }

    @Override
    protected void onRowSelected(int row) {
        int id = (int) tableModel.getValueAt(row, 0);
        ParsonsProblem selected = problems.stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .orElse(null);
        int index = problems.indexOf(selected);
        new SolverView(selected, service, index);
    }
}
