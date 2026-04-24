

# Final Project for CS 5004 - Parsons Problem Authoring & Delivery Tool

## Authors: (Group 1)
### McKillop, Parker @pem2k
### O'Bannon, Michael @oban2319
### Pooley, Oksana @OksanaPooley
### Singh, Arsh @arshsinghphd

**Description**

Our project  is called the Parsons Problem Authoring & Delivery Tool. This application allows creation, storage, and solving of Parsons problems, which are coding puzzles that can be solved by arranging prewritten code blocks in the order that produces valid code. Setters can import problems from file and save them to a persistent local repository. Solvers are able to access a simplified version of the setter's UI, allowing them to select and solve problems. All operations take place locally, no internet connection is required.

**Running the Application**

The application can be run with the following commands from the root of the project:

```
java -jar parsons-problem-tool-0.1.jar
```

To bulk import problems, use the cli import with the following command:

```
java -jar parsons-problem-tool-0.1.jar -cli ./path/to/problem/file.txt
```

**Documents**

For full user instructions, please see the manual:
* [Manual](Manual/README.md)

For information pertaining to the design of the application, please see the following documents:
* [Initial Design Document](DesignDocuments/README.md)
* [Features Implemented](DesignDocuments/FeaturesImplemented.md)
* [Conceptual Design UML](DesignDocuments/ConceptualDesignUML.md)

GUI Manual Testing Documentation can be found below:
* [GUI Testing](GUITesting/GUI_Development+Testing.md)

Generated Javadoc documentation can be found here:
* [Java Documentation](JavaDocumentation/index.html)