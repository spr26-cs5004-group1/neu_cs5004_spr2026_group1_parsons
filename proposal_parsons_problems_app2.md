# Project Proposal: Parsons Problem Authoring & Delivery Tool

## Summary
This proposal outlines the design and development of a desktop tool for creating and delivering Parsons Problems — a teaching exercise format where students arrange shuffled lines of code into the correct order. The tool targets computer science educators and students. It is launched from the terminal by passing a puzzle file as an argument, and saves problem state as XML.

---

## Problem Statement
Parsons Problems are an effective and low-barrier method for teaching programming concepts. However, no lightweight, self-hosted, open-source desktop tool currently exists that allows educators to author these problems and deliver them to students without an internet connection, LMS subscription, or server setup.

---

## Proposed Solution
A self-contained Java desktop application with two distinct roles:

- **Problem Setter**: An educator who authors a puzzle as a plain text file and passes it to the application at the terminal.
- **Student**: A learner who solves the puzzle using a drag-and-drop Swing interface and submits their answer.

The application is packaged as a single executable JAR file requiring no internet connection or external server.

---

## Technical Architecture

### Technology Stack

| Layer | Technology | Rationale |
|---|---|---|
| Language | Java 21 | Stable LTS, wide adoption |
| UI | Swing (javax.swing) | Built-in, no extra dependencies, supports drag-and-drop |
| XML Persistence | Jackson XML | Annotation-driven, clean serialisation |
| Build | Gradle | Flexible, modern build tooling. Supported in IntelliJ IDE we have used throughout the class |
| Testing | JUnit 5 + manually written simulated objects | Most accessible based on the level of the class |

### Architectural Pattern: MVC
The application follows the **Model-View-Controller** pattern with clear package separation:

```
com.parsons/
|
+── model/      -> Data structures (Puzzle, CodeBlock)
|
+── controller/ -> Terminal arg handling, application flow
|
+── view/       -> Swing UI, drag-and-drop interface
|
+── xml/        -> Jackson XML save and load
```

Each layer has a single, well-defined responsibility. The Controller reads terminal arguments and coordinates the Model and View. Business logic lives in the Model, making it independently testable without a running UI.


## Data Model

### `CodeBlock`
Represents a single draggable block of code.

| Field | Type | Description |
|---|---|---|
| `codeContent` | `String` | The code string displayed to the student |
| `isDistractor` | `boolean` | Whether this block is a red herring |
| `orderIndex` | `Integer` | Correct position (null for distractors) |

### `Puzzle`
Represents a complete Parsons Problem.

| Field | Type | Description |
|---|---|---|
| `title` | `String` | Problem title |
| `instructions` | `String` | Instructions shown to the student |
| `blocks` | `List<CodeBlock>` | All blocks including distractors |

---

## XML Save Format
A saved puzzle result will be serialised by Jackson XML into the following structure:

```xml
<Puzzle>
  <title>TITLE</title>
  <instructions>POSSIBLY MANY LINES OF INSTRUCTIONS.</instructions>
  <blocks>
    <block>
      <codeContent>CODE LINE OR BLOCK</codeContent>
      <isDistractor>BOOLEAN</isDistractor>
      <orderIndex>INT</orderIndex>
    </block>
    ...
    ...
    
  </blocks>
</Puzzle>
```

---

## Development Methodology

### Test Driven Development (TDD)
The project follows a **Red → Green → Refactor** TDD cycle. Tests are written before implementation code.

Three kinds of testing are implemented:

**1. Unit Tests (Model)**
Validation rules tested independently. Tests cover blank fields, null values, and correct shuffling behaviour.

**2. XML Tests**
Tests verify that a `Puzzle` object serialises to valid XML and deserialises back to an equal object without data loss.

**3. Controller Tests**
Use manually created simulated objects. 

### Design Principles Applied
- **Single Responsibility**: Each class has one clearly defined job.
- **Fail Fast**: Argument validation occurs at startup in the Controller before any Model or View is constructed.
- **Defensive Programming**: Null guards and meaningful exceptions used throughout.

---

## Key Features

### Problem Setter
- Author puzzles as plain `.txt` files
- Pass puzzle file at terminal: `java -jar parsons.jar puzzle.txt`
- Saved results written as XML to the same directory

### Student
- Solve problems using a drag-and-drop Swing interface
- Receive immediate feedback (correct / incorrect) on submission
- Retry as many times as needed

### Answer Checking Algorithm
Three-stage validation in the Model layer:
1. **Length check**: Student response must contain the same number of non-distractor blocks as the correct solution.
2. **Distractor check**: Response must not include any distractor blocks.
3. **Order check**: `codeContent` of each submitted block must match the correct solution order by `orderIndex`.

---

## Packaging & Deployment
The application is packaged as a single self-contained Gradle-built JAR:

```
$ java -jar parsons.jar puzzle.txt
```

No installation, internet connection, or external database required. Puzzle results are saved locally as XML files.