package com.parsons.controller.setter;

import com.parsons.model.ParsonsProblem;
import com.parsons.service.ParsonsProblemsService;
import java.io.IOException;
import java.util.List;

import static com.parsons.controller.Utils.parseFile;

/**
 * Controller class for Problem setter, allows problem creation via CLI.
 */
public class SetterCli {

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
            int updated = 0;
            int added = 0;
            //call parseFile(filepath)
            List<ParsonsProblem> problemList = parseFile(filePath);
            //call parseFile(filepath)
            for (int i = 0; i < problemList.size(); i++) {
                if (service.getProblemById(problemList.get(i).getId()) != null) {
                    service.updateProblem(problemList.get(i).getId(), problemList.get(i));
                    updated++;
                } else {
                    service.saveProblem(problemList.get(i));
                    added++;
                }
                // TODO: UPDATE TO OUTPUT STREAM
                System.out.printf("Problems Updated: %d\nNewProblemsSaved: %d\n", updated, added);
            }
        } catch (IOException e) {
            System.out.printf("Error while processing input file: %s\n", e);
            return;
        }
    }

}
