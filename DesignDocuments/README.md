# Design Documents

You may have multiple design documents for this project. Place them all in this folder. File naming is up to you, but it should be clear what the document is about. At the bare minimum, you will want a pre/post UML diagram for the project.


## Initial Design Thoughts: Also Counting Features

Note: Swing UI supports drag and drop:
https://docs.oracle.com/javase/tutorial/uiswing/examples/dnd/index.html


### FlowChart for Setter using CLI 

FEATURE 1: Setting problems from CLI by passing a text file

FEATURE 2: reasonable accommodation for spelling mistake in text files

In the text files, ParsonsProblem are separated by ---

Parsing expects the following structure:

1. First line of each ParsonsProblem is id, an int. 
   * For a new problem use int <= 0 (is not honored, the program returns a new id based on the current max id). 
   * If problem is being updated/edited, correct id is needed.
   * Another way (if user cannot recall id) - create a new problem and delete the older version using browser -- cannot be done via CLI so far.

2. Second line is the instructions. 
   * Assumed all correct. Treated as a final String.

3. All other lines (\n) are expected to be ParsonProblem with three elements separated by a pipe "|".
   * First element isDistractor, second the index and third the CodeBlock. 
   * `isDistractor` can have the following valid values: `t` and `f`; this way partials of `true` and `false` are accepted. There can be leading or trailing spaces.
   * for a distractor, index field is ignored 
   * Leading and trailing spaces are ignored other than "\t\s*" or "\t" for 4-space tabbing.

```text
EXAPLE_TWO_PROBLEMS.txt

// empty lines are ignored

id      // an int - non-negotiatble
false | 0 | int findMin(List list) {
// for tabbing "\t " is useful for clarity
false | 1 | \t int min = list[0];
// index 1 is incorrect but inconsequential
true | 1 | \t min = list[0];
// f is enough for isDistrator; is parsed false
f | 2 | \t for (int i = 0; i < list.size(); i++) {
// ture is acceptable for isDistrator since it starts with t; is parsed true
ture | 2 | \t for ( i = 0; i < list.size(); i++) {
}
// "\t" is also acceptable for tabbing
false | 3 |\t\t if (list[i] < min) {        
false| 4 | \t\t\t min = list[i];
false| 5 | \t\t }
false| 6 | \t }
false| 7 | \t return min;
// flase is acceptable for isDistrator since it starts with f; is parsed false
flase| 8 | }

// empty lines are ignored

---             // problems are separated by three dashes

// empty lines are ignored

id_next     // an int 
false | 0 | int findMax(List list) {
// for tabbing "\t " is useful for clarity
fal | 1 | \t int max = list[0];
// index 1 is incorrect but inconsequential
true | 1 | \t max = list[0]; 
f | 2 | \t for (int i = 0; i < list.size(); i++) {
// t is enough for isDistrator; is parsed true
t | 2 | \t for ( i = 0; i < list.size(); i++) {
}
// "\t" is also acceptable for tabbing
false | 3 |\t\t if (list[i] > min) {        
false| 4 | \t\t\t max = list[i];
false| 5 | \t\t }
false| 6 | \t }
false| 7 | \t return max;
// flase is acceptable for isDistrator since it starts with f; is parsed false
flase| 8 | }
```


```mermaid
flowchart TD

    START([java -jar parsons.jar input.txt])
    APP[ParsonsApplication\n detects file arg, routes to SetterCli]
    RUN[SetterCli.run filepath\n opens and reads the text file]
    FILE_EXISTS{file exists?}
    ERR_FILE[print error]
    EXIT_FILE([exit])
    PARSE[parseFile filepath\n splits on pipe delimiter into problems]
    PARSE_VALID{parse valid?}
    ERR_PARSE[print error]
    EXIT_PARSE([exit])
    LOOP[for each problem in file\n check if id exists in repo]
    ID_EXISTS{id exists in repo?}
    UPDATE[update existing problem]
    INSERT[insert as new problem]
    DONE([print summary and exit])

    START --> APP
    APP --> RUN
    RUN --> FILE_EXISTS

    FILE_EXISTS -- no --> ERR_FILE
    ERR_FILE --> EXIT_FILE

    FILE_EXISTS -- yes --> PARSE
    PARSE --> PARSE_VALID

    PARSE_VALID -- no --> ERR_PARSE
    ERR_PARSE --> EXIT_PARSE

    PARSE_VALID -- yes --> LOOP
    LOOP --> ID_EXISTS

    ID_EXISTS -- yes --> UPDATE
    ID_EXISTS -- no --> INSERT

    UPDATE --> DONE
    INSERT --> DONE
```

### Student's FlowChart

Feature 3: Student can browse all problems

Feature 4: Student gets to retry as many times as they want with instant feedback

```mermaid
flowchart TD

    START([launch parsons-student.jar])
    WELCOME[StudentWelcomeView\n shows list of all problems]

    SELECT[click a problem]
    SOLVER[SolverView\n shows shuffled code blocks]

    DRAG[drag and drop blocks\n into answer panel]

    SUBMIT[click Submit]
    CHECK{checkAnswer\n service call}

    CORRECT[show correct feedback\n outline turns green]
    INCORRECT[show incorrect feedback\n outline turns red]

    SHOW_SOL{show solution?}
    SOLUTION[reveal correct order\n highlight differences]

    RETRY[click Retry]
    BACK[click Back\n return to list]

    DONE([exit app at any time])

    START --> WELCOME

    WELCOME -- select problem --> SELECT

    SELECT --> SOLVER
    SOLVER --> DRAG
    DRAG --> SUBMIT
    SUBMIT --> CHECK

    CHECK -- correct --> CORRECT
    CHECK -- incorrect --> INCORRECT

    INCORRECT --> SHOW_SOL
    SHOW_SOL -- yes --> SOLUTION
    SHOW_SOL -- no --> RETRY
    SOLUTION --> RETRY
    RETRY --> SOLVER
    BACK --> WELCOME
    CORRECT --> BACK
    
```


### Setter's Editor GUI view (with Student View Preview)

Feature 5: editor can write the ParsonsProblem's instruction and CodeBlocks directly using a GUI `textField`.

Feature 6: choose distractor `boolean` with a radio button.

```mermaid
flowchart TD

    START([launch parsons-setter.jar])
    SETTER_WELCOME[SetterWelcomeView\n shows list of all problems]

    CREATE[click New Problem]
    EDITOR_NEW[EditorView\n empty form]

    SELECT[click existing problem]
    EDITOR_EDIT[EditorView\n populated with problem data]

    DELETE{confirm delete?}
    DELETED[problem removed from repo\n list refreshed]

    FILL[fill in title, instructions\n add code blocks and distractors]
    VALID{fields valid?}
    ERR_VALID[show validation error]
    SAVE[save to XML repo\n assign or update id]

    PREVIEW[SolverView\n read-only student view]
    BACK[back to SetterWelcomeView]

    DONE([exit app])

    START --> SETTER_WELCOME

    SETTER_WELCOME -- new problem --> CREATE
    SETTER_WELCOME -- select problem --> SELECT
    SETTER_WELCOME -- delete problem --> DELETE
    SETTER_WELCOME -- quit --> DONE

    CREATE --> EDITOR_NEW
    SELECT --> EDITOR_EDIT

    EDITOR_NEW --> FILL
    EDITOR_EDIT --> FILL

    FILL --> VALID
    VALID -- no --> ERR_VALID
    ERR_VALID --> FILL

    VALID -- yes --> SAVE
    SAVE --> SETTER_WELCOME

    DELETE -- no --> SETTER_WELCOME
    DELETE -- yes --> DELETED
    DELETED --> SETTER_WELCOME

    SETTER_WELCOME -- preview problem --> PREVIEW
    PREVIEW --> BACK
    BACK --> SETTER_WELCOME
```

### Dependency Map
[Legend: --> =  depends on]

`Swing UI/Controller  --> Service --> Repository --> Model`


### File Structure
```
com.parsons/
|
|-- model/
|   |-- CodeBlock.java
|   |-- ParsonsProblem.java
|   |-- ProblemsList.java
|
|-- repository/
|   |-- IParsonsProblemsRepository.java
|   |-- XmlParsonsProblemsRepository.java
|
|-- service/
|   |-- ParsonsProblemsService.java
|
|-- controller/
|   |-- setter/
|   |   |-- SetterCli.java
|   |   |-- SetterView.java* (maybe)
|   |   |-- EditorView.java* (maybe)
|   |-- student/
|       |-- StudentView.java
|       |-- SolverView.java
|
|-- AppMetadata.java
|-- ParsonsApplication.java
```
### Design/Vision UML

```mermaid
classDiagram
direction TD

%%--- ENTRY POINT ---
class ParsonsApplication {
    +main(String[]) void
}

%%--- APP-WIDE METADATA ---
class AppMetadata {
    +String APP_NAME$
    +String SETTER_BUTTON_DESC$
    +String STUDENT_BUTTON_DESC$
    +String CREDITS$
}

namespace model {

    class CodeBlock {
        -int id
        -String codeContent
        -boolean isDistractor
        -Integer orderIndex
        +CodeBlock()
        +CodeBlock(String, boolean, Integer)
        +getId() int
        +setId(int) void
        +getCodeContent() String
        +setCodeContent(String) void
        +getIsDistractor() boolean
        +setIsDistractor(boolean) void
        +getOrderIndex() Integer
        +setOrderIndex(Integer) void
    }

    class ParsonsProblem {
        -int id
        -String title
        -String instructions
        -List~CodeBlock~ code
        +ParsonsProblem()
        +ParsonsProblem(String, String, List~CodeBlock~)
        +getId() int
        +setId(int) void
        +getTitle() String
        +setTitle(String) void
        +getInstructions() String
        +setInstructions(String) void
        +getCode() List~CodeBlock~
        +setCode(List~CodeBlock~) void
    }

    class ProblemsList {
        -List~ParsonsProblem~ problems
        +ProblemsList()
        +getProblems() List~ParsonsProblem~
        +setProblems(List~ParsonsProblem~) void
    }
}

namespace repository {

    class IParsonsProblemsRepository {
        <<Interface>>
        +save(ParsonsProblem) int
        +findById(int) ParsonsProblem
        +findAll() List~ParsonsProblem~
        +deleteById(int) void
        +existsById(int) boolean
    }

    class XmlParsonsProblemsRepository {
        -XmlMapper xmlMapper
        -File repo
        +XmlParsonsProblemsRepository(String)
        +save(ParsonsProblem) int
        +findById(int) ParsonsProblem
        +findAll() List~ParsonsProblem~
        +deleteById(int) void
        +existsById(int) boolean
        -readFromRepo() ProblemsList
        -writeToRepo(ProblemsList) void
    }
}

namespace service {

    class ParsonsProblemsService {
        -IParsonsProblemsRepository repo
        +ParsonsProblemsService(IParsonsProblemsRepository)
        +saveProblem(ParsonsProblem) int
        +updateProblem(int, ParsonsProblem) ParsonsProblem
        +deleteProblem(int) void
        +getAllProblems() List~ParsonsProblem~
        +getProblemById(int) ParsonsProblem
        +checkAnswer(int, List~CodeBlock~) boolean
    }
}

%%--- NOT WRITTEN YET ---

namespace controller_setter {

    class SetterCli {
        -ParsonsProblemsService service
        +SetterCli(ParsonsProblemsService)
        +run(String filePath) void
        -parseFile(String filePath) List~ParsonsProblem~
    }

    class SetterWelcomeView {
        -ParsonsProblemsService service
        +SetterWelcomeView(ParsonsProblemsService)
        +show() void
    }

    class EditorView {
        -ParsonsProblemsService service
        +EditorView(ParsonsProblemsService)
        +show() void
    }
}

namespace controller_student {

    class StudentWelcomeView {
        -ParsonsProblemsService service
        +StudentWelcomeView(ParsonsProblemsService)
        +show() void
    }

    class SolverView {
        -ParsonsProblemsService service
        +SolverView(ParsonsProblemsService)
        +show() void
    }
}

%%--- RELATIONSHIPS ---

%% entry point
ParsonsApplication --> ParsonsProblemsService
ParsonsApplication --> XmlParsonsProblemsRepository
ParsonsApplication --> SetterCli
ParsonsApplication --> SetterWelcomeView
ParsonsApplication --> StudentWelcomeView

%% model
ParsonsProblem --> CodeBlock
ProblemsList --> ParsonsProblem

%% repository
XmlParsonsProblemsRepository ..|> IParsonsProblemsRepository
XmlParsonsProblemsRepository --> ProblemsList

%% service
ParsonsProblemsService --> IParsonsProblemsRepository
ParsonsProblemsService --> ParsonsProblem
ParsonsProblemsService --> CodeBlock

%% setter controller
SetterCli --> ParsonsProblemsService
SetterCli --> AppMetadata
SetterWelcomeView --> ParsonsProblemsService
SetterWelcomeView --> EditorView
SetterWelcomeView --> AppMetadata
EditorView --> ParsonsProblemsService
EditorView --> AppMetadata

%% student controller
StudentWelcomeView --> ParsonsProblemsService
StudentWelcomeView --> SolverView
StudentWelcomeView --> AppMetadata
SolverView --> ParsonsProblemsService
SolverView --> AppMetadata
```


## Initial Design: Student GUI SolverView 

```
 ---------------------------------------------------
│                                           [quit]  │
|                  Instructions                     |
| ---------------------  -------------------------- |
│  BLOCKS (pick up)     │  ANSWER (drop here)       │
│   ---------------     │                           │
│   │  int i;      │    │                           │
│   ├──────────────┤    │                           │
│   │  i = 0;      │    │                           │
│   ├──────────────┤    │                           │
│   │while(i<10){  │    │                           │
│   ----------------    │                           │
│                       │          [Submit]         │
 ---------------------------------------------------
```

