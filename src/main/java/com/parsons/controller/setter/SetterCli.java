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

public class SetterCli {

    private ParsonsProblemsService service;

    public SetterCli(ParsonsProblemsService service){
        this.service = service;
    }

    public void run(String filePath) {
        try {
            int updated = 0;
            int added = 0;
            //call parseFile(filepath)
            List<ParsonsProblem> problemList = parseFile(filePath);
            //call parseFile(filepath)
            for (int i = 0; i < problemList.size(); i++) {
                if (service.getProblemById(problemList.get(i).getId()) != null) {
                    service.updateProblem(i, problemList.get(i));
                    updated++;
                } else {
                    service.saveProblem(problemList.get(i));
                    added++;
                }
                // TODO: UPDATE TO OUTPUTSTREAM
                System.out.printf("Problems Updated: %d\nNewProblemsSaved: %d\n", updated, added);
            }
        } catch (IOException e) {
            // TODO: update this catch and class to take an outputstream, that way errors can bubble to here and send to gui or system.out
            return;
        }
    }

    private List<ParsonsProblem> parseFile(String filepath) throws IOException {
        List<ParsonsProblem> problemList = new ArrayList<>();

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

                ParsonsProblem problem = new ParsonsProblem("", instructions, codeBlocks);
                problem.setId(id);
                problemList.add(problem);
            }
        return problemList;
    }
}
