package com.parsons.model;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.ArrayList;
import java.util.List;


@JacksonXmlRootElement(localName = "Problem")
public class ParsonsProblem {
    /** Stores ParsonsProblem id, initiated at -1. */
    private int id = -1;

    /** Stores title of the ParsonsProblem. */
    private String title;

    /** Stores instructions of the ParsonsProblem. */
    private String instructions;

    /** Stores List<CodeBlock> that make the ParsonsProblem. */
    @JacksonXmlElementWrapper(localName = "blocks")
    @JacksonXmlProperty(localName = "block")
    private List<CodeBlock> code = new ArrayList<>();

    /**
     * Setter for id.
     *
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Getter for id.
     *
     * @return id long.
     */
    public int getId() {
        return id;
    }

    /**
     * Setter for title.
     *
     * @param title String.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Getter for title.
     *
     * @return title String
     */
    public String getTitle() {
        return title;
    }

    /**
     * Setter for instructions.
     *
     * @param instructions String.
     */
    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    /**
     * Getter for instructions.
     *
     * @return instructions String
     */
    public String getInstructions() {
        return instructions;
    }

    /**
     * Setter for code.
     *
     * @param code String.
     */
    public void setCode(List<CodeBlock> code) {
        if (code == null) {
            throw new IllegalArgumentException("Code should not be null");
        }
        this.code = code;
    }

    /**
     * Getter for code.
     *
     * @return code String.
     */
    public List<CodeBlock> getCode() {
        return code;
    }

    public ParsonsProblem() {
        // empty
    }

    public ParsonsProblem(String title, String instructions,  List<CodeBlock> code) {
        this.setTitle(title);
        this.setInstructions(instructions);
        this.setCode(code);
    }

}
