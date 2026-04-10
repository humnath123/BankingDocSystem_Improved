# Database Architecture - Personal Budget Tracker

## Database Overview

**Database Name:** prashant
**Connection String:** jdbc:mysql://localhost:3306/prashant

## Table Structures

### 1. USERS Table
Stores user account information and authentication details.

```
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone VARCHAR(20),
    full_name VARCHAR(100),
    role ENUM('ADMIN', 'USER') NOT NULL DEFAULT 'USER',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
)
```

**Purpose:** User authentication and profile management
**Key Fields:** 
- `id` - Unique user identifier
- `username` - Login username (unique)
- `password` - Encrypted password
- `role` - User role (ADMIN or USER)

---

### 2. CATEGORIES Table
Stores transaction categories for organizing expenses and income.

```
CREATE TABLE categories (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE
)
```

**Purpose:** Transaction categorization
**Default Categories:**
- Food
- Rent
- Travel
- Bills
- Entertainment
- Salary
- Healthcare
- Education
- Other

---

### 3. BUDGETS Table
Stores monthly budget limits for each user and category.

```
CREATE TABLE budgets (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    category_id INT,
    limit_amount DECIMAL(10,2) NOT NULL,
    month INT NOT NULL,
    year INT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (category_id) REFERENCES categories(id),
    UNIQUE KEY unique_budget (user_id, category_id, month, year)
)
```

**Purpose:** Budget management and tracking
**Key Fields:**
- `user_id` - User who created the budget
- `category_id` - Category the budget applies to
- `limit_amount` - Maximum amount for the budget
- `month` - Budget month (1-12)
- `year` - Budget year

---

### 4. TRANSACTIONS Table
Records all income and expense transactions.

```
CREATE TABLE transactions (
    id INT AUTO_INCREMENT PRIMARY KEY,
    amount DECIMAL(10,2) NOT NULL,
    type ENUM('INCOME', 'EXPENSE') NOT NULL,
    category_id INT,
    date DATE NOT NULL,
    note TEXT,
    user_id INT NOT NULL,
    FOREIGN KEY (category_id) REFERENCES categories(id),
    FOREIGN KEY (user_id) REFERENCES users(id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
)
```

**Purpose:** Transaction logging
**Key Fields:**
- `amount` - Transaction amount
- `type` - INCOME or EXPENSE
- `category_id` - Transaction category
- `date` - Transaction date
- `note` - Optional description

---

### 5. SAVING_GOALS Table
Stores savings goals and progress.

```
CREATE TABLE saving_goals (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    goal_name VARCHAR(100) NOT NULL,
    target_amount DECIMAL(10,2) NOT NULL,
    current_amount DECIMAL(10,2) DEFAULT 0,
    deadline DATE,
    status ENUM('ACTIVE', 'COMPLETED', 'CANCELLED') DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
)
```

**Purpose:** Savings goal management
**Key Fields:**
- `goal_name` - Name of the savings goal
- `target_amount` - Target savings amount
- `current_amount` - Amount saved so far
- `deadline` - Target completion date
- `status` - Goal status (ACTIVE, COMPLETED, CANCELLED)

---

### 6. SAVING_TRANSACTIONS Table
Records individual deposits/contributions to savings goals.

```
CREATE TABLE saving_transactions (
    id INT AUTO_INCREMENT PRIMARY KEY,
    goal_id INT NOT NULL,
    user_id INT NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    date DATE NOT NULL,
    note TEXT,
    FOREIGN KEY (goal_id) REFERENCES saving_goals(id),
    FOREIGN KEY (user_id) REFERENCES users(id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
)
```

**Purpose:** Track individual savings contributions
**Key Fields:**
- `goal_id` - Associated savings goal
- `amount` - Contribution amount
- `date` - Contribution date

---

## Entity Relationships

```
USERS (1) ──→ (M) BUDGETS
USERS (1) ──→ (M) TRANSACTIONS
USERS (1) ──→ (M) SAVING_GOALS
USERS (1) ──→ (M) SAVING_TRANSACTIONS
CATEGORIES (1) ──→ (M) BUDGETS
CATEGORIES (1) ──→ (M) TRANSACTIONS
SAVING_GOALS (1) ──→ (M) SAVING_TRANSACTIONS
```

Where:
- (1) = One
- (M) = Many

---

## Data Flow

### User Registration/Login
1. User enters credentials
2. System queries USERS table
3. Authenticates user role (ADMIN or USER)

### Transaction Management
1. User creates transaction
2. System inserts into TRANSACTIONS table
3. Links to CATEGORIES and USERS
4. Calculates budget usage

### Budget Tracking
1. System queries TRANSACTIONS for period
2. Calculates spending per category
3. Compares against BUDGETS table
4. Generates alerts if budget exceeded

### Savings Goals
1. User creates goal in SAVING_GOALS
2. User makes contributions tracked in SAVING_TRANSACTIONS
3. System calculates progress
4. Updates goal status when complete

---

## Database Performance Tips

1. **Indexes:** Ensure user_id and category_id are indexed for faster queries
2. **Date Range Queries:** Transactions table benefits from date indexes
3. **Unique Constraints:** Prevents duplicate budgets for same period
4. **Foreign Keys:** Maintain referential integrity

---

## Backup Strategy

Recommended backup frequency:
- **Daily:** Transaction and user data changes
- **Weekly:** Full database backup
- **Monthly:** Archive backup off-site

Use MySQL's built-in backup tools:
```bash
mysqldump -u root -p prashant > backup.sql
```

---

## Maintenance

### Check Database Size
```sql
SELECT table_name, (data_length + index_length)/1024/1024 AS size_mb 
FROM information_schema.tables 
WHERE table_schema = 'prashant';
```

### Optimize Tables
```sql
OPTIMIZE TABLE users, categories, budgets, transactions, saving_goals, saving_transactions;
```


