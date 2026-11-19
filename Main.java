import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {
    private static final Scanner in = new Scanner(System.in);
    private static final StudentManager manager = new StudentManager();

    public static void main(String[] args) {
        System.out.println("=== Student Management System ===");
        while (true) {
            printMenu();
            int choice = readInt("Choose an option: ");
            switch (choice) {
                case 1: addStudent(); break;
                case 2: listStudents(); break;
                case 3: searchStudent(); break;
                case 4: addGrade(); break;
                case 5: updateDepartment(); break;
                case 6: deleteStudent(); break;
                case 7: showTopStudent(); break;
                case 0:
                    System.out.println("Goodbye!");
                    return;
                default: System.out.println("Invalid option, try again.");
            }
        }
    }

    private static void printMenu() {
        System.out.println("\n1. Add student");
        System.out.println("2. List all students");
        System.out.println("3. Search student by ID");
        System.out.println("4. Add course grade");
        System.out.println("5. Update department");
        System.out.println("6. Delete student");
        System.out.println("7. Show top student");
        System.out.println("0. Exit");
    }

    private static void addStudent() {
        int id = readInt("ID: ");
        String name = readText("Name: ");
        String email = readText("Email: ");
        String dept = readText("Department: ");
        if (name.isEmpty()) {
            System.out.println("Name cannot be empty.");
            return;
        }
        boolean ok = manager.addStudent(new Student(id, name, email, dept));
        System.out.println(ok ? "Student added." : "A student with this ID already exists.");
    }

    private static void listStudents() {
        if (manager.getAll().isEmpty()) {
            System.out.println("No students yet.");
            return;
        }
        for (Student s : manager.getAll()) System.out.println(s);
    }

    private static void searchStudent() {
        Student s = manager.findById(readInt("ID: "));
        if (s == null) {
            System.out.println("Student not found.");
            return;
        }
        System.out.println(s);
        for (Map.Entry<String, Double> e : s.getGrades().entrySet()) {
            System.out.printf("  %s -> %.2f%n", e.getKey(), e.getValue());
        }
    }

    private static void addGrade() {
        Student s = manager.findById(readInt("Student ID: "));
        if (s == null) {
            System.out.println("Student not found.");
            return;
        }
        String course = readText("Course name: ");
        double gp = readDouble("Grade point (0.0 - 4.0): ");
        try {
            s.addGrade(course, gp);
            System.out.printf("Saved. New CGPA: %.2f%n", s.calculateCGPA());
        } catch (IllegalArgumentException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private static void updateDepartment() {
        Student s = manager.findById(readInt("Student ID: "));
        if (s == null) {
            System.out.println("Student not found.");
            return;
        }
        s.setDepartment(readText("New department: "));
        System.out.println("Updated.");
    }

    private static void deleteStudent() {
        boolean ok = manager.removeById(readInt("Student ID: "));
        System.out.println(ok ? "Deleted." : "Student not found.");
    }

    private static void showTopStudent() {
        Student top = manager.topStudent();
        System.out.println(top == null ? "No students yet." : "Top student -> " + top);
    }

    private static String readText(String prompt) {
        System.out.print(prompt);
        return in.nextLine().trim();
    }

    private static int readInt(String prompt) {
        while (true) {
            try {
                return Integer.parseInt(readText(prompt));
            } catch (NumberFormatException e) {
                System.out.println("Please enter a whole number.");
            }
        }
    }

    private static double readDouble(String prompt) {
        while (true) {
            try {
                return Double.parseDouble(readText(prompt));
            } catch (NumberFormatException e) {
                System.out.println("Please enter a number.");
            }
        }
    }
}

interface Gradable {
    double calculateCGPA();
}

abstract class Person {
    private String name;
    private String email;

    public Person(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String getName() { return name; }
    public String getEmail() { return email; }
    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }

    public abstract String getRole();

    @Override
    public String toString() {
        return getRole() + ": " + name + " (" + email + ")";
    }
}

class Student extends Person implements Gradable {
    private final int id;
    private String department;

    private final Map<String, Double> grades = new LinkedHashMap<>();

    public Student(int id, String name, String email, String department) {
        super(name, email);
        this.id = id;
        this.department = department;
    }

    public int getId() { return id; }
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public void addGrade(String course, double gradePoint) {
        if (gradePoint < 0.0 || gradePoint > 4.0) {
            throw new IllegalArgumentException("Grade point must be between 0.0 and 4.0");
        }
        grades.put(course, gradePoint);
    }

    public Map<String, Double> getGrades() { return grades; }

    @Override
    public double calculateCGPA() {
        if (grades.isEmpty()) return 0.0;
        double sum = 0;
        for (double g : grades.values()) sum += g;
        return sum / grades.size();
    }

    @Override
    public String getRole() { return "Student"; }

    @Override
    public String toString() {
        return String.format("ID: %d | %s | Dept: %s | CGPA: %.2f",
                id, super.toString(), department, calculateCGPA());
    }
}

class StudentManager {
    private final List<Student> students = new ArrayList<>();

    public boolean addStudent(Student s) {
        if (findById(s.getId()) != null) return false;
        students.add(s);
        return true;
    }

    public Student findById(int id) {
        for (Student s : students) {
            if (s.getId() == id) return s;
        }
        return null;
    }

    public boolean removeById(int id) {
        Student s = findById(id);
        return s != null && students.remove(s);
    }

    public List<Student> getAll() { return students; }

    public Student topStudent() {
        Student best = null;
        for (Student s : students) {
            if (best == null || s.calculateCGPA() > best.calculateCGPA()) best = s;
        }
        return best;
    }
}
