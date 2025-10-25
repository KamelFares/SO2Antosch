import java.util.Scanner;

public class WordCounter {
    
    // Method to check if a character is a vowel (case insensitive)
    public static boolean isVowel(char c) {
        char lowerC = Character.toLowerCase(c);
        return lowerC == 'a' || lowerC == 'e' || lowerC == 'i' || 
               lowerC == 'o' || lowerC == 'u';
    }
    
    // Method to check if a character is a consonant (case insensitive)
    public static boolean isConsonant(char c) {
        char lowerC = Character.toLowerCase(c);
        return Character.isLetter(c) && !isVowel(lowerC);
    }
    
    public static int countSentences(String text) {
        int sentenceCount = 0;
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c == '.' || c == '!' || c == '?') {
                sentenceCount++;
            }
        }
        return sentenceCount;
    }
    
    // Method to find the longest word
    public static String findLongestWord(String text) {
        String[] words = text.split("\\s+");
        String longestWord = "";
        
        for (String word : words) {
            // Remove punctuation from word for comparison
            String cleanWord = word.replaceAll("[^a-zA-Z]", "");
            if (cleanWord.length() > longestWord.length()) {
                longestWord = cleanWord;
            }
        }
        
        return longestWord;
    }
    
    // Method to count words
    public static int countWords(String text) {
        if (text.trim().isEmpty()) {
            return 0;
        }
        String[] words = text.trim().split("\\s+");
        return words.length;
    }
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        // Display welcome message
        System.out.println("=== Word Counter Program ===");
        System.out.println("Analyze text and get detailed statistics!");
        System.out.println();
        
        // Get user input
        System.out.print("Enter a sentence or paragraph to analyze:\n");
        String text = scanner.nextLine();
        
        // Initialize counters
        int totalChars = text.length();
        int charsWithoutSpaces = 0;
        int vowelCount = 0;
        int consonantCount = 0;
        
        // Analyze each character
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            
            if (Character.isLetter(c)) {
                charsWithoutSpaces++;
                if (isVowel(c)) {
                    vowelCount++;
                } else {
                    consonantCount++;
                }
            } else if (c != ' ') {
                charsWithoutSpaces++; // Count non-space, non-letter characters
            }
        }
        
        // Calculate other statistics
        int wordCount = countWords(text);
        int sentenceCount = countSentences(text);
        String longestWord = findLongestWord(text);
        
        // Display results
        System.out.println("\n=== TEXT ANALYSIS REPORT ===");
        System.out.println("Original text: \"" + text + "\"");
        System.out.println();
        
        System.out.println("Character Statistics:");
        System.out.println("- Total characters (with spaces): " + totalChars);
        System.out.println("- Total characters (without spaces): " + charsWithoutSpaces);
        System.out.println("- Number of vowels: " + vowelCount);
        System.out.println("- Number of consonants: " + consonantCount);
        System.out.println();
        
        System.out.println("Word Statistics:");
        System.out.println("- Total number of words: " + wordCount);
        System.out.println("- Longest word: \"" + longestWord + "\"");
        System.out.println();
        
        System.out.println("Sentence Statistics:");
        System.out.println("- Total number of sentences: " + sentenceCount);
        
        // Close scanner
        scanner.close();
        
        System.out.println("\nText analysis complete! 📊");
    }
}