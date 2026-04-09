package com.parsons.controller.setter;

import com.parsons.model.CodeBlock;
import com.parsons.model.ParsonsProblem;
import com.parsons.service.ParsonsProblemsService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Controller class for Problem setter, allows problem creation via CLI.
 */
public class SetterCli {

    /**
     * Instance of service for parsons problem management.
     */
    private ParsonsProblemsService service;

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
                // TODO: UPDATE TO OUTPUTSTREAM
                System.out.printf("Problems Updated: %d\nNewProblemsSaved: %d\n", updated, added);
            }
        } catch (IOException e) {
            System.out.printf("Error while processing input file: %s\n", e);
            return;
        }
    }

    /**
     * File parser helper method for extracting new parsons problem from a .txt file.
     *
     * @param filepath the user provided path to the new problems file.
     * @return A list of parsed ParsonsProblem objects.
     * @throws IOException Occurs when the input file cannot be found or read.
     */
    private List<ParsonsProblem> parseFile(String filepath) throws IOException {
        //TODO: if time allows abstract this out to its own parser class.
        int errorCount = 0;
        List<String> errorLines = new ArrayList<>();

        List<ParsonsProblem> problemList = new ArrayList<>();
        // Note: Caller is responsible for error catching/reporting
        List<String> lines = Files.readAllLines(Path.of(filepath));
        List<List<String>> problemChunks = new ArrayList<>();
        List<String> current = new ArrayList<>();


        for (String line : lines) {
            if (line.trim().isEmpty() || line.trim().startsWith("//")) continue;
            if (line.trim().equals("---")) {
                problemChunks.add(current);
                current = new ArrayList<>();
            } else {
                current.add(line);
            }
        }

        problemChunks.add(current);

        for (List<String> chunk : problemChunks) {
            try {
                int id = Integer.parseInt(chunk.getFirst());
                String instructions = chunk.get(1);
                List<CodeBlock> codeBlocks = new ArrayList<>();

                for (int i = 2; i < chunk.size(); i++) {
                    String[] parts = chunk.get(i).split("\\|");
                    boolean isDistractor = parts[0].trim().toLowerCase(Locale.ROOT).startsWith("t");
                    Integer orderIndex = Integer.parseInt(parts[1].trim());
                    String codeContent = parts[2].stripTrailing();
                    codeBlocks.add(new CodeBlock(codeContent, isDistractor, orderIndex));
                }
                // TODO: title set in gui editor, but probably should add title to file, that way fresh ones dont
                // have to be titled manually.
                ParsonsProblem problem = new ParsonsProblem("", instructions, codeBlocks);
                problem.setId(id);
                problemList.add(problem);
            } catch (Exception e) {
                // TODO: instantiate these variables as fields instead of local so they can
                //  be reported in error catch under the run method.
                errorCount++;
                errorLines.add("Failed to parse chunk starting at: '" + (chunk.isEmpty() ?
                        "(empty)" : chunk.getFirst()) + "' \n" + e.getMessage());
            }
        }
        return problemList;
    }
}