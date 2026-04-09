package com.banking;

import com.banking.model.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for model validation, the User inheritance hierarchy,
 * and polymorphic permission resolution.
 */
@DisplayName("Model validation and OOP hierarchy tests")
class ModelValidationTest {

    // ── CustomerProfile validation ─────────────────────────────────────────

    @Test
    @DisplayName("CustomerProfile rejects blank name")
    void customerProfile_blankName_throwsException() {
        CustomerProfile c = new CustomerProfile();
        assertThrows(IllegalArgumentException.class, () -> c.setName(""));
        assertThrows(IllegalArgumentException.class, () -> c.setName("   "));
        assertThrows(IllegalArgumentException.class, () -> c.setName(null));
    }

    @Test
    @DisplayName("CustomerProfile rejects invalid phone")
    void customerProfile_invalidPhone_throwsException() {
        CustomerProfile c = new CustomerProfile();
        assertThrows(IllegalArgumentException.class, () -> c.setPhone("abc"));
        assertThrows(IllegalArgumentException.class, () -> c.setPhone("123")); // too short
    }

    @Test
    @DisplayName("CustomerProfile accepts valid phone")
    void customerProfile_validPhone_doesNotThrow() {
        CustomerProfile c = new CustomerProfile();
        assertDoesNotThrow(() -> c.setPhone("9841000001"));
        assertDoesNotThrow(() -> c.setPhone(null));   // nullable
    }

    @Test
    @DisplayName("CustomerProfile rejects invalid email")
    void customerProfile_invalidEmail_throwsException() {
        CustomerProfile c = new CustomerProfile();
        assertThrows(IllegalArgumentException.class, () -> c.setEmail("notanemail"));
        assertThrows(IllegalArgumentException.class, () -> c.setEmail("@nodomain"));
    }

    @Test
    @DisplayName("CustomerProfile accepts valid email")
    void customerProfile_validEmail_doesNotThrow() {
        CustomerProfile c = new CustomerProfile();
        assertDoesNotThrow(() -> c.setEmail("user@example.com"));
        assertDoesNotThrow(() -> c.setEmail(null));
    }

    // ── User inheritance and polymorphism ─────────────────────────────────

    @Test
    @DisplayName("Admin has all permissions")
    void admin_hasAllExpectedPermissions() {
        User admin = new Admin();
        String[] perms = admin.getPermissions();
        assertTrue(perms.length > 10, "Admin should have many permissions");

        // Check key admin-only permissions exist
        boolean hasUserWrite = false, hasAuditRead = false;
        for (String p : perms) {
            if ("USER_WRITE".equals(p))   hasUserWrite = true;
            if ("AUDIT_READ".equals(p))   hasAuditRead = true;
        }
        assertTrue(hasUserWrite, "Admin must have USER_WRITE permission");
        assertTrue(hasAuditRead, "Admin must have AUDIT_READ permission");
    }

    @Test
    @DisplayName("Staff has limited permissions — no user management or audit")
    void staff_doesNotHaveAdminPermissions() {
        User staff = new Staff();
        String[] perms = staff.getPermissions();

        for (String p : perms) {
            assertNotEquals("USER_DELETE", p, "Staff must not have USER_DELETE");
            assertNotEquals("AUDIT_READ",  p, "Staff must not have AUDIT_READ");
        }
    }

    @Test
    @DisplayName("Customer has read-only own-data permissions only")
    void customer_hasReadOnlyPermissions() {
        User customer = new Customer();
        String[] perms = customer.getPermissions();

        for (String p : perms) {
            assertFalse(p.contains("WRITE"),  "Customer must have no WRITE permissions");
            assertFalse(p.contains("DELETE"), "Customer must have no DELETE permissions");
            assertFalse(p.contains("VERIFY"), "Customer must have no VERIFY permissions");
        }
    }

    @Test
    @DisplayName("Each role returns correct role description (polymorphism)")
    void getRoleDescription_polymorphicDispatch() {
        User admin    = new Admin();
        User staff    = new Staff();
        User customer = new Customer();

        assertNotNull(admin.getRoleDescription());
        assertNotNull(staff.getRoleDescription());
        assertNotNull(customer.getRoleDescription());

        // Each description should be distinct
        assertNotEquals(admin.getRoleDescription(),    staff.getRoleDescription());
        assertNotEquals(staff.getRoleDescription(),    customer.getRoleDescription());
        assertNotEquals(admin.getRoleDescription(),    customer.getRoleDescription());
    }

    @Test
    @DisplayName("Role strings are set correctly by each subclass")
    void role_setCorrectlyBySubclass() {
        assertEquals("Admin",    new Admin().getRole());
        assertEquals("Staff",    new Staff().getRole());
        assertEquals("Customer", new Customer().getRole());
    }

    @Test
    @DisplayName("User setUsername rejects blank input")
    void user_setUsername_rejectsBlank() {
        Admin admin = new Admin();
        assertThrows(IllegalArgumentException.class, () -> admin.setUsername(""));
        assertThrows(IllegalArgumentException.class, () -> admin.setUsername(null));
    }

    // ── Session permission caching ─────────────────────────────────────────

    @Test
    @DisplayName("Session hasPermission returns correct results for Admin")
    void session_hasPermission_adminHasAllPermissions() {
        com.banking.util.Session session = com.banking.util.Session.getInstance();
        Admin admin = new Admin(1, "testadmin", "hash", null);
        session.setCurrentUser(admin);

        assertTrue(session.hasPermission("USER_WRITE"));
        assertTrue(session.hasPermission("AUDIT_READ"));
        assertTrue(session.hasPermission("TRANSACTION_WRITE"));
        assertFalse(session.hasPermission("NONEXISTENT_PERMISSION"));

        session.logout();
    }

    @Test
    @DisplayName("Session hasPermission returns false after logout")
    void session_hasPermission_falseAfterLogout() {
        com.banking.util.Session session = com.banking.util.Session.getInstance();
        session.setCurrentUser(new Admin(1, "admin", "hash", null));
        session.logout();

        assertFalse(session.hasPermission("USER_WRITE"));
        assertFalse(session.isLoggedIn());
    }
}