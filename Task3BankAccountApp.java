import java.util.Scanner;

// Lab 2 - Task 3: Simple Bank Account Simulator
class BankAccount {
    private double balance;
    private String accountHolder;

    public BankAccount(String name, double initialBalance) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Account holder name required");
        }
        if (initialBalance < 0) {
            throw new IllegalArgumentException("Initial balance cannot be negative");
        }
        this.accountHolder = name.trim();
        this.balance = initialBalance;
    }

    public String getAccountHolder() {
        return accountHolder;
    }

    public double getBalance() {
        return balance;
    }

    public boolean deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            return true;
        }
        return false;
    }

    public boolean withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "BankAccount{holder='" + accountHolder + "', balance=" + String.format("%.2f", balance) + "}";
    }
}

public class Task3BankAccountApp {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("=== Task 3: Bank Account Simulator ===");

        System.out.print("Enter account holder name: ");
        String name = readLine(sc);
        System.out.print("Enter initial balance: ");
        double initial = readPositiveOrZero(sc);

        BankAccount account = new BankAccount(name, initial);
        int choice;
        do {
            System.out.println("\n--- Menu ---");
            System.out.println("1: Check balance");
            System.out.println("2: Make deposit");
            System.out.println("3: Make withdrawal");
            System.out.println("4: Display account info");
            System.out.println("0: Exit");
            System.out.print("Enter choice: ");
            choice = readInt(sc);

            switch (choice) {
                case 1:
                    System.out.printf("Current balance: %.2f%n", account.getBalance());
                    break;
                case 2:
                    System.out.print("Deposit amount: ");
                    double dep = readPositive(sc);
                    if (account.deposit(dep)) {
                        System.out.printf("Deposit successful. New balance: %.2f%n", account.getBalance());
                    } else {
                        System.out.println("Deposit failed. Amount must be positive.");
                    }
                    break;
                case 3:
                    System.out.print("Withdrawal amount: ");
                    double w = readPositive(sc);
                    if (account.withdraw(w)) {
                        System.out.printf("Withdrawal successful. New balance: %.2f%n", account.getBalance());
                    } else {
                        System.out.println("Withdrawal failed. Ensure amount is positive and funds are sufficient.");
                    }
                    break;
                case 4:
                    System.out.println(account);
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

    private static int readInt(Scanner sc) {
        while (!sc.hasNextInt()) {
            System.out.print("Please enter a valid integer: ");
            sc.next();
        }
        return sc.nextInt();
    }

    private static double readPositive(Scanner sc) {
        while (true) {
            if (sc.hasNextDouble()) {
                double v = sc.nextDouble();
                if (v > 0) return v;
            } else {
                sc.next();
            }
            System.out.print("Please enter a positive number: ");
        }
    }

    private static double readPositiveOrZero(Scanner sc) {
        while (true) {
            if (sc.hasNextDouble()) {
                double v = sc.nextDouble();
                if (v >= 0) return v;
            } else {
                sc.next();
            }
            System.out.print("Please enter a non-negative number: ");
        }
    }

    private static String readLine(Scanner sc) {
        String line = sc.nextLine();
        if (line.isEmpty()) line = sc.nextLine();
        return line;
    }
}
