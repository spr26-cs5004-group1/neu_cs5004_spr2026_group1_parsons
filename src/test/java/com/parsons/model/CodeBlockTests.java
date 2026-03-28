package com.parsons.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for CodeBlock.
 * Tests constructor behavior, default values, and setters.
 */
public class CodeBlockTests {

    private CodeBlock block;

    /**
     * Creates a default CodeBlock before each test.
     */
    @BeforeEach
    void setUp() {
        block = new CodeBlock("return;", false, 0);
    }

    /**
     * Verifies that a new CodeBlock has a default id of -1
     * before being saved to the repository.
     */
    @Test
    void defaultIdShouldBeMinusOne() {
        assertEquals(-1, block.getId());
    }

    /**
     * Verifies that the constructor correctly sets the codeContent field.
     */
    @Test
    void constructorShouldSetCodeContent() {
        assertEquals("return;", block.getCodeContent());
    }

    /**
     * Verifies that the constructor correctly sets the isDistractor field.
     */
    @Test
    void constructorShouldSetIsDistractor() {
        assertFalse(block.getIsDistractor());
    }

    /**
     * Verifies that the constructor correctly sets the orderIndex field.
     */
    @Test
    void constructorShouldSetOrderIndex() {
        assertEquals(0, block.getOrderIndex());
    }

    /**
     * Verifies that setId() correctly updates the id field.
     */
    @Test
    void setIdShouldUpdateId() {
        block.setId(5);
        assertEquals(5, block.getId());
    }

    /**
     * Verifies that setCodeContent() correctly updates the codeContent field.
     */
    @Test
    void setCodeContentShouldUpdateContent() {
        block.setCodeContent("int i;");
        assertEquals("int i;", block.getCodeContent());
    }

    /**
     * Verifies that a distractor block can be created with a null orderIndex,
     * since distractors do not have a position in the correct solution.
     */
    @Test
    void distractorBlockShouldHaveNullOrderIndex() {
        CodeBlock distractor = new CodeBlock("while (i <= 10) {", true, null);
        assertTrue(distractor.getIsDistractor());
        assertNull(distractor.getOrderIndex());
    }

    /**
     * Verifies that the empty constructor creates a CodeBlock
     * with the default id of -1.
     */
    @Test
    void emptyConstructorShouldHaveDefaultId() {
        CodeBlock empty = new CodeBlock();
        assertEquals(-1, empty.getId());
    }
}
