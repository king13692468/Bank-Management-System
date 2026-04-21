package com.bank;

import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;



public class User {
    Connection con;
    Scanner sc;
    User(Connection con, Scanner sc){
        this.con=con;
        this.sc=sc;
    }
    public void register(){
        System.out.print("enter the email");
        String email=sc.next();
        if(!email.contains("@")||!email.contains(".")){
            System.out.println("invalid email");
            return;
        }
        if(exist(email)){
            System.out.println("already exist");
            return;
        }
        sc.nextLine();
        System.out.print("enter name:");
        String name=sc.nextLine();
        System.out.print("enter password:");
        String password=sc.next();

        String hash=hashPassword(password);
        String query="INSERT INTO user (name,email,password)values(?,?,?)";
        try {
            PreparedStatement pr = con.prepareStatement(query);
            pr.setString(1, name);
            pr.setString(2, email);
            pr.setString(3, hash);

            int upt= pr.executeUpdate();
            if(upt>0){
                System.out.println("registration successful");
            }
            else{
                System.out.println("registration failed");
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }

    }
    public String login() {
        System.out.print("enter email");
        String email = sc.next();

        if(!email.contains("@")||!email.contains(".")){
            System.out.println("invalid email");
            return null;
        }

        System.out.print("enter the password");
        String password = sc.next();
        String hash=hashPassword(password);
        String  query="SELECT email FROM user WHERE  email=? AND password=?";

        try{
            PreparedStatement pr=con.prepareStatement(query);
            pr.setString(1,email);
            pr.setString(2,hash);
            try(ResultSet rs=pr.executeQuery()){
                if(rs.next()){
                    return rs.getString("email");
                }else{
                    System.out.println("invalid password");
                }
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }
    public boolean exist(String email){
        String query="SELECT email FROM user WHERE email=?";
        try {
            PreparedStatement pr=con.prepareStatement(query);
            pr.setString(1,email);
            ResultSet rs=pr.executeQuery();
            return rs.next();
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return  false;

    }
    private String hashPassword(String pass){
        try{
            MessageDigest md=MessageDigest.getInstance("SHA-256");
            byte[]passwordByte= md.digest(pass.getBytes());
            StringBuilder password=new StringBuilder();
            for(byte b:passwordByte){
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) password.append('0');
                password.append(hex);
            }return password.toString();
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }
}

