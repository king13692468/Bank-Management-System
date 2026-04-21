package com.bank;

import java.security.MessageDigest;
import java.sql.*;
import java.util.Scanner;

public class Accounts {
    Connection con;
    Scanner sc;

    Accounts(Connection con, Scanner sc){
        this.con = con;
        this.sc = sc;
    }

    public long openAccount(String email){
        long acc_number = accNumber();
        String query = "INSERT INTO account(acc_number,name,email,balance,pin) VALUES(?,?,?,?,?)";

        if(!exist(email)){
            sc.nextLine();

            System.out.print("Enter name: ");
            String name = sc.nextLine();

            System.out.print("Enter balance: ");
            double balance = sc.nextDouble();

            System.out.print("Enter PIN: ");
            String pin = sc.next();

            String hash = pin(pin);

            try{
                PreparedStatement pr = con.prepareStatement(query);
                pr.setLong(1, acc_number);
                pr.setString(2, name);
                pr.setString(3, email);
                pr.setDouble(4, balance);
                pr.setString(5, hash);

                int upt = pr.executeUpdate();

                if(upt > 0){
                    return acc_number;
                } else {
                    System.out.println("Account creation failed");
                }

            } catch(SQLException e){
                System.out.println(e.getMessage());
            }
        }

        throw new RuntimeException("Already exist");
    }

    public long getNumber(String email){
        String query = "SELECT acc_number FROM account WHERE email=?";
        try{
            PreparedStatement pr = con.prepareStatement(query);
            pr.setString(1, email);
            ResultSet rs = pr.executeQuery();

            if(rs.next()){
                return rs.getLong("acc_number");
            }
        } catch(SQLException e){
            System.out.println(e.getMessage());
        }

        throw new RuntimeException("Account not registered");
    }

    public boolean exist(String email){
        String query = "SELECT * FROM account WHERE email=?";
        try{
            PreparedStatement pr = con.prepareStatement(query);
            pr.setString(1, email);
            ResultSet rs = pr.executeQuery();
            return rs.next();
        } catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return false;
    }

    public long accNumber(){
        String query = "SELECT acc_number FROM account ORDER BY acc_number DESC LIMIT 1";
        try{
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query);

            if(rs.next()){
                long acc_number = rs.getLong("acc_number");
                return acc_number + 1;
            } else {
                return 10000100;
            }
        } catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return 10000100;
    }

    private String pin(String pin) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(pin.getBytes());

            StringBuilder password = new StringBuilder();
            for(byte b : hash){
                String hex = Integer.toHexString(0xff & b);
                if(hex.length() == 1) password.append('0');
                password.append(hex);
            }
            return password.toString();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}