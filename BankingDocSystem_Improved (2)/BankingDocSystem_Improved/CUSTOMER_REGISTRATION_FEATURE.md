# Customer Registration Feature - Admin/Staff Dashboard

## Overview
This document describes the enhanced Customer Management Dashboard that allows **Admin and Staff users** to add new customers with full user account credentials (username, email, and password) in a single operation.

## Features Implemented

### 1. Enhanced Customer Add Dialog
When Admin/Staff click the **"+ Add Customer"** button in the Customer Management Dashboard, they now get:

#### Customer Information Fields (Always visible):
- **Full Name**: Customer's complete name
- **Phone**: Customer's phone number (7-15 digits)
- **Email**: Customer's email address
- **Address**: Customer's residential/business address
- **Date of Birth**: Date in YYYY-MM-DD format

#### Login Credentials (Only when adding new customers):
- **Username**: Unique username for the customer to use for login
- **Password**: Secure password (minimum 6 characters)
- **Confirm Password**: Password confirmation to prevent typos

### 2. Data Validation
The system performs comprehensive validation:
- **Full Name**: Required field
- **Phone**: Required, must be 7-15 digits
- **Email**: Required, must be valid email format (contains @)
- **Username**: Required for new customers, must be unique
- **Password**: Required for new customers, minimum 6 characters
- **Passwords**: Must match between password and confirm password fields

### 3. Two-Phase User Creation Process

When adding a new customer, the system performs the following steps in sequence:

1. **Create User Account**:
   - Creates a new user record with the provided credentials
   - Username must be unique (validation prevents duplicates)
   - Password is automatically hashed using BCrypt for security
   - Email is validated for uniqueness
   - User role is automatically set to "Customer"
   - Full name is populated from the customer's full name field

2. **Create Customer Record**:
   - Links the newly created user account to the customer record
   - Stores customer personal information (phone, address, DOB)
   - Establishes the 1-to-1 relationship between users and customers

### 4. Security Features
- **Password Hashing**: Passwords are never stored in plain text; they are hashed using BCrypt
- **Email Uniqueness**: Email addresses must be unique across the system
- **Username Uniqueness**: Usernames must be unique to prevent login conflicts
- **Session Management**: Only Admin and Staff can access customer creation feature
- **Soft Deletion**: User accounts are soft-deleted (marked inactive) rather than hard-deleted

### 5. Error Handling
The system provides clear, user-friendly error messages:
- "Username may already exist" - if the username is taken
- "Invalid email format" - if email doesn't contain @
- "Passwords do not match" - if password confirmation fails
- "Failed to create user account" - if user creation fails
- "Customer record creation failed" - if customer record creation fails

### 6. Success Feedback
Upon successful creation:
- User sees confirmation message: "Customer and user account created successfully"
- Display shows the username for reference
- Customer list is automatically refreshed
- New customer appears immediately in the table

## Technical Implementation

### Modified Files

#### 1. `CustomerView.java`
- Added `UserController` to handle user account creation
- Enhanced `showForm()` method to support dual account creation
- Added password fields with confirmation validation
- Implemented two-phase creation: User first, then Customer
- Enhanced validation rules for email format

#### 2. `UserController.java`
- Added `getUserByUsername(String username)` method
- Used to retrieve newly created user after registration

#### 3. `UserDAO.java`
- Added `getUserByUsername(String username)` method
- Queries user by username with role information
- Used to link customer to newly created user

### Database Schema Compliance
The implementation follows the existing database schema:
- **users table**: Stores user credentials with role_id
- **customers table**: One-to-one relationship with users via user_id foreign key
- **roles table**: Customer role automatically assigned (role_id = 3)

### User Workflow

#### For Admin/Staff:
```
1. Click "Add Customer" button
2. Fill in customer information
3. Provide login credentials (username, password, email)
4. Click "Save"
5. System creates user account and customer record
6. Customer list updates automatically
7. New customer can now login with provided credentials
```

#### For New Customer:
```
1. Launch application
2. Click "Login" or enter login screen
3. Enter username and password provided by Admin/Staff
4. Customer account is activated
5. Can access own documents and submit requests
```

## Database Changes Required
No new database schema changes required. The implementation uses existing tables:
- `users`: Stores account credentials
- `customers`: Stores customer details
- `roles`: Already has "Customer" role defined

## Role-Based Access Control
- **Admin**: Can create customers with full credentials
- **Staff**: Can create customers with full credentials
- **Customer**: Cannot create other customer accounts

## Integration with Existing Features
This feature integrates seamlessly with:
- **User Management**: Created users appear in user management list
- **Document Management**: Customers can immediately upload documents
- **Authentication**: New customers can login immediately after creation
- **Audit Logging**: All customer creation actions should be logged

## Benefits
1. **Streamlined Workflow**: Admin/Staff can create customer accounts and profiles in one step
2. **Reduced Errors**: Validation prevents invalid data entry
3. **Security**: Passwords are hashed; credentials are handled securely
4. **Efficiency**: No need to create user account separately from customer record
5. **Immediate Access**: New customers can login immediately after creation
6. **Audit Trail**: System tracks who created which customer accounts

## Usage Example

### Scenario: Admin creates a new customer
1. Admin logs into the system
2. Navigates to "Customer Management" dashboard
3. Clicks "+ Add Customer" button
4. Dialog appears with customer and credential fields
5. Enters:
   - Full Name: "John Doe"
   - Phone: "9841234567"
   - Email: "john.doe@example.com"
   - Address: "123 Main Street, Kathmandu"
   - Date of Birth: "1990-05-15"
   - Username: "johndoe"
   - Password: "secure@123"
   - Confirm Password: "secure@123"
6. Clicks "Save"
7. System creates:
   - User account (username: johndoe, email: john.doe@example.com, role: Customer)
   - Customer record linked to the user
8. Success message shows: "Customer and user account created successfully. Username: johndoe"
9. John Doe can now login with username "johndoe" and password "secure@123"

## Future Enhancements
- Email notification with login credentials sent to customer email
- Password strength indicator in UI
- Bulk customer import from CSV
- Customer template/default values
- SMS notification of credentials

