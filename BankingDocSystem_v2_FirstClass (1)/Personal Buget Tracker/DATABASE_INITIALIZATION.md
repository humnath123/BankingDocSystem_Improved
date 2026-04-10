# Database Initialization Setup - Personal Budget Tracker

## Overview
The application now includes an automatic database initialization system that creates the database and tables on first run.

## What Was Done

### 1. Created DatabaseInitializer.java
A new utility class (`com.budgettracker.util.DatabaseInitializer`) has been created that:
- Automatically creates the database `prashant` if it doesn't exist
- Creates all required tables if they don't exist
- Inserts default categories (Food, Rent, Travel, Bills, Entertainment, Salary, Healthcare, Education, Other)
- Inserts a default admin user (username: `admin`, password: `admin1223`)

### 2. Updated Main.java
The Main class now calls `DatabaseInitializer.initializeDatabase()` before launching the JavaFX application.

### 3. Updated module-info.java
Added the MySQL connector module requirement:
```
requires mysql.connector.j;
```

## Database Connection Details

**Database:** prashant
**Host:** localhost:3306
**Username:** root
**Password:** humnath123@$ASD

These credentials are configured in `DatabaseUtil.java`.

## How It Works

1. When the application starts, `Main.main()` is called
2. Before launching the JavaFX UI, `DatabaseInitializer.initializeDatabase()` is executed
3. The initializer:
   - Connects to MySQL using the root credentials
   - Creates the `prashant` database if needed
   - Creates all required tables (users, categories, budgets, transactions, saving_goals, saving_transactions)
   - Inserts default data if needed

## Tables Created

| Table Name | Purpose |
|------------|---------|
| users | Stores user accounts and credentials |
| categories | Stores transaction categories (Food, Rent, etc.) |
| budgets | Stores budget limits for users |
| transactions | Stores all income and expense transactions |
| saving_goals | Stores savings goals for users |
| saving_transactions | Stores transactions related to savings goals |

## Default Admin Account

**Username:** admin
**Password:** admin1223

Use this account to log in to the application on first run.

## Tables Auto-Creation Features

The system uses `CREATE TABLE IF NOT EXISTS` which means:
- Tables are created only if they don't already exist
- Safe to run multiple times without errors
- Existing data is never deleted

## Troubleshooting

### If you get "Table 'prashant.users' doesn't exist"

1. Make sure MySQL is running on localhost:3306
2. Verify the credentials in `DatabaseUtil.java` are correct
3. Check that the MySQL JDBC driver is in your classpath
4. Run the application again - the initializer will create the tables

### If database creation fails

Check that:
1. MySQL service is running
2. Root user can connect with password: `humnath123@$ASD`
3. You have permissions to create databases
4. No firewall is blocking port 3306

## Files Modified

1. `src/Main.java` - Added database initialization call
2. `src/module-info.java` - Added MySQL module requirement
3. `src/com/budgettracker/util/DatabaseInitializer.java` - NEW FILE

## Running the Application

Simply run the application normally. The database initialization happens automatically before the login screen appears.

The console will show messages like:
```
Initializing database...
Database 'prashant' ensured to exist.
Table 'users' ensured to exist.
Table 'categories' ensured to exist.
...
Database initialization completed successfully.
```

## Manual Database Setup (Alternative)

If you prefer manual setup, you can run the SQL file:
1. Open `create_tables.sql` in MySQL Workbench or your MySQL client
2. Execute all the SQL commands
3. The tables will be created with the default data


