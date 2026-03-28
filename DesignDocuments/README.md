# Design Documents

You may have multiple design documents for this project. Place them all in this folder. File naming is up to you, but it should be clear what the document is about. At the bare minimum, you will want a pre/post UML diagram for the project.

## Initial Design Thoughts

### Model and Controller UML

Vision:

Dependency Map (--> =  depends on)

GUI (Swing) --> Controller  --> Service --> Repository --> Model


Application Flow:

ParsonsApplication (main) --creates--> XmlParsonsProblemsRepository --injected into--> ParsonsProblemsService
--injected into-->Swing UI--calls-->ParsonsProblemsService (when button clicked)


```mermaid

classDiagram
direction TB

%%--- ENTRY POINT ---
class ParsonsApplication {
  + ParsonsApplication() 
  + main(String[]) void
}

%% ---- STATIC PROPERTIES: used in all UI ---
class AppMetadata {
    +String APP_NAME$
    +String SETTER_BUTTON_DESC$
    +String STUDENT_BUTTON_DESC$
    +String CREDITS$
    %% to be expanded as needed
}

namespace model {

    class CodeBlock {
        +CodeBlock(String, boolean, Integer) 
        +CodeBlock()
        -Integer orderIndex
        -String codeContent
        -int id
        -boolean isDistractor

        +getId() int
        +setId(int) void
        +getCodeContent() String
        +setCodeContent(String) void
        +getIsDistractor() boolean
        +setIsDistractor(boolean) void
        +getOrderIndex() Integer
        +setOrderIndex(Integer) void
        +CodeBlock()
        +CodeBlock(String, boolean, Integer)
    }

    class ParsonsProblem {
        +ParsonsProblem(String, String, List~CodeBlock~) 
        +ParsonsProblem()
        -int id
        -String title
        -String instructions
        -List~CodeBlock~ code

        +getId() int
        +setId(int) void
        +getTitle() String
        +setTitle(String) void
        +getInstructions() String
        +setInstructions(String) void
        +getCode() List~CodeBlock~
        +setCode(List~CodeBlock~) void
        +ParsonsProblem()
        +ParsonsProblem(String, String, List~CodeBlock~)
    }

    class ProblemsList {
        +ProblemsList() 
        -List~ParsonsProblem~ problems

        +getProblems() List~ParsonsProblem~
        +setProblems(List~ParsonsProblem~) void
    }

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

namespace Repository {
    class IParsonsProblemsRepository {
        <<Interface>>
        +existsById(int) boolean
        +findById(int) ParsonsProblem
        +save(ParsonsProblem) int
        +findAll() List~ParsonsProblem~
        +deleteById(int) void
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

%%--- NOT WRITTEN YET: UI is combined controller and view ---
namespace UI {  
    class HomeView {
        -ParsonsProblemsService service
        +HomeView(ParsonsProblemsService)
        +show() void
    }

    class SetterWelcomeView {
        -ParsonsProblemsService service
        +SetterView(ParsonsProblemsService)
        +show() void
    }

    class SetterEditorView {
        -ParsonsProblemsService service
        +SetterEditorView(ParsonsProblemsService)
        +show() void
    }

    class StudentWelcomeView {
        -ParsonsProblemsService service
        +StudentView(ParsonsProblemsService)
        +show() void
    }

    class StudentSolverView {
        -ParsonsProblemsService service
        +StudentSolverView(ParsonsProblemsService)
        +show() void
    }
}

ParsonsApplication  -->  ParsonsProblemsService: has a
ParsonsApplication  -->  XmlParsonsProblemsRepository : has a
ParsonsApplication --> HomeView

ParsonsProblem --> CodeBlock: has a

ProblemsList --> ParsonsProblem : has a

IParsonsProblemsRepository  -->  ParsonsProblem : has a

XmlParsonsProblemsRepository  ..|>  IParsonsProblemsRepository : implements
XmlParsonsProblemsRepository  -->  ProblemsList : has a 
XmlParsonsProblemsRepository  -->  ParsonsProblem : has  

ParsonsProblemsService  -->  CodeBlock : has a
ParsonsProblemsService  -->  IParsonsProblemsRepository: has a
ParsonsProblemsService  -->  ParsonsProblem : has a

HomeView --> ParsonsProblemsService
HomeView --> SetterWelcomeView
HomeView --> StudentWelcomeView
HomeView --> AppMetadata: uses

SetterWelcomeView --> EditorView
SetterWelcomeView --> ParsonsProblemsService
SetterWelcomeView --> AppMetadata: uses

EditorView --> ParsonsProblemsService
EditorView --> AppMetadata: uses

StudentWelcomeView --> SolverView
StudentWelcomeView --> ParsonsProblemsService
StudentWelcomeView --> AppMetadata: uses

SolverView --> ParsonsProblemsService
SolverView --> AppMetadata: uses
```


reference:
https://docs.oracle.com/javase/tutorial/uiswing/examples/dnd/index.html