package com.parsons.service;

import com.parsons.model.CodeBlock;
import com.parsons.model.ParsonsProblem;
import com.parsons.repository.IParsonsProblemsRepository;

import java.util.Comparator;
import java.util.List;

/**
 * Service layer for managing ParsonsProblem objects.
 * Contains all business logic for creating, updating, deleting,
 * retrieving, and answer-checking Parsons problems.
 * Delegates persistence to IParsonsProblemsRepository which can be done using any format (XML or otherwise).
 */

public class ParsonsProblemsService {

    /** Repository for persisting ParsonsProblem objects. */
    private final IParsonsProblemsRepository repo;

    /**
     * Constructs a ParsonsProblemsService with the given repository.
     *
     * @param repo the repository implementation to use for persistence.
     */
    public ParsonsProblemsService(IParsonsProblemsRepository repo) {
        this.repo = repo;
    }

    /**
     * Saves the Parson Problem in the local repo.
     * returns the id that gets automatically assigned to the problem in the repo. 
     *
     * @return assigned id
     */
    public int saveProblem(ParsonsProblem problem) {
        return repo.save(problem);
    }

    /**
     * Updates the ParsonProblem repository at id with problem.
     *
     * @param id
     * @param problem
     * @return updated problem
     */
    public ParsonsProblem updateProblem(int id, ParsonsProblem problem) {
        // find existing
        ParsonsProblem existing = repo.findById(id);
        if (existing != null) {
            // update its fields
            existing.setTitle(problem.getTitle());
            existing.setInstructions(problem.getInstructions());
            existing.setCode(problem.getCode());
            // save the existing object
            repo.save(existing);
            return existing;
        }
        throw new IllegalArgumentException("Problem not found with id: " + id);
    }


    /**
     * Deletes the ParsonsProblem at id in the repository if it exists.
     *
     * @param id
     */
    public void deleteProblem(int id) {
        repo.deleteById(id); // delete by id from repository
    }


    /**
     * Returns all the ParsonsProblems in repository as a List of ParsonProblem objects.
     *
     * @return List of ParsonProblem objects.
     */
    public List<ParsonsProblem> getAllProblems() {
        return repo.findAll();
    }


    /**
     * Get the ParsonsProblem in repository at id.
     *
     * @param id
     * @return
     */
    public ParsonsProblem getProblemById(int id) {
        return repo.findById(id);
    }


    /**
     * Returns only the non-distractor blocks from a problem,
     * sorted by orderIndex.
     *
     * @param problem the problem to extract solution blocks from.
     * @return sorted list of non-distractor code blocks.
     */
    private List<CodeBlock> getSortedSolution(ParsonsProblem problem) {
        return problem.getCode().stream()
                .filter(block -> !block.getIsDistractor())
                .sorted(Comparator.comparing(CodeBlock::getOrderIndex))
                .toList();
    }


    /**
     * Check if the response list of code block matches the ParsonProblem at id in the repository.
     *
     * @param id
     * @param answer
     * @return boolean
     */
    public boolean checkAnswer(int id, List<CodeBlock> answer) {
        ParsonsProblem retrieved = repo.findById(id);
        if (retrieved == null) {
            throw new IllegalArgumentException("Problem not found with id: " + id);
        }
        List<CodeBlock> sortedSolution = getSortedSolution(retrieved);
        if (answer.size() != sortedSolution.size()) {
            return false;
        }
        for (int i = 0; i < sortedSolution.size(); i++) {
            if (!sortedSolution.get(i).getCodeContent().equals(answer.get(i).getCodeContent())
                    || answer.get(i).getIsDistractor()) {
                return false;
            }
        }
        return true;
    }

}
