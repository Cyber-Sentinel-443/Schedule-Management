import java.io.*;
import java.util.*;

public class filehandler {
    public static void main(String[] args) {
        // Define the arrays
        int[] intArray1 = {1, 2, 3, 4, 5};
        int[] intArray2 = {10, 20, 30, 40, 50};
        String[] stringArray1 = {"apple", "banana", "cherry"};
        String[] stringArray2 = {"dog", "elephant", "fox"};

        String filename = "data.txt";

        // Write arrays to file
        writeArraysToFile(filename, intArray1, intArray2, stringArray1, stringArray2);

        // Read arrays from file
        readArraysFromFile(filename);
    }

    public static void writeArraysToFile(String filename, int[] intArr1, int[] intArr2, String[] strArr1, String[] strArr2) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            // Write integer arrays
            writer.write(Arrays.toString(intArr1) + "\n");
            writer.write(Arrays.toString(intArr2) + "\n");

            // Write string arrays
            writer.write(Arrays.toString(strArr1) + "\n");
            writer.write(Arrays.toString(strArr2) + "\n");

            System.out.println("Arrays successfully written to file!");
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }

    public static void readArraysFromFile(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            // Read and parse integer arrays
            int[] intArr1 = parseIntArray(reader.readLine());
            int[] intArr2 = parseIntArray(reader.readLine());

            // Read and parse string arrays
            String[] strArr1 = parseStringArray(reader.readLine());
            String[] strArr2 = parseStringArray(reader.readLine());

            // Display the read arrays
            System.out.println("Read from file:");
            System.out.println("Integer Array 1: " + Arrays.toString(intArr1));
            System.out.println("Integer Array 2: " + Arrays.toString(intArr2));
            System.out.println("String Array 1: " + Arrays.toString(strArr1));
            System.out.println("String Array 2: " + Arrays.toString(strArr2));
        } catch (IOException e) {
            System.err.println("Error reading from file: " + e.getMessage());
        }
    }

    private static int[] parseIntArray(String line) {
        return Arrays.stream(line.replaceAll("[\\[\\]]", "").split(", "))
                .mapToInt(Integer::parseInt)
                .toArray();
    }

    private static String[] parseStringArray(String line) {
        return line.replaceAll("[\\[\\]]", "").split(", ");
    }
}
