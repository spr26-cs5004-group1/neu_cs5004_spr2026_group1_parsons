package com.parsons.controller;

import com.parsons.repository.XmlParsonsProblemsRepository;
import com.parsons.service.ParsonsProblemsService;

import javax.swing.*;

public class MainControllerTest {
    public static void main(String[] args) {
        XmlParsonsProblemsRepository repo = new XmlParsonsProblemsRepository("src/main/resources/DemoRepo.xml");
        ParsonsProblemsService service = new ParsonsProblemsService(repo);
        SwingUtilities.invokeLater(() -> new MainController(service));
    }
}
