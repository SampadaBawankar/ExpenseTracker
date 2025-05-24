package expensetracker;

import java.io.*;
import java.time.LocalDate;
import java.util.*;

public class ExpenseTracker {
    private static List<Transaction> transactions = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n1. Add Transaction");
            System.out.println("2. View Monthly Summary");
            System.out.println("3. Load From File");
            System.out.println("4. Save To File");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    addTransaction();
                    break;
                case 2:
                    viewMonthlySummary();
                    break;
                case 3:
                    System.out.print("Enter filename to load (e.g., src/expensetracker/data.txt): ");
                    String loadFile = scanner.nextLine();
                    loadFromFile(loadFile);
                    break;
                case 4:
                    System.out.print("Enter filename to save (e.g., src/expensetracker/data.txt): ");
                    String saveFile = scanner.nextLine();
                    saveToFile(saveFile);
                    break;
                case 5:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private static void addTransaction() {
        System.out.print("Enter type (INCOME/EXPENSE): ");
        String type = scanner.nextLine().toUpperCase();

        if (!type.equals("INCOME") && !type.equals("EXPENSE")) {
            System.out.println("Invalid type.");
            return;
        }

        System.out.print("Enter category (e.g., Salary/Business or Food/Rent/Travel): ");
        String category = scanner.nextLine();

        System.out.print("Enter amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine(); // consume newline

        System.out.print("Enter date (YYYY-MM-DD): ");
        String dateStr = scanner.nextLine();
        LocalDate date = LocalDate.parse(dateStr);

        transactions.add(new Transaction(type, category, amount, date));
        System.out.println("Transaction added successfully.");
    }

    private static void viewMonthlySummary() {
        System.out.print("Enter month (1-12): ");
        int month = scanner.nextInt();
        System.out.print("Enter year (e.g., 2025): ");
        int year = scanner.nextInt();
        scanner.nextLine(); // consume newline

        double totalIncome = 0;
        double totalExpense = 0;

        System.out.println("\n--- Monthly Summary for " + month + "/" + year + " ---");

        for (Transaction t : transactions) {
            if (t.getDate().getMonthValue() == month && t.getDate().getYear() == year) {
                System.out.println(t);
                if (t.getType().equals("INCOME")) {
                    totalIncome += t.getAmount();
                } else {
                    totalExpense += t.getAmount();
                }
            }
        }

        System.out.println("Total Income: " + totalIncome);
        System.out.println("Total Expense: " + totalExpense);
        System.out.println("Net Balance: " + (totalIncome - totalExpense));
    }

    private static void saveToFile(String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (Transaction t : transactions) {
                writer.write(t.toString());
                writer.write("\n"); // add a blank line between transactions
            }
            System.out.println("Data saved successfully to " + filename);
        } catch (IOException e) {
            System.out.println("Error saving file: " + e.getMessage());
        }
    }

    private static void loadFromFile(String filename) {
        File file = new File(filename);
        if (!file.exists()) {
            System.out.println("File not found: " + filename);
            return;
        }

        transactions.clear(); // prevent duplicate entries

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            String type = "", category = "", date = "";
            double amount = 0;

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("TYPE:")) {
                    type = line.substring(5).trim();
                } else if (line.startsWith("CATEGORY:")) {
                    category = line.substring(9).trim();
                } else if (line.startsWith("AMOUNT:")) {
                    amount = Double.parseDouble(line.substring(7).trim());
                } else if (line.startsWith("DATE:")) {
                    date = line.substring(5).trim();
                    transactions.add(new Transaction(type, category, amount, LocalDate.parse(date)));
                }
            }

            System.out.println("Data loaded successfully from text file.\n");
            System.out.println("Loaded Transactions:");
            for (Transaction t : transactions) {
                System.out.println(t);
            }

        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }
}
