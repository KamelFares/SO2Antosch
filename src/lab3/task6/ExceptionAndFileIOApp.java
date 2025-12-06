package lab3.task6;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.logging.*;

/*
 * Lab 3 - Task 6: Exception Handling and File I/O
 * ------------------------------------------------
 * Goal: Add robust exception handling, custom exceptions, and file-based persistence.
 *
 * What you will learn:
 * - How to define a custom exception hierarchy.
 * - How to validate inputs and throw domain-specific exceptions.
 * - How to use try-with-resources for safe file IO (CSV + simple JSON).
 * - How to use java.util.logging for basic logging with levels.
 */

// ------------------------- Custom exception hierarchy -------------------------
class StudentManagementException extends Exception {
    public StudentManagementException(String message) { super(message); }
    public StudentManagementException(String message, Throwable cause) { super(message, cause); }
}

class StudentNotFoundException extends StudentManagementException {
    public StudentNotFoundException(int matNo) { super("Student with matriculation number " + matNo + " not found"); }
}

class StudentRegistryFullException extends StudentManagementException {
    public StudentRegistryFullException(int maxCapacity) { super("Student registry is full. Maximum capacity: " + maxCapacity); }
}

class InvalidMatriculationNumberException extends StudentManagementException {
    public InvalidMatriculationNumberException(String message) { super(message); }
}

class DataPersistenceException extends StudentManagementException {
    public DataPersistenceException(String message, Throwable cause) { super(message, cause); }
}

// ------------------------- Validation helpers -------------------------
class InputValidator {
    private static final String NAME_REGEX = "[a-zA-ZäöüÄÖÜß\\s-]+";

    public static void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        if (!name.matches(NAME_REGEX)) {
            throw new IllegalArgumentException("Name contains invalid characters");
        }
    }

    public static void validateMatriculationNumber(int number) throws InvalidMatriculationNumberException {
        if (number < 1001 || number > 999999) {
            throw new InvalidMatriculationNumberException("Matriculation number must be between 1001 and 999999");
        }
    }
}

// ------------------------- Domain model -------------------------
class Student {
    private final int matriculationNumber;
    private final String firstName;
    private final String lastName;
    private final int birthYear;

    public Student(int matriculationNumber, String firstName, String lastName, int birthYear)
            throws InvalidMatriculationNumberException {
        InputValidator.validateMatriculationNumber(matriculationNumber);
        InputValidator.validateName(firstName);
        InputValidator.validateName(lastName);
        this.matriculationNumber = matriculationNumber;
        this.firstName = firstName.trim();
        this.lastName = lastName.trim();
        this.birthYear = birthYear;
    }

    public int getMatriculationNumber() { return matriculationNumber; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public int getBirthYear() { return birthYear; }

    public int getAge() {
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        return Math.max(0, currentYear - birthYear);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Student)) return false;
        Student student = (Student) o;
        return matriculationNumber == student.matriculationNumber;
    }

    @Override
    public int hashCode() { return Objects.hash(matriculationNumber); }

    @Override
    public String toString() {
        return String.format("Student{matNo=%d, name=%s %s, birthYear=%d}",
                matriculationNumber, firstName, lastName, birthYear);
    }
}

// ------------------------- Registry with capacity + logging -------------------------
class StudentRegistry {
    private static final Logger LOG = Logger.getLogger(StudentRegistry.class.getName());

    private final int maxCapacity;
    private final List<Student> students = new ArrayList<>();
    private final Set<Integer> matSet = new HashSet<>();

    public StudentRegistry(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public void addStudent(Student s) throws StudentRegistryFullException {
        if (students.size() >= maxCapacity) {
            LOG.warning("Registry full");
            throw new StudentRegistryFullException(maxCapacity);
        }
        if (!matSet.add(s.getMatriculationNumber())) {
            LOG.warning("Duplicate matriculation number: " + s.getMatriculationNumber());
            // silently ignore duplicate add; alternatively throw a custom DuplicateStudentException
            return;
        }
        students.add(s);
        LOG.info("Added student: " + s);
    }

    public Student findByMatric(int matNo) throws StudentNotFoundException {
        for (Student s : students) if (s.getMatriculationNumber() == matNo) return s;
        LOG.warning("Not found: matNo=" + matNo);
        throw new StudentNotFoundException(matNo);
    }

    public boolean removeByMatric(int matNo) throws StudentNotFoundException {
        Iterator<Student> it = students.iterator();
        while (it.hasNext()) {
            Student s = it.next();
            if (s.getMatriculationNumber() == matNo) {
                it.remove();
                matSet.remove(matNo);
                LOG.info("Removed student: " + s);
                return true;
            }
        }
        throw new StudentNotFoundException(matNo);
    }

    public List<Student> list() { return new ArrayList<>(students); }
}

// ------------------------- Data persistence (CSV + simple JSON) -------------------------
class StudentDataManager {
    private static final String CSV_HEADER = "MatriculationNumber,FirstName,LastName,BirthYear";

    // Save as CSV using try-with-resources
    public void saveToCsv(List<Student> students, String filename) throws DataPersistenceException {
        try (PrintWriter out = new PrintWriter(new OutputStreamWriter(new FileOutputStream(filename), StandardCharsets.UTF_8))) {
            out.println(CSV_HEADER);
            for (Student s : students) {
                out.printf("%d,%s,%s,%d%n", s.getMatriculationNumber(), s.getFirstName(), s.getLastName(), s.getBirthYear());
            }
        } catch (IOException e) {
            throw new DataPersistenceException("Failed to save CSV", e);
        }
    }

    // Load from CSV with basic validation and error recovery (skip bad lines)
    public List<Student> loadFromCsv(String filename) throws DataPersistenceException {
        List<Student> res = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filename), StandardCharsets.UTF_8))) {
            String line = br.readLine(); // header
            if (line == null) return res;
            while ((line = br.readLine()) != null) {
                try {
                    String[] parts = line.split(",");
                    if (parts.length != 4) throw new IllegalArgumentException("Invalid CSV format");
                    int mat = Integer.parseInt(parts[0].trim());
                    String fn = parts[1].trim();
                    String ln = parts[2].trim();
                    int by = Integer.parseInt(parts[3].trim());
                    res.add(new Student(mat, fn, ln, by));
                } catch (Exception ex) {
                    // Skip malformed line quietly but could also log
                }
            }
        } catch (IOException e) {
            throw new DataPersistenceException("Failed to load CSV", e);
        }
        return res;
    }

    // Simple JSON writer (no external libs): writes a JSON array of objects
    public void saveToJson(List<Student> students, String filename) throws DataPersistenceException {
        try (PrintWriter out = new PrintWriter(new OutputStreamWriter(new FileOutputStream(filename), StandardCharsets.UTF_8))) {
            out.println("[");
            for (int i = 0; i < students.size(); i++) {
                Student s = students.get(i);
                out.printf(Locale.ROOT,
                        "  {\"matriculationNumber\":%d,\"firstName\":\"%s\",\"lastName\":\"%s\",\"birthYear\":%d}%s%n",
                        s.getMatriculationNumber(), escapeJson(s.getFirstName()), escapeJson(s.getLastName()), s.getBirthYear(),
                        (i < students.size() - 1 ? "," : ""));
            }
            out.println("]");
        } catch (IOException e) {
            throw new DataPersistenceException("Failed to save JSON", e);
        }
    }

    // Naive JSON loader that expects the format produced by saveToJson above
    public List<Student> loadFromJson(String filename) throws DataPersistenceException {
        List<Student> res = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filename), StandardCharsets.UTF_8))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) sb.append(line.trim());
            String json = sb.toString();
            if (json.length() < 2 || json.charAt(0) != '[') return res;
            // Remove [ ] and split on '},{' boundaries conservatively
            String body = json.substring(1, json.length() - 1).trim();
            if (body.isEmpty()) return res;
            String[] objs = body.split("},\\s*\\{");
            for (String obj : objs) {
                String o = obj;
                if (!o.startsWith("{")) o = "{" + o;
                if (!o.endsWith("}")) o = o + "}";
                Map<String, String> map = parseFlatJsonObject(o);
                int mat = Integer.parseInt(map.get("matriculationNumber"));
                String fn = map.get("firstName");
                String ln = map.get("lastName");
                int by = Integer.parseInt(map.get("birthYear"));
                res.add(new Student(mat, fn, ln, by));
            }
        } catch (IOException e) {
            throw new DataPersistenceException("Failed to load JSON", e);
        } catch (Exception e) {
            throw new DataPersistenceException("Malformed JSON content", e);
        }
        return res;
    }

    // Helpers for minimal JSON
    private static String escapeJson(String s) { return s.replace("\\", "\\\\").replace("\"", "\\\""); }

    // Very naive parser for a flat JSON object with numeric or string values
    private static Map<String, String> parseFlatJsonObject(String obj) {
        Map<String, String> map = new HashMap<>();
        String inner = obj.trim();
        if (inner.startsWith("{")) inner = inner.substring(1);
        if (inner.endsWith("}")) inner = inner.substring(0, inner.length() - 1);
        // Split on commas not inside quotes (simple state machine)
        List<String> pairs = new ArrayList<>();
        StringBuilder cur = new StringBuilder();
        boolean inStr = false;
        for (int i = 0; i < inner.length(); i++) {
            char c = inner.charAt(i);
            if (c == '"') {
                inStr = !inStr;
                cur.append(c);
            } else if (c == ',' && !inStr) {
                pairs.add(cur.toString());
                cur.setLength(0);
            } else {
                cur.append(c);
            }
        }
        if (cur.length() > 0) pairs.add(cur.toString());
        for (String p : pairs) {
            String[] kv = p.split(":", 2);
            String k = stripQuotes(kv[0].trim());
            String v = kv[1].trim();
            if (v.startsWith("\"")) v = stripQuotes(v);
            map.put(k, v);
        }
        return map;
    }

    private static String stripQuotes(String s) {
        if (s.startsWith("\"") && s.endsWith("\"")) return s.substring(1, s.length() - 1);
        return s;
    }
}

// ------------------------- Demo application -------------------------
public class ExceptionAndFileIOApp {
    private static final Logger LOG = Logger.getLogger(ExceptionAndFileIOApp.class.getName());

    public static void main(String[] args) {
        configureLogging();
        System.out.println("=== Lab 3 - Task 6: Exceptions and File I/O ===");

        StudentRegistry registry = new StudentRegistry(3); // small capacity to trigger errors
        StudentDataManager io = new StudentDataManager();

        try {
            // Create students with validation
            Student a = new Student(1001, "Alice", "Anderson", 2000);
            Student b = new Student(1002, "Bob", "Brown", 1999);
            Student c = new Student(1003, "Charlie", "Clark", 1998);

            // Add them (capacity 3)
            registry.addStudent(a);
            registry.addStudent(b);
            registry.addStudent(c);

            // Attempt to add one more to trigger StudentRegistryFullException
            try {
                Student d = new Student(1004, "Diana", "Dawson", 2001);
                registry.addStudent(d);
            } catch (StudentRegistryFullException ex) {
                LOG.log(Level.WARNING, "Cannot add more students: " + ex.getMessage());
            }

            // Save to CSV and JSON
            io.saveToCsv(registry.list(), "students.csv");
            io.saveToJson(registry.list(), "students.json");
            System.out.println("Saved students to students.csv and students.json");

            // Load back from CSV and JSON
            List<Student> fromCsv = io.loadFromCsv("students.csv");
            List<Student> fromJson = io.loadFromJson("students.json");
            System.out.println("Loaded from CSV: " + fromCsv.size() + " students");
            System.out.println("Loaded from JSON: " + fromJson.size() + " students");

            // Find and remove example with exception handling
            try {
                Student found = registry.findByMatric(1002);
                System.out.println("Found: " + found);
                registry.removeByMatric(1002);
                System.out.println("Removed matNo 1002 from registry");
            } catch (StudentNotFoundException ex) {
                LOG.log(Level.WARNING, ex.getMessage());
            }
        } catch (InvalidMatriculationNumberException | DataPersistenceException | StudentRegistryFullException e) {
            LOG.log(Level.SEVERE, "Critical error: " + e.getMessage(), e);
        }
    }

    private static void configureLogging() {
        Logger root = Logger.getLogger("");
        for (Handler h : root.getHandlers()) root.removeHandler(h);
        ConsoleHandler handler = new ConsoleHandler();
        handler.setLevel(Level.ALL);
        root.addHandler(handler);
        root.setLevel(Level.ALL);
    }
}
