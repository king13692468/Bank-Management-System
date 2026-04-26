Bank Management System

A Java-based console application that simulates core banking operations such as account creation, deposits, withdrawals, and balance inquiry. The system uses JDBC for database connectivity and MySQL for data storage.

 Features
Create new bank accounts
Deposit and withdraw money
Check account balance
Store and retrieve data from database
Simple and user-friendly console interaction

 Project Structure
com.bank
 ├── Main class
 ├── Accounts
 ├── Database connection
 ├── Utility classes

  Database Setup
 Create Database
CREATE DATABASE bank_system;
 Example Table (Account)
CREATE TABLE account (
    acc_number BIGINT PRIMARY KEY,
    name VARCHAR(100),
    email VARCHAR(100),
    balance DOUBLE,
    pin INT
);

How to Run
Clone the repository
Open in IntelliJ IDEA or VS Code
Configure MySQL connection in code
Run the main class

🧪 Usage
Run the application
Follow console instructions
Perform operations like:
Create account
Deposit money
Withdraw money
Check balance

🎯 Future Improvements
Add GUI (JavaFX / Web UI)
Add authentication system
Improve transaction security
Add transaction history
👨‍💻 Author
