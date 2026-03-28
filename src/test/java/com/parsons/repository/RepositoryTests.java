package com.parsons.repository;

import com.parsons.model.CodeBlock;
import com.parsons.model.ParsonsProblem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Unit tests for repository operations.
 * Uses an anonymous class implementation of IParsonsProblemsRepository
 * to test repository behavior in memory without touching the XML file.
 */
public class RepositoryTests {

    private IParsonsProblemsRepository repo;
    private List<CodeBlock> code;

    /**
     * Creates a fresh anonymous repo as a Map
     * and an empty code list before each test.
     */
    @BeforeEach
    void setUp() {
        code = new ArrayList<>();
        code.add(new CodeBlock("return;", false, 0));

        repo = new IParsonsProblemsRepository() {

            private final Map<Integer, ParsonsProblem> store = new HashMap<>();
            private int nextId = 1;

            /**
             * Saves a ParsonsProblem to the in-memory store.
             * Assigns a new id if the problem has the default id of -1.
             * Updates the existing entry otherwise.
             */
            @Override
            public int save(ParsonsProblem problem) {
                if (problem.getId() == -1) {
                    problem.setId(nextId++);
                }
                store.put(problem.getId(), problem);
                return problem.getId();
            }

            /**
             * Retrieves a ParsonsProblem by its id.
             * Returns null if not found.
             */
            @Override
            public ParsonsProblem findById(int id) {
                return store.get(id);
            }

            /**
             * Returns all ParsonsProblem objects in the store.
             */
            @Override
            public List<ParsonsProblem> findAll() {
                return new ArrayList<>(store.values());
            }

            /**
             * Deletes the ParsonsProblem with the given id from the store.
             */
            @Override
            public void deleteById(int id) {
                store.remove(id);
            }

            /**
             * Returns true if a ParsonsProblem with the given id exists.
             */
            @Override
            public boolean existsById(int id) {
                return store.containsKey(id);
            }
        };
    }

    /**
     * Verifies that a saved ParsonsProblem can be
     * retrieved by its assigned id with matching fields.
     */
    @Test
    void savedProblemCanBeRetrievedById() {
        ParsonsProblem problem = new ParsonsProblem("Try This", "Arrange these", code);
        int assignedId = repo.save(problem);
        ParsonsProblem retrieved = repo.findById(assignedId);
        assertNotNull(retrieved);
        assertEquals(problem.getTitle(), retrieved.getTitle());
        assertEquals(problem.getInstructions(), retrieved.getInstructions());
    }

    /**
     * Verifies that searching for a non-existent id returns null.
     */
    @Test
    void callingForAnIdThatDoesNotExistShouldReturnNull() {
        ParsonsProblem problem = new ParsonsProblem("Try This", "Arrange these", code);
        int assignedId = repo.save(problem);
        ParsonsProblem retrieved = repo.findById(assignedId + 10);
        assertNull(retrieved);
    }

    /**
     * Verifies that a deleted ParsonsProblem can no
     * longer be retrieved by its id.
     */
    @Test
    void callingForAnIdThatIsRemovedShouldReturnNull() {
        ParsonsProblem problem = new ParsonsProblem("Try This", "Arrange these", code);
        int assignedId = repo.save(problem);
        repo.deleteById(assignedId);
        assertNull(repo.findById(assignedId));
    }

    /**
     * Verifies that findAll() returns all saved problems.
     */
    @Test
    void findAllShouldReturnAllSavedProblems() {
        repo.save(new ParsonsProblem("Problem 1", "Instructions 1", code));
        repo.save(new ParsonsProblem("Problem 2", "Instructions 2", code));
        assertEquals(2, repo.findAll().size());
    }

    /**
     * Verifies that existsById() returns true for a saved problem.
     */
    @Test
    void existsByIdShouldReturnTrueForSavedProblem() {
        ParsonsProblem problem = new ParsonsProblem("Try This", "Arrange these", code);
        int assignedId = repo.save(problem);
        assertTrue(repo.existsById(assignedId));
    }

    /**
     * Verifies that existsById() returns false for a non-existent id.
     */
    @Test
    void existsByIdShouldReturnFalseForNonExistentId() {
        assertFalse(repo.existsById(999));
    }
}
