package com.bank;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    private static final String url = "jdbc:mysql://localhost:3306/bank_management";
    private static final String username = "root";
    private static final String password = "13692468";

    public static void main(String[] args) {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }

        try (Connection con = DriverManager.getConnection(url, username, password);
             Scanner sc = new Scanner(System.in)) {

            User user = new User(con, sc);
            Accounts accounts = new Accounts(con, sc);
            AccountManager accountManager = new AccountManager(con, sc);

            boolean accMenu = true;

            while (accMenu) {
                System.out.println("\nEnter your choice");
                System.out.println("1. Register");
                System.out.println("2. Login");
                System.out.println("3. Exit");

                if (sc.hasNextInt()) {
                    int c = sc.nextInt();
                    sc.nextLine();

                    switch (c) {
                        case 1:
                            user.register();
                            break;

                        case 2:
                            String email = user.login();

                            if (email != null) {
                                System.out.println("Login successful");

                                if (!accounts.exist(email)) {
                                    System.out.println("No account found. Opening account...");
                                    long acc = accounts.openAccount(email);
                                    System.out.println("Account created. Number: " + acc);
                                }

                                long number = accounts.getNumber(email);

                                boolean bankMenu = true;
                                while (bankMenu) {
                                    System.out.println("\nEnter a choice");
                                    System.out.println("1. Credit");
                                    System.out.println("2. Debit");
                                    System.out.println("3. Transfer");
                                    System.out.println("4. Check Balance");
                                    System.out.println("5. Logout");

                                    if (sc.hasNextInt()) {
                                        int ch = sc.nextInt();
                                        sc.nextLine();

                                        switch (ch) {
                                            case 1:
                                                accountManager.credit(number);
                                                break;
                                            case 2:
                                                accountManager.debit(number);
                                                break;
                                            case 3:
                                                accountManager.transfer(number);
                                                break;
                                            case 4:
                                                accountManager.getBalance();
                                                break;
                                            case 5:
                                                bankMenu = false;
                                                break;
                                            default:
                                                System.out.println("Invalid choice");
                                        }
                                    } else {
                                        System.out.println("Invalid input");
                                        sc.nextLine();
                                    }
                                }

                            } else {
                                System.out.println("Invalid login");
                            }
                            break;

                        case 3:
                            accMenu = false;
                            break;

                        default:
                            System.out.println("Enter a valid choice");
                    }

                } else {
                    System.out.println("Invalid input");
                    sc.nextLine();
                }
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}