

package lab3.task1;

import java.util.Scanner;

/*
 * Lab 3 - Task 1: Student Management with Inheritance
 * ---------------------------------------------------
 * Goal: Show INHERITANCE by creating a Student class that extends Person.
 *       Practice scalable arrays, constants, and menu-driven interaction.
 *
 * What you will learn:
 * - How a subclass (Student) reuses and extends a base class (Person).
 * - How to add a new field (matriculationNumber) that auto-increments.
 * - How to use a constant MAX_ANZAHL to scale to many students.
 * - How to design a simple menu that uses special sentinel inputs:
 *   0 -> exit; MAX_ANZAHL+1 -> display all; MAX_ANZAHL+2 -> display capacity.
 */

// ------------------------- Base class (Superclass) -------------------------
// Person holds common attributes shared by all people
// Use 'protected' so subclasses can access fields directly if needed.
class Person {
    protected String firstName;
    protected String lastName;
    protected int day;    // birth day
    protected int month;  // birth month
    protected int year;   // birth year

    private static int personCount = 0; // counts all Person (and Student) objects created

    public Person(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        personCount++;
    }

    // Getters and setters (encapsulation) — kept simple here
    public String getFirstName() { return firstName; }
    public String getLastName()  { return lastName; }
    public int getDay()          { return day; }
    public int getMonth()        { return month; }
    public int getYear()         { return year; }

    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName)   { this.lastName = lastName; }
    public void setDay(int day)                { this.day = day; }

    // Simple plausibility check: months must be 1..12
    public void setMonth(int month) {
        if (month >= 1 && month <= 12) this.month = month;
        else System.out.println("Invalid month! Must be between 1-12.");
    }

    public void setYear(int year) { this.year = year; }

    public static int getPersonCount() { return personCount; }

    @Override
    public String toString() {
        return firstName + " " + lastName + " (DOB: " + day + "." + month + "." + year + ")";
    }
}

// ------------------------- Derived class (Subclass) ------------------------
// Student extends Person => inherits firstName/lastName/day/month/year + behavior
// Adds a new attribute: matriculationNumber (auto-incremented, starts at 1001)
class Student extends Person {
    private int matriculationNumber; // encapsulated: access via getter/setter

    // static keeps the next number shared across ALL Student objects
    private static int nextMatriculationNumber = 1001; // starts at 1001

    public Student(String firstName, String lastName) {
        super(firstName, lastName);          // call Person constructor
        this.matriculationNumber = nextMatriculationNumber++; // assign and increment
    }

    public int getMatriculationNumber() { return matriculationNumber; }

    // Setter included to satisfy "encapsulation" requirement; in practice, 
    // changing IDs is uncommon. Here we validate a reasonable range.
    public void setMatriculationNumber(int nr) {
        if (nr >= 1001 && nr <= 999999) this.matriculationNumber = nr;
        else System.out.println("Invalid matriculation number.");
    }

    @Override
    public String toString() {
        return "Student{matNo=" + matriculationNumber + ", name=" + firstName + " " + lastName +
               ", DOB=" + day + "." + month + "." + year + "}";
    }
}

// ------------------------- Application (menu) ------------------------------
public class Task1StudentManagementApp {
    // MAX_ANZAHL defines the maximum manageable students (default 500)
    private static final int MAX_ANZAHL = 500;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("=== Lab 3 - Task 1: Student Management with Inheritance ===");

        // Ask how many students we should create now (<= MAX_ANZAHL)
        System.out.print("How many students do you want to create (max " + MAX_ANZAHL + ")? ");
        int count = readInt(sc);
        if (count < 0) count = 0; // normalize
        if (count > MAX_ANZAHL) {
            System.out.println("Capping to MAX_ANZAHL = " + MAX_ANZAHL);
            count = MAX_ANZAHL;
        }

        // Fixed-size array sized to MAX_ANZAHL for scalability
        Student[] students = new Student[MAX_ANZAHL];

        // Collect data for 'count' students
        for (int i = 0; i < count; i++) {
            System.out.println("\nEnter data for student #" + (i + 1));
            System.out.print("First name: ");
            String fn = sc.next();
            System.out.print("Last name: ");
            String ln = sc.next();
            Student s = new Student(fn, ln);

            // Optional birthday (kept consistent with Person API)
            System.out.print("Birth day (1-31): ");
            s.setDay(readInt(sc));

            while (true) {
                System.out.print("Birth month (1-12): ");
                int m = readInt(sc);
                if (m >= 1 && m <= 12) { s.setMonth(m); break; }
                System.out.println("Invalid month! Must be between 1-12.");
            }

            System.out.print("Birth year (e.g., 1999): ");
            s.setYear(readInt(sc));

            students[i] = s; // store in the array
        }

        // ----------------- Menu using sentinel inputs -----------------
        //  0                 -> Exit
        //  MAX_ANZAHL + 1    -> Display ALL students (all attributes)
        //  MAX_ANZAHL + 2    -> Display maximum manageable students (capacity)
        //  Any 1..count      -> Display the student at that 1-based index
        int choice;
        do {
            System.out.println("\n--- Menu ---");
            System.out.println("Enter a number:");
            System.out.println(" 0               -> Exit");
            System.out.println(" " + (MAX_ANZAHL + 1) + "           -> Display ALL students");
            System.out.println(" " + (MAX_ANZAHL + 2) + "           -> Display MAX capacity");
            System.out.println(" 1.." + count + "        -> Display student at that (1-based) index");
            System.out.print("Your input: ");

            choice = readInt(sc);

            if (choice == 0) {
                System.out.println("Goodbye!");
            } else if (choice == MAX_ANZAHL + 1) {
                displayAll(students, count);
            } else if (choice == MAX_ANZAHL + 2) {
                System.out.println("Maximum students: " + MAX_ANZAHL);
            } else if (choice >= 1 && choice <= count) {
                displayOne(students, choice - 1); // convert 1-based -> 0-based
            } else {
                System.out.println("Invalid input.");
            }
        } while (choice != 0);

        sc.close();
    }

    private static void displayAll(Student[] students, int count) {
        if (count == 0) {
            System.out.println("No students.");
            return;
        }
        for (int i = 0; i < count; i++) {
            System.out.println("[" + (i + 1) + "] " + students[i]);
        }
        System.out.println("Total Person objects ever created: " + Person.getPersonCount());
    }

    private static void displayOne(Student[] students, int idx) {
        Student s = students[idx];
        System.out.println(s);
        System.out.println("Matriculation #: " + s.getMatriculationNumber());
        System.out.println("Name: " + s.getFirstName() + " " + s.getLastName());
        System.out.println("DOB: " + s.getDay() + "." + s.getMonth() + "." + s.getYear());
    }

    // Helper: keep asking until user provides an integer
    private static int readInt(Scanner sc) {
        while (!sc.hasNextInt()) {
            System.out.print("Please enter a valid integer: ");
            sc.next();
        }
        return sc.nextInt();
    }
}
