package com.pluralsight;
import org.w3c.dom.ls.LSOutput;
import java.io.File;

import java.io.*;
import java.sql.SQLOutput;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.Scanner;

//review filereaderandwriter to throw out or display person's info
public class Main {

    //dang code
//    String date;
//    String time;
//    String description;
    String vendor;
//    String amount;
    static Scanner myscanner = new Scanner(System.in);

    public static void main(String[] args) throws IOException {

        displayMenu();
    }


     public static void displayMenu() throws IOException {

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


    public static void makePayment () throws IOException {
        System.out.println("How much of your balance would you like to pay now: ");
        double payment = myscanner.nextDouble();
        myscanner.nextLine();
        System.out.println("Enter card Number: ");
        double cardInfo = myscanner.nextDouble();
        System.out.println("Enter expiration date: ");
       myscanner.nextLine();
        String expirationDate = myscanner.nextLine();
        System.out.println("Enter CVV: ");
        int cvv = myscanner.nextInt();
        myscanner.nextLine();
        //-------------------------------------------------------------------------------------------
        String cardinformation = cardInfo + "|" + payment + "|" + expirationDate;
        BufferedWriter cw = new BufferedWriter(new FileWriter("transactions.csv", true));
        cw.newLine();
        cw.write(cardinformation);
        cw.newLine();
        cw.close();
    }

    public static void addDeposit () throws IOException {
        System.out.println("Enter amount you would like to deposit:");
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
        BufferedWriter cw = new BufferedWriter(new FileWriter ("transactions.csv", true)); //Going over file to apend string into the file
        cw.write(transcation);
        //Close it because the program is going to think i am still writing stuff basically waiting for it to be done. in while loop until closed.
        cw.close();

    }

    public static void Ledger () throws IOException {
        String ledgerOptions;
        System.out.println("A) All \nD) Deposit \nP) Payments \nR) Reports");
        ledgerOptions = myscanner.nextLine();

        if (ledgerOptions.equalsIgnoreCase("A")) {
            displayAll();
        } else if (ledgerOptions.equalsIgnoreCase("D")) {
            allDeposits();
        } else if (ledgerOptions.equalsIgnoreCase("P")) {
            System.out.println();
        } else if (ledgerOptions.equalsIgnoreCase("R")) {
            System.out.println();
        } else {
            System.out.println("Invalid option");
        }
    }

    private static void displayAll (){
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

    private static void allDeposits() {
        File file = new File("transactions.csv");

        try (Scanner scanner = new Scanner(file)) {
            System.out.println("Deposits:");
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] fields = line.split("\\|"); // Split by the delimiter
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