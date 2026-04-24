Core of the application:
    Drag and Drop Problems with feedback of True/False shared between Solver and Editor

Additional Features:
1. XML database persistence via Jackson
2. Home View for strict separation of concerns for setter and solver 
3. Home View requires a login name for personalization 

For Setter/Editor:

1. Batch imports problems from a .txt file directly into the repository, no GUI needed
2. From Setter Welcome View GUI: personalized welcome message using login name
3. Get to Editor View GUI from Welcome View JTable's first row labelled "NEW"
4. Explicit instructions about the format of the txt file to be passed
5. Setting new problems from CLI by passing a text file 
6. Fixing problems from GUI by passing a text file
7. File filtering: only txt file are allowed to be uploaded from JFileChooser to prevent errors
8. Instant feedback about whether the file is in proper format or not
9. Testing the problems: see and do everything a student can see and do 
10. Deleting exisiting problems
11. Confirmation Dialog when saving over an existing problem
12. Confirmation Dialog when deleting an existing problem
13. For creating/editing/deleting problems see refreshed view of the SetterWelcomeView instantly
 
For Student/Solver:

1. Personalized welcome message using name
2. Student can browse all problems
3. Open any/all problems in separate windows
4. Instant feeedback in text and colored border
5. Retry with reshuffled codeblocks 
6. Student gets to retry/submit as many times as they want
7. Students can go to the next problem in the repo
