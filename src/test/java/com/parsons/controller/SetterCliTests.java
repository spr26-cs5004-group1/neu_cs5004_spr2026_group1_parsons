package com.parsons.controller;

import com.parsons.model.CodeBlock;
import com.parsons.model.ParsonsProblem;
import com.parsons.repository.IParsonsProblemsRepository;
import com.parsons.service.ParsonsProblemsService;
import org.junit.jupiter.api.BeforeEach;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SetterCliTests {

    private IParsonsProblemsRepository repository;
    private ParsonsProblemsService service;
    private ParsonsProblem problem;
    private List<CodeBlock> code;

    /**
     * Creates a fresh in-memory anonymous repository, service,
     * and test data before each test.
     */
    @BeforeEach
    void setUp() {
        code = new ArrayList<>();
        code.add(new CodeBlock("int i;", false, 0));
        code.add(new CodeBlock("i = 0;", false, 1));
        code.add(new CodeBlock("while (i < 10) {", false, 2));
        code.add(new CodeBlock("while (i <= 10) {", true, null));
        problem = new ParsonsProblem("Title", "Instructions", code);

        repository = new IParsonsProblemsRepository() {

            private final Map<Integer, ParsonsProblem> store = new HashMap<>();
            private int nextId = 1;

            @Override
            public int save(ParsonsProblem p) {
                if (p.getId() == -1) {
                    p.setId(nextId++);
                }
                store.put(p.getId(), p);
                return p.getId();
            }

            @Override
            public ParsonsProblem findById(int id) {
                return store.get(id);
            }

            @Override
            public List<ParsonsProblem> findAll() {
                return new ArrayList<>(store.values());
            }

            @Override
            public void deleteById(int id) {
                store.remove(id);
            }

            @Override
            public boolean existsById(int id) {
                return store.containsKey(id);
            }
        };

        service = new ParsonsProblemsService(repository);
    }



}
