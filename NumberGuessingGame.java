import java.util.Random;
import java.util.Scanner;

public class NumberGuessingGame {
    public static void main(String[] args) {
        // create psuedo random and scanner objects
        Random rand = new Random();
        Scanner scanner = new Scanner(System.in);
        
        // Generate random number between 1 and 100 (including the borders)
        int randomNumber = rand.nextInt(100) + 1;
        
        // Display welcome message and game rules
        System.out.println("=== Welcome to the Number Guessing Game! ===");
        System.out.println("I've generated a random number between 1 and 100.");
        System.out.println("Try to guess what it is!");
        System.out.println("I'll give you hints: 'Too small!' or 'Too big!'");
        System.out.println("Good luck!\n");
        
        // Initialize game variables
        int attempts = 0;
        //flag for the start and end
        boolean gameRunning = true;
        
        // Main  loop
        while (gameRunning) {
            // Ask for user's guess
            System.out.print("Enter your guess: ");
            int userGuess = scanner.nextInt();
            attempts++; // Increment attempt counter
            
            // Check the guess and provide feedback
            if (userGuess == randomNumber) {
                // Correct guess - end the game
                System.out.println("u r right");
                System.out.println("the number was " + randomNumber);
                System.out.println("it took you " + attempts + " attempts to guess correctly");
                gameRunning = false; // End the game
            } else if (userGuess < randomNumber) {
                // Guess is too small
                System.out.println("Too small! Try a bigger number.");
            } else {
                // Guess is too big
                System.out.println("Too big! Try a smaller number.");
            }
            
           
        }
        
        scanner.close();
        
    }
}