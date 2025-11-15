import java.util.Scanner;

// Lab 2 - Task 1: Enhanced Name Management System
// Single-file solution: contains Person class and a console app with menu
class Person {
    private String firstName;
    private String lastName;
    private int day;
    private int month;
    private int year;

    private static int personCount = 0;

    public Person(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        personCount++;
    }

    // Getters
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public int getDay() { return day; }
    public int getMonth() { return month; }
    public int getYear() { return year; }

    // Setters
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setDay(int day) { this.day = day; }
    public void setYear(int year) { this.year = year; }

    // Setter with validation for month (must be 1..12)
    public void setMonth(int month) {
        if (month >= 1 && month <= 12) {
            this.month = month;
        } else {
            System.out.println("Invalid month! Must be between 1-12.");
        }
    }

    public static int getPersonCount() { return personCount; }

    @Override
    public String toString() {
        return firstName + " " + lastName + " (DOB: " + day + "." + month + "." + year + ")";
    }
}

public class Task1App {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("=== Task 1: Enhanced Name Management System ===");

        System.out.print("How many persons do you want to create? ");
        int n = readInt(sc);
        if (n <= 0) {
            System.out.println("No persons to manage. Exiting.");
            return;
        }

        Person[] persons = new Person[n];

        // Input persons
        for (int i = 0; i < n; i++) {
            System.out.println("\nEnter data for person #" + (i + 1));
            System.out.print("First name: ");
            String first = sc.next();
            System.out.print("Last name: ");
            String last = sc.next();
            Person p = new Person(first, last);

            System.out.print("Birth day (1-31): ");
            p.setDay(readInt(sc));

            // Ensure valid month using setter validation; keep asking until valid
            while (true) {
                System.out.print("Birth month (1-12): ");
                int m = readInt(sc);
                if (m >= 1 && m <= 12) {
                    p.setMonth(m);
                    break;
                } else {
                    System.out.println("Invalid month! Must be between 1-12.");
                }
            }

            System.out.print("Birth year (e.g., 1999): ");
            p.setYear(readInt(sc));

            persons[i] = p;
        }

        // Menu
        int choice;
        do {
            System.out.println("\n--- Menu ---");
            System.out.println("1: Display all persons");
            System.out.println("2: Display one person by index");
            System.out.println("3: Update a person's birth month");
            System.out.println("4: Change a person's name");
            System.out.println("5: Display total person count (static)");
            System.out.println("0: Exit");
            System.out.print("Enter choice: ");
            choice = readInt(sc);

            switch (choice) {
            case 1:
                displayAll(persons);
                break;
            case 2:
                displayOne(sc, persons);
                break;
            case 3:
                updateMonth(sc, persons);
                break;
            case 4:
                changeName(sc, persons);
                break;
            case 5:
                System.out.println("Total persons created: " + Person.getPersonCount());
                break;
            case 0:
                System.out.println("Goodbye!");
                break;
            default:
                System.out.println("Invalid choice.");
            }
        } while (choice != 0);

        sc.close();
    }

    private static void displayAll(Person[] persons) {
        for (int i = 0; i < persons.length; i++) {
            System.out.println("[" + i + "] " + persons[i]);
        }
    }

    private static void displayOne(Scanner sc, Person[] persons) {
        System.out.print("Index (0-" + (persons.length - 1) + "): ");
        int idx = readInt(sc);
        if (idx >= 0 && idx < persons.length) {
            System.out.println(persons[idx]);
        } else {
            System.out.println("Invalid index.");
        }
    }

    private static void updateMonth(Scanner sc, Person[] persons) {
        System.out.print("Index (0-" + (persons.length - 1) + "): ");
        int idx = readInt(sc);
        if (idx < 0 || idx >= persons.length) { System.out.println("Invalid index."); return; }
        while (true) {
            System.out.print("New month (1-12): ");
            int m = readInt(sc);
            if (m >= 1 && m <= 12) {
                persons[idx].setMonth(m);
                System.out.println("Month updated.");
                break;
            } else {
                System.out.println("Invalid month! Must be between 1-12.");
            }
        }
    }

    private static void changeName(Scanner sc, Person[] persons) {
        System.out.print("Index (0-" + (persons.length - 1) + "): ");
        int idx = readInt(sc);
        if (idx < 0 || idx >= persons.length) { System.out.println("Invalid index."); return; }
        System.out.print("New first name: ");
        persons[idx].setFirstName(sc.next());
        System.out.print("New last name: ");
        persons[idx].setLastName(sc.next());
        System.out.println("Name updated.");
    }

    private static int readInt(Scanner sc) {
        while (!sc.hasNextInt()) {
            System.out.print("Please enter a valid integer: ");
            sc.next();
        }
        return sc.nextInt();
    }
}
