package com.parsons.service;

import com.parsons.model.CodeBlock;
import com.parsons.model.ParsonsProblem;
import com.parsons.repository.IParsonsProblemsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Unit tests for ParsonsProblemsService.
 * Uses an anonymous class implementation of IParsonsProblemsRepository
 * to test service behaviour in memory without touching the XML file.
 */
public class ParsonsProblemsServiceTests {

    private IParsonsProblemsRepository repository;
    private ParsonsProblemsService service;
    private ParsonsProblem problem;
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
        problem = new ParsonsProblem("Title", "Instructions", code);

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
    }

    /**
     * Verifies that saveProblem() returns the assigned id.
     */
    @Test
    void testProperSaving() {
        int id = service.saveProblem(problem);
        assertEquals(1, id);
    }

    /**
     * Verifies that updateProblem() updates fields of an existing problem
     * and that the returned object reflects the new values.
     */
    @Test
    void testUpdateWithValidId() {
        int id = service.saveProblem(problem);
        ParsonsProblem updatedProblem = new ParsonsProblem("New Title", "New Instructions", code);
        ParsonsProblem result = service.updateProblem(id, updatedProblem);
        assertEquals("New Title", result.getTitle());
        assertEquals("New Instructions", result.getInstructions());
    }

    /**
     * Verifies that after updateProblem(), retrieving the problem
     * by id returns the updated values — confirming persistence.
     */
    @Test
    void testUpdatePersistsToRepository() {
        int id = service.saveProblem(problem);
        ParsonsProblem updatedProblem = new ParsonsProblem("New Title", "New Instructions", code);
        service.updateProblem(id, updatedProblem);
        ParsonsProblem retrieved = service.getProblemById(id);
        assertNotNull(retrieved);
        assertEquals("New Title", retrieved.getTitle());
        assertEquals("New Instructions", retrieved.getInstructions());
    }

    /**
     * Verifies that updateProblem() throws IllegalArgumentException
     * when the given id does not exist.
     */
    @Test
    void testUpdateWithInvalidId() {
        ParsonsProblem updatedProblem = new ParsonsProblem("New Title", "New Instructions", code);
        assertThrows(IllegalArgumentException.class,
                () -> service.updateProblem(999, updatedProblem));
    }

    /**
     * Verifies that deleteProblem() removes a problem from the repository
     * by checking via the service layer, not directly via the repository.
     */
    @Test
    void testDeleteWithValidId() {
        int id = service.saveProblem(problem);
        service.deleteProblem(id);
        assertNull(service.getProblemById(id));
    }

    /**
     * Verifies that deleteProblem() on a non-existent id
     * does not throw an exception.
     */
    @Test
    void testDeleteWithInvalidId() {
        assertDoesNotThrow(() -> service.deleteProblem(999));
    }

    /**
     * Verifies that getAllProblems() returns all saved problems.
     */
    @Test
    void testGetAllProblemsWithNonEmptyRepo() {
        service.saveProblem(problem);
        assertEquals(1, service.getAllProblems().size());
    }

    /**
     * Verifies that getAllProblems() returns an empty list
     * when no problems have been saved.
     */
    @Test
    void testGetAllProblemsWithEmptyRepo() {
        assertTrue(service.getAllProblems().isEmpty());
    }

    /**
     * Verifies that getProblemById() returns the correct problem
     * for a valid id.
     */
    @Test
    void testGetProblemByIdWithValidID() {
        int id = service.saveProblem(problem);
        ParsonsProblem retrieved = service.getProblemById(id);
        assertNotNull(retrieved);
        assertEquals(problem.getTitle(), retrieved.getTitle());
    }

    /**
     * Verifies that getProblemById() returns null for a non-existent id.
     */
    @Test
    void testGetByIdWithInvalidID() {
        assertNull(service.getProblemById(999));
    }

    /**
     * Verifies that checkAnswer() returns true for a correct answer
     * in the correct order without distractors.
     */
    @Test
    void testCheckAnswerCorrectAnswer() {
        int id = service.saveProblem(problem);
        List<CodeBlock> answer = new ArrayList<>();
        answer.add(new CodeBlock("int i;", false, 0));
        answer.add(new CodeBlock("i = 0;", false, 1));
        answer.add(new CodeBlock("while (i < 10) {", false, 2));
        assertTrue(service.checkAnswer(id, answer));
    }

    /**
     * Verifies that checkAnswer() returns false when blocks
     * are submitted in the wrong order.
     */
    @Test
    void testCheckAnswerCorrectAnswer_WrongOrder() {
        int id = service.saveProblem(problem);
        List<CodeBlock> answer = new ArrayList<>();
        answer.add(new CodeBlock("while (i < 10) {", false, 2));
        answer.add(new CodeBlock("int i;", false, 0));
        answer.add(new CodeBlock("i = 0;", false, 1));
        assertFalse(service.checkAnswer(id, answer));
    }

    /**
     * Verifies that checkAnswer() returns false when the answer
     * has the wrong number of blocks.
     */
    @Test
    void testCheckAnswerWrongAnswer_WrongLen() {
        int id = service.saveProblem(problem);
        List<CodeBlock> answer = new ArrayList<>();
        answer.add(new CodeBlock("int i;", false, 0));
        answer.add(new CodeBlock("i = 0;", false, 1));
        assertFalse(service.checkAnswer(id, answer));
    }

    /**
     * Verifies that checkAnswer() returns false when the answer
     * contains distractor blocks.
     */
    @Test
    void testCheckAnswerWrongAnswer_WithDistractors() {
        int id = service.saveProblem(problem);
        List<CodeBlock> answer = new ArrayList<>();
        answer.add(new CodeBlock("int i;", false, 0));
        answer.add(new CodeBlock("i = 0;", false, 1));
        answer.add(new CodeBlock("while (i <= 10) {", true, null));
        assertFalse(service.checkAnswer(id, answer));
    }

    /**
     * Verifies that checkAnswer() throws IllegalArgumentException
     * when the given id does not exist.
     */
    @Test
    void testCheckAnswerWithInvalidID() {
        List<CodeBlock> answer = new ArrayList<>();
        answer.add(new CodeBlock("int i;", false, 0));
        assertThrows(IllegalArgumentException.class,
                () -> service.checkAnswer(999, answer));
    }
}
