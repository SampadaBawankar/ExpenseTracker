package expensetracker;

import java.time.LocalDate;


public class Transaction {
    private String type; // INCOME or EXPENSE
    private String category;
    private double amount;
    private LocalDate date;

    public Transaction(String type, String category, double amount, LocalDate date) {
        this.type = type;
        this.category = category;
        this.amount = amount;
        this.date = date;
    }

    public String getType() { return type; }
    public String getCategory() { return category; }
    public double getAmount() { return amount; }
    public LocalDate getDate() { return date; }

    @Override
    public String toString() {
        return "TYPE: " + type + "\nCATEGORY: " + category + "\nAMOUNT: " + amount + "\nDATE: " + date + "\n";
    }
}
