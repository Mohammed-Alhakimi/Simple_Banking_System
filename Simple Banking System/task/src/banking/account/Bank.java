package banking.account;

import banking.card.Validator;
import banking.carddao.CardDAO;

import java.io.Serializable;
import java.util.*;

/**
 * The type Bank.
 */
public class Bank implements Serializable {

    private final List<Account> accounts = new ArrayList<>();

    public void showActions() {
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;
        String choice;
        while (!exit) {
            System.out.println("1. Create an account\n" +
                    "2. Log into account\n" +
                    "0. Exit");
            choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    System.out.println();
                    addNewAccount();
                    break;
                case "2":
                    System.out.println();
                    Account account = logIntoAccount(accounts);
                    if (account != null) {
                        System.out.println();
                        System.out.println("You have successfully logged in!\n");
                        showAccountInformation(account);
                    } else {
                        System.out.println("Wrong card number or PIN!\n");
                    }
                    break;
                case "0":
                    System.out.println("\nBye!");
                    exit = true;
                    break;
            }
        }
    }

    /**
     * @param account Takes and account only if log in is successful and displays information about that account
     */
    private void showAccountInformation(Account account) {
        Scanner scanner = new Scanner(System.in);
        boolean logOut = false;
        while (!logOut) {
            System.out.println("1. Balance\n" +
                    "2. Add income\n" +
                    "3. Do transfer\n" +
                    "4. Close account\n" +
                    "5. Log out\n" +
                    "0. Exit");
            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    System.out.println("\nBalance: " + CardDAO.getBalance(account) + "\n");
                    break;
                case "2":
                    System.out.println("\nEnter income:");
                    String incomeToAdd = scanner.nextLine();
                    CardDAO.addIncome(account, incomeToAdd);
                    break;
                case "3":
                    System.out.println("\nTransfer");
                    System.out.println("Enter card number:");
                    String cardNumber = scanner.nextLine();
                    transferAmounts(account, cardNumber);
                    break;
                case "4":
                    CardDAO.closeAccount(account);
                    System.out.println("\nThe account has been closed!\n");
                    break;
                case "5":
                    logOut = true;
                    System.out.println("\nYou have successfully logged out!\n");
                    break;
                case "0":
                    System.out.println("\nBye!");
                    System.exit(0);
                    break;
            }
        }
    }

    private void transferAmounts(Account from, String cardNumber) {
        boolean valid = Validator.validateCardNumber(cardNumber);
        if (valid) {
            Account accountToTransferTo = CardDAO.checkAccountToTransfer(cardNumber);
            if (accountToTransferTo == null) {
                System.out.println("Such a card does not exist.");
            } else {
                if (accountToTransferTo.getCurrentCard().getCardNumber().equals(from.getCurrentCard().getCardNumber())) {
                    System.out.println("You can't transfer money to the same account!\n");
                } else {
                    Scanner scanner = new Scanner(System.in);
                    System.out.println("Enter how much money you want to transfer:");
                    int amountToTransfer = Integer.parseInt(scanner.nextLine());
                    CardDAO.transferAmounts(from, accountToTransferTo, amountToTransfer);
                }
            }
        } else {
            System.out.println("Probably you made a mistake in the card number. Please try again!\n");
        }
    }

    /**
     * @param accounts Takes a list of account and then searches for an account in it via
     *                 the card number and tries to use the PIN to log in
     * @return If the account is found in the Database then returns an account to be Used in the
     * showAccountInformation() Method or else returns null
     */
    private Account logIntoAccount(List<Account> accounts) {
        Scanner scanner = new Scanner(System.in);
        String cardNumberToSearchFor;
        String pinToValidate;
        System.out.println("Enter your card number:");
        cardNumberToSearchFor = scanner.nextLine();
        System.out.println("Enter your PIN");
        pinToValidate = scanner.nextLine();
        return CardDAO.logIn(cardNumberToSearchFor, pinToValidate);
    }

    private void addNewAccount() {
        Account account = new Account();
        accounts.add(account);
        CardDAO.addCard(account);
        System.out.println("Your card has been created\n" +
                "Your card number:\n" +
                account.getCurrentCard().getCardNumber() + "\n" +
                "Your card PIN:\n" +
                account.getCurrentCard().getPin() + "\n");
    }
}
