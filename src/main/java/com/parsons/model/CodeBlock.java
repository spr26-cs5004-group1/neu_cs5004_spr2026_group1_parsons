package com.parsons.model;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "block")
public class CodeBlock{

    private int id = -1;

    private String codeContent;
    private boolean isDistractor;
    private Integer orderIndex;

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
     */
    public int getId() {
        return id;
    }

    /**
     * Setter for codeContent.
     *
     * @param codeContent the string of correct code.
     */
    public void setCodeContent(String codeContent) {
        this.codeContent = codeContent;
    }

    /**
     * Getter for codeContent.
     *
     * @return codeContent String.
     */
    public String getCodeContent() {
        return codeContent;
    }

    /**
     * Setter for isDistractor.
     *
     * @param isDistractor
     */
    public void setIsDistractor(boolean isDistractor) {
        this.isDistractor = isDistractor;
    }

    /**
     * Getter for isDistractor.
     *
     * @return isDistactor boolean.
     */
    public boolean getIsDistractor() {
        return isDistractor;
    }

    /**
     * Setter for orderIndex
     *
     * @param orderIndex
     */
    public void setOrderIndex(Integer orderIndex) {
        this.orderIndex = orderIndex;
    }

    /**
     * Getter for orderIndex.
     *
     * @return orderIndex Integer.
     */
    public Integer getOrderIndex() {
        return orderIndex;
    }

    /**
     * Primary constructor for CodeBlock.
     *
     * @param codeContent
     * @param isDistractor
     * @param orderIndex
     */
    public CodeBlock(String codeContent, boolean isDistractor, Integer orderIndex) {
        setCodeContent(codeContent);
        setIsDistractor(isDistractor);
        setOrderIndex(orderIndex);
    }

    /**
     * Empty constructor for CodeBlock.
     */
    public CodeBlock() {
        //
    }
}
