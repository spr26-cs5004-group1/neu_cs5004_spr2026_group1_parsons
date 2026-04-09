package com.parsons.repository;
import com.parsons.model.*;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class XmlParsonsProblemsRepository implements IParsonsProblemsRepository{

    /** Stores Jackson XMLMapper. */
    private final XmlMapper xmlMapper = new XmlMapper();

    /** Repository for persisting ParsonsProblem objects. */
    private final File repo;

    /**
     * Constructor for XmlParsonsProblemsRepository.
     *
     * @param filePath is the path to the local XML repo
     */
    public XmlParsonsProblemsRepository(String filePath) {
        this.repo = new File(filePath);
    }

    /**
     * Reads from the local XML repo using jackson.
     *
     * @return ProblemsList object with all the ParsonProblem
     * @throws RuntimeException if there are any IOExceptions thrown by Jackson methods
     */
    private ProblemsList readFromRepo() {
        try {
            if (!repo.exists()) return new ProblemsList();
            return xmlMapper.readValue(repo, ProblemsList.class);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read from XML file: " + e.getMessage(), e);
        }
    }

    /**
     * Write to the local XML file using jackson.
     * @param list is the ProblemsList object to be written to local XML repo
     * @throws RuntimeException if there are any IOExceptions thrown by Jackson methods
     */
    private void writeToRepo(ProblemsList list) {
        try {
            xmlMapper.writeValue(repo, list);
        } catch (IOException e) {
            throw new RuntimeException("Failed to write to XML file: " + e.getMessage(), e);
        }
    }

    /**
     * Saves a ParsonsProblem to the repository.
     * If the problem has no id, a new id is assigned.
     * If the problem already has an id, it is updated.
     *
     * @param problem the problem to save.
     *
     * @return the assigned or existing id of the saved problem.
     */
    public int save(ParsonsProblem problem) {
        ProblemsList problemsList = readFromRepo();
        List<ParsonsProblem> problems = problemsList.getProblems();
        int id;
        if (problems.isEmpty()) {
            id = 1;
        } else {
            id = problems.stream().mapToInt(ParsonsProblem::getId).max().getAsInt() + 1;
        }
        if (problem.getId() == -1) {
            // new problem
            problem.setId(id);
            problems.add(problem);
        } else {
            // update existing — find and replace
            for (int i = 1; i < problems.size(); i++) {
                if (problems.get(i).getId() == problem.getId()) {
                    problems.set(i, problem);
                    break;
                }
            }
        }
        problemsList.setProblems(problems);
        writeToRepo(problemsList);
        return problem.getId();
    }


    /**
     * Retrieves a ParsonsProblem by its id.
     *
     * @param id the id of the problem to retrieve.
     * @return the problem with the given id, or null if not found.
     */
    public ParsonsProblem findById(int id) {
        ProblemsList list = readFromRepo();
        List<ParsonsProblem> problems = list.getProblems();
        for(ParsonsProblem problem: problems) {
            if (problem.getId() == id) {
                return problem;
            }
        }
        return null;
    }


    /**
     * Retrieves all ParsonsProblem objects in the repository.
     *
     * @return a list of all problems, or an empty list if none exist.
     */
    public List<ParsonsProblem> findAll() {
        ProblemsList list = readFromRepo();
        return list.getProblems();
    }

    /**
     * Checks whether a ParsonsProblem with the given id exists.
     *
     * @param id the id to check.
     * @return true if a problem with the given id exists, false otherwise.
     */
    public boolean existsById(int id) {
        return findById(id) != null;
    }

    /**
     * Deletes the ParsonsProblem with the given id if it is in the list.
     *
     * @param id the id of the problem to delete.
     */
    public void deleteById(int id) {
        ProblemsList problemsList = readFromRepo();
        List<ParsonsProblem> problems = problemsList.getProblems();
        problems.removeIf(p -> p.getId() == id);
        problemsList.setProblems(problems);
        writeToRepo(problemsList);
    }

}
