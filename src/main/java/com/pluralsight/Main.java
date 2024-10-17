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
        System.out.println("Good afternoon what would you like to do?"); //Later will add if time is am print more if it is pm print good afternoon
        System.out.println("D) Add Deposit \nP) Make payment (Debit) \nL) Ledger \nX) Exit");
        options = myscanner.nextLine();

        if (options.equalsIgnoreCase("D")) {
            addDeposit();
        } else if (options.equalsIgnoreCase("P")) {
            makePayment();
        } else if (options.equalsIgnoreCase("L")) {
            Ledger();
        } else if (options.equalsIgnoreCase("X")) {
            System.out.println("Exiting... Have a great day");
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
        LocalDate todayDate = LocalDate.now();
        LocalTime todayTime = LocalTime.now().truncatedTo(ChronoUnit.SECONDS);
        //-------------------------------------------------------------------------------------------

        String cardinformation = todayDate + "|" + todayTime + "|" + name + "|" + (-payment); //display input that was entered with negative
        BufferedWriter cw = new BufferedWriter(new FileWriter("transactions.csv", true));
        cw.newLine();
        cw.write(cardinformation);
        cw.newLine();
        cw.close();
    }

    public static void addDeposit() throws IOException {
        System.out.println("Enter amount you would like to deposit: ");
        double deposit = myscanner.nextDouble();
        myscanner.nextLine();
        System.out.println("Description: ");
        String description = myscanner.nextLine();
        System.out.println("Name: ");
        String name = myscanner.nextLine();
        //-----------------------------------------------------------------------------------------------

        LocalDate todayDate = LocalDate.now();
        LocalTime todayTime = LocalTime.now().truncatedTo(ChronoUnit.SECONDS);
        //-----------------------------------------------------------------------------------------------
        String transcation = todayDate + "|" + todayTime + "|" + description + "|" + name + "|" + deposit;
        //-----------------------------------------------------------------------------------------------
        BufferedWriter cw = new BufferedWriter(new FileWriter("transactions.csv", true)); //Going over file to apend string into the file
        cw.write(transcation);
        //Close it because the program is going to think i am still writing stuff basically waiting for it to be done. in while loop until closed.
        cw.close();

    }

    public static void Ledger() throws IOException {
        String ledgerOptions;
        System.out.println("A) All \nD) Deposit \nP) Payments \nR) Reports");
        ledgerOptions = myscanner.nextLine();

        if (ledgerOptions.equalsIgnoreCase("A")) {

        } else if (ledgerOptions.equalsIgnoreCase("D")) {
            allDeposits();
//            sortTransactionsByDate();
        } else if (ledgerOptions.equalsIgnoreCase("P")) {
            makePayment();
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

        } else if (reportOption.equals("3")) {

        } else if (reportOption.equals("4")) {

        } else if (reportOption.equals("5")){

        } else if(reportOption.equals("0")){

        } else if (reportOption.equalsIgnoreCase("H")){

        } else{
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
        List<String> filteredTransactions = new ArrayList<>();

        // Read the file and find matching transactions
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] fields = line.split("\\|");

                // Check if the transaction date matches the specified month and year
                if (fields.length > 0) {
                    try {
                        LocalDate transactionDate = LocalDate.parse(fields[0]);
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

    private static void allDeposits() {
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
                if (fields.length > 4) { // Check if there are enough fields
                    String amountInString = fields[fields.length - 1]; // Last field is the amount
                    double amount = Double.parseDouble(amountInString);
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
}