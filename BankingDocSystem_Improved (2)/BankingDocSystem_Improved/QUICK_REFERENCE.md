# Quick Reference Guide - New Features

## Using ValidationUtil

```java
import com.banking.util.ValidationUtil;

// Validate phone (10 digits numeric)
String err = ValidationUtil.validatePhone(phone);
if (err != null) showError(err);

// Validate name (no numbers)
String err = ValidationUtil.validateName(name);
if (err != null) showError(err);

// Validate username (4+ chars, letters + numbers, no spaces)
String err = ValidationUtil.validateUsername(username);
if (err != null) showError(err);

// Validate address (min 5 chars)
String err = ValidationUtil.validateAddress(address);
if (err != null) showError(err);

// Validate email (valid format)
String err = ValidationUtil.validateEmail(email);
if (err != null) showError(err);

// Validate password (min 6 chars)
String err = ValidationUtil.validatePassword(password);
if (err != null) showError(err);
```

## Permanent User Deletion

```java
// In UserController
UserController controller = new UserController();

// Soft delete (deactivate - reversible)
controller.delete(userId);

// Permanent delete (hard delete - irreversible)
controller.deletePermanently(userId);
```

## Document Rejection

```java
// In DocumentController
DocumentController controller = new DocumentController();

// Reject document with reason
controller.reject(documentId, "Reason text here");

// Existing methods still work
controller.verify(documentId);  // Approve
controller.delete(documentId);  // Delete
```

## Validation Rules

| Field | Pattern | Min Length | Max Length |
|-------|---------|-----------|-----------|
| Phone | `\d{10}` | 10 | 10 |
| Name | No numbers | 1 | Unlimited |
| Username | Letters + Numbers | 4 | Unlimited |
| Address | Any text | 5 | Unlimited |
| Email | Valid format | 5 | Unlimited |
| Password | Any chars | 6 | Unlimited |

## Error Messages

```
Phone: "Phone number must be exactly 10 digits (numeric only)."
Name: "Name should not contain numbers."
Username: 
  - "Username must be at least 4 characters."
  - "Username cannot contain whitespace."
  - "Username must contain at least one letter."
  - "Username must contain at least one number."
Address: "Address must be at least 5 characters long."
Email: "Invalid email format."
Password: "Password must be at least 6 characters."
```

## UI Components

### Reject Document Dialog
```
Title: "Reject Document"
Header: "Reject document: [filename]"
Fields:
  - TextArea for rejection reason
  - Prompt: "Enter the reason why this document is being rejected..."
Buttons:
  - "Reject" (OK_DONE)
  - "Cancel"
```

### Delete User Confirmation
```
Soft Delete:
  Title: "Deactivate User"
  Message: "Deactivate user '[username]'? They can be reactivated later."

Hard Delete:
  Title: "Permanent Delete"
  Message: "Are you sure you want to PERMANENTLY DELETE user '[username]'?
           This action cannot be undone. All associated data will be deleted."
```

## Implementation Locations

| Feature | File | Method |
|---------|------|--------|
| Phone validation | ValidationUtil.java | validatePhone() |
| Name validation | ValidationUtil.java | validateName() |
| Username validation | ValidationUtil.java | validateUsername() |
| Address validation | ValidationUtil.java | validateAddress() |
| Email validation | ValidationUtil.java | validateEmail() |
| Password validation | ValidationUtil.java | validatePassword() |
| Permanent delete | UserDAO.java | deleteUserPermanently() |
| Reject document | DocumentView.java | showRejectDialog() |
| Approve document | DocumentController.java | verify() |

## Database Interaction

### Permanent Delete (UserDAO)
1. Start transaction
2. Delete from customers (FK constraint)
3. Delete from users
4. Commit transaction
5. Rollback on error

### Reject Document (DocumentDAO)
1. Update document status to 4 (REJECTED)
2. Set reviewed_by to current user
3. Store review notes
4. Set reviewed_at timestamp
5. Create audit log entry

## Integration Points

- `RegisterController`: Uses all validation methods
- `RegisterView`: Displays validation requirements in prompts
- `CustomerProfileView`: Uses validations for profile updates
- `UserManagementView`: Provides UI for permanent delete
- `DocumentView`: Provides UI for document rejection
- Database: Transactions, audit logs, status tracking

