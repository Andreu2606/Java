

public class Main
{
    public static void main(String[] args)
    {
        FinanceTracker tracker = new FinanceTracker();
        ConsoleUI ui = new ConsoleUI(tracker);
        ui.start();
    }
}