
public enum OperationType
{
    INCOME("Доход"),
    EXPENSE("Расход");

    private final String russianName;

    OperationType(String russianName)
    {
        this.russianName = russianName;
    }
    public String getRussianName()
    {
        return russianName;
    }
}