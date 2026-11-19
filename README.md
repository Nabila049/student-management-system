Student Management System in Java

A console program that keeps student records. You can add a student, see the full list, search by ID, change a department, delete a student, add course grades and find the student with the highest CGPA. The CGPA is worked out automatically from the grade points (0.0 to 4.0) of all the courses a student has. Duplicate IDs and wrong inputs are handled, so the program does not crash.

The project is a small exercise in object oriented programming. Here is how the files fit together:

Person is an abstract class that holds a name and an email.
Student extends Person and implements the Gradable interface.
StudentManager looks after the list of students.
Main shows the menu and takes the input.

Inheritance, abstraction, polymorphism and encapsulation all appear somewhere in the code.

How to run

You need JDK 11 or newer. Open the project folder in a terminal and run:

    javac *.java
    java Main
