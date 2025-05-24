package expensetracker;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ExpenseTracker {
    private static List<Transaction> transactions = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            System.out.println("1. Add Transaction");
            System.out.println("2. View Monthly Summary");
            System.out.println("3. Load From File");
            System.out.println("4. Save To File");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1 -> addTransaction();
                case 2 -> showSummary();
                case 3 -> loadFromFile();
                case 4 -> saveToFile();
                case 5 -> {
                    System.out.println("Goodbye!");
                    return;
                }
                default -> System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private static void addTransaction() {
        System.out.print("Enter type (INCOME/EXPENSE): ");
        String type = scanner.nextLine().toUpperCase();
        System.out.print("Enter category: ");
        String category = scanner.nextLine();
        System.out.print("Enter amount: ");
        double amount = Double.parseDouble(scanner.nextLine());
        System.out.print("Enter date (yyyy-mm-dd): ");
        LocalDate date = LocalDate.parse(scanner.nextLine());

        transactions.add(new Transaction(type, category, amount, date));
        System.out.println("Transaction added.\n");
    }

    private static void showSummary() {
        Map<String, Double> income = new HashMap<>();
        Map<String, Double> expense = new HashMap<>();

        for (Transaction t : transactions) {
            String month = t.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM"));
            if (t.getType().equals("INCOME")) {
                income.put(month, income.getOrDefault(month, 0.0) + t.getAmount());
            } else if (t.getType().equals("EXPENSE")) {
                expense.put(month, expense.getOrDefault(month, 0.0) + t.getAmount());
            }
        }

        Set<String> months = new TreeSet<>(income.keySet());
        months.addAll(expense.keySet());

        for (String month : months) {
            double in = income.getOrDefault(month, 0.0);
            double ex = expense.getOrDefault(month, 0.0);
            System.out.println(month + " -> Income: " + in + ", Expense: " + ex);
        }
    }

    private static void loadFromFile() {
        System.out.print("Enter filename to load (e.g., data.txt): ");
        String filename = scanner.nextLine();
        File file = new File(filename);
        if (!file.exists()) {
            System.out.println("File not found: " + filename);
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            String type = "", category = "", dateStr = "";
            double amount = 0;

            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                if (line.startsWith("TYPE:")) {
                    type = line.split(":")[1].trim();
                } else if (line.startsWith("CATEGORY:")) {
                    category = line.split(":")[1].trim();
                } else if (line.startsWith("AMOUNT:")) {
                    amount = Double.parseDouble(line.split(":")[1].trim());
                } else if (line.startsWith("DATE:")) {
                    dateStr = line.split(":")[1].trim();
                    LocalDate date = LocalDate.parse(dateStr);
                    transactions.add(new Transaction(type, category, amount, date));
                }
            }
            System.out.println("Data loaded successfully from text file.\n");
        } catch (Exception e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }

    private static void saveToFile() {
        System.out.print("Enter filename to save to (e.g., output.txt): ");
        String filename = scanner.nextLine();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
            for (Transaction t : transactions) {
                bw.write(t.toString());
                bw.newLine();
            }
            System.out.println("Data saved successfully.\n");
        } catch (IOException e) {
            System.out.println("Error saving file: " + e.getMessage());
        }
    }
}
