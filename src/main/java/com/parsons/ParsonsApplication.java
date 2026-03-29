package com.parsons;

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
     * HomeView (controller)
     *
     * @param args command line arguments (not used).
     */
    public static void main(String[] args) {
        // repository points to local XML file
        XmlParsonsProblemsRepository repository =
                new XmlParsonsProblemsRepository("data/problems.xml");

        // service receives repository via dependency injection
        ParsonsProblemsService service = new ParsonsProblemsService(repository);

        // logic to work out - call HomeView
        // HomeView will call
        // setter.SetterCli OR
        // setter.SetterView (welcome/browse problems) OR
        // student.StudentView (welcome/browse problems)
    }
}
