import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

// A user interface class for interacting with the console
public class ConsoleUI
{
    private FinanceTracker tracker;
    private Scanner scanner;
    private DateTimeFormatter dateFormatter;

    public ConsoleUI(FinanceTracker tracker)
    {
        this.tracker = tracker;
        this.scanner = new Scanner(System.in);
        this.dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    }
    // The main method that starts an endless loop with a menu
    public void start()
    {
        System.out.println("===Welcome to Finance Tracker!===");

        while(true)
        {
            showMenu();
            System.out.print("Select an option: ");

            try
            {
                int choice = Integer.parseInt(scanner.nextLine().trim());
                switch (choice)
                {
                    case 1:
                        addNewRecord();
                        break;
                    case 2:
                        showAllRecords();
                        break;
                    case 3:
                        showBalance();
                        break;
                    case 4:
                        showStatistics();
                        break;
                    case 5:
                        saveToFile();
                        break;
                    case 6:
                        loadFromFile();
                        break;
                    case 0:
                        System.out.println("Goodbye!");
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }
            catch (NumberFormatException e)
            {
                System.out.println("Error: Enter a number from 0 to 6.");
            }
            catch (Exception e)
            {
                System.out.println("Error: " + e.getMessage());
            }
            System.out.println();
        }
    }
    private void showMenu()
    {
        System.out.println("===Menu===");
        System.out.println("1. Add new record");
        System.out.println("2. Show all records");
        System.out.println("3. Show balance");
        System.out.println("4. Show statistics by category");
        System.out.println("5. Save data to a file");
        System.out.println("6. Download data from a file");
        System.out.println("0. Exit");
    }
    private void addNewRecord()
    {
        System.out.println("===Add new record===");
        try
        {
            // Operation type request
            OperationType type = getOperationTypeFromUser();

            System.out.print("Enter a category: ");
            String category = scanner.nextLine().trim();
            if (category.isEmpty())
            {
                System.out.println("Error: Description cannot be empty.");
                return;
            }

            double amount = getAmountFromUser();

            LocalDate date = getDateFromUser();

            FinancialRecord record = new FinancialRecord(0, type, category, amount, date);
            tracker.addRecord(record);
            System.out.println("Record added successfully.");
        }
        catch (Exception e)
        {
            System.out.println("Error when adding an entry: " + e.getMessage());
        }
    }
    private void showAllRecords()
    {
        System.out.println("===All financial records===");

        List<FinancialRecord> records = tracker.getAllRecords();

        if (records.isEmpty())
        {
            System.out.println("No records found.");
            return;
        }
        System.out.printf("%-4s %-10s %-20s %-10s %-12s%n",
                "ID", "Type", "Category", "Amount", "Date");
        System.out.println("========================================");

        for (FinancialRecord record : records)
        {
            String typeStr = record.getType() == OperationType.INCOME ? "Income" : "Expense";
            String amountStr = String.format("%.2f", record.getAmount());
            String dateStr = record.getDate().format(dateFormatter);

            System.out.printf("%-4d %-10s %-20s %-10s %-12s%n",
                    record.getId(), typeStr, record.getCategory(), amountStr, dateStr);
        }
        System.out.println("========================================");
        System.out.println("Total records: " + records.size());
    }
    private void showBalance()
    {
        System.out.println("===Balance===");
        double balance = tracker.getTotalBalance();
        double totalIncome = tracker.getTotalIncome();
        double totalExpense = tracker.getTotalExpenses();

        System.out.printf("Total Income: %.2f%n", totalIncome);
        System.out.printf("Total Expense: %.2f%n", totalExpense);
        System.out.printf("Current balance: %.2f%n", balance);

        if (balance > 0)
        {
            System.out.println("You balance is positive.");
        }
        else if (balance < 0)
        {
            System.out.println("You balance is negative.");
        }
        else
        {
            System.out.println("You balance is zero.");
        }
    }
    private void showStatistics()
    {
        System.out.println("===Statistics===");

        try
        {
            System.out.print("Enter the start date (yyyy-mm-dd): ");
            LocalDate startDate = getDateFromUser();

            System.out.print("Enter the end date (yyyy-mm-dd): ");
            LocalDate endDate = getDateFromUser();

            if (startDate.isAfter(endDate))
            {
                System.out.println("Error: The start cannot be later than the end date.");
                return;
            }

            var statistics = tracker.getStatisticsByCategory(startDate, endDate);

            if (statistics.isEmpty())
            {
                System.out.println("There is no data for the specified period.");
                return;
            }
            System.out.printf("Statistics for the period from %s to %s:%n",
                    startDate.format(dateFormatter), endDate.format(dateFormatter));
            System.out.println("========================================");

            for (var entry : statistics.entrySet())
            {
                System.out.printf("Category: %-20s Amount: %8.2f%n",
                        entry.getKey(), entry.getValue());
            }
        }
        catch (Exception e)
        {
            System.out.println("Error when getting statistics: " + e.getMessage());
        }
    }
    private void saveToFile()
    {
        System.out.println("Enter the file name to save: ");
        String fileName = scanner.nextLine().trim();

        if (fileName.isEmpty())
        {
            System.out.println("Error: File name cannot be empty.");
            return;
        }
        tracker.saveToFile(fileName);
    }
    private void loadFromFile()
    {
        System.out.println("Enter the file name to load: ");
        String fileName = scanner.nextLine().trim();

        if (fileName.isEmpty())
        {
            System.out.println("Error: File name cannot be empty.");
            return;
        }
        tracker.loadFromFile(fileName);
    }
    private OperationType getOperationTypeFromUser()
    {
        while (true)
        {
            System.out.print("Enter operation type (1 for income, 2 for expense): ");
            String input = scanner.nextLine().trim();

            switch (input)
            {
                case "1":
                    return OperationType.INCOME;
                case "2":
                    return OperationType.EXPENSE;
                default:
                    System.out.println("Invalid input. Please enter 1 or 2.");
            }
        }
    }
    private double getAmountFromUser()
    {
        while(true)
        {
            System.out.print("Enter the amount: ");
            try
            {
                double amount = Double.parseDouble(scanner.nextLine().trim());
                if (amount <= 0)
                {
                    System.out.println("Error: Amount must be greater than zero.");
                    continue;
                }
                return amount;
            }
            catch (NumberFormatException e)
            {
                System.out.println("Error: Invalid amount format.");
            }
        }
    }
    private LocalDate getDateFromUser()
    {
        while (true)
        {
            System.out.print("Enter the date (yyyy-mm-dd) or press enter to use current date: ");
            String input = scanner.nextLine().trim();
            if (input.isEmpty())
            {
                return LocalDate.now();
            }
            try
            {
                return LocalDate.parse(input, dateFormatter);
            }
            catch (DateTimeParseException e)
            {
                System.out.println("Error: Invalid date format. Please use yyyy-mm-dd.");
            }
        }
    }
}