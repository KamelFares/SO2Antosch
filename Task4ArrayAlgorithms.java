import java.util.*;

// Lab 2 - Task 4: Array Sorting and Searching
public class Task4ArrayAlgorithms {
    // Counters for comparison statistics
    private static int linearComparisons;
    private static int binaryComparisons;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Random random = new Random();
        int[] numbers = new int[10];
        for (int i = 0; i < numbers.length; i++) {
            numbers[i] = random.nextInt(100) + 1; // 1-100
        }

        System.out.println("=== Task 4: Array Sorting and Searching ===");
        System.out.print("Original array: ");
        printArray(numbers);

        // Bubble sort
        bubbleSort(numbers);
        System.out.print("Sorted array:   ");
        printArray(numbers);

        // Ask for search target and compare search algorithms
        System.out.print("Enter a value to search for: ");
        int target = readInt(sc);

        linearComparisons = 0;
        int idxLinear = linearSearch(numbers, target);
        System.out.println("Linear search index: " + idxLinear + ", comparisons: " + linearComparisons);

        binaryComparisons = 0;
        int idxBinary = binarySearch(numbers, target);
        System.out.println("Binary search index: " + idxBinary + ", comparisons: " + binaryComparisons);

        sc.close();
    }

    public static void bubbleSort(int[] arr) {
        boolean swapped;
        for (int pass = 0; pass < arr.length - 1; pass++) {
            swapped = false;
            for (int i = 0; i < arr.length - 1 - pass; i++) {
                if (arr[i] > arr[i + 1]) {
                    int tmp = arr[i];
                    arr[i] = arr[i + 1];
                    arr[i + 1] = tmp;
                    swapped = true;
                }
            }
            if (!swapped) break; // already sorted
        }
    }

    public static int linearSearch(int[] arr, int target) {
        for (int i = 0; i < arr.length; i++) {
            linearComparisons++;
            if (arr[i] == target) return i;
        }
        return -1;
    }

    public static int binarySearch(int[] arr, int target) {
        int lo = 0, hi = arr.length - 1;
        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            binaryComparisons++; // comparing target with arr[mid]
            if (arr[mid] == target) return mid;
            if (target < arr[mid]) {
                hi = mid - 1;
            } else {
                lo = mid + 1;
            }
        }
        return -1;
    }

    public static void printArray(int[] arr) {
        System.out.println(Arrays.toString(arr));
    }

    private static int readInt(Scanner sc) {
        while (!sc.hasNextInt()) {
            System.out.print("Please enter a valid integer: ");
            sc.next();
        }
        return sc.nextInt();
    }
}
