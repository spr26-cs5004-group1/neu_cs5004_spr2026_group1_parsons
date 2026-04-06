package com.parsons.controller.setter;

import com.parsons.model.ParsonsProblem;
import com.parsons.service.ParsonsProblemsService;

import java.util.List;

public class SetterCli {

    private ParsonsProblemsService service;

    public SetterCli(ParsonsProblemsService service){
        this.service = service;
    }

    public void run(String filepath) {
        return;
    }

    private List<ParsonsProblem> parseFile() {
        return List.of();
    }
}
