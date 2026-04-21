package com.bank;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class AccountManager {
    Connection con;
    Scanner sc;
    AccountManager(Connection con, Scanner sc){
        this.con=con;
        this.sc=sc;
    }
    public void credit(long accNumber) {
        System.out.print("Enter amount: ");
        int amount = sc.nextInt();

        System.out.print("Enter PIN: ");
        String pin = hashPassword(sc.next());

        String query = "UPDATE account SET balance = balance + ? WHERE acc_number=? AND pin=?";

        try (PreparedStatement pr = con.prepareStatement(query)) {
            pr.setInt(1, amount);
            pr.setLong(2, accNumber);
            pr.setString(3, pin);

            int updated = pr.executeUpdate();

            if (updated > 0) {
                System.out.println(amount + " credited successfully");
            } else {
                System.out.println("Invalid account or PIN");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public void debit(long accNumber) {
        System.out.print("Enter amount: ");
        int amount = sc.nextInt();

        System.out.print("Enter PIN: ");
        String pin = hashPassword(sc.next());

        String check = "SELECT balance FROM account WHERE acc_number=? AND pin=?";
        String debit = "UPDATE account SET balance = balance - ? WHERE acc_number=?";

        try {
            con.setAutoCommit(false);

            PreparedStatement ps = con.prepareStatement(check);
            ps.setLong(1, accNumber);
            ps.setString(2, pin);

            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                System.out.println("Invalid account or PIN");

                System.out.print("Forgot PIN? (yes/no): ");
                if (sc.next().equalsIgnoreCase("yes")) {
                    resetPin(accNumber);
                }
                return;
            }

            double balance = rs.getDouble("balance");

            if (balance < amount) {
                System.out.println("Insufficient balance");
                return;
            }

            PreparedStatement db = con.prepareStatement(debit);
            db.setInt(1, amount);
            db.setLong(2, accNumber);

            int updated = db.executeUpdate();

            if (updated > 0) {
                con.commit();
                System.out.println(amount + " debited successfully");
            } else {
                con.rollback();
                System.out.println("Transaction failed");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }finally {
            try { con.setAutoCommit(true);
            }
            catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
    public void transfer(long accNumber) {
        System.out.print("Enter amount: ");
        int amount = sc.nextInt();

        System.out.print("Enter PIN: ");
        String pin = hashPassword(sc.next());

        System.out.print("Enter receiver account: ");
        long receiver = sc.nextLong();

        String checkSender = "SELECT balance FROM account WHERE acc_number=? AND pin=?";
        String checkReceiver = "SELECT * FROM account WHERE acc_number=?";
        String debit = "UPDATE account SET balance = balance - ? WHERE acc_number=?";
        String credit = "UPDATE account SET balance = balance + ? WHERE acc_number=?";

        try {
            con.setAutoCommit(false);

            // check sender
            PreparedStatement ps = con.prepareStatement(checkSender);
            ps.setLong(1, accNumber);
            ps.setString(2, pin);
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                System.out.println("Invalid account or PIN");
                return;
            }

            double balance = rs.getDouble("balance");
            if (balance < amount) {
                System.out.println("Insufficient balance");
                return;
            }

            // check receiver
            PreparedStatement pr = con.prepareStatement(checkReceiver);
            pr.setLong(1, receiver);
            if (!pr.executeQuery().next()) {
                System.out.println("Receiver not found");
                return;
            }

            // transfer
            PreparedStatement db = con.prepareStatement(debit);
            db.setInt(1, amount);
            db.setLong(2, accNumber);

            PreparedStatement cr = con.prepareStatement(credit);
            cr.setInt(1, amount);
            cr.setLong(2, receiver);

            int d = db.executeUpdate();
            int c = cr.executeUpdate();

            if (d > 0 && c > 0) {
                con.commit();
                System.out.println("Transfer successful");
            } else {
                con.rollback();
                System.out.println("Transfer failed");
            }

        } catch (SQLException e) {
            try { con.rollback();
            } catch (Exception ex) {
                System.out.println(e.getMessage());
            }
            System.out.println(e.getMessage());
        } finally {
            try { con.setAutoCommit(true);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
    public void getBalance(){
        System.out.print("enter account number");
        long accNumber=sc.nextLong();
        System.out.print("enter pin");
        String pin=sc.next();
        String query="SELECT balance FROM account WHERE acc_number=? AND pin=?";
        try{
            con.setAutoCommit(false);
            PreparedStatement pr=con.prepareStatement(query);
            pr.setLong(1,accNumber);
            pr.setString(2,pin);
            ResultSet rs=pr.executeQuery();
            if(rs.next()){
                double balance=rs.getInt("balance");
                System.out.println("balance:"+balance);
            }
            else{
                System.out.println("account not found");
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }
    public void resetPin(long accNumber) {

        System.out.print("Enter registered email: ");
        String email = sc.next();

        String verify = "SELECT * FROM account WHERE acc_number=? AND email=?";

        try (PreparedStatement ps = con.prepareStatement(verify)) {
            ps.setLong(1, accNumber);
            ps.setString(2, email);

            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                System.out.println("Email does not match this account");
                return;
            }

            System.out.print("Enter new PIN: ");
            String newPin = sc.next();

            System.out.print("Confirm new PIN: ");
            String confirmPin = sc.next();

            if (!newPin.equals(confirmPin)) {
                System.out.println("PINs do not match");
                return;
            }

            String update = "UPDATE account SET pin=? WHERE acc_number=?";
            try (PreparedStatement ps2 = con.prepareStatement(update)) {
                ps2.setString(1, newPin);
                ps2.setLong(2, accNumber);

                int updated = ps2.executeUpdate();

                if (updated > 0) {
                    System.out.println("PIN reset successful");
                } else {
                    System.out.println("Failed to reset PIN");
                }
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    private String hashPassword(String input) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = md.digest(input.getBytes());

            StringBuilder hex = new StringBuilder();
            for (byte b : hashBytes) {
                String h = Integer.toHexString(0xff & b);
                if (h.length() == 1) hex.append('0');
                hex.append(h);
            }
            return hex.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

