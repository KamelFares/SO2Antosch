package lab3.task4;

import java.util.*;
import java.util.function.Predicate;

/*
 * Lab 3 - Task 4: Collections in Java Programming
 * ------------------------------------------------
 * Goal: Replace arrays with Java Collections (ArrayList) and add sorting and searching.
 *       Demonstrate List vs Set vs Map, custom comparators, and lambda-based filters.
 *
 * What you will learn:
 * - Using ArrayList<Student> instead of Student[].
 * - Sorting with Comparator (by name, matriculation number, age).
 * - Searching with predicates (partial name, matric range, age conditions).
 * - Preventing duplicates with Set and quick lookups with Map.
 */

// ------------------------- Domain model -------------------------
// Keep Student simple and self-contained for this task/package.
class Student {
    private final int matriculationNumber; // unique identifier
    private String firstName;
    private String lastName;
    private int birthYear; // for simple age calculation

    public Student(int matriculationNumber, String firstName, String lastName, int birthYear) {
        this.matriculationNumber = matriculationNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthYear = birthYear;
    }

    public int getMatriculationNumber() { return matriculationNumber; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public int getBirthYear() { return birthYear; }

    public String getFullName() { return firstName + " " + lastName; }

    public int getAge() {
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        return Math.max(0, currentYear - birthYear);
    }

    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setBirthYear(int birthYear) { this.birthYear = birthYear; }

    @Override
    public String toString() {
        return String.format("Student{matNo=%d, name=%s %s, birthYear=%d, age=%d}",
                matriculationNumber, firstName, lastName, birthYear, getAge());
    }
}

// ------------------------- Registry using Collections -------------------------
class StudentRegistry {
    // Primary storage keeps insertion order and allows indexing
    private final List<Student> students = new ArrayList<>();

    // A Set that prevents duplicate matric numbers (we store the matNo only)
    private final Set<Integer> knownMatricNumbers = new HashSet<>();

    // A Map for O(1)-ish lookups by matriculation number
    private final Map<Integer, Student> byMatric = new HashMap<>();

    // Add student if not already present (by matriculation number)
    public boolean addStudent(Student s) {
        if (knownMatricNumbers.add(s.getMatriculationNumber())) {
            students.add(s);
            byMatric.put(s.getMatriculationNumber(), s);
            return true;
        }
        return false; // duplicate prevented by Set characteristic
    }

    public List<Student> asList() { return new ArrayList<>(students); }

    public Student findByMatric(int matNo) { return byMatric.get(matNo); }

    // Generic filter helper using a lambda Predicate
    public List<Student> filter(Predicate<Student> predicate) {
        List<Student> result = new ArrayList<>();
        for (Student s : students) if (predicate.test(s)) result.add(s);
        return result;
    }

    // Search features
    public List<Student> findByPartialName(String part) {
        String needle = part.toLowerCase();
        return filter(s -> s.getFullName().toLowerCase().contains(needle));
    }

    public List<Student> findByMatricRange(int min, int max) {
        return filter(s -> s.getMatriculationNumber() >= min && s.getMatriculationNumber() <= max);
    }

    public List<Student> findOlderThan(int age) {
        return filter(s -> s.getAge() > age);
    }

    public List<Student> findYoungerThan(int age) {
        return filter(s -> s.getAge() < age);
    }

    // Sorting options (return new lists, keep internal order intact)
    public List<Student> sortedByName() {
        List<Student> copy = asList();
        copy.sort(Comparator.comparing(Student::getLastName, String.CASE_INSENSITIVE_ORDER)
                            .thenComparing(Student::getFirstName, String.CASE_INSENSITIVE_ORDER)
                            .thenComparingInt(Student::getMatriculationNumber));
        return copy;
    }

    public List<Student> sortedByMatric() {
        List<Student> copy = asList();
        copy.sort(Comparator.comparingInt(Student::getMatriculationNumber));
        return copy;
    }

    public List<Student> sortedByAgeDescending() {
        List<Student> copy = asList();
        copy.sort(Comparator.comparingInt(Student::getAge).reversed()
                            .thenComparing(Student::getLastName, String.CASE_INSENSITIVE_ORDER)
                            .thenComparing(Student::getFirstName, String.CASE_INSENSITIVE_ORDER));
        return copy;
    }
}

// ------------------------- Demo application -------------------------
public class StudentRegistryApp {
    public static void main(String[] args) {
        System.out.println("=== Lab 3 - Task 4: Collections in Java Programming ===");
        StudentRegistry reg = new StudentRegistry();

        // Insert demo data; duplicates prevented by Set (matric numbers must be unique)
        reg.addStudent(new Student(1001, "Alice", "Anderson", 2000));
        reg.addStudent(new Student(1002, "Bob", "Brown", 1999));
        reg.addStudent(new Student(1003, "Charlie", "Clark", 2002));
        reg.addStudent(new Student(1004, "Diana", "Dawson", 2001));
        boolean added = reg.addStudent(new Student(1002, "Bob", "Brown", 1999)); // duplicate matNo
        System.out.println("Attempt to add duplicate matNo 1002 -> " + (added ? "added" : "blocked"));

        // Show List vs Set vs Map
        System.out.println("\nAll students (List, preserves insertion order):");
        for (Student s : reg.asList()) System.out.println(" - " + s);

        System.out.println("\nLookup by matric 1003 (Map gives fast direct access):\n -> " + reg.findByMatric(1003));

        // Searching (using lambdas/predicates)
        System.out.println("\nFind by partial name 'an':");
        for (Student s : reg.findByPartialName("an")) System.out.println(" - " + s);

        System.out.println("\nFind by matric range [1002..1004]:");
        for (Student s : reg.findByMatricRange(1002, 1004)) System.out.println(" - " + s);

        System.out.println("\nFind older than 23:");
        for (Student s : reg.findOlderThan(23)) System.out.println(" - " + s);

        // Sorting demonstrations
        System.out.println("\nSorted by name:");
        for (Student s : reg.sortedByName()) System.out.println(" - " + s);

        System.out.println("\nSorted by matriculation number:");
        for (Student s : reg.sortedByMatric()) System.out.println(" - " + s);

        System.out.println("\nSorted by age (descending):");
        for (Student s : reg.sortedByAgeDescending()) System.out.println(" - " + s);
    }
}
