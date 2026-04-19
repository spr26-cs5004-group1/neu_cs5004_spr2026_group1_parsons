package com.parsons.controller.setter;

import com.parsons.controller.WelcomeView;
import com.parsons.model.ParsonsProblem;
import com.parsons.service.ParsonsProblemsService;

/**
 * SetterWelcomeView displays the list of Parsons problems for a problem setter.
 * The first row always offers an "Add New Problem" option.
 * Selecting any other row opens the problem in an EditorView.
 */
public class SetterWelcomeView extends WelcomeView {

    /** Stores the name passed to this. */
    private final String name;

    /** Constructor for SetterWelcomeView. */
    public SetterWelcomeView(ParsonsProblemsService service, String name) {
        super(
            service,
            "Parsons Problems: Setter's View",
            "Welcome " + name + "!",
            """
            Add a new problem: click first row,\s
            or select a parson's problem from the table to edit below.
            The problem will open in a new window."""
        );
        this.name = name;
    }

    @Override
    protected void populateTable() {
        tableModel.addRow(new Object[]{"NEW", "Add New Problem..."});
        for (ParsonsProblem p : problems) {
            tableModel.addRow(new Object[]{p.getId(), p.getTitle()});
        }
    }

    @Override
    protected void onRowSelected(int row) {
        Object idValue = tableModel.getValueAt(row, 0);
        if (row == 0 || idValue.equals("NEW")) {
            new EditorView(null, service, name, this);
        } else {
            int id = (int) idValue;
            ParsonsProblem selected = problems.stream()
                    .filter(p -> p.getId() == id)
                    .findFirst()
                    .orElse(null);
            new EditorView(selected, service, name, this);
        }
    }
}
