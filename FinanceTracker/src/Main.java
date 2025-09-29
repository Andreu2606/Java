import java.time.LocalDate;
import java.util.List;

public class Main
{
    public static void main(String[] args)
    {
        FinanceTracker tracker = new FinanceTracker();

        FinancialRecord salary = new FinancialRecord(1, OperationType.INCOME, "Salary", 2500.00, LocalDate.now());
        FinancialRecord bonus = new FinancialRecord(2, OperationType.INCOME, "Bonus", 500.00, LocalDate.now());
        FinancialRecord rent = new FinancialRecord(3, OperationType.EXPENSE, "Rent", 800.00, LocalDate.now());
        FinancialRecord groceries = new FinancialRecord(4, OperationType.EXPENSE, "Groceries", 150.00, LocalDate.now());
        FinancialRecord utilities = new FinancialRecord(5, OperationType.EXPENSE, "Utilities", 120.00, LocalDate.now());

        System.out.println("=== ADDING RECORDS ===");
        tracker.addRecord(salary);
        tracker.addRecord(bonus);
        tracker.addRecord(rent);
        tracker.addRecord(groceries);
        tracker.addRecord(utilities);

        System.out.println("\n=== ALL ENTRIES ===");
        List<FinancialRecord> allRecords = tracker.getAllRecords();
        for (FinancialRecord record : allRecords)
        {
            System.out.println(record);
        }

        System.out.println("\n=== FINANCIAL STATISTICS ===");
        System.out.printf("Total number of records: %d%n", tracker.getRecordsCount());
        System.out.printf("Total income: %.2f%n", tracker.getTotalIncome());
        System.out.printf("General expenses: %.2f%n", tracker.getTotalExpenses());
        System.out.printf("Current balance: %.2f%n", tracker.getTotalBalance());

        System.out.println("\n=== RECORDS BY TYPE ===");

        System.out.println("INCOME");
        List<FinancialRecord> incomes = tracker.getRecordsByType(OperationType.INCOME);
        for (FinancialRecord income : incomes)
        {
            System.out.println(" - " + income.getCategory() + ": " + income.getAmount());
        }

        System.out.println("EXPENSE:");
        List<FinancialRecord> expenses = tracker.getRecordsByType(OperationType.EXPENSE);
        for (FinancialRecord expense : expenses)
        {
            System.out.println(" -" + expense.getCategory() + ": " + expense.getAmount());
        }
    }
}