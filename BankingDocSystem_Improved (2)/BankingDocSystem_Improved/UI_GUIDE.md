# Customer Registration Feature - User Interface Guide

## Visual Workflow

### 1. Customer Management Dashboard
```
┌─────────────────────────────────────────────────────────┐
│  Customer Management                                    │
│  Add, edit, search and manage customer records         │
├─────────────────────────────────────────────────────────┤
│  [🔍 Search...]        [spacer]        [+ Add Customer]│
├─────────────────────────────────────────────────────────┤
│  ID | Full Name | Phone | Email | Address | DOB | Act │
├─────────────────────────────────────────────────────────┤
│  1  | John Doe | 9841... | john@... | ...   | 1990 | ✎✕│
│  2  | Jane ...| 9856... | jane@... | ...    | 1995 | ✎✕│
└─────────────────────────────────────────────────────────┘
```

### 2. Click "Add Customer" Button
- Opens the "Add New Customer" dialog
- Shows all customer information fields
- Shows login credential section

### 3. Add New Customer Dialog

```
╔═══════════════════════════════════════════════════════╗
║ Add New Customer                                      ║
║ Enter new customer details and login credentials      ║
╟───────────────────────────────────────────────────────╢
│                                                       │
│  Full Name:               [_________________]        │
│  Phone:                   [_________________]        │
│  Email:                   [_________________]        │
│  Address:                 [_________________]        │
│  Date of Birth:           [_________________]        │
│                           (YYYY-MM-DD)               │
│  ───────────────────────────────────────────        │
│                                                       │
│  Login Credentials                                   │
│                                                       │
│  Username:                [_________________]        │
│  Password:                [*****]        (hidden)    │
│  Confirm Password:        [*****]        (hidden)    │
│                                                       │
│                          [Save] [Cancel]             │
╚═══════════════════════════════════════════════════════╝
```

### 4. Edit Existing Customer Dialog

```
╔═══════════════════════════════════════════════════════╗
║ Edit Customer                                         ║
║ Update customer details                               ║
╟───────────────────────────────────────────────────────╢
│                                                       │
│  Full Name:               [John Doe_____]           │
│  Phone:                   [9841234567_]             │
│  Email:                   [john@example.com_]       │
│  Address:                 [123 Main St___]          │
│  Date of Birth:           [1990-05-15____]          │
│                                                       │
│  (No Login Credentials fields shown for edit)        │
│                                                       │
│                          [Save] [Cancel]             │
╚═══════════════════════════════════════════════════════╝
```

## Step-by-Step Process

### Adding a New Customer

**Step 1: Click Add Customer**
- Location: Top-right of Customer Management page
- Button text: "+ Add Customer"
- Action: Opens dialog

**Step 2: Fill Customer Information**
```
Field              Required    Format/Rules
─────────────────────────────────────────────
Full Name            ✓         Text, any name format
Phone                ✓         7-15 digits
Email                ✓         Must contain @
Address              ✓         Text, free format
Date of Birth        ✓         YYYY-MM-DD format
```

**Step 3: Enter Login Credentials** (New customers only)
```
Field              Required    Format/Rules
─────────────────────────────────────────────
Username             ✓         Unique, alphanumeric
Password             ✓         Minimum 6 characters
Confirm Password     ✓         Must match password
```

**Step 4: Click Save**
- System validates all fields
- Shows error if validation fails
- If successful:
  - Creates user account
  - Creates customer record
  - Refreshes customer list
  - Shows success message

## Validation Error Messages

### Input Validation

| Condition | Error Message |
|-----------|---------------|
| Empty Full Name | "Name is required." |
| Empty Phone | "Phone is required." |
| Invalid Phone Format | "Phone must be 7–15 digits." |
| Empty Email | "Email is required." |
| Invalid Email Format | "Invalid email format." |
| Empty Username | "Username is required." |
| Empty Password | "Password is required." |
| Password < 6 chars | "Password must be at least 6 characters." |
| Passwords Don't Match | "Passwords do not match." |

### Database Validation

| Condition | Error Message |
|-----------|---------------|
| Duplicate Username | "Failed to create user account. Username may already exist." |
| User Creation Fails | "Failed to create user account. Username may already exist." |
| User Not Retrieved | "User was created but could not be retrieved." |
| Customer Creation Fails | "Customer record creation failed." |

## Success Messages

### Add Customer Success
```
┌─────────────────────────────────────────┐
│ Success                                 │
├─────────────────────────────────────────┤
│ Customer and user account created       │
│ successfully.                           │
│ Username: johndoe                       │
│                       [OK]              │
└─────────────────────────────────────────┘
```

### Edit Customer Success
```
┌─────────────────────────────────────────┐
│ Success                                 │
├─────────────────────────────────────────┤
│ Customer updated.                       │
│                       [OK]              │
└─────────────────────────────────────────┘
```

## Edit Customer Workflow

1. Click "Edit" button on a customer row
2. Dialog opens with current customer data
3. **No login credential fields shown**
4. Update customer information as needed
5. Click "Save"
6. If successful, list refreshes

## Delete Customer Workflow

1. Click "Delete" button on a customer row
2. Confirmation dialog appears:
   ```
   Delete Customer
   Delete John Doe? This cannot be undone.
                   [OK] [Cancel]
   ```
3. Click "OK" to confirm deletion
4. Customer record is deleted
5. Associated user account is soft-deleted (marked inactive)

## Key Features

### ✓ Automatic Features
- Password is automatically hashed
- Email is automatically used from customer email field
- Full Name is automatically used from customer name field
- User role is automatically set to "Customer"
- Customer is automatically linked to user

### ✓ Validation Features
- Real-time validation feedback
- Error messages clear and actionable
- Phone number regex validation
- Email format validation
- Password confirmation required
- Username uniqueness check

### ✓ Security Features
- Passwords never displayed (masked with dots)
- Password confirmation prevents typos
- Unique username enforcement
- BCrypt hashing on backend
- Soft delete (data preservation)

## User Permissions

| Role | Can Add Customer | Can Edit Customer | Can Delete Customer |
|------|------------------|------------------|---------------------|
| Admin | ✓ Yes | ✓ Yes | ✓ Yes |
| Staff | ✓ Yes | ✓ Yes | ✓ Yes |
| Customer | ✗ No | ✗ No | ✗ No |

## Common Use Cases

### Use Case 1: Bulk Customer Addition
**Scenario**: Admin needs to add 10 new customers to system

**Process**:
1. Open Customer Management dashboard
2. For each customer:
   - Click "+ Add Customer"
   - Enter customer information
   - Enter login credentials
   - Click "Save"
3. All 10 customers created with active accounts
4. Customers can login immediately

### Use Case 2: Add Customer by Staff
**Scenario**: Staff member receives request to onboard new customer

**Process**:
1. Staff opens Customer Management
2. Clicks "+ Add Customer"
3. Fills form with customer data
4. Generates credentials
5. Saves
6. Can provide credentials to customer via email or in-person

### Use Case 3: Correct Customer Information
**Scenario**: Need to update existing customer's phone number

**Process**:
1. Open Customer Management
2. Find customer in table
3. Click "Edit" button
4. Update phone number (no credentials shown)
5. Click "Save"
6. Change is applied

## Tips and Best Practices

### For Admin/Staff:
1. **Username Choice**: Use predictable format (firstname.lastname or email prefix)
2. **Password Security**: Use strong passwords (mix of upper/lower case, numbers, symbols if possible)
3. **Email Verification**: Ensure email addresses are correct for future notifications
4. **Phone Format**: Use consistent phone format (include area codes)
5. **Documentation**: Keep record of credentials provided to customers

### For Customers:
1. **Change Password**: Encourage customers to change password on first login
2. **Save Credentials**: Store username/password securely
3. **Profile Update**: Customers can't edit their own info (only admin/staff can)
4. **Account Recovery**: Use email for password reset (if implemented)

## Troubleshooting

| Issue | Cause | Solution |
|-------|-------|----------|
| "Username already exists" | Username is taken | Use different username |
| "Invalid email format" | Email missing @ symbol | Check email format |
| "Passwords do not match" | Password fields differ | Retype confirmation |
| "Phone must be 7-15 digits" | Invalid phone number | Use 7-15 digits only |
| "Customer creation failed" | Database error | Check database connection |

## Database Changes

### Users Table Entry Created:
```
user_id: [auto-generated]
username: [from dialog]
password_hash: [hashed password]
email: [from dialog]
full_name: [from dialog]
role_id: 3 (Customer)
is_active: 1 (active)
created_at: [current timestamp]
```

### Customers Table Entry Created:
```
customer_id: [auto-generated]
user_id: [from newly created user]
full_name: [from dialog]
address: [from dialog]
phone: [from dialog]
date_of_birth: [from dialog]
created_at: [current timestamp]
```

---

**Status**: ✅ Feature Complete
**User Guide**: Complete
**Ready for Production**: Yes

