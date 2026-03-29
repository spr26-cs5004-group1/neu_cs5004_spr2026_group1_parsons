package com.parsons.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.ArrayList;
import java.util.List;

/**
 * Wrapper class representing a collection of ParsonsProblem objects.
 * The local XML will contain one <Problems> containing multiple <Problem> elements.
 */
@JacksonXmlRootElement(localName = "Problems")
public class ProblemsList {

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "Problem")
    private List<ParsonsProblem> problems = new ArrayList<>();

    /**
     * Sets the list of ParsonsProblem objects.
     *
     * @param problems the list of problems to set.
     *                 should not be null.
     */
    public void setProblems(List<ParsonsProblem> problems) {
        this.problems = problems;
    }

    /**
     * Returns the list of all ParsonsProblem objects.
     *
     * @return list of problems, empty list if none exist.
     */
    public List<ParsonsProblem> getProblems() {
        return problems;
    }
}
