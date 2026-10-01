# Banking Documentation System
### Humnath Pokharel | Student ID: 2531266 | CIS096-1
**University of Bedfordshire — Week 9: OOP Architecture and Implementation**

---

## Project Overview
A desktop-based Banking Documentation System built with **Java**, demonstrating core OOP principles, design patterns, and data structures. The system digitises banking records — customer management, accounts, transactions, and document storage.

---

## Repository Structure

```
src/
├── model/
│   ├── User.java              # Abstract base class (Encapsulation, Abstraction)
│   ├── Admin.java             # Extends User (Inheritance, Polymorphism)
│   ├── Staff.java             # Extends User (Inheritance, Polymorphism)
│   ├── Customer.java          # Customer entity with validated setters
│   ├── Account.java           # deposit() / withdraw() with business rules
│   ├── Transaction.java       # Immutable transaction record
│   └── Document.java          # PDF conversion, composition with Customer
├── controller/
│   └── AccountController.java # MVC Controller — routes View ↔ Model
├── dao/
│   └── CustomerDAO.java       # DAO Pattern — CRUD for Customer (PreparedStatements)
├── util/
│   └── DatabaseConnection.java# Singleton Pattern — one shared MySQL connection
└── test/
    └── BankingSystemTest.java # 9 manual test cases covering all core features
```

---

## Design Patterns Used

| Pattern    | Class                  | Purpose                                      |
|------------|------------------------|----------------------------------------------|
| MVC        | AccountController      | Separates UI, logic, and data layers         |
| DAO        | CustomerDAO            | Encapsulates all SQL; prevents SQL injection |
| Singleton  | DatabaseConnection     | One shared MySQL connection across the app   |

---

## OOP Concepts Demonstrated

| Concept       | Where Applied                        |
|---------------|--------------------------------------|
| Encapsulation | Account.balance (private), Customer.setPhone() validation |
| Abstraction   | User abstract class, DAO hides SQL   |
| Inheritance   | Admin extends User, Staff extends User |
| Polymorphism  | login() and generateReport() overridden per role |

---

## How to Run

### Prerequisites
- Java 17+
- MySQL 8.0+
- MySQL Connector/J (add to classpath)

### Database Setup
```sql
CREATE DATABASE banking_db;
USE banking_db;

CREATE TABLE customer (
  customer_id     INT PRIMARY KEY,
  name            VARCHAR(50) NOT NULL,
  address         VARCHAR(100),
  phone           VARCHAR(20),
  email           VARCHAR(100),
  date_of_birth   DATE,
  id_proof_number VARCHAR(50)
);

CREATE TABLE user (
  user_id  INT PRIMARY KEY,
  username VARCHAR(50) NOT NULL,
  password VARCHAR(100) NOT NULL,
  role     VARCHAR(20) NOT NULL
);

CREATE TABLE account (
  account_number VARCHAR(30) PRIMARY KEY,
  customer_id    INT,
  account_type   VARCHAR(30),
  balance        DECIMAL(10,2),
  status         VARCHAR(20),
  FOREIGN KEY (customer_id) REFERENCES customer(customer_id)
);

CREATE TABLE transaction (
  transaction_id   INT PRIMARY KEY,
  account_number   VARCHAR(30),
  transaction_type VARCHAR(20),
  amount           DECIMAL(10,2),
  transaction_date DATETIME,
  staff_id         INT,
  FOREIGN KEY (account_number) REFERENCES account(account_number)
);

CREATE TABLE document (
  document_id   INT PRIMARY KEY,
  customer_id   INT,
  document_type VARCHAR(50),
  file_path     VARCHAR(255),
  upload_date   DATE,
  FOREIGN KEY (customer_id) REFERENCES customer(customer_id)
);
```

### Compile & Run Tests
```bash
# Compile all source files
javac -cp . src/model/*.java src/util/*.java src/dao/*.java src/controller/*.java src/test/*.java

# Run test suite (no DB required for model/controller tests)
java -cp . test.BankingSystemTest
```

---

## Test Results (Expected)
```
=================================================
  BANKING DOCUMENTATION SYSTEM — TEST SUITE
  Humnath Pokharel | CIS096-1 | 2531266
=================================================

--- TEST 1: Inheritance & Polymorphism ---
  [PASS] Admin login grants full access
  [PASS] Staff login grants restricted access
  [PASS] Admin report says FULL SYSTEM REPORT
  [PASS] Staff report says TRANSACTION REPORT

--- TEST 2: Customer Validation ---
  [PASS] Valid customer created
  [PASS] Invalid phone correctly rejected
  [PASS] Invalid email correctly rejected

--- TEST 3: Account Deposit ---
  [PASS] Balance increased to £1500 after deposit

--- TEST 4: Account Withdrawal ---
  [PASS] Balance reduced to £1250 after withdrawal

--- TEST 5: Overdraft Prevention ---
  [PASS] Overdraft blocked — error message returned

--- TEST 6: Closed Account Blocks Transactions ---
  [PASS] Closed account correctly blocks deposit

--- TEST 7: Transaction Record ---
  [PASS] Transaction record contains TXN ID
  [PASS] Transaction record contains account number
  [PASS] Transaction record contains amount

--- TEST 8: Document PDF Conversion ---
  [PASS] Image converted to PDF path
  [PASS] File path updated in document

--- TEST 9: Singleton Pattern ---
  [PASS] Both getInstance() calls return identical object (same reference)

=================================================
  RESULTS: 16 PASSED | 0 FAILED
=================================================
```

---

## Module Information
- **Module:** CIS096-1 — Principles of Programming and Data Structures
- **Institution:** University of Bedfordshire
- **Student:** Humnath Pokharel (2531266)
- **Week 9 Deliverable:** OOP Architecture Documentation and Codebase
"# Manishsir-c-" 
