import java.io.*;
import java.util.*;

class Appointment {
    String date, timeRange, clientName, description;

    public Appointment(String date, String timeRange, String clientName, String description) {
        this.date = date;
        this.timeRange = timeRange;
        this.clientName = clientName;
        this.description = description;
    }

    @Override
    public String toString() {
        return date + ", " + timeRange + ", " + clientName + ", " + description;
    }
}

class AppointmentManager {
    private static final String FILE_NAME = "appointments.txt";
    private ArrayList<Appointment> appointments = new ArrayList<>();

    public AppointmentManager() {
        loadAppointments();
    }

    private void loadAppointments() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return;
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(", ");
                if (parts.length == 4) {
                    appointments.add(new Appointment(parts[0], parts[1], parts[2], parts[3]));
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading appointments.");
        }
    }

    public boolean isOverlapping(String date, String timeRange) {
        String[] newTimes = timeRange.split("-");
        int newStart = convertTimeToMinutes(newTimes[0]);
        int newEnd = convertTimeToMinutes(newTimes[1]);
        
        for (Appointment appt : appointments) {
            if (appt.date.equals(date)) {
                String[] existingTimes = appt.timeRange.split("-");
                int existingStart = convertTimeToMinutes(existingTimes[0]);
                int existingEnd = convertTimeToMinutes(existingTimes[1]);
                
                if (newStart < existingEnd && newEnd > existingStart) {
                    return true;
                }
            }
        }
        return false;
    }

    private int convertTimeToMinutes(String time) {
        String[] parts = time.split(":");
        return Integer.parseInt(parts[0]) * 60 + Integer.parseInt(parts[1]);
    }
    
    public boolean isValidTimeRange(String timeRange) {
        String[] times = timeRange.split("-");
        if (times.length != 2) return false;
        return isValidTime(times[0]) && isValidTime(times[1]) && convertTimeToMinutes(times[0]) < convertTimeToMinutes(times[1]);
    }

    private boolean isValidTime(String time) {
        if (!time.matches("\\d{2}:\\d{2}")) return false;
        String[] parts = time.split(":");
        try {
            int hours = Integer.parseInt(parts[0]);
            int minutes = Integer.parseInt(parts[1]);
            return hours >= 0 && hours < 24 && minutes >= 0 && minutes < 60;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public void addAppointment(Scanner scanner) {
        System.out.print("Enter date (YYYY-MM-DD): ");
        String date = scanner.nextLine().trim();
        System.out.print("Enter time range (HH:MM-HH:MM): ");
        String timeRange = scanner.nextLine().trim();
        
        if (!isValidTimeRange(timeRange)) {
            System.out.println("Invalid time range. Please enter a valid time.");
            return;
        }
        
        System.out.print("Enter client name: ");
        String clientName = scanner.nextLine().trim();
        System.out.print("Enter description: ");
        String description = scanner.nextLine().trim();
        
        if (isOverlapping(date, timeRange)) {
            System.out.println("Time slot already booked or overlapping. Try another time.");
            return;
        }
        
        appointments.add(new Appointment(date, timeRange, clientName, description));
        System.out.println("Appointment added successfully.");
    }

    public void viewAppointments() {
        if (appointments.isEmpty()) {
            System.out.println("No appointments found.");
            return;
        }
        for (Appointment appt : appointments) {
            System.out.println();
            System.out.println(appt);
        }
    }

    public void searchAppointment(Scanner scanner) {
        System.out.print("Enter client name or date to search: ");
        String query = scanner.nextLine();
        for (Appointment appt : appointments) {
            if (appt.clientName.equalsIgnoreCase(query) || appt.date.equals(query)) {
                System.out.println(appt);
            }
        }
    }

    public void rescheduleAppointment(Scanner scanner) {
        System.out.print("Enter client name for rescheduling: ");
        String clientName = scanner.nextLine();
        System.out.print("Enter current date: ");
        String oldDate = scanner.nextLine();
        System.out.print("Enter current time range: ");
        String oldTimeRange = scanner.nextLine();
        
        for (Appointment appt : appointments) {
            if (appt.clientName.equalsIgnoreCase(clientName) && appt.date.equals(oldDate) && appt.timeRange.equals(oldTimeRange)) {
                System.out.print("Enter new date: ");
                String newDate = scanner.nextLine();
                System.out.print("Enter new time range: ");
                String newTimeRange = scanner.nextLine();
                
                if (!isValidTimeRange(newTimeRange)) {
                    System.out.println("Invalid time range. Please enter a valid time.");
                    return;
                }
                
                if (isOverlapping(newDate, newTimeRange)) {
                    System.out.println("New time slot already booked. Try another time.");
                    return;
                }
                
                appt.date = newDate;
                appt.timeRange = newTimeRange;
                System.out.println("Appointment rescheduled successfully.");
                return;
            }
        }
        System.out.println("Appointment not found.");
    }

    public void cancelAppointment(Scanner scanner) {
        System.out.print("Enter client name to cancel: ");
        String clientName = scanner.nextLine();
        System.out.print("Enter date: ");
        String date = scanner.nextLine();
        
        Iterator<Appointment> iterator = appointments.iterator();
        while (iterator.hasNext()) {
            Appointment appt = iterator.next();
            if (appt.clientName.equalsIgnoreCase(clientName) && appt.date.equals(date)) {
                iterator.remove();
                System.out.println("Appointment canceled successfully.");
                return;
            }
        }
        System.out.println("Appointment not found.");
    }

    public void saveAppointments() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Appointment appt : appointments) {
                writer.write(appt.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving appointments.");
        }
    }
}

public class AppointmentScheduler {
    public static void main(String[] args) {
        AppointmentManager manager = new AppointmentManager();
        Scanner scanner = new Scanner(System.in);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> manager.saveAppointments()));
        
        while (true) {
            System.out.println("\nAppointment Scheduling System");
            System.out.println("1. Add Appointment");
            System.out.println("2. View Appointments");
            System.out.println("3. Search Appointment");
            System.out.println("4. Reschedule Appointment");
            System.out.println("5. Cancel Appointment");
            System.out.println("6. Exit");
            System.out.print("Choose an option: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine();
            
            switch (choice) {
                case 1:
                    manager.addAppointment(scanner);
                    break;
                case 2:
                    manager.viewAppointments();
                    break;
                case 3:
                    manager.searchAppointment(scanner);
                    break;
                case 4:
                    manager.rescheduleAppointment(scanner);
                    break;
                case 5:
                    manager.cancelAppointment(scanner);
                    break;
                case 6:
                    return;
                default:
                    System.out.println("Invalid choice, please try again.");
            }
        }
    }
}