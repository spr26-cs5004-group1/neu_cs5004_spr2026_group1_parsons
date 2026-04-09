package com.parsons.controller;

import com.parsons.model.CodeBlock;
import com.parsons.model.ParsonsProblem;

import java.util.List;

/**
 * Provides dummy ParsonsProblem data for manual GUI testing.
 * Not for production use.
 */
public class DummyData {

    /** Utility class -- do not instantiate */
    private DummyData() {}

    /**
     * Returns a list of 6 sample Parsons problems with code blocks and distractors.
     *
     * @return List of ParsonsProblem
     */
    public static List<ParsonsProblem> getProblems() {

        ParsonsProblem p1 = new ParsonsProblem(
                "Check if a number is positive",
                "Arrange the code to define a function that returns True if n is positive, False otherwise.",
                List.of(
                        new CodeBlock("def is_positive(n):", false, 0),
                        new CodeBlock("    if n > 0:", false, 1),
                        new CodeBlock("        return True", false, 2),
                        new CodeBlock("    else:", false, 3),
                        new CodeBlock("        return False", false, 4),
                        new CodeBlock("    if n >= 0:", true, 1)
                ));
        p1.setId(1);

        ParsonsProblem p2 = new ParsonsProblem(
                "Sum a list using a for loop",
                "Arrange the code to define a function that returns the sum of all elements in a list.",
                List.of(
                        new CodeBlock("def sum_list(lst):", false, 0),
                        new CodeBlock("    total = 0", false, 1),
                        new CodeBlock("    for x in lst:", false, 2),
                        new CodeBlock("        total += x", false, 3),
                        new CodeBlock("    return total", false, 4),
                        new CodeBlock("        total -= x", true, 3)
                ));
        p2.setId(2);

        ParsonsProblem p3 = new ParsonsProblem(
                "Absolute value",
                "Arrange the code to define a function that returns the absolute value of n.",
                List.of(
                        new CodeBlock("def absolute(n):", false, 0),
                        new CodeBlock("    if n < 0:", false, 1),
                        new CodeBlock("        return -n", false, 2),
                        new CodeBlock("    return n", false, 3),
                        new CodeBlock("    if n > 0:", true, 1)
                ));
        p3.setId(3);

        ParsonsProblem p4 = new ParsonsProblem(
                "Print even numbers from 0 to 10",
                "Arrange the code to print all even numbers from 0 to 10 inclusive.",
                List.of(
                        new CodeBlock("for i in range(11):", false, 0),
                        new CodeBlock("    if i % 2 == 0:", false, 1),
                        new CodeBlock("        print(i)", false, 2),
                        new CodeBlock("    if i % 2 == 1:", true, 1)
                ));
        p4.setId(4);

        ParsonsProblem p5 = new ParsonsProblem(
                "Find the maximum value in a list",
                "Arrange the code to define a function that returns the largest value in a list.",
                List.of(
                        new CodeBlock("def find_max(lst):", false, 0),
                        new CodeBlock("    max_val = lst[0]", false, 1),
                        new CodeBlock("    for x in lst:", false, 2),
                        new CodeBlock("        if x > max_val:", false, 3),
                        new CodeBlock("            max_val = x", false, 4),
                        new CodeBlock("    return max_val", false, 5),
                        new CodeBlock("        if x < max_val:", true, 3)
                ));
        p5.setId(5);

        ParsonsProblem p6 = new ParsonsProblem(
                "Letter grade from score",
                "Arrange the code to define a function that returns a letter grade for a given numeric score.",
                List.of(
                        new CodeBlock("def get_grade(score):", false, 0),
                        new CodeBlock("    if score >= 90:", false, 1),
                        new CodeBlock("        return \"A\"", false, 2),
                        new CodeBlock("    elif score >= 80:", false, 3),
                        new CodeBlock("        return \"B\"", false, 4),
                        new CodeBlock("    elif score >= 70:", false, 5),
                        new CodeBlock("        return \"C\"", false, 6),
                        new CodeBlock("    else:", false, 7),
                        new CodeBlock("        return \"F\"", false, 8),
                        new CodeBlock("    elif score >= 60:", true, 3)
                ));
        p6.setId(6);

        return List.of(p1, p2, p3, p4, p5, p6);
    }
}
