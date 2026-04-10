package com.parsons;

import com.parsons.controller.HomeController;
import com.parsons.controller.setter.ICli;
import com.parsons.controller.setter.SetterCli;
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

        if (args != null && args.length > 0) {
            if (args[0].equalsIgnoreCase("-cli")) {
                ICli cli = new SetterCli(service);
                cli.run(args[1]);
            }
        } else {
            /* Call MainController. */
            SwingUtilities.invokeLater(() -> new HomeController(service));
        }
    }
}
