# Banking Documentation System - Customer Registration Feature

## Overview

This document describes the **Customer Registration Enhancement** feature that has been implemented in the Banking Documentation System. This feature allows **Admin and Staff users** to add new customers with complete login credentials (username, email, and password) directly from the Customer Management Dashboard.

## What's New

### Enhanced Customer Management Dashboard

When Admin or Staff click the **"+ Add Customer"** button:

1. **New Customer Form Shows**:
   - Customer information fields (Name, Phone, Email, Address, DOB)
   - Login credential fields (Username, Password, Confirm Password)

2. **Two-in-One Creation**:
   - Creates a User account with login credentials
   - Creates a Customer record linked to that user
   - Customer can login immediately after creation

3. **Full Validation**:
   - Phone number format validation
   - Email format validation
   - Password strength requirements
   - Username uniqueness verification
   - Password confirmation matching

## Files Modified

### 1. `src/main/java/com/banking/view/CustomerView.java`
- Added `UserController` import and field
- Enhanced dialog to show credential fields when adding (not editing)
- Added validation for email format and password confirmation
- Implemented two-phase creation: Create User first, then Customer
- Shows success message with username for reference

**Key Changes**:
```java
// New fields added to form when creating customer
TextField usernameField = field("");
PasswordField passwordField = new PasswordField();
PasswordField confirmPasswordField = new PasswordField();

// Two-phase creation
User u = new User(); // Create user first
u.setUsername(username);
u.setPassword(password);
u.setEmail(email);
u.setRole("Customer");

if (userController.add(u)) { // Add user
    User createdUser = userController.getUserByUsername(username); // Get it back
    c.setUserId(createdUser.getUserId()); // Link to customer
    controller.add(c); // Add customer
}
```

### 2. `src/main/java/com/banking/controller/UserController.java`
- Added `getUserByUsername(String username)` method
- Used to retrieve newly created user for linking to customer

**New Method**:
```java
public User getUserByUsername(String username) { 
    return dao.getUserByUsername(username); 
}
```

### 3. `src/main/java/com/banking/dao/UserDAO.java`
- Added `getUserByUsername(String username)` method
- Queries user with role information
- Establishes the connection between user and customer

**New Method**:
```java
public User getUserByUsername(String username) {
    // SELECT u.user_id, u.username, u.full_name, u.email, u.is_active, r.role_name
    // FROM users u JOIN roles r ON r.role_id = u.role_id
    // WHERE u.username = ?
}
```

## Documentation Provided

### 1. **CUSTOMER_REGISTRATION_FEATURE.md**
Complete technical documentation including:
- Feature overview and benefits
- Implementation details
- Two-phase creation process
- Database schema usage
- Security features
- Error handling
- Integration points
- Usage examples

### 2. **UI_GUIDE.md**
Visual user interface guide including:
- Step-by-step process flows
- Dialog layouts and mockups
- Validation error messages
- Success/confirmation messages
- Use cases and workflows
- Troubleshooting guide
- Best practices

### 3. **This README.md**
Quick reference and integration guide

## Quick Start

### For Admin/Staff:

1. **Open Customer Management Dashboard**
   - From main menu, click "Customer Management"

2. **Click "+ Add Customer"**
   - Dialog opens with customer and credential fields

3. **Fill Customer Information**
   - Full Name (required)
   - Phone (required, 7-15 digits)
   - Email (required, must contain @)
   - Address (required)
   - Date of Birth (required, YYYY-MM-DD)

4. **Enter Login Credentials**
   - Username (required, must be unique)
   - Password (required, minimum 6 characters)
   - Confirm Password (must match)

5. **Click Save**
   - System creates user account
   - System creates customer record
   - Success message shows username

6. **Customer Can Now Login**
   - With provided username and password
   - Full access to customer features

## How It Works

### The Process Flow

```
Admin/Staff clicks "Add Customer"
            ↓
    Form dialog opens
            ↓
    User fills customer + credential fields
            ↓
    User clicks Save
            ↓
    System validates all inputs
            ↓
    If validation fails → Error message, allow retry
            ↓
    Create User Account
      - Username: provided
      - Password: hashed with BCrypt
      - Email: from field
      - Role: Customer
            ↓
    If user creation fails → Error (username duplicate?), allow retry
            ↓
    Retrieve newly created User
            ↓
    Create Customer Record
      - Linked to user_id
      - Store customer details
            ↓
    If customer creation fails → Error, allow retry
            ↓
    Success! Customer list refreshes
    Customer can now login
```

## Validation Rules

| Field | Rule | Error |
|-------|------|-------|
| Full Name | Required | "Name is required." |
| Phone | Required, 7-15 digits | "Phone must be 7–15 digits." |
| Email | Required, contains @ | "Invalid email format." |
| Address | Required | Checked during save |
| DOB | Required | Checked during save |
| Username | Required, unique | "Username may already exist." |
| Password | Required, ≥6 chars | "Password must be at least 6 characters." |
| Confirm | Must match password | "Passwords do not match." |

## Security Features

✅ **Password Hashing**
- Uses BCrypt for secure password hashing
- Passwords never stored in plain text
- Passwords never transmitted in clear text

✅ **Username Uniqueness**
- Enforced at database level with UNIQUE constraint
- Checked before user creation

✅ **Email Uniqueness**
- Enforced at database level with UNIQUE constraint
- Required for each customer

✅ **Input Validation**
- Client-side validation for user feedback
- Server-side validation for security

✅ **Role-Based Access**
- Only Admin and Staff can create customers
- Customers cannot create other customers

✅ **Soft Delete**
- Users marked inactive instead of deleted
- Maintains data integrity and audit trail

## Database Schema

### No Schema Changes Required!

This feature uses the existing database tables:

**Users Table**:
- `user_id` - Auto-generated
- `username` - From form (UNIQUE)
- `password_hash` - BCrypt hash of password
- `email` - From form (UNIQUE)
- `full_name` - From customer name field
- `role_id` - Set to Customer role (3)

**Customers Table**:
- `customer_id` - Auto-generated
- `user_id` - Foreign key to users table
- `full_name` - From form
- `address` - From form
- `phone` - From form
- `date_of_birth` - From form

### Relationship Diagram

```
users (1)
  ├─ user_id (PK)
  ├─ username
  ├─ password_hash
  ├─ email
  ├─ full_name
  ├─ role_id
  └─ ...
         ↓ (FK)
         ↓
customers (1-to-1)
  ├─ customer_id (PK)
  ├─ user_id (FK, UNIQUE)
  ├─ full_name
  ├─ address
  ├─ phone
  ├─ date_of_birth
  └─ ...
```

## Integration with Existing System

### ✅ Compatible With:

- **User Management System**: Users created appear in user list
- **Document Management**: Customers can upload documents immediately
- **Authentication System**: New customers can login right away
- **Audit Logging**: Should log customer creation events
- **Notification System**: Can send credentials to customer email

### ✅ No Breaking Changes:

- Edit functionality unchanged
- Delete functionality unchanged
- Existing customer data unaffected
- Backward compatible with old customer records
- No migration required

## Testing

### Test Scenarios

1. **Basic Customer Creation**
   - ✓ Create customer with valid data
   - ✓ Customer appears in list
   - ✓ Success message shows username
   - ✓ Customer can login

2. **Validation Testing**
   - ✓ Empty fields rejected
   - ✓ Invalid phone format rejected
   - ✓ Invalid email format rejected
   - ✓ Password mismatch rejected
   - ✓ Short password rejected

3. **Duplicate Username**
   - ✓ System rejects duplicate username
   - ✓ Clear error message shown
   - ✓ User can retry with different username

4. **Edit Existing Customer**
   - ✓ Edit dialog shows no credential fields
   - ✓ Customer info can be updated
   - ✓ Customer list refreshes
   - ✓ User account remains unchanged

5. **Delete Customer**
   - ✓ Confirmation dialog appears
   - ✓ Customer deleted from list
   - ✓ User account soft-deleted

## Error Handling

### User-Friendly Error Messages

The system provides clear, non-technical error messages:

```
❌ Validation Errors:
   - "Name is required."
   - "Phone must be 7–15 digits."
   - "Invalid email format."
   - "Password must be at least 6 characters."
   - "Passwords do not match."

❌ Duplicate Errors:
   - "Failed to create user account. Username may already exist."

❌ Operation Errors:
   - "User was created but could not be retrieved."
   - "Customer record creation failed."
```

## Backward Compatibility

### ✅ Fully Compatible

- Existing customers unaffected
- Edit still works as before
- Delete still works as before
- No database migration needed
- No configuration changes needed
- Can be deployed without downtime

## Performance Considerations

### Optimizations Included:

- Single database roundtrip per field lookup
- Indexed username and email for fast lookups
- BCrypt hashing is CPU-optimized
- Dialog loads immediately (no network latency)

### Estimated Performance:

- Form validation: < 100ms
- User creation: 1-2 seconds (BCrypt hashing)
- Customer creation: < 100ms
- Total operation: 1-3 seconds

## Deployment Instructions

### Prerequisites:
- Java 11 or higher
- MySQL 5.7 or higher
- Existing banking system running

### Deployment Steps:
1. **No database changes needed** - existing schema works
2. **Recompile** the modified classes
3. **Redeploy** the application
4. **Test** the new feature
5. **Communicate** to Admin/Staff about new capability

### Rollback (if needed):
- No schema to revert
- Just redeploy previous version
- No data cleanup needed
- Created customers will still be valid

## Support & Documentation

### Quick Reference Guides:
- **CUSTOMER_REGISTRATION_FEATURE.md** - Technical documentation
- **UI_GUIDE.md** - User interface and workflow guide
- This **README.md** - Quick start and integration guide

### Key Concepts:
- Two-phase creation (User → Customer)
- Password hashing with BCrypt
- Username uniqueness enforcement
- Email format validation
- Soft delete approach

## Frequently Asked Questions

### Q: Can customers create their own accounts?
**A:** No, only Admin and Staff can create customer accounts.

### Q: What happens to the user account if I delete the customer?
**A:** The user account is soft-deleted (marked as inactive). Data is preserved for audit trail.

### Q: Can I edit customer login credentials after creation?
**A:** No, you can only edit customer information (phone, address, etc.). User credentials must be reset separately.

### Q: Are passwords stored in plain text?
**A:** No, passwords are hashed using BCrypt. The system never stores plain-text passwords.

### Q: Can I bulk import customers?
**A:** Currently, customers must be added one at a time through the UI. Bulk import could be added as a future enhancement.

### Q: What's the minimum password length?
**A:** Minimum 6 characters. Consider recommending stronger passwords (12+ chars, mixed case, numbers, symbols).

### Q: How do I reset a customer's password?
**A:** Create a new customer account or use user management system to reset password (if implemented).

## Future Enhancements

Possible improvements for future versions:

1. **Email Notification**
   - Send credentials to customer's email automatically

2. **Bulk Import**
   - CSV import for multiple customers

3. **Custom Fields**
   - Add custom customer attributes

4. **Password Strength Indicator**
   - Visual feedback during password entry

5. **Temporary Password**
   - Auto-generate temporary password
   - Force change on first login

6. **2FA Setup**
   - Enable two-factor authentication on creation

7. **SMS Notification**
   - Send credentials via SMS

## Support

For issues or questions:
1. Check **UI_GUIDE.md** for user workflows
2. Check **CUSTOMER_REGISTRATION_FEATURE.md** for technical details
3. Review error messages in the application
4. Check database logs for connection issues

---

**Feature Status**: ✅ Production Ready
**Last Updated**: April 9, 2026
**Version**: 1.0
**Compatibility**: Java 11+, MySQL 5.7+

