import java.util.Scanner;

public class Calculator {
    
    // Method for addition
    public static double add(double a, double b) {
        return a + b;
    }
    
    // Method for subtraction
    public static double subtract(double a, double b) {
        return a - b;
    }
    
    // Method for multiplication
    public static double multiply(double a, double b) {
        return a * b;
    }
    
    // Method for division with zero check
    public static double divide(double a, double b) {
        if (b == 0) {
            System.out.println("Error: Division by zero!");
            return 0.0;
        }
        return a / b;
    }
    
    // Method to display the menu
    public static void displayMenu() {
        System.out.println("=== Simple Calculator ===");
        System.out.println("1. Addition (+)");
        System.out.println("2. Subtraction (-)");
        System.out.println("3. Multiplication (*)");
        System.out.println("4. Division (/)");
        System.out.println("5. Exit");
        System.out.print("Choose an operation (1-5): ");
    }
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        
        System.out.println("Welcome to the Simple Calculator! 🧮");
        System.out.println();
        
        // Main program loop
        while (running) {
            // Display menu
            displayMenu();
            
            // Get user's choice
            int choice = scanner.nextInt();
            
            // Handle user's choice
            switch (choice) {
                case 1: // Addition
                    System.out.print("Enter first number: ");
                    double num1 = scanner.nextDouble();
                    System.out.print("Enter second number: ");
                    double num2 = scanner.nextDouble();
                    double result = add(num1, num2);
                    System.out.printf("Result: %.1f + %.1f = %.1f\n\n", num1, num2, result);
                    break;
                    
                case 2: // Subtraction
                    System.out.print("Enter first number: ");
                    num1 = scanner.nextDouble();
                    System.out.print("Enter second number: ");
                    num2 = scanner.nextDouble();
                    result = subtract(num1, num2);
                    System.out.printf("Result: %.1f - %.1f = %.1f\n\n", num1, num2, result);
                    break;
                    
                case 3: // Multiplication
                    System.out.print("Enter first number: ");
                    num1 = scanner.nextDouble();
                    System.out.print("Enter second number: ");
                    num2 = scanner.nextDouble();
                    result = multiply(num1, num2);
                    System.out.printf("Result: %.1f * %.1f = %.1f\n\n", num1, num2, result);
                    break;
                    
                case 4: // Division
                    System.out.print("Enter first number: ");
                    num1 = scanner.nextDouble();
                    System.out.print("Enter second number: ");
                    num2 = scanner.nextDouble();
                    result = divide(num1, num2);
                    System.out.printf("Result: %.1f / %.1f = %.1f\n\n", num1, num2, result);
                    break;
                    
                case 5: // Exit
                    System.out.println("Thank you for using the calculator! 👋");
                    running = false;
                    break;
                    
                default: // Invalid choice
                    System.out.println("Invalid choice! Please select 1-5.\n");
                    break;
            }
        }
        
        // Close scanner
        scanner.close();
    }
}
