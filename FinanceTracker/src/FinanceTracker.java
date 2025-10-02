import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.Set;

public class FinanceTracker
{
    private List<FinancialRecord> records = new ArrayList<>();
    private Map<String, OperationType> categories = new HashMap<>();

    // Default constructor
    public FinanceTracker()
    {
        this.records = new ArrayList<>();
        this.categories = new HashMap<>();
        initializeDefaultCategories();
    }
    // Initialize of standard categories
    private void initializeDefaultCategories()
    {
        categories.put("Salary", OperationType.INCOME);
        categories.put("Prize", OperationType.INCOME);
        categories.put("Investment", OperationType.INCOME);
        categories.put("Gift", OperationType.INCOME);
        categories.put("Refund", OperationType.INCOME);
        categories.put("Freelance", OperationType.INCOME);

        categories.put("Food", OperationType.EXPENSE);
        categories.put("Transport", OperationType.EXPENSE);
        categories.put("Housing", OperationType.EXPENSE);
        categories.put("Entertainments", OperationType.EXPENSE);
        categories.put("Clothes", OperationType.EXPENSE);
        categories.put("Health", OperationType.EXPENSE);
        categories.put("Education", OperationType.EXPENSE);
        categories.put("Communal services", OperationType.EXPENSE);
    }
    public void addRecord(FinancialRecord record)
    {
        if (record != null)
        {
            records.add(record);

            // Automatically add a category if it doesn't exist yet
            if (!categories.containsKey(record.getCategory()))
            {
                categories.put(record.getCategory(), record.getType());
            }
        }
    }
    // Creates and adds a new record with category validation
    public boolean addRecord(OperationType type, String category, double amount, LocalDate date)
    {
        OperationType expectedType = categories.get(category);
        if (expectedType != null && expectedType != type)
        {
            throw new IllegalArgumentException(
                    String.format("The category '%s' must be of type '%s'",
                    category, expectedType.getRussianName()));
        }
        // Generating ID
        int id = generateNextId();
        FinancialRecord record = new FinancialRecord(id, type, category, amount, date);
        addRecord(record);
        return true;
    }
    private int generateNextId()
    {
        return records.size() + 1;
    }

    // Returns the entire history of operations
    public List<FinancialRecord> getAllRecords()
    {
        return new ArrayList<>(records);
    }
    // Returns records sorted by date(new ones first)
    public List<FinancialRecord> getAllRecordsSortedByDate()
    {
        return records.stream()
                .sorted(Comparator.comparing(FinancialRecord::getDate).reversed())
                .collect(Collectors.toList());
    }
    // Calculates the total balance
    public double getTotalBalance()
    {
        return records.stream().mapToDouble(record ->
                record.getType() == OperationType.INCOME ? record.getAmount() : -record.getAmount()).sum();
    }
    // Returns operations for a specific period
    public List<FinancialRecord> getRecordsByDateRange(LocalDate start, LocalDate end)
    {
        validateDateRange(start, end);
        return records.stream()
                .filter(record -> isDateInRange(record.getDate(), start, end))
                .collect(Collectors.toList());
    }
    // Returns statistics for a specific period
    public Map<String, Double> getStatisticsByCategory(LocalDate start, LocalDate end)
    {
        validateDateRange(start, end);
        return records.stream()
                .filter(record -> isDateInRange(record.getDate(), start, end))
                .collect(Collectors.groupingBy(FinancialRecord::getCategory,
                        Collectors.summingDouble(FinancialRecord::getAmount)));
    }
    // Returns detailed statistics on the types of operations for the period
    public Map<OperationType, Double> getStatisticsByType(LocalDate start, LocalDate end)
    {
        validateDateRange(start, end);
        return records.stream()
                .filter(record -> isDateInRange(record.getDate(), start, end))
                .collect(Collectors.groupingBy(FinancialRecord::getType,
                        Collectors.summingDouble(FinancialRecord::getAmount)));
    }
    // Get entries by category
    public List<FinancialRecord> getRecordsByCategory(String category)
    {
        return records.stream()
                .filter(record -> record.getCategory().equalsIgnoreCase(category))
                .collect(Collectors.toList());
    }
    // Get records by operation type
    public List<FinancialRecord> getRecordsByType(OperationType type)
    {
        return records.stream()
                .filter(record -> record.getType() == type)
                .collect(Collectors.toList());
    }
    // Get all categories of a certain type
    public Set<String> getCategoriesByType(OperationType type)
    {
        return categories.entrySet().stream()
                .filter(entry -> entry.getValue().equals(type))
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());
    }
    // Add a new category
    public void addCategory(String category, OperationType type)
    {
        if (category == null || category.trim().isEmpty())
        {
            throw new IllegalArgumentException("The category name cannot be empty");
        }
        categories.put(category.trim(), type);
    }
    // Delete an entry by ID
    public boolean removeRecord(int id)
    {
        return records.removeIf(record -> record.getId() == id);
    }
    // Update the record
    public boolean updateRecord(int id, OperationType newType, String newCategory,
                                double newAmount, LocalDate newDate)
    {
        for (FinancialRecord record : records)
        {
            if (record.getId() == id)
            {
                record.setType(newType);
                record.setCategory(newCategory);
                record.setAmount(newAmount);
                record.setDate(newDate);
                return true;
            }
        }
        return false;
    }
    // Auxiliary methods
    private void validateDateRange(LocalDate start, LocalDate end)
    {
        if (start == null || end == null)
        {
            throw new IllegalArgumentException(
                    "Dates cannot be null");
        }
        if (start.isAfter(end))
        {
            throw new IllegalArgumentException(
                    "The start date cannot be after the end date");
        }
    }
    private boolean isDateInRange(LocalDate date, LocalDate start, LocalDate end)
    {
        return !date.isBefore(start) && !date.isAfter(end);
    }

    public int getRecordsCount()
    {
        return records.size();
    }
    public Map<String, OperationType> getCategories()
    {
        return new HashMap<>(categories);
    }
    public double getTotalIncome()
    {
        return records.stream()
                .filter(record -> record.getType() == OperationType.INCOME)
                .mapToDouble(FinancialRecord::getAmount)
                .sum();
    }
    public double getTotalExpenses()
    {
        return records.stream()
                .filter(record -> record.getType() == OperationType.EXPENSE)
                .mapToDouble(FinancialRecord::getAmount)
                .sum();
    }
    private FileHandler fileHandler = new FileHandler();

    public void saveToFile(String fileName)
    {
        fileHandler.saveToFile(records, fileName);
    }

    public void loadFromFile(String fileName)
    {
        List<FinancialRecord> loadedRecord = fileHandler.loadFromFile(fileName);
        for (FinancialRecord record : loadedRecord)
        {
            this.addRecord(record);
        }
    }

    public void loadFromFileReplace(String fileName)
    {
        List<FinancialRecord> loadedRecords = fileHandler.loadFromFile(fileName);
        this.records.clear();
        for (FinancialRecord record : loadedRecords)
        {
            this.addRecord(record);
        }
    }
}
