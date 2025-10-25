import java.util.Scanner;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

class Person {
    // private variables foolowing encapsulation and for privacy
    private String first_name;
    private String last_name;
    private LocalDate name_change_date;
    
    public Person(String first_name, String last_name, int day, int month, int year) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.name_change_date = LocalDate.of(year, month, day);
    }
    
    public String getFirst_name() {
        return first_name;
    }
    
    public String getLast_name() {
        return last_name;
    }
    
    public LocalDate getName_change_date() {
        return name_change_date;
    }
    
    public void setName(String first_name, String last_name, int day, int month, int year) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.name_change_date = LocalDate.of(year, month, day);
    }
    
    public boolean canChangeName(LocalDate newDate) {
        LocalDate threeYearsAgo = LocalDate.now().minusYears(3);
        return newDate.isAfter(threeYearsAgo);
    }
    
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        return "Name: " + first_name + " " + last_name + ", Name Change Date: " + name_change_date.format(formatter);
    }
}

public class one {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("moinsen ");
        
        // Create three Person objects
        Person person_1 = new Person("Max", "Mustermann", 1, 1, 2020);
        Person person_2 = new Person("Anna", "Schmidt", 15, 6, 2021);
        Person person_3 = new Person("Peter", "Müller", 10, 12, 2019);
        
        while (true) {
            System.out.println("\nWhich person wants to change their name? (1, 2, 3)");
            System.out.println("Enter 0 to exit, 4 to display all persons:");
            System.out.print("Your choice: ");
            
            int choice = scanner.nextInt();
            
            if (choice == 0) {
                System.out.println("Goodbye!");
                break;
            } else if (choice == 4) {
                System.out.println("\nAll persons:");
                System.out.println("Person 1: " + person_1);
                System.out.println("Person 2: " + person_2);
                System.out.println("Person 3: " + person_3);
            } else if (choice >= 1 && choice <= 3) {
                Person selectedPerson = null;
                String personName = "";
                
                switch (choice) {
                    case 1:
                        selectedPerson = person_1;
                        personName = "Person 1";
                        break;
                    case 2:
                        selectedPerson = person_2;
                        personName = "Person 2";
                        break;
                    case 3:
                        selectedPerson = person_3;
                        personName = "Person 3";
                        break;
                }
                
                System.out.println("\nCurrent " + personName + ": " + selectedPerson);
                
                System.out.print("Enter new first name: ");
                scanner.nextLine(); // consume newline
                String newFirstName = scanner.nextLine();
                
                System.out.print("Enter new last name: ");
                String newLastName = scanner.nextLine();
                
                System.out.print("Enter day of name change: ");
                int day = scanner.nextInt();
                
                System.out.print("Enter month of name change: ");
                int month = scanner.nextInt();
                
                System.out.print("Enter year of name change: ");
                int year = scanner.nextInt();
                
                LocalDate newDate = LocalDate.of(year, month, day);
                
                if (selectedPerson.canChangeName(newDate)) {
                    selectedPerson.setName(newFirstName, newLastName, day, month, year);
                    System.out.println("Name change successful!");
                    System.out.println("Updated " + personName + ": " + selectedPerson);
                } else {
                    System.out.println("Error: Name change not allowed. The date must be within the last 3 years from today.");
                }
            } else {
                System.out.println("Invalid choice. Please enter 0, 1, 2, 3, or 4.");
            }
        }
        
        scanner.close();
    }
}
