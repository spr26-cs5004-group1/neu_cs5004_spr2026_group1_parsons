package com.parsons.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Unit tests for ParsonsProblem.
 * Tests constructor behavior, default values, setters, and null guards.
 */
public class ParsonsProblemTests {

    private List<CodeBlock> code;

    /**
     * Creates an empty code list before each test.
     */
    @BeforeEach
    void setUp() {
        code = new ArrayList<>();
    }

    /**
     * Verifies that a new ParsonsProblem has a default id of -1
     * before being saved to the repository.
     */
    @Test
    void defaultIdShouldBeMinusOne() {
        ParsonsProblem problem = new ParsonsProblem("Title", "Instructions", code);
        assertEquals(-1, problem.getId());
    }

    /**
     * Verifies that the constructor correctly sets the title field.
     */
    @Test
    void constructorShouldSetTitle() {
        ParsonsProblem problem = new ParsonsProblem("My Title", "Instructions", code);
        assertEquals("My Title", problem.getTitle());
    }

    /**
     * Verifies that the constructor correctly sets the instructions field.
     */
    @Test
    void constructorShouldSetInstructions() {
        ParsonsProblem problem = new ParsonsProblem("Title", "My Instructions", code);
        assertEquals("My Instructions", problem.getInstructions());
    }

    /**
     * Verifies that the constructor correctly sets the code list.
     */
    @Test
    void constructorShouldSetCode() {
        code.add(new CodeBlock("return;", false, 0));
        ParsonsProblem problem = new ParsonsProblem("Title", "Instructions", code);
        assertEquals(1, problem.getCode().size());
    }

    /**
     * Verifies that passing null for code throws an
     * IllegalArgumentException as a null guard.
     */
    @Test
    void nullCodeShouldThrowException() {
        assertThrows(IllegalArgumentException.class,
                () -> new ParsonsProblem("Title", "Instructions", null));
    }

    /**
     * Verifies that an empty code list is valid —
     * a problem setter may save a draft before adding blocks.
     */
    @Test
    void emptyCodeListShouldBeValid() {
        ParsonsProblem problem = new ParsonsProblem("Title", "Instructions", code);
        assertNotNull(problem.getCode());
        assertTrue(problem.getCode().isEmpty());
    }

    /**
     * Verifies that setTitle() correctly updates the title field.
     */
    @Test
    void setTitleShouldUpdateTitle() {
        ParsonsProblem problem = new ParsonsProblem("Old Title", "Instructions", code);
        problem.setTitle("New Title");
        assertEquals("New Title", problem.getTitle());
    }

    /**
     * Verifies that setInstructions() correctly updates the instructions field.
     */
    @Test
    void setInstructionsShouldUpdateInstructions() {
        ParsonsProblem problem = new ParsonsProblem("Title", "Old Instructions", code);
        problem.setInstructions("New Instructions");
        assertEquals("New Instructions", problem.getInstructions());
    }

    /**
     * Verifies that setCode() correctly updates the code list.
     */
    @Test
    void setCodeShouldUpdateCodeList() {
        ParsonsProblem problem = new ParsonsProblem("Title", "Instructions", code);
        List<CodeBlock> newCode = new ArrayList<>();
        newCode.add(new CodeBlock("int i;", false, 0));
        problem.setCode(newCode);
        assertEquals(1, problem.getCode().size());
    }

    /**
     * Verifies that setCode() throws IllegalArgumentException
     * when null is passed.
     */
    @Test
    void setCodeWithNullShouldThrowException() {
        ParsonsProblem problem = new ParsonsProblem("Title", "Instructions", code);
        assertThrows(IllegalArgumentException.class,
                () -> problem.setCode(null));
    }

    /**
     * Verifies that a happy path problem with valid fields
     * and code blocks is created successfully.
     */
    @Test
    void happyPathShouldCreateValidProblem() {
        code.add(new CodeBlock("int i;", false, 0));
        code.add(new CodeBlock("i = 0;", false, 1));
        code.add(new CodeBlock("while (i <= 10) {", true, null));
        ParsonsProblem problem = new ParsonsProblem("Title", "Instructions", code);
        assertEquals(3, problem.getCode().size());
        assertEquals("Title", problem.getTitle());
        assertEquals("Instructions", problem.getInstructions());
    }
}
