import java.time.LocalDate;

public class FinancialRecord
{
    private int id;
    private OperationType type;
    private String category;
    private Double amount;
    private LocalDate date;

    public FinancialRecord(int id, OperationType type, String category, Double amount, LocalDate date)
    {
        this.id = id;
        this.type = type;
        this.category = category;
        this.amount = amount;
        this.date = date;
    }

    public int getId()
    {
        return id;
    }

    public OperationType getType()
    {
        return type;
    }

    public String getCategory()
    {
        return category;
    }

    public Double getAmount()
    {
        return amount;
    }

    public LocalDate getDate()
    {
        return date;
    }

    public void setCategory(String category)
    {
        this.category = category;
    }

    public void setAmount(Double amount)
    {
        this.amount = amount;
    }

    public void setDate(LocalDate date)
    {
        this.date = date;
    }

    @Override
    public String toString()
    {
        return String.format("ID: %d | %s | Category: %-10s | Amount: %10.2f | Date: %s",
                id, type == OperationType.INCOME ? "Income" : "Expense",
                category, amount, date.toString());
    }
}
