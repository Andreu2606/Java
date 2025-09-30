import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

public class Main
{
    public static void main(String[] args)
    {
        FinanceTracker tracker = new FinanceTracker();
        FileHandler fileHandler = new FileHandler();

        List<FinancialRecord> records = new ArrayList<>();
        records.add(new FinancialRecord(1, OperationType.INCOME, "Salary", 2500.00, LocalDate.of(2024, 1, 15)));
        records.add(new FinancialRecord(2, OperationType.EXPENSE, "Food", 150.00, LocalDate.of(2024, 1, 10)));
        records.add(new FinancialRecord(3, OperationType.INCOME, "Freelance", 800.00, LocalDate.of(2024, 1, 20)));

        System.out.println("=== The save test ===");
        fileHandler.saveToFile(records, "finance_data.csv");

        System.out.println("=== The load test ===");
        List<FinancialRecord> loadedRecords = fileHandler.loadFromFile("finance_data.csv");

        System.out.println("=== Uploaded recordings ===");
        for (FinancialRecord record : loadedRecords)
        {
            System.out.println(record);
        }

        System.out.println("=== Integration test with FinanceTracker ===");
        tracker.loadFromFile("finance_data.csv");
        System.out.println("Tracker entries after uploading: " + tracker.getRecordsCount());
        System.out.println("Balance: " + tracker.getTotalBalance());
    }
}