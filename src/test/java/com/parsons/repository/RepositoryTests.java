package com.parsons.repository;

import com.parsons.model.CodeBlock;
import com.parsons.model.ParsonsProblem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Tests for XmlParsonsProblemsRepository.
 * Tests actual XML file read/write operations
 * temporary directory that is automatically cleaned up after each test.
 */
public class RepositoryTests {

    @TempDir
    Path tempDir;

    private XmlParsonsProblemsRepository repo;
    private List<CodeBlock> code;

    /**
     * Creates a fresh repository pointing to a temp XML file
     * and a sample code list before each test.
     */
    @BeforeEach
    void setUp() {
        String testFile = tempDir.resolve("test-problems.xml").toString();
        repo = new XmlParsonsProblemsRepository(testFile);
        code = new ArrayList<>();
        code.add(new CodeBlock("int i;", false, 0));
        code.add(new CodeBlock("i = 0;", false, 1));
        code.add(new CodeBlock("while (i < 10) {", false, 2));
        code.add(new CodeBlock("while (i <= 10) {", true, null));
    }

    /**
     * Verifies that a saved problem can be retrieved by its assigned id
     * with matching title and instructions — tests XML write then read.
     */
    @Test
    void savedProblemCanBeRetrievedById() {
        ParsonsProblem problem = new ParsonsProblem("Try This", "Arrange these", code);
        int assignedId = repo.save(problem);
        ParsonsProblem retrieved = repo.findById(assignedId);
        assertNotNull(retrieved);
        assertEquals("Try This", retrieved.getTitle());
        assertEquals("Arrange these", retrieved.getInstructions());
    }

    /**
     * Verifies that a saved problem's code blocks are
     * correctly serialized and deserialized from XML.
     */
    @Test
    void savedProblemShouldPreserveCodeBlocks() {
        ParsonsProblem problem = new ParsonsProblem("Try This", "Arrange these", code);
        int assignedId = repo.save(problem);
        ParsonsProblem retrieved = repo.findById(assignedId);
        assertNotNull(retrieved);
        assertEquals(4, retrieved.getCode().size());
        assertEquals("int i;", retrieved.getCode().get(0).getCodeContent());
        assertEquals("while (i <= 10) {", retrieved.getCode().get(3).getCodeContent());
        assertTrue(retrieved.getCode().get(3).getIsDistractor());
    }

    /**
     * Verifies that a new problem is assigned id 1
     * when the repository is empty.
     */
    @Test
    void firstSavedProblemShouldHaveIdOne() {
        ParsonsProblem problem = new ParsonsProblem("Try This", "Arrange these", code);
        int assignedId = repo.save(problem);
        assertEquals(1, assignedId);
    }

    /**
     * Verifies that sequential saves assign incrementing ids.
     */
    @Test
    void sequentialSavesShouldAssignIncrementingIds() {
        int id1 = repo.save(new ParsonsProblem("Problem 1", "Instructions 1", code));
        int id2 = repo.save(new ParsonsProblem("Problem 2", "Instructions 2", code));
        assertEquals(1, id1);
        assertEquals(2, id2);
    }

    /**
     * Verifies that searching for a non-existent id returns null.
     */
    @Test
    void findByIdWithNonExistentIdShouldReturnNull() {
        assertNull(repo.findById(999));
    }

    /**
     * Verifies that findAll() returns all saved problems
     * after multiple saves.
     */
    @Test
    void findAllShouldReturnAllSavedProblems() {
        repo.save(new ParsonsProblem("Problem 1", "Instructions 1", code));
        repo.save(new ParsonsProblem("Problem 2", "Instructions 2", code));
        repo.save(new ParsonsProblem("Problem 3", "Instructions 3", code));
        assertEquals(3, repo.findAll().size());
    }

    /**
     * Verifies that findAll() returns an empty list
     * when no problems have been saved.
     */
    @Test
    void findAllOnEmptyRepoShouldReturnEmptyList() {
        assertTrue(repo.findAll().isEmpty());
    }

    /**
     * Verifies that a deleted problem can no longer
     * be retrieved by its id.
     */
    @Test
    void deletedProblemShouldNotBeRetrievable() {
        int assignedId = repo.save(new ParsonsProblem("Try This", "Arrange these", code));
        repo.deleteById(assignedId);
        assertNull(repo.findById(assignedId));
    }

    /**
     * Verifies that deleting one problem does not affect others.
     */
    @Test
    void deletingOneProblemShouldNotAffectOthers() {
        int id1 = repo.save(new ParsonsProblem("Problem 1", "Instructions 1", code));
        int id2 = repo.save(new ParsonsProblem("Problem 2", "Instructions 2", code));
        repo.deleteById(id1);
        assertNull(repo.findById(id1));
        assertNotNull(repo.findById(id2));
        assertEquals(1, repo.findAll().size());
    }

    /**
     * Verifies that existsById() returns true for a saved problem.
     */
    @Test
    void existsByIdShouldReturnTrueForSavedProblem() {
        int assignedId = repo.save(new ParsonsProblem("Try This", "Arrange these", code));
        assertTrue(repo.existsById(assignedId));
    }

    /**
     * Verifies that existsById() returns false for a non-existent id.
     */
    @Test
    void existsByIdShouldReturnFalseForNonExistentId() {
        assertFalse(repo.existsById(999));
    }

    /**
     * Verifies that updating a problem persists the new
     * title and instructions to the XML file.
     */
    @Test
    void updatedProblemShouldPersistChanges() {
        ParsonsProblem problem = new ParsonsProblem("Original Title", "Original Instructions", code);
        int assignedId = repo.save(problem);
        problem.setTitle("Updated Title");
        problem.setInstructions("Updated Instructions");
        repo.save(problem);
        ParsonsProblem retrieved = repo.findById(assignedId);
        assertNotNull(retrieved);
        assertEquals("Updated Title", retrieved.getTitle());
        assertEquals("Updated Instructions", retrieved.getInstructions());
    }

    /**
     * Verifies that the XML file is created on disk after saving.
     */
    @Test
    void saveShouldCreateXmlFile() {
        repo.save(new ParsonsProblem("Try This", "Arrange these", code));
        assertTrue(new File(tempDir.resolve("test-problems.xml").toString()).exists());
    }
}
