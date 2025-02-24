import java.io.*;
import java.util.*;

class FileManger {

    //consider adding a save option

    //implemnet "void() {} "
    //This methos is automatically called when started. This could be used to pull all information orginally

    //Need to add all of the schedule modifiers

    //Need to build a searching and organization system for all events

    //When this is working, come edit and try to condense code. All the "if" statements are not nessary for this file to run
    //Change the the program to look for something else besides "" Maybe something like &*& (something not normally used)

    //Possible consider imbedding the class within another class
    //Try the extend method but I don't think that will work

    private int[] intArr1;
    private int[] intArr2;
    private String[] strArr1;
    private String[] strArr2;

    public void writeArraysToFile() {

        System.out.println("Saving Information");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("appointments.txt"))) {
            int maxLength = Math.max(Math.max(intArr1.length, intArr2.length), Math.max(strArr1.length, strArr2.length));

            for (int i = 0; i < maxLength; i++) {
                String col1 = (i < intArr1.length) ? String.valueOf(intArr1[i]) : "";
                String col2 = (i < intArr2.length) ? String.valueOf(intArr2[i]) : "";
                String col3 = (i < strArr1.length) ? "\"" + strArr1[i] + "\"" : "";  // Wrap multi-word strings in quotes
                String col4 = (i < strArr2.length) ? "\"" + strArr2[i] + "\"" : "";  // Wrap multi-word strings in quotes

                writer.write(col1 + " " + col2 + " " + col3 + " " + col4 + "\n");
            }

            System.out.println("Scheduled Saved");
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }

    public void readArraysFromFile() {
        System.out.println("Pulling Schedule");
        try (BufferedReader reader = new BufferedReader(new FileReader("appointments.txt"))) {
            List<Integer> intList1 = new ArrayList<>();
            List<Integer> intList2 = new ArrayList<>();
            List<String> strList1 = new ArrayList<>();
            List<String> strList2 = new ArrayList<>();

            String line;
            while ((line = reader.readLine()) != null) {
                // Use regex to properly split numbers and quoted strings
                List<String> parts = new ArrayList<>();
                Scanner scanner = new Scanner(line);
                while (scanner.hasNext()) {
                    if (scanner.hasNextInt()) {
                        parts.add(scanner.next());
                    } else {
                        parts.add(scanner.findInLine("\"[^\"]*\"").replace("\"", ""));  // Extracts quoted strings
                    }
                }

                scanner.close();

                if (parts.size() > 0 && !parts.get(0).isEmpty()) intList1.add(Integer.parseInt(parts.get(0)));
                if (parts.size() > 1 && !parts.get(1).isEmpty()) intList2.add(Integer.parseInt(parts.get(1)));
                if (parts.size() > 2 && !parts.get(2).isEmpty()) strList1.add(parts.get(2));
                if (parts.size() > 3 && !parts.get(3).isEmpty()) strList2.add(parts.get(3));
            }

            // Convert lists back to arrays
            intArr1 = intList1.stream().mapToInt(i -> i).toArray();
            intArr2 = intList2.stream().mapToInt(i -> i).toArray();
            strArr1 = strList1.toArray(new String[0]);
            strArr2 = strList2.toArray(new String[0]);
            System.out.println("Schedule Found");
        } catch (IOException e) {
            System.err.println("Error reading from file: " + e.getMessage());
        }
    }

    //Redo the time display to include ':' between the hours and minutes
    //There may also be an error here where if there is a leading zero it mya try to display part of the next time
    public void display() {
        for (int i = 0; i < intArr1.length; i ++) {
            System.out.println("Appointment " + (i + 1) + ": ");
            System.out.println("  Date: " + (intArr1[i] / 1000000) + '/' + ((intArr1[i] / 10000) % 100) + '/' + (intArr1[i] % 10000));
            System.out.println("  Time: " + timeConversion("start", intArr2[i]) + '-' + timeConversion("end", intArr2[i]));
            System.out.println("  Name: " + strArr1[i]);
            System.out.println("  Description: " + strArr2[i]);
        }
    }




    //These are tested and they work
    //Consider returning string instead to preserve leading '0'
    public int timeConversion(int start, int end) {
        return (start * 10000) + end;
    }
    public static int timeConversion(String a, int time) {
        //If stirng equals start return start time but if it equals end then return end time
        if (a == "start") {
            return (time / 10000);
        } if (a == "end") {
            return (time % 10000);
        } else {
            //In case of failure return this to let user know of failure
            return -1;
        }
    }

}

class Menu {

    private FileManger file = new FileManger();

    public Menu() {
        file.readArraysFromFile();
    }

    public void end() {
        file.writeArraysToFile();
    }

    public void selector(int input) {

        if(input == 1) {

        } if(input == 2) {
            
        } if(input == 3) {
            
        } if(input == 4) {
            file.display();
        } if(input == 5) {
            System.out.println("Program ending");
        } else {

        }

    }

}


public class filehandler {
    public static void main(String[] args) {

        Menu menu = new Menu();

        Scanner sc = new Scanner(System.in);
        int input = 0;

        //Consider useing char instead of int
        while (input != 5) {
            System.out.println("To add an event press 1. To delete an even press 2, To rescheudle an event press 3, To display all events press 4, To save schedule and end program enter 5");
            input = sc.nextInt();
            menu.selector(input);
        }

        //Saving data
        menu.end();
        sc.close();
    

    }
    
}