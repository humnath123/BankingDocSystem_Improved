# Personal Budget Tracker

A JavaFX application for managing personal budgets and expenses.

## Features
- User authentication with roles (Admin/User)
- Add, edit, delete transactions
- Categorize expenses
- Automatic balance calculation
- Transaction history with filtering
- Dashboard with financial summary

## Tech Stack
- Java 11+
- JavaFX
- MySQL
- JDBC

## Setup Instructions

1. **Database Setup:**
   - Install MySQL Workbench
   - Run the `create_tables.sql` script to create the database and tables.
   - Ensure MySQL server is running on localhost:3306

2. **Project Setup:**
   - Open the project in IntelliJ IDEA
   - Add JavaFX library to the project:
     - Download JavaFX SDK from https://openjfx.io/
     - Add the lib folder to project libraries
   - Add MySQL Connector/J JAR:
     - Download from https://dev.mysql.com/downloads/connector/j/
     - Add to project libraries
   - Set VM options for JavaFX: `--module-path /path/to/javafx/lib --add-modules javafx.controls,javafx.fxml`

3. **Run the Application:**
   - Run the Main.java class
   - Login with username: admin, password: admin123

## Running the Application

### Option 1: Using IntelliJ IDEA
1. Set VM options in Run Configuration:
   ```
   --module-path "C:\Users\godav\Downloads\openjfx-25.0.2_windows-x64_bin-sdk (1)\javafx-sdk-25.0.2\lib" --add-modules javafx.controls,javafx.fxml --add-opens javafx.graphics/javafx.scene=ALL-UNNAMED
   ```
2. Run Main.java

### Option 2: Using the Batch Script
- Double-click `run.bat` to run the application with correct module settings.

### Important Notes
- FXML files are now located in `src/fxml/` for proper resource loading in the module system
- Ensure MySQL is running before starting the application
- Default login: admin / admin123

## Architecture
- **Model Layer:** Entities (User, Transaction, Category)
- **DAO Layer:** Data Access Objects with JDBC
- **Service Layer:** Business Logic
- **Controller Layer:** JavaFX Controllers
- **Util Layer:** Database connection, session management

## Database Schema
- users: id, username, password, role
- categories: id, name
- transactions: id, amount, type, category_id, date, note, user_id

## Default Data
- Admin user: admin / admin123
- Categories: Food, Rent, Travel, Bills, Entertainment, Salary, Other
