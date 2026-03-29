package com.parsons.repository;
import com.parsons.model.ParsonsProblem;
import java.util.List;
/**
 * Repository interface for managing ParsonsProblem
 * store data in any format of files.
 */
public interface IParsonsProblemsRepository {
    /**
     * Saves a ParsonsProblem to the repository.
     * If the problem has no id, a new id is assigned.
     * If the problem already has an id, it is updated.
     *
     * @param problem the problem to save.
     *
     * @return the assigned or existing id of the saved problem.
     */
    int save(ParsonsProblem problem);

    /**
     * Retrieves a ParsonsProblem by its id.
     *
     * @param id the id of the problem to retrieve.
     * @return the problem with the given id, or null if not found.
     */
    ParsonsProblem findById(int id);

    /**
     * Retrieves all ParsonsProblem objects in the repository.
     *
     * @return a list of all problems, or an empty list if none exist.
     */
    List<ParsonsProblem> findAll();

    /**
     * Checks whether a ParsonsProblem with the given id exists.
     *
     * @param id the id to check.
     * @return true if a problem with the given id exists, false otherwise.
     */
    boolean existsById(int id);

    /**
     * Deletes the ParsonsProblem with the given id.
     *
     * @param id the id of the problem to delete.
     */
    void deleteById(int id);
}
