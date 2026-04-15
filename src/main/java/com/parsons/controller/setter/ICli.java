package com.parsons.controller.setter;

/**
 * Interface enforcing the CLI run method for problem parsing.
 */
public interface ICli {
    /**
     * Main run method for cli
     * @param filepath A string that contains the location of the problem file for import.
     */
    void run(String filepath);
}
