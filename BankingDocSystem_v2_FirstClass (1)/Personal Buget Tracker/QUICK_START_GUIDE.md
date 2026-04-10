# QUICK START GUIDE - Personal Budget Tracker

## 🚀 Instant Setup (NEW - Automatic Database Initialization!)

### ✨ What's New
The application now **automatically creates the database and tables** on first run!
No manual SQL setup required.

### Step 1: Run Application
- Press **Shift + F10** or click **Run** button
- The database initializes automatically
- Check console for: "Database initialization completed successfully."

### Step 2: Wait for Database Initialization
You'll see messages like:
```
Initializing database...
Database 'prashant' ensured to exist.
Table 'users' ensured to exist.
Table 'categories' ensured to exist.
Table 'budgets' ensured to exist.
Table 'transactions' ensured to exist.
Table 'saving_goals' ensured to exist.
Table 'saving_transactions' ensured to exist.
Default categories inserted.
Default admin user inserted.
Database initialization completed successfully.
```

### Step 3: Login
- Admin Username: `admin`
- Admin Password: `admin1223`

---

## 📋 Prerequisites Before Running

1. **MySQL Server Running**
   - Running on localhost:3306
   - Root password: `humnath123@$ASD`

2. **JavaFX Configured**
   - JavaFX SDK path set in IDE
   - VM options configured (see Old Setup below if needed)

3. **MySQL Driver**
   - mysql-connector-j added to classpath

---

## 🔧 Old Setup (Manual Database - Optional)
1. File → Open → Select project folder
2. Wait for indexing to complete

### If Manual Setup is Needed
1. **Add JavaFX Library**
   - File → Project Structure → Libraries
   - Click **+** → Java
   - Navigate to: `C:\Users\godav\Downloads\openjfx-25.0.2_windows-x64_bin-sdk (1)\javafx-sdk-25.0.2\lib`
   - Click OK

2. **Add MySQL Driver**
   - Click **+** → Java
   - Navigate to: `C:\Users\godav\Downloads\mysql-connector-j-9.6.0\mysql-connector-j-9.6.0`
   - Select `mysql-connector-j-9.6.0.jar`
   - Click OK

3. **Configure Run Settings**
   - Run → Edit Configurations
   - Select **Main** (or create new)
   - Add to **VM options**:
```
--module-path "C:\Users\godav\Downloads\openjfx-25.0.2_windows-x64_bin-sdk (1)\javafx-sdk-25.0.2\lib" --add-modules javafx.controls,javafx.fxml --add-opens javafx.graphics/javafx.scene=ALL-UNNAMED
```

---

## 🔑 Login Credentials

**Admin Account:**
- Username: `admin`
- Password: `admin1223`

**Create New User:**
- Click "Register Here"
- Fill all fields with validation
- Login with new account

## 📋 Features Overview

### For Regular Users:
1. **Dashboard** - See balance, income, expenses
2. **Transactions** - Add income/expense, delete, filter
3. **Budget** - Create and manage monthly budgets
4. **Saving Goals** - Track and manage savings
5. **Reports** - View analytics and statistics

### For Admins:
1. **Dashboard** - System overview
2. **Manage Users** - View and delete users
3. **User Activity** - Monitor user actions
4. **System Reports** - View system-wide statistics

## 💡 Common Operations

### Add a Transaction
1. Go to Transactions
2. Click "Add Income" or "Add Expense"
3. Fill amount, category, date, note
4. Click OK

### Create a Budget
1. Go to Budget
2. Click "Create Budget"
3. Select category and monthly limit
4. Click OK

### Create Saving Goal
1. Go to Saving Goals
2. Click "Create Goal"
3. Enter goal name, target amount, deadline
4. Click OK

### View Reports
1. Go to Reports
2. See monthly summary
3. View category breakdown

## 🆘 Troubleshooting

### Application won't start
- Check JavaFX VM options are correct
- Verify paths don't have spaces or special characters
- Rebuild project (Build → Rebuild)

### Database connection error
- Ensure MySQL server is running
- Check database credentials in DatabaseUtil.java
- Verify create_tables.sql was executed

### FXML not found
- Ensure src/fxml/ directory exists
- Check file names match exactly
- Clear cache and rebuild

## 📚 Documentation Files

- **COMPLETE_APPLICATION_GUIDE.md** - Full documentation
- **FEATURE_CHECKLIST.md** - Feature list
- **create_tables.sql** - Database schema
- **JavaFX_Fix_Instructions.txt** - JavaFX setup
- **README.md** - Project overview

## ✅ Verification Checklist

Before running, ensure:
- [x] MySQL server is running
- [x] create_tables.sql executed successfully
- [x] JavaFX SDK downloaded and path set
- [x] MySQL Connector JAR added
- [x] VM options configured
- [x] Project rebuilt
- [x] src/fxml/ directory exists with FXML files
- [x] src/com/budgettracker/ has all packages

## 🎯 First Time User Steps

1. **Start Application**
   - Login with: admin / admin1223

2. **Explore Admin Panel**
   - View Dashboard
   - Check Manage Users
   - See System Reports

3. **Create Test User**
   - Logout
   - Click Register
   - Fill details: 
     - Full Name: Test User
     - Email: test@example.com
     - Phone: 9999999999
     - Username: testuser
     - Password: test123
   - Click Register

4. **Login as Test User**
   - Username: testuser
   - Password: test123

5. **Try Features**
   - Add Income: $1000
   - Add Expense: $500
   - Create Budget: $400/month on Food
   - Create Saving Goal: $5000 target
   - View Reports

## 📞 Support

**Issues?**
1. Check console for error messages
2. Verify MySQL is running
3. Check database connection
4. Ensure all files are in correct directories
5. Rebuild project

**Need Help?**
- Read COMPLETE_APPLICATION_GUIDE.md
- Check FEATURE_CHECKLIST.md
- Review error messages carefully

---

**Version:** 2.0  
**Status:** ✅ READY TO USE  
**Estimated Setup Time:** 5 minutes
