package com.pluralsight;
import java.util.*;
import java.util.Scanner;


public class Main {

    public static void main(String[] args) {
        Scanner myscanner = new Scanner(System.in);
        String options;
        System.out.println("Good afternoon what would you like to do?"); //Later will add if time is am print more if it is pm print good afternoon
        System.out.println("D) Add Deposit \nP) Make payment (Debit) \nL) Ledger \nX) Exit");
        options = myscanner.nextLine();

        if (options.equalsIgnoreCase("D")){
            System.out.println();
        }else if (options.equalsIgnoreCase("p")){
            System.out.println();
        }else if (options.equalsIgnoreCase("L")){
            System.out.println();
        }else if (options.equalsIgnoreCase("X")){
            System.out.println();
        }

    }
}