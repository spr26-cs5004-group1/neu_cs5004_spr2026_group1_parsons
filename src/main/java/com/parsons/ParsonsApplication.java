package com.parsons;

import com.parsons.controller.MainController;
import com.parsons.repository.XmlParsonsProblemsRepository;
import com.parsons.service.ParsonsProblemsService;

import javax.swing.SwingUtilities;

/**
 * Entry point for the Parsons Problem Tool application.
 * Initializes the repository, service, and launches the Swing UI.
 */
public class ParsonsApplication {

    /**
     * Main method — creates an XmlParsonsProblemsRepository object and a ParsonsProblemsService object and calls the
     * MainController.
     *
     * @param args command line arguments (not used).
     */
    public static void main(String[] args) {
        /* Repository points to local XML file. */
        XmlParsonsProblemsRepository repository = new XmlParsonsProblemsRepository(
                "src/main/resources/DemoRepo.xml");
        /* Service receives repository via dependency injection. */
        ParsonsProblemsService service = new ParsonsProblemsService(repository);
        /* Call MainController. */
        SwingUtilities.invokeLater(() -> new MainController(service));
    }
}
