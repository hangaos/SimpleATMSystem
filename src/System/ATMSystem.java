/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package System;

import data.ATMProcessor;
import java.io.IOException;
import java.util.Scanner;
import ui.Menu;

/**
 *
 * @author hangaos
 */
public class ATMSystem {

    public static void main(String[] args) {
        Menu menu = new Menu();

        menu.title("SAMPLE BANK");

        menu.addNewOption("1. Withdrawl ");
        menu.addNewOption("2. Deposit ");
        menu.addNewOption("3. Transfer ");
        menu.addNewOption("4. exit.");
        Scanner scanner = new Scanner(System.in);

        ATMProcessor atmprocessor = new ATMProcessor("/Users/hangaos/Documents/PRO192/ATMSystem/src/data/carddata.txt");
        int choice;

        do {
            menu.printMenu();
            choice = menu.getChoice();
            switch (choice) {
                case 1:
                    atmprocessor.processWithdrawal();
                    break;
                case 2:
                    atmprocessor.processDeposit();
                    break;
                case 3:
                    atmprocessor.processTransfer();
                    break;
                case 4:
                    System.out.println("Bye bye, see you next time!");
                    break;
            }

        } while (choice != 4);
    }
}
