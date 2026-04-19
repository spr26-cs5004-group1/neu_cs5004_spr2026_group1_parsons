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

    /**
     * Constructor for SetterWelcomeView.
     *
     * @param service the service used to load and persist Parsons problems.
     * @param name    the setter's account name, displayed in the welcome greeting.
     */
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
        getTableModel().addRow(new Object[]{"NEW", "Add New Problem..."});
        for (ParsonsProblem p : getParsonsProblems()) {
            getTableModel().addRow(new Object[]{p.getId(), p.getTitle()});
        }
    }

    @Override
    protected void onRowSelected(int row) {
        Object idValue = getTableModel().getValueAt(row, 0);
        if (row == 0 || idValue.equals("NEW")) {
            new EditorView(null, getService(), name, this);
        } else {
            int id = (int) idValue;
            ParsonsProblem selected = getParsonsProblems().stream()
                    .filter(p -> p.getId() == id)
                    .findFirst()
                    .orElse(null);
            new EditorView(selected, getService(), name, this);
        }
    }
}
