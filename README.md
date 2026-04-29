# <p align="center">🏦 <strong>BANK MANAGEMENT SYSTEM</strong> 🏦</p>

<p align="center">
  <strong><big>A Java-based console application that simulates core banking operations such as account creation, deposits, withdrawals, and balance inquiry. The system uses JDBC for database connectivity and MySQL for data storage.</big></strong>
</p>

<br>

## <strong>✨ FEATURES</strong>
- ✅ Create new bank accounts
- ✅ Deposit and withdraw money
- ✅ Check account balance
- ✅ Store and retrieve data from database
- ✅ Simple and user-friendly console interaction

<br>

## <strong>📁 PROJECT STRUCTURE</strong>
com.bank
├── Main class
├── Accounts
├── User
└── AccountManager

<br>

## <strong>🗄️ DATABASE SETUP</strong>

### <strong>Create Database</strong>

CREATE DATABASE bank_system;
<strong>Example Table (Account)</strong>
sql
CREATE TABLE account (
    acc_number BIGINT PRIMARY KEY,
    name VARCHAR(100),
    email VARCHAR(100),
    balance DOUBLE,
    pin INT
);

<strong>🚀 HOW TO RUN</strong>
Clone the repository

Open in IntelliJ IDEA or VS Code

Configure MySQL connection in code

Run the main class


<strong>💻 USAGE</strong>
Run the application and follow console instructions to perform operations like:

🔹 Create account

🔹 Deposit money

🔹 Withdraw money

🔹 Check balance


<strong>🔮 FUTURE IMPROVEMENTS</strong>
🎨 Add GUI (JavaFX / Web UI)

🔐 Add authentication system

🛡️ Improve transaction security

📜 Add transaction history


<strong>👨‍💻 AUTHOR</strong>
<p><strong>Shadab Mobin</strong></p> ```





