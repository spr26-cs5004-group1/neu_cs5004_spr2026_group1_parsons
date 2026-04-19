package com.parsons.controller.setter;

import com.parsons.model.ParsonsProblem;
import com.parsons.service.ParsonsProblemsService;

import java.io.IOException;
import java.util.List;

import static com.parsons.controller.Utils.parseFile;

/**
 * Controller class for Problem setter, allows problem creation via CLI.
 */
public class SetterCli implements ICli {

    /**
     * Instance of service for parsons problem management.
     */
    private final ParsonsProblemsService service;

    /**
     * SetterCLI constructor that assigns and uses the passed in service instance..
     *
     * @param service Instance of service for parsons problem management.
     */
    public SetterCli(ParsonsProblemsService service) {
        this.service = service;
    }

    /**
     * Setter CLI main run method, contains logic for parsing, tracking, and storing
     * a new problem or list of problems provided via text file.
     *
     * @param filePath the user provided path to the new problems file.
     */
    public void run(String filePath) {
        try {
            int added = 0;

            List<ParsonsProblem> problemList = parseFile(filePath);

            for (ParsonsProblem parsonsProblem : problemList) {

                service.saveProblem(parsonsProblem);
                added++;

                System.out.printf("New Problems Saved: %d\n", added);
            }
            return;
        } catch (IOException e) {
            System.out.printf("Error while processing input file: %s\n", e);
            return;
        }
    }
}
