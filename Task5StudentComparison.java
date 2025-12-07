import java.util.*;

// Lab 2 - Task 5: Object Comparison and Equality
class Student implements Comparable<Student> {
    private int studentID;
    private String name;
    private double grade;

    public Student(int studentID, String name, double grade) {
        this.studentID = studentID;
        this.name = name;
        this.grade = grade;
    }

    public int getStudentID() { return studentID; }
    public String getName() { return name; }
    public double getGrade() { return grade; }

    public void setName(String name) { this.name = name; }
    public void setGrade(double grade) { this.grade = grade; }

    // Natural ordering: by grade descending, then name ascending, then ID ascending
    @Override
    public int compareTo(Student other) {
        int byGrade = Double.compare(other.grade, this.grade); // desc
        if (byGrade != 0) return byGrade;
        int byName = this.name.compareToIgnoreCase(other.name);
        if (byName != 0) return byName;
        return Integer.compare(this.studentID, other.studentID);
    }

    // Equality: define students as equal if they have the same studentID
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return studentID == student.studentID;
    }

    @Override
    public int hashCode() {
        return Objects.hash(studentID);
    }

    @Override
    public String toString() {
        return String.format("Student{id=%d, name='%s', grade=%.2f}", studentID, name, grade);
    }

    // Custom comparators
    public static Comparator<Student> byName() {
        return (a, b) -> {
            int c = a.name.compareToIgnoreCase(b.name);
            if (c != 0) return c;
            return Integer.compare(a.studentID, b.studentID);
        };
    }

    public static Comparator<Student> byId() {
        return Comparator.comparingInt(Student::getStudentID);
    }
}

public class Task5StudentComparison {
    public static void main(String[] args) {
        // Sample data with duplicates (same ID) and varying grades
        Student[] students = new Student[] {
            new Student(1001, "Alice", 87.5),
            new Student(1002, "Bob", 91.0),
            new Student(1003, "Charlie", 75.0),
            new Student(1004, "Diana", 91.0),
            new Student(1002, "Bob", 91.0), // duplicate by ID
            new Student(1005, "Eve", 60.0)
        };

        System.out.println("=== Task 5: Object Comparison and Equality ===");

        // 1) Demonstrate equality
        System.out.println("Equality check (students[1] vs students[4]): " + students[1].equals(students[4]));
        System.out.println("Equality check (students[0] vs students[2]): " + students[0].equals(students[2]));

        // 2) Sort students by grade (natural ordering)
        Student[] byGrade = Arrays.copyOf(students, students.length);
        Arrays.sort(byGrade);
        System.out.println("\nSorted by grade (natural): " + Arrays.toString(byGrade));

        // 3) Sort by name using custom comparator
        Student[] byName = Arrays.copyOf(students, students.length);
        Arrays.sort(byName, Student.byName());
        System.out.println("Sorted by name: " + Arrays.toString(byName));

        // 4) Find duplicate students (by ID)
        List<Student> duplicates = findDuplicatesById(students);
        System.out.println("\nDuplicate students (by ID): " + duplicates);

        // 5) Search for students by ID and by name
        int searchId = 1002;
        Student foundById = findById(students, searchId);
        System.out.println("Found by ID " + searchId + ": " + foundById);

        String searchName = "Bob";
        List<Student> foundByName = findByName(students, searchName);
        System.out.println("Found by name '" + searchName + "': " + foundByName);
    }

    public static List<Student> findDuplicatesById(Student[] students) {
        Set<Integer> seen = new HashSet<>();
        List<Student> dups = new ArrayList<>();
        for (Student s : students) {
            if (!seen.add(s.getStudentID())) {
                dups.add(s);
            }
        }
        return dups;
    }

    public static Student findById(Student[] students, int id) {
        for (Student s : students) {
            if (s.getStudentID() == id) return s;
        }
        return null;
    }

    public static List<Student> findByName(Student[] students, String name) {
        List<Student> res = new ArrayList<>();
        for (Student s : students) {
            if (s.getName().equalsIgnoreCase(name)) res.add(s);
        }
        return res;
    }
}
