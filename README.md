# <p align="center">🏦 <strong>BANK MANAGEMENT SYSTEM</strong> 🏦</p>

<p align="center">
  <strong><big>A console-based banking application built with Java and JDBC. Features include user registration, account management, secure transactions, and loan services with automatic EMI processing.</big></strong>
</p>

<br>

## 📋 TABLE OF CONTENTS

| # | Section |
|---|---------|
| 1 | [🚀 Features](#-features) |
| 2 | [🧱 Project Structure](#-project-structure) |
| 3 | [⚙️ Tech Stack](#️-tech-stack) |
| 4 | [🧠 Architecture](#-architecture) |
| 5 | [🗄️ Database Schema](#️-database-schema) |
| 6 | [🔧 Modules](#-modules) |
| 7 | [💳 Banking Operations](#-banking-operations) |
| 8 | [🏦 Loan Services](#-loan-services) |
| 9 | [🔐 Security Features](#-security-features) |
| 10 | [▶️ How to Run](#️-how-to-run) |
| 11 | [🧪 Testing](#-testing) |
| 12 | [🎯 Future Improvements](#-future-improvements) |
| 13 | [👨‍💻 Author](#-author) |

<br>

## 🚀 FEATURES

| # | Feature | Description |
|---|---------|-------------|
| ✅ | **User Registration** | Create new user account with email & password |
| ✅ | **User Login** | Secure login with SHA-256 password hashing |
| ✅ | **Open Account** | Create bank account after login |
| ✅ | **Credit Money** | Deposit money into account |
| ✅ | **Debit Money** | Withdraw money with PIN verification |
| ✅ | **Transfer Money** | Transfer funds between accounts |
| ✅ | **Check Balance** | View current account balance |
| ✅ | **PIN Reset** | Reset forgotten PIN via email verification |
| ✅ | **Loan Application** | Apply for loans with custom amounts |
| ✅ | **Loan Approval** | Auto-approval for loans under 5,00,000 |
| ✅ | **EMI Calculation** | Automatic EMI calculation based on amount, rate & tenure |
| ✅ | **Auto EMI Processing** | Automatic monthly EMI deduction |
| ✅ | **View Loans** | Check all active loans with details |
| ✅ | **Pay EMI** | Manual EMI payment option |

<br>

## 🧱 PROJECT STRUCTURE

```
BankManagement/
├── Main.java                 # Main application entry point
├── User.java                 # User registration & login
├── Accounts.java             # Account creation & management
├── AccountManager.java       # Credit, Debit, Transfer, Balance
├── Loan.java                 # Loan application, EMI, payments
└── bank_management.sql       # Database schema
```


## ⚙️ TECH STACK

| Technology | Purpose |
|------------|---------|
| **Java** | Core Programming Language |
| **JDBC** | Database Connectivity |
| **MySQL** | Database |
| **SHA-256** | Password Encryption |
| **Maven** | Build Tool (optional) |

<br>

## 🧠 ARCHITECTURE

```
┌─────────────────────────────────────────────────────┐
│                    CLIENT (Console)                  │
└─────────────────────────────────────────────────────┘
                           ↓
┌─────────────────────────────────────────────────────┐
│                      MAIN.java                       │
│            (Menu Navigation & Flow Control)          │
└─────────────────────────────────────────────────────┘
        ↓              ↓              ↓              ↓
┌──────────────┐ ┌──────────────┐ ┌──────────────┐ ┌──────────────┐
│    User.java │ │ Accounts.java│ │AccountManager│ │   Loan.java  │
│ Registration│ │   Account    │ │  Credit/Debit│ │  Apply Loan  │
│    Login    │ │  Creation    │ │  Transfer    │ │ EMI Payment  │
└──────────────┘ └──────────────┘ └──────────────┘ └──────────────┘
                           ↓
┌─────────────────────────────────────────────────────┐
│                   MySQL Database                     │
│  (user, account, loans, loan_payments tables)       │
└─────────────────────────────────────────────────────┘
```

<br>

## 🗄️ DATABASE SCHEMA

### User Table
```sql
CREATE TABLE user (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL
);
```

### Account Table
```sql
CREATE TABLE account (
    acc_number BIGINT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    balance DOUBLE DEFAULT 0.0,
    pin VARCHAR(255) NOT NULL,
    FOREIGN KEY (email) REFERENCES user(email)
);
```

### Loans Table
```sql
CREATE TABLE loans (
    loan_id INT PRIMARY KEY AUTO_INCREMENT,
    account_number BIGINT NOT NULL,
    amount DOUBLE NOT NULL,
    interest_rate DOUBLE NOT NULL,
    tenure_months INT NOT NULL,
    emi DOUBLE NOT NULL,
    total_amount DOUBLE NOT NULL,
    status VARCHAR(20) DEFAULT 'PENDING',
    FOREIGN KEY (account_number) REFERENCES account(acc_number)
);
```

### Loan Payments Table
```sql
CREATE TABLE loan_payments (
    payment_id INT PRIMARY KEY AUTO_INCREMENT,
    loan_id INT NOT NULL,
    amount DOUBLE NOT NULL,
    payment_date DATE NOT NULL,
    remaining_balance DOUBLE NOT NULL,
    FOREIGN KEY (loan_id) REFERENCES loans(loan_id)
);
```

<br>

## 🔧 MODULES

| Module | File | Responsibility |
|--------|------|----------------|
| **Main Controller** | `Main.java` | Menu navigation, flow control, user session management |
| **User Management** | `User.java` | Registration, login, email validation, password hashing |
| **Account Management** | `Accounts.java` | Open account, generate account number, check account existence |
| **Banking Operations** | `AccountManager.java` | Credit, debit, transfer, balance inquiry, PIN reset |
| **Loan Services** | `Loan.java` | Apply loan, EMI calculation, view loans, pay EMI, auto EMI |

<br>

## 💳 BANKING OPERATIONS

### 1. Credit Money
- Enter amount and PIN
- Balance increases instantly
- Transaction confirmation

### 2. Debit Money
- Check sufficient balance
- Verify PIN
- Deduct amount with transaction rollback on failure

### 3. Transfer Money
- Verify sender PIN and balance
- Check receiver account exists
- Atomic transaction (both debit & credit)

### 4. Check Balance
- Enter account number and PIN
- Display current balance

### 5. Reset PIN
- Verify registered email
- Enter and confirm new PIN
- Update encrypted PIN in database

<br>

## 🏦 LOAN SERVICES

### EMI Formula Used
```
EMI = P × r × (1 + r)^n / ((1 + r)^n - 1)

Where:
P = Principal loan amount
r = Monthly interest rate (annual rate / 12 / 100)
n = Tenure in months
```

### Loan Features
| Feature | Details |
|---------|---------|
| **Auto Approval** | Loans under ₹5,00,000 auto-approved |
| **Manual Approval** | Larger loans marked as PENDING |
| **Instant Credit** | Loan amount credited to account on approval |
| **Auto EMI** | Automatic monthly deduction from balance |
| **Payment Tracking** | Complete history of all EMI payments |

### EMI Process Flow
```
Apply Loan → Calculate EMI → Approve/Credit → Monthly Auto EMI → Track Remaining Balance → Complete Loan
```

<br>

## 🔐 SECURITY FEATURES

| Feature | Implementation |
|---------|----------------|
| **Password Encryption** | SHA-256 hashing (no plain text storage) |
| **PIN Encryption** | SHA-256 hashed before database storage |
| **Email Validation** | Basic format check for @ and . |
| **Duplicate Prevention** | Email/account duplicate checks |
| **Transaction Atomicity** | JDBC transactions with commit/rollback |
| **Session Management** | User stays logged in until logout |

### Password Hashing Example
```java
private String hashPassword(String input) {
    MessageDigest md = MessageDigest.getInstance("SHA-256");
    byte[] hashBytes = md.digest(input.getBytes());
    StringBuilder hex = new StringBuilder();
    for (byte b : hashBytes) {
        String h = Integer.toHexString(0xff & b);
        if (h.length() == 1) hex.append('0');
        hex.append(h);
    }
    return hex.toString();
}
```

<br>

## ▶️ HOW TO RUN

### Prerequisites
- Java 8 or higher
- MySQL 8.0 or higher
- MySQL Connector JAR

### Installation Steps

1. **Clone the repository**
   ```bash
   git clone https://github.com/king13692468/bank-management-system.git
   cd bank-management-system
   ```

2. **Create MySQL database**
   ```sql
   CREATE DATABASE bank_management;
   USE bank_management;
   ```

3. **Create tables** (Run the SQL schema provided above)

4. **Add MySQL Connector**
   - Download `mysql-connector-java-x.x.x.jar`
   - Add to project classpath

5. **Update database credentials** in `Main.java`
   ```java
   private static final String url = "jdbc:mysql://localhost:3306/bank_management";
   private static final String username = "root";
   private static final String password = "yourpassword";
   ```

6. **Compile and run**
   ```bash
   javac BankManagement/*.java
   java BankManagement.Main
   ```

### Or use IntelliJ/Eclipse
1. Open project in IDE
2. Add MySQL Connector to dependencies
3. Run `Main.java`

<br>

## 🧪 TESTING

### Sample Test Flow

```
1. Register: 
   - Email: test@example.com
   - Name: Test User
   - Password: test123

2. Login with same credentials

3. Open Account:
   - Name: Test User
   - Initial Balance: 5000
   - PIN: 1234

4. Credit Money: +2000 (Balance: 7000)

5. Debit Money: -1000 (Balance: 6000)

6. Transfer Money: 
   - Send 2000 to another account

7. Apply for Loan:
   - Amount: 50000
   - Interest Rate: 10%
   - Tenure: 12 months

8. View Loans - Check EMI amount

9. Check Auto EMI deduction
```

### Expected Outputs

| Operation | Expected Result |
|-----------|-----------------|
| Registration | "registration successful" |
| Login | "Login successful" |
| Open Account | "Account created. Number: 10000101" |
| Credit | "5000 credited successfully" |
| Debit | "2000 debited successfully" |
| Transfer | "Transfer successful" |
| Apply Loan | "Loan amount credited to your account!" |
| Check Balance | "balance: XXXX" |

<br>

## 🎯 FUTURE IMPROVEMENTS

- [ ] 🖥️ **Graphical User Interface (Swing/JavaFX)**
- [ ] 🔐 **Two-Factor Authentication**
- [ ] 📱 **Mobile Banking Simulation**
- [ ] 💱 **Multi-Currency Support**
- [ ] 📊 **Transaction History & Statement Generation**
- [ ] 📈 **Interest Calculation on Savings**
- [ ] 🏧 **ATM Simulation Module**
- [ ] 📧 **Email Notifications for Transactions**
- [ ] 🔍 **Advanced Search & Filter for Transactions**
- [ ] 💰 **Fixed Deposit & Recurring Deposit Features**
- [ ] 📑 **PDF Export of Account Statement**
- [ ] 🌐 **Spring Boot REST API Version**

<br>

## 👨‍💻 AUTHOR

**Md Shadab Mobin**

<div align="center">

[![GitHub](https://img.shields.io/badge/GitHub-king13692468-181717?style=for-the-badge&logo=github)](https://github.com/king13692468)
[![Portfolio](https://img.shields.io/badge/Portfolio-Live_Demo-28a745?style=for-the-badge&logo=railway)](https://portfolio-production-7853.up.railway.app/)

</div>

<br>

## ⭐ SHOW YOUR SUPPORT

If you found this project helpful, please give it a ⭐ on GitHub!

<div align="center">

### Built with ❤️ using Java & JDBC

</div>
