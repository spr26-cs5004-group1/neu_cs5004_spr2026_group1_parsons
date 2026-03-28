package com.parsons;

import com.parsons.repository.XmlParsonsProblemsRepository;
import com.parsons.service.ParsonsProblemsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;

/**
 * Integration tests for ParsonsApplication.
 * Verifies that the application components wire together correctly.
 */
class ParsonsApplicationTests {

    private ParsonsProblemsService service;

    /**
     * Sets up the service with a test XML file before each test.
     */
    @BeforeEach
    void setUp() {
        XmlParsonsProblemsRepository repository =
                new XmlParsonsProblemsRepository("data/test-problems.xml");
        service = new ParsonsProblemsService(repository);
    }

    /**
     * Verifies that the service is successfully instantiated
     * with a valid repository.
     */
    @Test
    void serviceShouldInitialiseCorrectly() {
        assertNotNull(service);
    }

    /**
     * Verifies that the repository XML file path is accessible
     * and the data directory can be created if needed.
     */
    @Test
    void dataDirShouldBeAccessible() {
        File dataDir = new File("data");
        assertTrue(dataDir.exists() || dataDir.mkdirs());
    }

    /**
     * Verifies that getAllProblems() returns a non-null list
     * even when the XML file does not exist yet.
     */
    @Test
    void getAllProblemsShouldReturnNonNullOnEmptyRepo() {
        assertNotNull(service.getAllProblems());
    }
}