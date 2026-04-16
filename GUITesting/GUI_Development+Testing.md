# GUI Testing   

As project development progressed, screenshots of new features and steps to them were taken.   

## Drag-and-Drop Feature
The development started with us working through the feature of drag-and-drop as a standalone class
with tests. Below are screenshots of some of the steps. 

![001_split_panel.png](images_drag_drop/001_split_panel.png)
Fig.01. Split panel to hold the drag-and-drop elements.

![002_with_instLabel.png](images_drag_drop/002_with_instLabel.png)
Fig.02. Label for the instructions panel describing how to use the drag-and-drop feature. 

![003_with_instTextArea.png](images_drag_drop/003_with_instTextArea.png)
Fig. 03. The text area that displays drag-and-drop instructions for the user.

![004_with_labels.png](images_drag_drop/004_with_labels.png)
Fig. 04. An example of drag-and-drop objects with labels.

![005_with_draggable_codeBlocks.png](images_drag_drop/005_with_draggable_codeBlocks.png)
Fig. 05. Blocks with code that can be dragged to the right field and dropped.

![006_when_moved_disappear_from_source.png](images_drag_drop/006_when_moved_disappear_from_source.png)
Fig. 06. Illustration that once the blocks of code are dropped, they no longer appear in the left field.

![007_can_be_moved_back.png](images_drag_drop/007_can_be_moved_back.png)
Fig.07. Demonstration that blocks of code can be dragged from the right field and dropped back into the left.

## Editor View

This section was most affected by the data loss our team experienced during a version control mishap
involving a revert to an earlier commit.

![001_EditorView.png](images_EditorView/001_EditorView.png)
Fig. 08. Setting a problem by arranging code blocks into the correct order, which will be saved as the answer key. 
All other arrangements are considered incorrect.
The Setter arranges the code blocks into the correct order and clicks the Save This Problem button to save the problem.

This was the sequence of the development:

First design choice was how does the SetterWelcomeView get to the Editor View. There are two ways:
1. Edit an existing problem accessing this from the JTable row.
2. This opens the editor view

For this view, we decided that the editor will be allowed to test the problem like a solver. This part was simple: copy from the existing solver class - complete with submit and retry button.

Then we had to make a choice about how a problem will be edited: for this class project we decided that editing existing and creating new problem is the same way by uploading a file.

We restrict this to txt files in a very strict format.

So we created a JTextBox with instructions about the file format allowed.

The we added the fileBrowserPanel which allows user to choose a txt file - and only a txt file. This was defaulting open to user's main, so I changed that to open at the resource folder changing the default folder.

If the file was not in the correct format it will not be parsed. Or if it is parsed the user will be informed it is in a JLabel.

If it is parsed it creates a new ParsonProblem and populates the LeftPane as an exiting problem would, but is not yet added to the repo. The setter can test it see if the instructions and CodeBlocks are as they intend. If it is user can save it. If it is not setter can delete it, make changed to the txt file and upload again.

We created two buttons for these actions that call the service object methods.

But saving and deleting were happing silently and setter had no feedback on if it worked or not, so once could save twice or try to delete twice. So, we added a JLable that shows the outcome of the action:

For exisiting problems with an id:

"Uploading a file will overwrite the existing problem. Are you sure?",

"Confirm Overwrite"

and "Problem updated successfully!"

For new problems identified as if the window was called using NULL and thus does not have an ID:

"Problem saved successfully!"

After this the SetterWelcomeView that displays all the problems should be updated: if a problem is saved it should show up there, if an existing problem was deleted it should not be in the JTable anymore.

This presented a unique problem of the kind we had never encountered. Solved it by passing the SetterWelcomeView when opening the EditorView. Now the EditorView can close the existing view and create a new one that refreshes the JTable. This was the easiest way to do it - although there maybe others.

But this created a new problem. By this time we were asking users to provide an ID in the txt for the problems. In the repo if a different problem had the same id, it would be over-ridden.

So, we decided to remove id from the txt format and id is only provided by the service upon the time of saving.

This is the current final EditorView.   

## Main Controller
![001_main_controller.png](images_MainController/001_main_controller.png)   
Fig.09. Welcome screen displayed at program start. The user is prompted to select a role before continuing.

![002_main_controller_addName_launchStudent.png](images_MainController/002_main_controller_addName_launchStudent.png)   
Fig.10. Student role launch view.   

![003_main_controller_passingRepo_StudentWelcomeViewDisplays.png](images_MainController/003_main_controller_passingRepo_StudentWelcomeViewDisplays.png)   
Fig.11. Student view displaying a choice of problems to be solved.   

![004_main_controller_calledFromAppMain.png](images_MainController/004_main_controller_calledFromAppMain.png)   
Fig.12. The main controller is called from the program `main()`.   

![005_main_controller_calledFromAppMain.png.png](images_MainController/005_main_controller_calledFromAppMain.png.png)   
Fig.13. The main controller, called from `main()`, calls up for display the program welcome screen.   

## Setter View

![001_setterwelcomeview_copiedfrom_studentwelcomeview.png](images_SetterWelcomeView/001_setterwelcomeview_copiedfrom_studentwelcomeview.png)   
Fig.14. Setter's View, welcome screen.   


## Solver View
![001_solver_view.png](images_SolverView/001_solver_view.png)   
Fig.15. Initial empty window is ready to accept elements.   

![002_solver_view_changetitle.png](images_SolverView/002_solver_view_changetitle.png)   
Fig.16. Window title is displaying correctly.   

![003_solver_view_addTitleInstr.png](images_SolverView/003_solver_view_addTitleInstr.png)
Fig.17. Problem title and instructions displaying correctly.   

![004_solver_view_addPanel_padding.png](images_SolverView/004_solver_view_addPanel_padding.png)   
Fig.18. The padding on the panel has been added.   

![005_solver_view_addSubmit.png](images_SolverView/005_solver_view_addSubmit.png)   
Fig.19. The `Submit` button has been added.   

![006_solver_view_populatedShuffledCodeBlocks.png](images_SolverView/006_solver_view_populatedShuffledCodeBlocks.png)   
Fig.20. The shuffled code block populates correctly.   

![007_solver_view_draggable.png](images_SolverView/007_solver_view_draggable.png)   
Fig.21. Drag-and-drop feature functioning in the Solver view.   

![008_solver_view_addRetyButton.png](images_SolverView/008_solver_view_addRetyButton.png)   
Fig.22. Retry button added and displaying correctly.   

![009_solver_view_dragWrong.png](images_SolverView/009_solver_view_dragWrong.png)   
Fig.23. Drag-and-drop is not restricted to correct solution elements — incorrect 
elements can be moved as well.   

![010_solver_view_afterReset.png](images_SolverView/010_solver_view_afterReset.png)   
Fig.24. Display after the `Reset` button has been clicked.   

![011_solver_view_emptyIsIncorrect.png](images_SolverView/011_solver_view_emptyIsIncorrect.png)   
Fig.25. Incorrect answer view. An empty answer is highlighted with a red frame.   

![012_solver_view_incorrectResponse.png](images_SolverView/012_solver_view_incorrectResponse.png)   
Fig.26. An incorrect response highlighted by a red frame.   

![013_solver_view_correctResonse.png](images_SolverView/013_solver_view_correctResonse.png)   
Fig.27. Correct response view. The correct answer is highlighted by a green frame.   

![014_solver_view_opensFromStudentWelcomeView.png](images_SolverView/014_solver_view_opensFromStudentWelcomeView.png)   
Fig.28. The Solver view open from the Student welcome view.   

![015_solver_view_manyWindows.png](images_SolverView/015_solver_view_manyWindows.png)   
Fig.29. The Solver view, multiple windows displaying at once.   

## Student Welcome View

![001_studentwelcomeview.png](images_StudentWelcomeView/001_studentwelcomeview.png)   
Fig.30. Student view initial window displaying correctly.    

![002_studentwelcomeview.png](images_StudentWelcomeView/002_studentwelcomeview.png)   
Fig.31. Student view with choice of Parson's problems.   

![003_studentwelcomeview_selectable.png](images_StudentWelcomeView/003_studentwelcomeview_selectable.png)   
Fig.32. Selection of problems displaying correctly.   

![004_studentwelcomeview_cosmetic.png](images_StudentWelcomeView/004_studentwelcomeview_cosmetic.png)   
Fig.33. Student view with cosmetic improvements applied.   