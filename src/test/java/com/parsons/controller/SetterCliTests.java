package com.parsons.controller;

import com.parsons.controller.setter.SetterCli;
import com.parsons.model.CodeBlock;
import com.parsons.model.ParsonsProblem;
import com.parsons.repository.IParsonsProblemsRepository;
import com.parsons.service.ParsonsProblemsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test Class for SetterCli functionality.
 */
public class SetterCliTests {

    @TempDir
    Path tempDir;

    private IParsonsProblemsRepository repository;
    private ParsonsProblemsService service;
    private SetterCli cli;
    private List<CodeBlock> code;

    /**
     * Creates a fresh in-memory anonymous repository, service,
     * and test data before each test.
     */
    @BeforeEach
    void setUp() {
        code = new ArrayList<>();
        code.add(new CodeBlock("int i;", false, 0));
        code.add(new CodeBlock("i = 0;", false, 1));
        code.add(new CodeBlock("while (i < 10) {", false, 2));
        code.add(new CodeBlock("while (i <= 10) {", true, null));

        repository = new IParsonsProblemsRepository() {

            private final Map<Integer, ParsonsProblem> store = new HashMap<>();
            private int nextId = 1;

            @Override
            public int save(ParsonsProblem p) {
                if (p.getId() == -1) {
                    p.setId(nextId++);
                }
                store.put(p.getId(), p);
                return p.getId();
            }

            @Override
            public ParsonsProblem findById(int id) {
                return store.get(id);
            }

            @Override
            public List<ParsonsProblem> findAll() {
                return new ArrayList<>(store.values());
            }

            @Override
            public void deleteById(int id) {
                store.remove(id);
            }

            @Override
            public boolean existsById(int id) {
                return store.containsKey(id);
            }
        };

        service = new ParsonsProblemsService(repository);
        cli = new SetterCli(service);
    }

    /**
     * Writes content to a temp file and returns its absolute path string.
     */
    private String writeTempFile(String filename, String content) throws IOException {
        Path file = tempDir.resolve(filename);
        Files.writeString(file, content);
        return file.toString();
    }

    /**
     * Verifies that run() saves a new problem to the service
     * when the problem id does not already exist.
     */
    @Test
    void runSavesNewProblem() throws IOException {
        String path = writeTempFile("problems.txt",
                "1\nArrange the loop\nf|0|int i;\nf|1|i = 0;\nt|2|while (i <= 10) {\n");
        cli.run(path);
        assertNotNull(service.getProblemById(1));
    }

    /**
     * Verifies that run() updates an existing problem in the service
     * when the problem id already exists.
     */
    @Test
    void runUpdatesExistingProblem() throws IOException {
        ParsonsProblem existing = new ParsonsProblem("Old", "Old Instructions", code);
        existing.setId(1);
        service.saveProblem(existing);

        String path = writeTempFile("problems.txt",
                "1\nNew Instructions\nf|0|int i;\nf|1|i = 0;\n");
        cli.run(path);
        assertEquals("New Instructions", service.getProblemById(1).getInstructions());
    }

    /**
     * Verifies that run() correctly handles a file with a mix of
     * new and existing problem ids.
     */
    @Test
    void runHandlesMixOfNewAndExistingProblems() throws IOException {
        ParsonsProblem existing = new ParsonsProblem("Old", "Old Instructions", code);
        existing.setId(1);
        service.saveProblem(existing);

        String path = writeTempFile("problems.txt",
                "1\nUpdated Instructions\nf|0|int i;\n---\n2\nNew Problem\nf|0|int x;\n");
        cli.run(path);
        assertEquals("Updated Instructions", service.getProblemById(1).getInstructions());
        assertNotNull(service.getProblemById(2));
    }


    /**
     * Verifies that parseFile correctly parses the expected number of code blocks.
     */
    @Test
    void parseFileCorrectlyParsesCodeBlocks() throws IOException {
        String path = writeTempFile("problems.txt",
                "1\nInstructions\nf|0|int i;\nf|1|i = 0;\nt|2|while (i <= 10) {\n");
        cli.run(path);
        ParsonsProblem saved = service.getProblemById(1);
        assertNotNull(saved);
        assertEquals(3, saved.getCode().size());
    }

    /**
     * Verifies that parseFile correctly parses multiple problems
     * separated by --- delimiters.
     */
    @Test
    void parseFileCorrectlyParsesMultipleProblems() throws IOException {
        String path = writeTempFile("problems.txt",
                "1\nFirst\nf|0|int i;\n---\n2\nSecond\nf|0|int x;\n");
        cli.run(path);
        assertNotNull(service.getProblemById(1));
        assertNotNull(service.getProblemById(2));
    }

    /**
     * Verifies that parseFile skips comment lines and blank lines
     * without affecting valid problem parsing.
     */
    @Test
    void parseFileSkipsCommentAndBlankLines() throws IOException {
        String path = writeTempFile("problems.txt",
                "// this is a comment\n\n1\nInstructions\nf|0|int i;\n\n// another comment\n");
        cli.run(path);
        assertNotNull(service.getProblemById(1));
        assertEquals(1, service.getAllProblems().size());
    }

    /**
     * Verifies that a chunk missing the instructions line is skipped
     * while subsequent valid chunks are still parsed.
     * The valid chunk receives id=1 since ids are auto-assigned.
     */
    @Test
    void parseFileSkipsChunkMissingInstructionsLine() throws IOException {
        String path = writeTempFile("problems.txt",
                "1\n---\n2\nValid\nf|0|int x;\n");
        cli.run(path);
        assertEquals(1, service.getAllProblems().size());
        assertNotNull(service.getProblemById(1));
    }

    /**
     * Verifies that a chunk with a code block line missing pipe separators
     * is skipped while subsequent valid chunks are still parsed.
     * The valid chunk receives id=1 since ids are auto-assigned.
     */
    @Test
    void parseFileSkipsChunkWithMissingPipeSeparators() throws IOException {
        String path = writeTempFile("problems.txt",
                "1\nInstructions\nnopipes\n---\n2\nValid\nf|0|int x;\n");
        cli.run(path);
        assertEquals(1, service.getAllProblems().size());
        assertNotNull(service.getProblemById(1));
    }

    /**
     * Verifies that a chunk with a non-numeric order index is skipped
     * while subsequent valid chunks are still parsed.
     * The valid chunk receives id=1 since ids are auto-assigned.
     */
    @Test
    void parseFileSkipsChunkWithNonNumericOrderIndex() throws IOException {
        String path = writeTempFile("problems.txt",
                "1\nInstructions\nf|notanumber|int i;\n---\n2\nValid\nf|0|int x;\n");
        cli.run(path);
        assertEquals(1, service.getAllProblems().size());
        assertNotNull(service.getProblemById(1));
    }
}
