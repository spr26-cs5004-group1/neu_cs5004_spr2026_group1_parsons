package com.parsons.controller.setter;

import com.parsons.repository.XmlParsonsProblemsRepository;
import com.parsons.service.ParsonsProblemsService;

import javax.swing.*;

public class SetterWelcomeViewTest {
    private static final XmlParsonsProblemsRepository repository = new XmlParsonsProblemsRepository(
            "src/main/resources/DemoRepo.xml");
    private static final ParsonsProblemsService service = new ParsonsProblemsService(repository);
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() ->
                new SetterWelcomeView(service, "PlaceHolder")
        );
    }
}
