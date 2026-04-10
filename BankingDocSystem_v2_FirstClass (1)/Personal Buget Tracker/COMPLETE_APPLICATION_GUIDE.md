# Personal Budget Tracker - Complete Application Guide

## ✅ Application Features Implemented

### 1. **User Registration & Authentication**
- ✅ User registration with validation (Name, Email, Phone, Username, Password)
- ✅ Login system with role-based access (Admin/User)
- ✅ Email and phone number validation
- ✅ Secure password confirmation
- ✅ Default admin account: admin / admin1223

### 2. **User Dashboard Features**

#### Dashboard Overview
- ✅ Total Balance Display
- ✅ Monthly Expense Tracking
- ✅ Total Income Summary
- ✅ Recent Transactions View

#### Transaction Management
- ✅ Add Income transactions
- ✅ Add Expense transactions
- ✅ Delete transactions
- ✅ Refresh transaction list
- ✅ Category selection for transactions
- ✅ Date picker for transaction date
- ✅ Notes/description field

#### Budget Management
- ✅ Create monthly budgets per category
- ✅ Edit budget limits
- ✅ Delete budgets
- ✅ Recalculate budget status
- ✅ Budget usage tracking (percentage)
- ✅ Budget vs Spending comparison
- ✅ Remaining budget calculation

#### Saving Goals
- ✅ Create saving goals with target amounts
- ✅ Add savings to goals
- ✅ Delete goals
- ✅ Track goal progress
- ✅ Deadline tracking
- ✅ Goal status (Active/Completed)

#### Reports & Analytics
- ✅ Monthly income report
- ✅ Monthly expense report
- ✅ Monthly savings calculation
- ✅ Category-wise breakdown
- ✅ Financial summary statistics

### 3. **Admin Dashboard Features**

#### Admin Overview
- ✅ Total users count
- ✅ Total transactions in system
- ✅ System-wide income tracking

#### Manage Users
- ✅ View all users
- ✅ Delete user accounts (except admin)
- ✅ User profile information display
- ✅ User management controls

#### User Activity
- ✅ User activity tracking
- ✅ Activity logs
- ✅ User behavior monitoring

#### System Reports
- ✅ System-wide income
- ✅ System-wide expenses
- ✅ Overall financial summary

## 📦 Database Schema

### Tables Created:
1. **users** - User accounts with authentication
2. **categories** - Transaction categories
3. **transactions** - User transactions (Income/Expense)
4. **budgets** - Monthly budget limits per category
5. **saving_goals** - User savings goals
6. **saving_transactions** - Savings tracking

## 🛠 Tech Stack Used

- **Java 11+** with JavaFX 25.0.2
- **MySQL** Database with JDBC
- **MVC Architecture** (Model-View-Controller)
- **Layered Architecture**:
  - Model Layer (Entities)
  - DAO Layer (Data Access)
  - Service Layer (Business Logic)
  - Controller Layer (UI Logic)
  - Util Layer (Database, Session, Validation)

## 📁 Project Structure

```
src/
├── Main.java (JavaFX Application Entry Point)
├── module-info.java (Java Module Configuration)
├── com/budgettracker/
│   ├── model/
│   │   ├── User.java
│   │   ├── Transaction.java
│   │   ├── Category.java
│   │   ├── Budget.java
│   │   ├── SavingGoal.java
│   │   └── TransactionType.java (Enum)
│   ├── dao/
│   │   ├── UserDao.java (Interface)
│   │   ├── UserDaoImpl.java
│   │   ├── TransactionDao.java
│   │   ├── TransactionDaoImpl.java
│   │   ├── CategoryDao.java
│   │   ├── CategoryDaoImpl.java
│   │   ├── BudgetDao.java
│   │   ├── BudgetDaoImpl.java
│   │   ├── SavingGoalDao.java
│   │   └── SavingGoalDaoImpl.java
│   ├── service/
│   │   ├── AuthenticationService.java
│   │   ├── TransactionService.java
│   │   ├── BudgetService.java
│   │   └── SavingGoalService.java
│   ├── controller/
│   │   ├── LoginController.java
│   │   ├── RegisterController.java
│   │   ├── UserDashboardController.java
│   │   ├── AdminDashboardController.java
│   │   ├── UserOverviewController.java
│   │   ├── UserTransactionsController.java
│   │   ├── BudgetManagementController.java
│   │   ├── SavingGoalsController.java
│   │   ├── UserReportsController.java
│   │   ├── AdminOverviewController.java
│   │   ├── ManageUsersController.java
│   │   ├── UserActivityController.java
│   │   └── SystemReportsController.java
│   └── util/
│       ├── DatabaseUtil.java
│       ├── SessionManager.java
│       ├── ValidationUtil.java
│       └── LoggerUtil.java
├── fxml/
│   ├── Login.fxml
│   ├── Register.fxml
│   ├── UserDashboard.fxml
│   ├── AdminDashboard.fxml
│   ├── UserOverview.fxml
│   ├── UserTransactions.fxml
│   ├── BudgetManagement.fxml
│   ├── SavingGoals.fxml
│   ├── UserReports.fxml
│   ├── AdminOverview.fxml
│   ├── ManageUsers.fxml
│   ├── UserActivity.fxml
│   ├── SystemReports.fxml
│   └── styles.css
└── create_tables.sql (Database Setup Script)
```

## 🚀 Setup Instructions

### 1. Database Setup
```bash
# Execute in MySQL Workbench or Command Line:
mysql -u root -p
source create_tables.sql
```

### 2. Project Configuration in IntelliJ IDEA

1. **Add JavaFX Library**
   - File > Project Structure > Libraries
   - Click + > Java
   - Select JavaFX SDK lib folder (C:\...\javafx-sdk-25.0.2\lib)
   - Apply

2. **Add MySQL Connector**
   - File > Project Structure > Libraries
   - Click + > Java
   - Select mysql-connector-j-9.6.0.jar
   - Apply

3. **Configure Run Configuration**
   - Run > Edit Configurations
   - Select "Main" configuration (or create new)
   - VM options:
   ```
   --module-path "C:\...\javafx-sdk-25.0.2\lib" --add-modules javafx.controls,javafx.fxml --add-opens javafx.graphics/javafx.scene=ALL-UNNAMED
   ```
   - Apply

4. **Rebuild Project**
   - Build > Rebuild Project

### 3. Running the Application

**Option 1: Using IntelliJ**
- Press Shift + F10 or click Run button

**Option 2: Using Batch Script**
- Double-click `run.bat` in project root

## 🔐 Default Credentials

**Admin Account:**
- Username: `admin`
- Password: `admin1223`

**Test User Registration:**
- Click "Register Here" on login screen
- Fill in all required fields with validation
- Login with created account

## ✨ Key Features & Validation

### Input Validation
- ✅ Full Name: 2-50 characters (letters and spaces only)
- ✅ Email: Valid email format
- ✅ Phone: 10 digits only
- ✅ Username: 3-50 characters
- ✅ Password: Minimum 6 characters
- ✅ Duplicate prevention for username and email

### Security
- ✅ PreparedStatements to prevent SQL injection
- ✅ Role-based access control (ADMIN/USER)
- ✅ Session management with Singleton pattern
- ✅ Password confirmation on registration

### UI/UX
- ✅ Responsive JavaFX layouts
- ✅ Clean and modern styling (CSS)
- ✅ Color-coded buttons (Green for success, Red for danger, Blue for primary)
- ✅ Intuitive navigation with sidebar menu
- ✅ Real-time data updates
- ✅ Error messages and validation feedback
- ✅ Alert dialogs for confirmations

## 💰 Financial Features

### Transaction Management
- Income and Expense tracking
- Category-based classification
- Date and note tracking
- Real-time balance calculation

### Budget Planning
- Monthly budget limits per category
- Budget vs Actual spending
- Budget status indicator
- Alert on budget exceeded

### Savings Goals
- Create and track savings goals
- Progress tracking
- Goal completion status
- Deadline monitoring

### Reports & Analytics
- Monthly income/expense summary
- Category-wise spending breakdown
- Savings rate calculation
- Financial statistics

## 🔄 Data Flow

```
User Input → Validation → Service Layer → DAO Layer → Database
                ↓
        Business Logic Processing
                ↓
        Response → UI Update → Display to User
```

## 📊 Database Relationships

```
users (1) ──→ (Many) transactions
users (1) ──→ (Many) budgets
users (1) ──→ (Many) saving_goals
categories (1) ──→ (Many) transactions
categories (1) ──→ (Many) budgets
saving_goals (1) ──→ (Many) saving_transactions
```

## 🐛 Troubleshooting

### JavaFX Module Issues
- Ensure JavaFX lib path in VM options is correct
- Check that --add-modules includes javafx.controls and javafx.fxml

### Database Connection Issues
- Verify MySQL server is running
- Check connection string in DatabaseUtil.java
- Ensure database credentials match

### FXML Loading Issues
- Ensure FXML files are in src/fxml/ directory
- Check controller class names match fx:controller in FXML
- Verify @FXML annotations are used correctly

## 📝 Future Enhancements

- Password hashing (BCrypt)
- Multi-currency support
- Export reports to PDF/Excel
- Email notifications
- Two-factor authentication
- Mobile app version
- Advanced analytics and charts
- Recurring transaction automation

## 📞 Support

For issues or questions:
1. Check the error messages in console
2. Verify database connection
3. Ensure all dependencies are installed
4. Check FXML file syntax and bindings

---

**Version:** 2.0  
**Last Updated:** April 10, 2026  
**Status:** Fully Functional ✅
