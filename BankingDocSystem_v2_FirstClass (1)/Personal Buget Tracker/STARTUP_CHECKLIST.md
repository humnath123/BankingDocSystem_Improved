# Pre-Launch Checklist

Before running the Personal Budget Tracker application, ensure the following:

## 1. MySQL Configuration
- [ ] MySQL Server is installed and running
- [ ] MySQL is accessible on localhost:3306
- [ ] MySQL root user password is set to: `humnath123@$ASD`
- [ ] MySQL JDBC Driver (mysql-connector-java) is in classpath

## 2. Project Setup
- [ ] JavaFX SDK is properly configured in IDE
- [ ] Project compiles without errors
- [ ] All dependencies are installed

## 3. Application Configuration
- [ ] Main.java imports DatabaseInitializer
- [ ] module-info.java includes MySQL module requirement
- [ ] DatabaseInitializer.java exists and compiles

## 4. Database Initialization
When you first run the application:
- [ ] The database 'prashant' will be created automatically
- [ ] All required tables will be created
- [ ] Default categories will be inserted
- [ ] Default admin user will be created
- [ ] Check console output for initialization messages

## 5. Testing
- [ ] Application starts without SQL errors
- [ ] Login screen appears after initialization
- [ ] Can login with admin/admin1223
- [ ] Can perform basic operations (add transactions, manage budgets, etc.)

## Default Login Credentials

**Username:** admin
**Password:** admin1223

## Console Output to Expect

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

## If You Encounter Issues

1. **"Table 'prashant.users' doesn't exist"**
   - Check MySQL is running
   - Verify credentials in DatabaseUtil.java
   - Check MySQL port 3306 is open

2. **"MySQL JDBC Driver not found"**
   - Add mysql-connector-java to project dependencies
   - Ensure mysql.connector.j is in module-info.java

3. **"Connection refused"**
   - Ensure MySQL server is running
   - Check that localhost:3306 is accessible
   - Verify firewall settings

4. **Application hangs during startup**
   - May be waiting for database initialization
   - Check MySQL logs
   - Ensure no deadlock in database

