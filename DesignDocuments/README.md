# Design Documents

You may have multiple design documents for this project. Place them all in this folder. File naming is up to you, but it should be clear what the document is about. At the bare minimum, you will want a pre/post UML diagram for the project.

## Initial Design Thoughts

Swing UI supports drag and drop:
https://docs.oracle.com/javase/tutorial/uiswing/examples/dnd/index.html


### Dependency Map
[Legend: --> =  depends on]

Swing UI/Controller  --> Service --> Repository --> Model


### Application Flow

`ParsonsApplication` (main) --creates--> `XmlParsonsProblemsRepository` --injected into--> `ParsonsProblemsService`
--injected into-->Swing UI--calls-->`ParsonsProblemsService` (when button clicked)


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

namespace controller.setter {

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

namespace controller.student {

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
