import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Main
{
    public static void main(String[] args)
    {
        FinanceTracker tracker = new FinanceTracker();

        System.out.println("=== Test Finance Tracker ===");

        testAddRecords(tracker);
        testStatistics(tracker);
        testFiltering(tracker);
        testDateRange(tracker);
        testCategories(tracker);
        testUpdateAndDelete(tracker);
        testErrorCases(tracker);
    }

    public static void testAddRecords(FinanceTracker tracker)
    {
        System.out.println("1. Test Adding records...");

        // Adding records via the FinanceTracker constructor
        FinancialRecord salary = new FinancialRecord(1, OperationType.INCOME,
                "Salary", 2500.00, LocalDate.of(2024, 1, 15));
        FinancialRecord freelance = new FinancialRecord(2, OperationType.INCOME,
                "Freelance", 800.00, LocalDate.of(2024, 1, 20));
        FinancialRecord food = new FinancialRecord(3, OperationType.EXPENSE,
                "Food", 150.00, LocalDate.of(2024, 1, 10));
        FinancialRecord rent = new FinancialRecord(4, OperationType.EXPENSE,
                "Housing", 800.00, LocalDate.of(2024, 1, 5));

        tracker.addRecord(salary);
        tracker.addRecord(freelance);
        tracker.addRecord(food);
        tracker.addRecord(rent);

        // Adding records via the addRecord method
        tracker.addRecord(OperationType.INCOME, "Investment", 300.00, LocalDate.of(2024, 1, 25));
        tracker.addRecord(OperationType.EXPENSE, "Transport", 50.00, LocalDate.of(2024, 1, 12));

        System.out.println("Records added: " + tracker.getRecordsCount());
        System.out.println("Records addition test passed.\n");
    }

    public static void testStatistics(FinanceTracker tracker)
    {
        System.out.println("2. Test Statistics:");
        double totalIncome = tracker.getTotalIncome();
        double totalExpenses = tracker.getTotalExpenses();
        double balance = tracker.getTotalBalance();

        System.out.printf("Total income: %.2f%n", totalIncome);
        System.out.printf("Total expenses: %.2f%n", totalExpenses);
        System.out.printf("Balance: %.2f%n", balance);

        // Checking the correctness of calculations
        double expectedIncome = 2500.00 + 800.00 + 300.00;
        double expectedExpenses = 150.00 + 800.00 + 50.00;
        double expectedBalance = expectedIncome - expectedExpenses;

        System.out.printf("Expected income: %.2f%n", expectedIncome);
        System.out.printf("Expected expenses: %.2f%n", expectedExpenses);
        System.out.printf("Expected balance: %.2f%n", expectedBalance);

        if (Math.abs(totalIncome - expectedIncome) < 0.01 &&
                Math.abs(totalExpenses - expectedExpenses) < 0.01 &&
                Math.abs(balance - expectedBalance) < 0.01)
        {
            System.out.println("Statistics test passed.\n");
        }
        else
        {
            System.out.println("Statistics test failed.\n");
        }
    }

    public static void testFiltering(FinanceTracker tracker)
    {
        System.out.println("3. Test Filtering:");

        // Filtering by type operation
        List<FinancialRecord> incomes = tracker.getRecordsByType(OperationType.INCOME);
        List<FinancialRecord> expenses = tracker.getRecordsByType(OperationType.EXPENSE);

        System.out.println("Income (" + incomes.size() + "):");
        for (FinancialRecord income : incomes)
        {
            System.out.println(" - " + income.getCategory() + ": " + income.getAmount());
        }
        System.out.println("Expenses (" + expenses.size() + "):");
        for (FinancialRecord expense : expenses)
        {
            System.out.println("  - " + expense.getCategory() + ": " + expense.getAmount());
        }

        // Filtering by category
        List<FinancialRecord> foodRecords = tracker.getRecordsByCategory("Food");
        System.out.println("Category entries 'Food': " + foodRecords.size() );

        if (incomes.size() == 3 && expenses.size() == 3 && foodRecords.size() == 1)
        {
            System.out.println("Filtering test passed.\n");
        }
        else
        {
            System.out.println("Filtering test failed.\n");
        }
    }

    public static void testDateRange(FinanceTracker tracker)
    {
        System.out.println("4. Test Date Range:");

        LocalDate start = LocalDate.of(2024, 1, 1);
        LocalDate end = LocalDate.of(2024, 1, 15);

        List<FinancialRecord> januaryRecords = tracker.getRecordsByDateRange(start, end);

        System.out.println("Entries from January 1 to 15: " + januaryRecords.size());

        // Statistics by category for the period
        Map<String, Double> categoryStats = tracker.getStatisticsByCategory(start, end);
        System.out.println("Statistics by category:");
        for (Map.Entry<String, Double> entry : categoryStats.entrySet())
        {
            System.out.printf(" - %s: %.2f%n", entry.getKey(), entry.getValue());
        }

        // Statistics on the types of operations for the period
        Map<OperationType, Double> typeStats = tracker.getStatisticsByType(start, end);
        System.out.println("Statistics on the types of operations:");
        for (Map.Entry<OperationType, Double> entry : typeStats.entrySet())
        {
            System.out.printf(" - %s: %.2f%n", entry.getKey(), entry.getValue());
        }
        if (januaryRecords.size() > 0)
        {
            System.out.println("Date range test passed.\n");
        }
        else
        {
            System.out.println("Date range test failed.\n");
        }
    }

    public static void testCategories(FinanceTracker tracker)
    {
        System.out.println("5. Test Categories:");

        // Getting all categories
        Map<String, OperationType> allCategories = tracker.getCategories();
        System.out.println("All categories: (" + allCategories.size() + ")");
        for (Map.Entry<String, OperationType> entry : allCategories.entrySet())
        {
            System.out.println(" - " + entry.getKey() + " (" + entry.getValue() + ")");
        }
        // Categories by type
        Set<String> incomeCategories = tracker.getCategoriesByType(OperationType.INCOME);
        Set<String> expenseCategories = tracker.getCategoriesByType(OperationType.EXPENSE);

        System.out.println("Categories of income: " + incomeCategories.size());
        System.out.println("Categories of expenses: " + expenseCategories.size());

        // Adding a new category
        tracker.addCategory("Travel", OperationType.EXPENSE);
        tracker.addCategory("Dividends", OperationType.INCOME);

        incomeCategories = tracker.getCategoriesByType(OperationType.INCOME);
        expenseCategories = tracker.getCategoriesByType(OperationType.EXPENSE);

        if (incomeCategories.contains("Dividends") && expenseCategories.contains("Travel"))
        {
            System.out.println("Categories test passed.\n");
        }
        else
        {
            System.out.println("Categories test failed.\n");
        }
    }

    public static void testUpdateAndDelete(FinanceTracker tracker)
    {
        System.out.println("6. Test Update and Delete:");

        int initialCount = tracker.getRecordsCount();
        System.out.println("Records before deletion: " + initialCount);

        // Deleting records
        boolean removed = tracker.removeRecord(3);
        System.out.println("Record with ID 3 removed: " + removed);
        System.out.println("Records after deletion: " + tracker.getRecordsCount());

        // Updating a record
        boolean updated = tracker.updateRecord(2, OperationType.INCOME, "Freelance", 1000.00, LocalDate.of(2024, 1, 22));
        System.out.println("Record updated: " + updated);

        // Checking the updated record
        List<FinancialRecord> freelanceRecords = tracker.getRecordsByCategory("Freelance");
        if (!freelanceRecords.isEmpty() && freelanceRecords.get(0).getAmount() == 1000.00)
        {
            System.out.println("Update and delete test passed.\n");
        }
        else
        {
            System.out.println("Update and delete test failed.\n");
        }
    }
    public static void testErrorCases(FinanceTracker tracker)
    {
        System.out.println("7. Error Scenarios test:");

        try
        {
            // An attempt to add an entry with an incorrect category type
            tracker.addRecord(OperationType.EXPENSE, "Salary", 100.00, LocalDate.now());
            System.out.println("A category validation error was expected.");
        }
        catch (IllegalArgumentException e)
        {
            System.out.println("The validation error was handled correctly: " + e.getMessage());
        }
        try
        {
            // An attempt to get records with an incorrect date range
            tracker.getRecordsByDateRange(LocalDate.of(2024, 1, 20), LocalDate.of(2024, 1, 10));
            System.out.println("Date range error was expected.");
        }
        catch (IllegalArgumentException e)
        {
            System.out.println("The date range error was handled correctly: " + e.getMessage());
        }
        System.out.println("Error test completed.\n");
    }
}