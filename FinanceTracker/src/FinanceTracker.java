import java.util.ArrayList;
import java.util.List;

public class FinanceTracker
{
    private List<FinancialRecord> records = new ArrayList<>();

    public void addRecord(FinancialRecord record)
    {
        if (record != null)
        {
            records.add(record);
            System.out.println("Record added successfully");
        }
        else
        {
            System.out.println("ERROR. Record not added");
        }
    }

    public List<FinancialRecord> getAllRecords()
    {
        return new ArrayList<>(records);
    }

    public double getTotalBalance()
    {
        double totalBalance = 0.0;
        for (FinancialRecord record : records)
        {
            if (record.getType() == OperationType.INCOME)
            {
                totalBalance += record.getAmount();
            }
            else if (record.getType() == OperationType.EXPENSE)
            {
                totalBalance -= record.getAmount();
            }
        }
        return totalBalance;
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

    public int getRecordsCount()
    {
        return records.size();
    }

    public List<FinancialRecord> getRecordsByType(OperationType type)
    {
        List<FinancialRecord> result = new ArrayList<>();
        for (FinancialRecord record : records)
        {
            if (record.getType() == type)
            {
                result.add(record);
            }
        }
        return result;
    }
}
