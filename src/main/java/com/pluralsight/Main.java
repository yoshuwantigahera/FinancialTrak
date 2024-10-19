package com.pluralsight;
import org.w3c.dom.ls.LSOutput;
import java.io.File;
import java.io.*;
import java.sql.SQLOutput;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.Scanner;

public class Main {
    static Scanner myscanner = new Scanner(System.in);

    public static void main(String[] args) throws IOException {
        displayMenu();
    }
    public static <Transaction> void displayMenu() throws IOException {
        String options;
        System.out.println("Hello there, what would you like to do today?"); //Later will add if time is am print more if it is pm print good afternoon
        System.out.println("D) Add Deposit \nP) Make payment (Debit) \nL) Ledger \nX) Exit");
        options = myscanner.nextLine();

        if (options.equalsIgnoreCase("D")) {
            System.out.println("Make a deposit");
            addDeposit();
            displayMenu();
        } else if (options.equalsIgnoreCase("P")) {
            System.out.println("Make a payment");
            makePayment();
            displayMenu();
        } else if (options.equalsIgnoreCase("L")) {
            System.out.println("Welcome to the ledger page");
            Ledger();
        } else if (options.equalsIgnoreCase("X")) {
            System.out.println("Exiting... Have a great day");
        }else {
            System.out.println("Error occured, try again.");
            displayMenu();
        }
    }
    public static void writeToCSV() {
        File file = new File("transactions.csv");

        try {
            if (file.createNewFile()) {
                System.out.println("file created: " + file.getName());
            }
            // Reading from the file
            try (Scanner scanner = new Scanner(file)) {
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    System.out.println(line); // Display the content
                }
            }
        } catch (IOException e) {
            System.out.println("An error occurred");
            e.printStackTrace();
        }
    }
    public static void makePayment() throws IOException {
        System.out.println("How much of your balance would you like to pay now: ");
        double payment = myscanner.nextDouble();
        myscanner.nextLine();
        System.out.println("Enter Name of company: ");
        String name = myscanner.nextLine();
        //-------------------------------------------------------------------------------------------
        String trans = "Payment";
        LocalDate todayDate = LocalDate.now();
        LocalTime todayTime = LocalTime.now().truncatedTo(ChronoUnit.SECONDS);
        //------------------------------------------------------------------------------------------
        // display input that was entered with negative
        String cardinformation = trans + "|" + todayDate + "|" + todayTime + "|" + name + "|" + (-payment);
        BufferedWriter cw = new BufferedWriter(new FileWriter("transactions.csv", true));
        cw.newLine();
        cw.write(cardinformation);
        cw.newLine();
        cw.close();
        System.out.println("Payment of $" + payment + "successful");
    }
    public static void addDeposit() throws IOException {
        System.out.println("Enter amount you would like to deposit: ");
        double deposit = myscanner.nextDouble();
        myscanner.nextLine();
        System.out.println("Description: ");
        String description = myscanner.nextLine();
        System.out.println("Vendor: ");
        String vendor = myscanner.nextLine();
        //-----------------------------------------------------------------------------------------------

        String type = "Deposit";
        LocalDate todayDate = LocalDate.now();
        LocalTime todayTime = LocalTime.now().truncatedTo(ChronoUnit.SECONDS); // help from osmig
        //-----------------------------------------------------------------------------------------------
        String transaction = String.format( "%s|%s|%s|%s|%f", todayDate, todayTime, description, vendor, deposit);

        //-----------------------------------------------------------------------------------------------
        BufferedWriter cw = new BufferedWriter(new FileWriter("transactions.csv", true)); //Going over file to apend string into the file
        cw.write(transaction);
        //Close it because the program is going to think i am still writing stuff basically waiting for it to be done. in while loop until closed.
        cw.close();
        System.out.println("Deposit of $" + deposit + " successful!");
    }
    public static void Ledger() throws IOException {
        String ledgerOptions;
        System.out.println("A) Display all entry \nD) Deposits \nP) Payments \nR) Reports");
        ledgerOptions = myscanner.nextLine();

        if (ledgerOptions.equalsIgnoreCase("A")) {
            displayAll();
        } else if (ledgerOptions.equalsIgnoreCase("D")) {
            allDeposits();
        } else if (ledgerOptions.equalsIgnoreCase("P")) {
            allPayments();
        } else if (ledgerOptions.equalsIgnoreCase("R")) {
            Reports();
        } else {
            System.out.println("Invalid option");
        }
    }
    private static void Reports() throws IOException {

        System.out.println("1) Month to Date \n2) Previous Month \n3) Year To Date \n4) Previous Year \n5) Search By Vender \n0) back \nH) Home");
        String reportOption = myscanner.nextLine();

        if (reportOption.equals("1")) {
            searchByMonth();
        } else if (reportOption.equals("2")) {
            searchPreviousMonth();
        } else if (reportOption.equals("3")) {
            searchByYear();
        } else if (reportOption.equals("4")) {
            searchPreviousYear();
        } else if (reportOption.equals("5")) {
            Vendor();
        } else if (reportOption.equals("0")) {
            Ledger();
        } else if (reportOption.equalsIgnoreCase("H")) {
            displayMenu();
        } else {
            System.out.println("Error occured");
        }
    }
    private static void allPayments() {
        File file = new File("transactions.csv");
        boolean isFirstLine = true;
        try (Scanner scanner = new Scanner(file)) {
            System.out.println("Deposits:");
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }
                String[] fields = line.split("\\|");
                // Split by the delimiter
                if (fields.length > 2) { // Check if there are enough fields
                    String amountInString = fields[fields.length - 1]; // Last field is the amount
                    BufferedWriter cw = new BufferedWriter(new FileWriter("transactions.csv", true));
                    double amount = Double.parseDouble(amountInString);
                    if (amount > 0) { // Check if the amount is a deposit
                        System.out.println(line);
                        cw.write(line + System.lineSeparator());
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("An error occurred while reading transactions.");
            e.printStackTrace();
        }
    }
    private static void displayAll() {
        File file = new File("transactions.csv");

        try (Scanner scanner = new Scanner(file)) {
            System.out.println("All Transactions: ");
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                System.out.println(line);
            }
        } catch (IOException e) {
            System.out.println("An error occurred while reading transactions.");
            e.printStackTrace();
        }
    }
    private static void searchByMonth() {
        System.out.print("Enter year (YYYY): ");
        int year = myscanner.nextInt();
        System.out.print("Enter month (1-12): ");
        int month = myscanner.nextInt();
        myscanner.nextLine(); // Clear the scanner buffer

        File file = new File("transactions.csv");
        //An empty list called filteredTrans is made/called to hold any transactions that match the vendor name
        List<String> filteredTransactions = new ArrayList<>();

        // Read the file and find matching transactions
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] fields = line.split("\\|");

                if (fields.length > 0) {
                    try {
                        LocalDate transactionDate = LocalDate.parse(fields[0]); //Being converted
                        if (transactionDate.getYear() == year && transactionDate.getMonthValue() == month) {
                            filteredTransactions.add(line);
                        }
                    } catch (DateTimeParseException e) {
                        System.out.println("Error parsing date: " + fields[0]);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("An error occurred while reading transactions.");
            e.printStackTrace();
            return;
        }
        // Display the filtered transactions
        if (filteredTransactions.isEmpty()) {
            System.out.println("No transactions found for the specified month and year.");
        } else {
            System.out.println("Transactions for " + month + "/" + year + ":");
            for (String transaction : filteredTransactions) {
                System.out.println(transaction);
            }
        }
    }
    private static void searchPreviousMonth() {
        LocalDate today = LocalDate.now();
        LocalDate firstDayOfCurrentMonth = today.withDayOfMonth(1);
        LocalDate lastMonth = firstDayOfCurrentMonth.minusMonths(1);

        int year = lastMonth.getYear();
        int month = lastMonth.getMonthValue();

        System.out.println("Searching for transactions for " + month + "/" + year + "...");

        File file = new File("transactions.csv");
        List<String> filteredTransactions = new ArrayList<>();

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] fields = line.split("\\|");

                if (fields.length > 0) {
                    try {
                        LocalDate transactionDate = LocalDate.parse(fields[0]);//chatgpt assisted
                        if (transactionDate.getYear() == year && transactionDate.getMonthValue() == month) {
                            filteredTransactions.add(line);
                        }
                    } catch (DateTimeParseException e) {
                        System.out.println("Error parsing date: " + fields[0]);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("An error occurred while reading transactions.");
            e.printStackTrace(); //chat gpt
            return;
        }
        // Display the results
        if (filteredTransactions.isEmpty()) {
            System.out.println("No transactions found for the previous month.");
        } else {
            System.out.println("Transactions for " + month + "/" + year + ":");
            for (String transaction : filteredTransactions) {
                System.out.println(transaction);
            }
        }
    }
    private static void allDeposits() {
        File file = new File("transactions.csv");
        boolean isFirstLine = true;
        try (Scanner scanner = new Scanner(file)) {
            System.out.println("Deposits:");
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                // Check if this is the first line we are looking at - skipping header
                if (isFirstLine) {
                    // If it is the first line set isFirstLine to false
                    // This way, we won't run this block of code again for the next lines
                    isFirstLine = false;
                    // Skip the rest of this loop and go to the next line
                    continue;
                }
                String[] fields = line.split("\\|");
                // Split by the delimiter
                if (fields.length > 4) { // Check if there are enough fields
                    String amountInString = fields[fields.length - 1]; // Last field is the amount - (bit of chatgpt assistance)
                    double amount = Double.parseDouble(amountInString); //taking the number and putting it in String format
                    if (amount > 0) { // Check if the amount is a deposit
                        System.out.println(line);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("An error occurred while reading transactions.");
            e.printStackTrace();
        }
    }
    private static void searchPreviousYear() {
        LocalDate today = LocalDate.now();
        LocalDate firstDayOfCurrentYear = today.withDayOfYear(1);
        LocalDate lastYear = firstDayOfCurrentYear.minusYears(1);

        int year = lastYear.getYear();

        System.out.println("Searching for transactions for " + year + "...");

        File file = new File("transactions.csv");
        List<String> filteredTransactions = new ArrayList<>();

        try (Scanner scanner = new Scanner(file)) {
            // Loop while there are more lines in the file
            while (scanner.hasNextLine()) {
                // Read the next line from the file
                String line = scanner.nextLine();
                // Split the line into an array of fields using the pipe '|' as the separator
                String[] fields = line.split("\\|");
                // Check if the fields array has at least one element making sure there's at least data inside
                if (fields.length > 0) {
                    // Try to parse the first element as a LocalDate
                    try {
                        LocalDate transactionDate = LocalDate.parse(fields[0]); // Assume the first field is the date
                        // Check if the year of the transaction date matches the specified year.
                        if (transactionDate.getYear() == year) {
                            // If it matches add the whole line (transaction) to the filteredTrans list
                            filteredTransactions.add(line);
                        }
                    } catch (DateTimeParseException e) {
                        // If there's an error parsing the date, print an error message with the reason/date.
                        System.out.println("Error parsing date: " + fields[0]);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("An error occurred while reading transactions.");
            e.printStackTrace();
            return;
        }
        // Display the results
        if (filteredTransactions.isEmpty()) {
            System.out.println("No transactions found for the previous Year.");
        } else {
            System.out.println("Transactions for " + year + ":");
            for (String transaction : filteredTransactions) {
                System.out.println(transaction);
            }
        }
    }
    private static void searchByYear() throws FileNotFoundException {
        System.out.print("Enter year (YYYY): ");
        int year = myscanner.nextInt();

        File file = new File("transactions.csv");
        List<String> filteredTransactions = new ArrayList<>();

        // Read the file and find matching transactions

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();

                // Display the filtered transactions
                if (filteredTransactions.isEmpty()) {
                    System.out.println("No transactions found for the specified month and year.");
                } else {
                    System.out.println("Transactions for " + year + ":");
                    for (String transaction : filteredTransactions) {
                        System.out.println(transaction);
                    }
                }
            }
        }
    }
    private static void Vendor() throws FileNotFoundException {
        System.out.print("Enter Vendor Name: ");
        String vendorName = myscanner.nextLine();
        myscanner.close();

        File file = new File("transactions.csv");
        List<String> filteredTransactions = new ArrayList<>();

        // Read the file and find matching transactions

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] field = line.split("\\|");

                //The fourth element (index 3) of fields, which should correspond to the vendor name in th file
                if (field.length > 3 && field[3].equalsIgnoreCase(vendorName)) {
                    //If both conditions are met the current line (transaction) is added to the list when called
                    filteredTransactions.add(line);
                }
            }

        } catch (IOException e) {
            System.out.println("An error occurred while reading transactions.");
            e.printStackTrace();
            return;
        }
        // Display the filtered transactions
        if (filteredTransactions.isEmpty()) {
            System.out.println("No transactions found for the specified Vendor.");
        } else {
            System.out.println("Vendor " + vendorName + ":");
            // loops through the filteredTrans list and prints each transaction
            for (String transaction : filteredTransactions) {
                System.out.println(transaction);
            }
        }
    }
}