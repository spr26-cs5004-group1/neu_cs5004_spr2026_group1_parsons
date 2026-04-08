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
        SwingUtilities.invokeLater(() -> new MainController(service));
        // logic to work out - call controller.HomeView
        // controller.HomeView will call
        //      controller.setter.SetterCli OR
        //      controller.setter.SetterView (welcome/browse problems) OR
        //      controller.student.StudentView (welcome/browse problems)
    }
}
