package banking;

import banking.entity.Account;
import banking.service.AccountService;
import banking.service.MathFunctions;

import java.util.Scanner;

public class Main {
    private static final AccountService accountService = new AccountService();

    public static void main(String[] args) {
        String fileName = args[1];
        process(fileName);
    }

    private static void process(String fileName) {
        accountService.connectDataSource(fileName);
        printMenu();
        Scanner scanner = new Scanner(System.in);
        int nextInt = scanner.nextInt();
        while (nextInt != 0) {
            if (nextInt == 1) {
                menuOne();
                System.out.println();
                printMenu();
            } else if (nextInt == 2) {
                int n = menuTwo(scanner);
                if (n == 0) {
                    break;
                }
                System.out.println();
                printMenu();
            }
            nextInt = scanner.nextInt();
        }
        System.out.println("\nBye!");
    }

    private static void printMenu() {
        System.out.println("" +
                "1. Create an account\n" +
                "2. Log into account\n" +
                "0. Exit");
    }

    private static Account menuOne() {
        Account account = createAccount();
        getAccountInfo(account);
        return account;
    }

    private static int menuTwo(Scanner scanner) {
        System.out.println("\nEnter your card number:");
        String number = scanner.next();
        System.out.println("Enter your PIN:");
        String pin = scanner.next();
        boolean success = accountService.hasSuchAccount(number, pin);
        int nextInt = -1;
        if (success) {
            Account account = accountService.getAccount(number, pin);
            System.out.println("\nYou have successfully logged in!\n");
            accountMenu();
            nextInt = scanner.nextInt();
            while (nextInt != 0) {
                if (nextInt == 1) {
                    subMenuOne(account);
                    accountMenu();
                } else if (nextInt == 2) {
                    account = subMenuTwo(scanner, account);
                    accountMenu();
                } else if (nextInt == 3) {
                    account = subMenuThree(scanner, account);
                    accountMenu();
                } else if (nextInt == 4) {
                    subMenuFour(account);
                    break;
                } else if (nextInt == 5) {
                    System.out.println("\nYou have successfully logged out!");
                    break;
                }
                nextInt = scanner.nextInt();
            }
        } else {
            System.out.println("\nWrong card number or PIN!");
        }
        return nextInt;
    }

    private static void accountMenu() {
        System.out.println("" +
                "1. Balance\n" +
                "2. Add income\n" +
                "3. Do transfer\n" +
                "4. Close account\n" +
                "5. Log out\n" +
                "0. Exit");
    }

    private static void subMenuOne(Account account) {
        System.out.println("\nBalance: " + account.getBalance() + "\n");
    }

    private static Account subMenuTwo(Scanner scanner, Account account) {
        System.out.println("\nEnter income:");
        int income = scanner.nextInt();
        account = accountService.addIncome(account.getId(), income);
        System.out.println("Income was added!\n");
        return account;
    }

    private static Account subMenuThree(Scanner scanner, Account account) {
        System.out.println("\nTransfer\n" +
                "Enter card number:");
        String cardNumber = scanner.next();
        boolean isValid = MathFunctions.passesLuhn(cardNumber);
        boolean hasCardNumber = accountService.containsNumber(cardNumber);
        boolean isSameAccount = accountService.isSameAccount(account, cardNumber);
        if (!isValid) {
            System.out.println("Probably you made a mistake in the card number. Please try again!\n");
        } else if (!hasCardNumber) {
            System.out.println("Such a card does not exist.\n");
        } else if (isSameAccount) {
            System.out.println("You can't transfer money to the same account!\n");
        } else {
            System.out.println("Enter how much money you want to transfer:");
            int amount = scanner.nextInt();
            boolean isEnough = accountService.hasEnoughMoney(account.getId(), amount);
            if (!isEnough) {
                System.out.println("Not enough money!\n");
            } else {
                var toAccount = accountService.getAccountFromNumber(cardNumber);
                account = accountService.transfer(account.getId(), toAccount.getId(), amount);
                System.out.println("Success!\n");
            }
        }
        return account;
    }

    private static Account subMenuFour(Account account) {
        account = accountService.deleteAccount(account.getId());
        System.out.println("\nThe account has been closed!");
        return account;
    }

    private static Account createAccount() {
        return accountService.createAccount();
    }

    private static void getAccountInfo(Account account) {
        if (account != null) {
            System.out.println("\nYour card has been created");
            System.out.println("Your card number:");
            System.out.println(account.getNumber());
            System.out.println("Your card PIN:");
            System.out.println(account.getPin());
        } else {
            System.out.println("You must first create your card!");
        }
    }
}