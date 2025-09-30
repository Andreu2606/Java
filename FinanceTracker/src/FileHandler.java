import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FileHandler
{
    public void saveToFile(List<FinancialRecord> records, String fileName)
    {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName)))
        {
            // Writing down the title (optional)
            writer.write("id,type,category,amount,date");
            writer.newLine();

            // We record every record
            for (FinancialRecord record : records)
            {
                String formattedAmount = String.format("%.2f", record.getAmount()).replace(',', '.');
                String line = String.format("%d,%s,%s,%s,%s",
                        record.getId(),
                        record.getType(),
                        record.getCategory(),
                        formattedAmount,
                        record.getDate());
                writer.write(line);
                writer.newLine();
            }
            System.out.println("The data has been successfully saved to a file: " + fileName);
        }
        catch (IOException e)
        {
            System.out.println("Error when saving to a file: " + e.getMessage());
        }
    }

    // Loads a list of finance records from a CSV file
    public List<FinancialRecord> loadFromFile(String fileName)
    {
        List<FinancialRecord> records = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName)))
        {
            String line;
            boolean isFirstLine = true;

            while ((line = reader.readLine()) != null)
            {
                // Skipping the title
                if (isFirstLine)
                {
                    isFirstLine = false;
                    continue;
                }
                if (line.trim().isEmpty())
                {
                    continue;
                }
                try
                {
                    FinancialRecord record = parseLine(line);
                    if (record != null)
                    {
                        records.add(record);
                    }
                }
                catch (Exception e)
                {
                    System.out.println("Error parsing a string: " +  line + " - " + e.getMessage());
                }
            }
            System.out.println("The data was successfully uploaded from a file: " + fileName);
            System.out.println("Uploaded records: " + records.size());
        }
        catch (FileNotFoundException e)
        {
            System.err.println("File not found: " + fileName);
        }
        catch (IOException e)
        {
            System.err.println("Error reading the file: " + e.getMessage());
        }
        return records;
    }
    // Parses a CSV string and creates a FinanceRecord object
    private FinancialRecord parseLine(String line)
    {
        try
        {
            String[] parts = line.split(",");

            if (parts.length != 5)
            {
                throw new IllegalArgumentException("Incorrect number of fields in a row: " + parts.length);
            }

            int id = Integer.parseInt(parts[0].trim());
            OperationType type = OperationType.valueOf(parts[1].trim());
            String category = parts[2].trim();

            String amountStr = parts[3].trim().replace(",", ".");
            double amount = Double.parseDouble(amountStr);
            LocalDate date = LocalDate.parse(parts[4].trim());

            return new FinancialRecord(id, type, category, amount, date);
        }
        catch (Exception e)
        {
            System.out.println("Error parsing a string: " + line + " - " + e.getMessage());
            return null;
        }
    }
}