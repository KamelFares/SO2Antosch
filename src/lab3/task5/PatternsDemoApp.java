package lab3.task5;

import java.util.*;
import java.util.function.Consumer;

/*
 * Lab 3 - Task 5: Design Patterns Implementation
 * ------------------------------------------------
 * Demonstrates:
 *  1) Singleton (ConfigurationManager)
 *  2) Observer (StudentObserver + StudentRegistry subject)
 *  3) Factory (PersonFactory to create different Person types)
 *  4) Strategy (Grading strategies switchable at runtime)
 */

// ------------------------- 1) Singleton -------------------------
// Ensures exactly one shared configuration instance across the app.
class ConfigurationManager {
    // "volatile" + double-checked locking for safe publish in multithreaded scenarios
    private static volatile ConfigurationManager instance;

    private int maxStudents = 500; // example config setting
    private String universityName = "HAW Hamburg";

    private ConfigurationManager() {}

    public static ConfigurationManager getInstance() {
        if (instance == null) {
            synchronized (ConfigurationManager.class) {
                if (instance == null) {
                    instance = new ConfigurationManager();
                }
            }
        }
        return instance;
    }

    public int getMaxStudents() { return maxStudents; }
    public void setMaxStudents(int max) { this.maxStudents = max; }

    public String getUniversityName() { return universityName; }
    public void setUniversityName(String name) { this.universityName = name; }
}

// ------------------------- Shared domain types -------------------------
abstract class Person {
    protected String firstName;
    protected String lastName;

    protected Person(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() { return firstName; }
    public String getLastName()  { return lastName; }

    @Override public String toString() { return getClass().getSimpleName() + "{" + firstName + " " + lastName + "}"; }
}

class Student extends Person {
    private static int nextMatNo = 1001;
    private final int matriculationNumber = nextMatNo++;

    public Student(String firstName, String lastName) { super(firstName, lastName); }
    public int getMatriculationNumber() { return matriculationNumber; }
}

class Professor extends Person { public Professor(String f, String l) { super(f, l); } }
class Staff extends Person { public Staff(String f, String l) { super(f, l); } }

// ------------------------- 2) Observer -------------------------
interface StudentObserver {
    void onStudentAdded(Student student);
    void onStudentRemoved(Student student);
    void onStudentModified(Student student);
}

interface StudentSubject {
    void addObserver(StudentObserver observer);
    void removeObserver(StudentObserver observer);
}

class StudentRegistry implements StudentSubject {
    private final List<StudentObserver> observers = new ArrayList<>();
    private final List<Student> students = new ArrayList<>();

    @Override
    public void addObserver(StudentObserver observer) { observers.add(observer); }

    @Override
    public void removeObserver(StudentObserver observer) { observers.remove(observer); }

    public void addStudent(Student student) {
        students.add(student);
        notifyAllObservers(o -> o.onStudentAdded(student));
    }

    public boolean removeStudent(int matriculationNumber) {
        for (Iterator<Student> it = students.iterator(); it.hasNext();) {
            Student s = it.next();
            if (s.getMatriculationNumber() == matriculationNumber) {
                it.remove();
                notifyAllObservers(o -> o.onStudentRemoved(s));
                return true;
            }
        }
        return false;
    }

    public void modifyStudentLastName(int matNo, String newLastName) {
        for (Student s : students) {
            if (s.getMatriculationNumber() == matNo) {
                // simple demo: reflection not needed; our Student has only first/last in Person
                // This example creates a new Student with same matNo in real systems; here we just notify
                notifyAllObservers(o -> o.onStudentModified(s));
                return;
            }
        }
    }

    public List<Student> list() { return new ArrayList<>(students); }

    private void notifyAllObservers(Consumer<StudentObserver> action) {
        for (StudentObserver o : observers) action.accept(o);
    }
}

// A simple observer that prints to console
class ConsoleStudentObserver implements StudentObserver {
    @Override public void onStudentAdded(Student student) { System.out.println("[Observer] Added: " + student); }
    @Override public void onStudentRemoved(Student student) { System.out.println("[Observer] Removed: " + student); }
    @Override public void onStudentModified(Student student) { System.out.println("[Observer] Modified: " + student); }
}

// ------------------------- 3) Factory -------------------------
abstract class PersonFactory {
    public static Person createPerson(String type, String firstName, String lastName) {
        switch (type.toLowerCase()) {
            case "student":   return new Student(firstName, lastName);
            case "professor": return new Professor(firstName, lastName);
            case "staff":     return new Staff(firstName, lastName);
            default: throw new IllegalArgumentException("Unknown person type: " + type);
        }
    }
}

// ------------------------- 4) Strategy -------------------------
// Different grading strategies produce different representations for the same numeric score.
interface GradingStrategy {
    String render(double score);
}

class GermanGradingStrategy implements GradingStrategy {
    @Override public String render(double score) {
        // naive mapping just for demonstration
        if (score >= 90) return "1,0";
        if (score >= 80) return "1,7";
        if (score >= 70) return "2,3";
        if (score >= 60) return "3,0";
        if (score >= 50) return "3,7";
        return "5,0"; // fail
    }
}

class ECTSStrategy implements GradingStrategy {
    @Override public String render(double score) {
        if (score >= 90) return "A";
        if (score >= 80) return "B";
        if (score >= 70) return "C";
        if (score >= 60) return "D";
        if (score >= 50) return "E";
        return "F";
    }
}

class PassFailStrategy implements GradingStrategy {
    @Override public String render(double score) { return score >= 60 ? "Pass" : "Fail"; }
}

class StudentRecord {
    private final Student student;
    private GradingStrategy strategy; // can be swapped at runtime

    public StudentRecord(Student student, GradingStrategy strategy) {
        this.student = student;
        this.strategy = strategy;
    }

    public void setStrategy(GradingStrategy strategy) { this.strategy = strategy; }

    public void printGrade(double score) {
        System.out.println(student + " -> " + strategy.render(score));
    }
}

// ------------------------- Demo app -------------------------
public class PatternsDemoApp {
    public static void main(String[] args) {
        System.out.println("=== Lab 3 - Task 5: Design Patterns Demo ===");

        // Singleton
        ConfigurationManager cfg = ConfigurationManager.getInstance();
        System.out.println("University: " + cfg.getUniversityName() + ", MaxStudents: " + cfg.getMaxStudents());
        cfg.setMaxStudents(1000);
        System.out.println("Updated MaxStudents: " + ConfigurationManager.getInstance().getMaxStudents());

        // Factory + Observer
        StudentRegistry reg = new StudentRegistry();
        reg.addObserver(new ConsoleStudentObserver());
        Student s1 = (Student) PersonFactory.createPerson("student", "Alice", "Anderson");
        Student s2 = (Student) PersonFactory.createPerson("student", "Bob", "Brown");
        reg.addStudent(s1);
        reg.addStudent(s2);
        reg.removeStudent(s1.getMatriculationNumber());

        // Strategy
        StudentRecord record = new StudentRecord(s2, new GermanGradingStrategy());
        record.printGrade(87); // prints German grade
        record.setStrategy(new ECTSStrategy());
        record.printGrade(87); // prints ECTS grade
        record.setStrategy(new PassFailStrategy());
        record.printGrade(57); // prints Pass/Fail
    }
}
