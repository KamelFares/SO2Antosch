import java.util.*;

// Lab 2 - Task 2: Student Grade Calculator
public class Task2GradeCalculator {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int[] grades = new int[5];

        System.out.println("=== Task 2: Student Grade Calculator ===");
        for (int i = 0; i < grades.length; i++) {
            grades[i] = readGrade(sc, i + 1);
        }

        System.out.println("\nEntered grades: " + Arrays.toString(grades));

        int average = calculateAverage(grades);
        int highest = findHighest(grades);
        int lowest = findLowest(grades);
        int passing = countPassing(grades);
        int[] dedup = deduplicatedGrades(grades);

        System.out.println("Average (int): " + average);
        System.out.println("Highest: " + highest);
        System.out.println("Lowest: " + lowest);
        System.out.println("Passing (>= 60): " + passing);
        System.out.println("Unique grades: " + Arrays.toString(dedup));

        sc.close();
    }

    private static int readGrade(Scanner sc, int idx) {
        while (true) {
            System.out.print("Enter grade for student " + idx + " (0-100): ");
            if (sc.hasNextInt()) {
                int g = sc.nextInt();
                if (g >= 0 && g <= 100) return g;
            } else {
                sc.next();
            }
            System.out.println("Invalid input. Please enter an integer between 0 and 100.");
        }
    }

    // Required signatures (ints)
    public static int calculateAverage(int[] grades) {
        int sum = 0;
        for (int g : grades) sum += g;
        return sum / grades.length; // integer division per spec
    }

    public static int findHighest(int[] grades) {
        int max = Integer.MIN_VALUE;
        for (int g : grades) if (g > max) max = g;
        return max;
    }

    public static int findLowest(int[] grades) {
        int min = Integer.MAX_VALUE;
        for (int g : grades) if (g < min) min = g;
        return min;
    }

    public static int countPassing(int[] grades) {
        int cnt = 0;
        for (int g : grades) if (g >= 60) cnt++;
        return cnt;
    }

    public static int[] deduplicatedGrades(int[] grades) {
        // Since grades are 0..100, use a boolean occurrence array for stable order of first seen
        boolean[] seen = new boolean[101];
        int uniqueCount = 0;
        for (int g : grades) {
            if (!seen[g]) {
                seen[g] = true;
                uniqueCount++;
            }
        }
        int[] result = new int[uniqueCount];
        int idx = 0;
        // Preserve order of first occurrence from original array
        Arrays.fill(seen, false);
        for (int g : grades) {
            if (!seen[g]) {
                seen[g] = true;
                result[idx++] = g;
            }
        }
        return result;
    }
}
