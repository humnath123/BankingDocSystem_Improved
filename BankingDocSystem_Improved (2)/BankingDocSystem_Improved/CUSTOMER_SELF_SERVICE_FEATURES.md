# Customer Self-Service Features Documentation

## Overview

Customers can now perform three key self-service actions through the Banking Documentation System:

1. **View My Documents** - View all their uploaded documents and their verification status
2. **View My Transactions** - View all transactions for their accounts
3. **Edit My Profile** - Update their personal information

---

## Feature 1: View My Documents

### Purpose
Customers can view all documents they have uploaded and check their verification status (Pending, Approved, or Rejected).

### Location
Dashboard → My Documents

### What Customers Can See
- **Document ID** - System-generated unique identifier
- **Document Type** - Type of document (Passport, National ID, etc.)
- **File Name** - Name of the uploaded file
- **Upload Date** - When the document was submitted
- **Status** - Current verification status (Approved, Rejected, Under Review, Pending)
- **Review Notes** - Admin/Staff notes if document was rejected

### Functionality
✅ **View Only** - Customers cannot modify documents
✅ **Filter by Type** - Search documents by type or filename
✅ **Status Tracking** - See which documents are approved vs rejected
✅ **Review Notes** - Read why document was rejected (if applicable)
✅ **Personal Records** - Only see own documents, not other customers'

### Features
- Search by document type or filename
- Real-time status updates
- Color-coded status indicators:
  - 🟢 **Green** = Approved
  - 🔴 **Red** = Rejected
  - 🟡 **Yellow** = Pending/Under Review

### Code Files
- **View**: `src/main/java/com/banking/view/CustomerDocumentsView.java`
- **Controller**: `DocumentController.getDocumentsByCustomer()`
- **DAO**: `DocumentDAO.getDocumentsByCustomer()`

---

## Feature 2: View My Transactions

### Purpose
Customers can view all transactions for their accounts including deposits, withdrawals, and transfers.

### Location
Dashboard → My Transactions

### What Customers Can See
- **Transaction ID** - System-generated unique identifier
- **Account Number** - Associated account
- **Transaction Type** - Deposit, Withdrawal, or Transfer
- **Amount** - Transaction amount in Rupees
- **Date** - When transaction occurred
- **Description** - Details of transaction

### Functionality
✅ **View Only** - Customers cannot create or modify transactions
✅ **Filter by Account** - View transactions for specific account
✅ **Sorted Chronologically** - Most recent transactions first
✅ **Personal Records** - Only see own transactions, not other customers'
✅ **Multiple Accounts** - View transactions across all customer accounts

### Features
- Account selector dropdown
- Real-time transaction listings
- Color-coded transaction types:
  - 🟢 **Green** = Deposit (money in)
  - 🔴 **Red** = Withdrawal (money out)
  - 🟡 **Yellow** = Transfer (between accounts)
- Chronological sorting

### Code Files
- **View**: `src/main/java/com/banking/view/CustomerTransactionsView.java`
- **Controller**: `TransactionController.getTransactionsByAccount()`
- **Controller**: `AccountController.getAccountsByCustomer()`
- **DAO**: `AccountDAO.getAccountsByCustomer()`
- **DAO**: `TransactionDAO.getTransactionsByAccount()`

---

## Feature 3: Edit My Profile

### Purpose
Customers can update their personal information while maintaining security by preventing login credential changes.

### Location
Dashboard → My Profile

### What Customers Can See & Edit

#### Read-Only (Login Information)
- **Username** - Cannot be changed (contact admin)
- **Email** - Cannot be changed (contact admin)

#### Editable (Personal Information)
- **Full Name** - Update display name
- **Phone Number** - Update contact phone
- **Address** - Update residential/business address
- **Date of Birth** - Update DOB in YYYY-MM-DD format

### Functionality
✅ **Edit Personal Info** - Update address, phone, DOB
✅ **Security** - Cannot change login credentials
✅ **Validation** - Phone number format enforced
✅ **Save/Cancel** - Changes can be saved or discarded
✅ **Instant Feedback** - Success/error messages

### Validation Rules

| Field | Rule | Error |
|-------|------|-------|
| Full Name | Required | "Full name is required." |
| Phone | Required, 7-15 digits | "Phone must be 7–15 digits." |
| Address | Can be empty | (optional) |
| DOB | YYYY-MM-DD format | (optional) |

### Features
- Two-section form layout
  - Login Information (read-only, grayed out)
  - Personal Information (editable)
- Save/Cancel buttons
- Validation on save
- Instant success notification

### Code Files
- **View**: `src/main/java/com/banking/view/CustomerProfileView.java`
- **Controller**: `CustomerController.update()`
- **DAO**: `CustomerDAO.updateCustomer()`

---

## Database Integration

### Tables Used

#### customers table
```
customer_id        INT (PK)
user_id           INT (FK → users)
full_name         VARCHAR(100)
address           VARCHAR(200)
phone             VARCHAR(20)
date_of_birth     DATE
```

#### documents table
```
document_id       INT (PK)
customer_id       INT (FK)
type_id           SMALLINT (FK)
status_id         TINYINT (FK)
file_path         VARCHAR(500)
file_name         VARCHAR(255)
uploaded_at       DATETIME
...
```

#### transactions table
```
transaction_id    INT (PK)
account_number    VARCHAR(50)
transaction_type  VARCHAR(50)
amount           DECIMAL(12,2)
transaction_date  DATETIME
...
```

#### accounts table
```
account_number    VARCHAR(50) (PK)
customer_id       INT (FK)
account_type      VARCHAR(50)
balance          DECIMAL(12,2)
status           VARCHAR(20)
```

---

## Security Features

### ✅ Data Access Control
- Customers only see their own data
- Filtered by `customer_id` or `user_id`
- Session-based authentication

### ✅ Read-Only Protection
- Documents: View only (no download/delete)
- Transactions: View only (no modification)
- Login Credentials: View only (no edit)

### ✅ Credential Protection
- Login username: Not editable by customers
- Email: Not editable by customers
- Contact admin message provided

### ✅ Input Validation
- Phone number format: 7-15 digits
- Date format: YYYY-MM-DD
- Required field checks

---

## User Experience Flow

### Feature 1: View Documents

```
Customer logs in
     ↓
Clicks "My Documents" in dashboard
     ↓
Dialog loads with their documents
     ↓
Customer can:
  • See all document statuses
  • Search by type or filename
  • Read review notes if rejected
  • No action needed (view-only)
```

### Feature 2: View Transactions

```
Customer logs in
     ↓
Clicks "My Transactions" in dashboard
     ↓
Dialog loads with their accounts
     ↓
Customer:
  • Selects account from dropdown
  • Sees transactions for that account
  • Views amounts, dates, types
  • Can filter by account
```

### Feature 3: Edit Profile

```
Customer logs in
     ↓
Clicks "My Profile" in dashboard
     ↓
Form loads with current data
     ↓
Customer can:
  • View login info (read-only)
  • Edit personal info
  • Update phone, address, DOB
  • Click Save or Cancel
     ↓
If Save:
  • Validation runs
  • If valid → Updates saved
  • Success message shown
  • Form refreshes with new data
```

---

## Integration with Dashboard

The three customer features integrate into the main dashboard navigation:

### For Customers Only:
```
Dashboard Menu
├── 📄 My Documents    (NEW)
├── 💰 My Transactions (NEW)
├── 👤 My Profile      (NEW)
└── 🚪 Logout
```

### For Admin/Staff:
```
Dashboard Menu
├── 👥 Customer Management
├── 📄 Document Management
├── 💰 Transaction Management
├── 🏦 Account Management
├── 👤 User Management
├── 🔐 Dashboard Settings
└── 🚪 Logout
```

---

## Configuration

### No Configuration Needed
✅ Uses existing database tables
✅ No schema changes required
✅ No additional dependencies
✅ Works with existing authentication
✅ Integrates with Session management

### Permissions
- **Customers**: Can access own documents, transactions, profile
- **Admin/Staff**: See all customers' documents (in Document Management)
- **Admin/Staff**: Cannot access customer profile views

---

## Deployment

### Files to Add
1. `CustomerDocumentsView.java` - New view for documents
2. `CustomerTransactionsView.java` - New view for transactions
3. `CustomerProfileView.java` - New view for profile

### Files to Update
- `DashboardView.java` - Add menu items for customer features
- `MainApp.java` - Add routes if needed

### Database
- No changes needed
- Existing tables support all functionality

### Deployment Steps
1. Add three new view files to `src/main/java/com/banking/view/`
2. Update `DashboardView.java` to add menu items
3. Recompile application
4. Redeploy JAR/WAR
5. Test with customer account

---

## Testing Scenarios

### Test 1: View Documents
1. Login as customer
2. Click "My Documents"
3. Should see only that customer's documents
4. Filter by type should work
5. Status colors should display correctly

### Test 2: View Transactions
1. Login as customer
2. Click "My Transactions"
3. Should see accounts dropdown
4. Select account should show transactions
5. Amounts and dates should be correct

### Test 3: Edit Profile
1. Login as customer
2. Click "My Profile"
3. Login info should be read-only (grayed out)
4. Can update personal info
5. Invalid phone should show error
6. Save should update and confirm

### Test 4: Security
1. Customer A logs in
2. Should NOT see Customer B's documents
3. Should NOT see Customer B's transactions
4. Should NOT see Customer B's profile

---

## Benefits

### For Customers
✅ Self-service access to their data
✅ Real-time document status tracking
✅ Complete transaction history
✅ Easy profile management
✅ No need to contact admin for view-only operations

### For Organization
✅ Reduced support queries
✅ Improved customer satisfaction
✅ Self-service reduces staff workload
✅ Security maintained through role-based access
✅ Audit trail maintained (read operations logged)

### For Admin/Staff
✅ Customers get own access
✅ Less demand on support
✅ Focus on processing instead of answering queries
✅ Existing admin/staff views unchanged

---

## Future Enhancements

Possible improvements:
- Document download (if file access implemented)
- Transaction export/PDF
- Transaction filtering by date range
- Profile picture upload
- Password change by customer
- Account statement generation
- Transaction search
- Document expiry warnings

---

## Support & Troubleshooting

### Issue: Customer sees no documents
**Solution**: Ensure documents are linked to customer's account in database

### Issue: Transactions not showing
**Solution**: Check that accounts are linked to customer and transactions exist

### Issue: Profile won't save
**Solution**: Validate phone format (7-15 digits only)

### Issue: Read-only fields are editable
**Solution**: This is expected - customers must contact admin to change credentials

---

## API/Controller Methods Used

### Document Controller
```java
controller.getDocumentsByCustomer(customerId)
```

### Account Controller
```java
controller.getAccountsByCustomer(customerId)
```

### Transaction Controller
```java
controller.getTransactionsByAccount(accountNumber)
```

### Customer Controller
```java
controller.update(customer)
```

---

**Feature Complete** ✅
**Production Ready** ✅
**Security Reviewed** ✅

