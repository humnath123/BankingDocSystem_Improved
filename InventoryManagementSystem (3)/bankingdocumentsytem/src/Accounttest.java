package com.banking;

import com.banking.exception.InsufficientFundsException;
import com.banking.model.Account;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Account model.
 *
 * <p>Tests validate:
 * <ul>
 *   <li>Deposit with positive amount succeeds</li>
 *   <li>Deposit with zero/negative amount throws exception</li>
 *   <li>Withdrawal within balance succeeds</li>
 *   <li>Withdrawal exceeding balance throws InsufficientFundsException</li>
 *   <li>Withdrawal of exact balance (boundary value) succeeds</li>
 *   <li>Balance remains accurate after sequential operations</li>
 *   <li>Status checks (isActive, isClosed, isSuspended)</li>
 * </ul>
 */
@DisplayName("Account model unit tests")
class AccountTest {

    private Account account;

    @BeforeEach
    void setUp() {
        account = new Account("ACC-TEST-001", 1, "Savings", 1000.00, "Active");
    }

    // ── Deposit tests ──────────────────────────────────────────────────────

    @Test
    @DisplayName("Deposit positive amount increases balance")
    void deposit_positiveAmount_increasesBalance() {
        account.deposit(new BigDecimal("500.00"));
        assertEquals(new BigDecimal("1500.00"), account.getBalance());
    }

    @Test
    @DisplayName("Deposit zero amount throws IllegalArgumentException")
    void deposit_zeroAmount_throwsException() {
        assertThrows(IllegalArgumentException.class,
                () -> account.deposit(BigDecimal.ZERO));
    }

    @Test
    @DisplayName("Deposit negative amount throws IllegalArgumentException")
    void deposit_negativeAmount_throwsException() {
        assertThrows(IllegalArgumentException.class,
                () -> account.deposit(new BigDecimal("-100.00")));
    }

    @Test
    @DisplayName("Deposit null amount throws IllegalArgumentException")
    void deposit_nullAmount_throwsException() {
        assertThrows(IllegalArgumentException.class,
                () -> account.deposit(null));
    }

    // ── Withdrawal tests ───────────────────────────────────────────────────

    @Test
    @DisplayName("Withdraw amount less than balance succeeds")
    void withdraw_lessThanBalance_succeeds() {
        account.withdraw(new BigDecimal("400.00"));
        assertEquals(new BigDecimal("600.00"), account.getBalance());
    }

    @Test
    @DisplayName("Withdraw exact balance (boundary value) succeeds")
    void withdraw_exactBalance_succeeds() {
        account.withdraw(new BigDecimal("1000.00"));
        assertEquals(BigDecimal.ZERO.setScale(2), account.getBalance());
    }

    @Test
    @DisplayName("Withdraw more than balance throws InsufficientFundsException")
    void withdraw_exceedsBalance_throwsInsufficientFundsException() {
        InsufficientFundsException ex = assertThrows(
                InsufficientFundsException.class,
                () -> account.withdraw(new BigDecimal("1500.00"))
        );
        assertEquals(1500.0, ex.getRequestedAmount(), 0.001);
        assertEquals(1000.0, ex.getAvailableBalance(), 0.001);
    }

    @Test
    @DisplayName("Withdraw zero amount throws IllegalArgumentException")
    void withdraw_zeroAmount_throwsException() {
        assertThrows(IllegalArgumentException.class,
                () -> account.withdraw(BigDecimal.ZERO));
    }

    // ── Sequential operation tests ─────────────────────────────────────────

    @Test
    @DisplayName("Sequential deposits accumulate correctly")
    void sequentialDeposits_accumulateCorrectly() {
        account.deposit(new BigDecimal("200.00"));
        account.deposit(new BigDecimal("300.00"));
        account.deposit(new BigDecimal("50.50"));
        assertEquals(new BigDecimal("1550.50"), account.getBalance());
    }

    @Test
    @DisplayName("Deposit then withdraw gives correct final balance")
    void depositThenWithdraw_correctFinalBalance() {
        account.deposit(new BigDecimal("500.00")); // 1500
        account.withdraw(new BigDecimal("200.00")); // 1300
        assertEquals(new BigDecimal("1300.00"), account.getBalance());
    }

    @Test
    @DisplayName("Balance unchanged after failed withdrawal")
    void failedWithdrawal_doesNotChangeBalance() {
        BigDecimal balanceBefore = account.getBalance();
        assertThrows(InsufficientFundsException.class,
                () -> account.withdraw(new BigDecimal("9999.00")));
        assertEquals(balanceBefore, account.getBalance());
    }

    // ── Status tests ───────────────────────────────────────────────────────

    @Test
    @DisplayName("Active account returns isActive true")
    void activeAccount_isActiveTrue() {
        assertTrue(account.isActive());
        assertFalse(account.isClosed());
        assertFalse(account.isSuspended());
    }

    @Test
    @DisplayName("Closed account returns isClosed true")
    void closedAccount_isClosedTrue() {
        account.setStatus("Closed");
        assertFalse(account.isActive());
        assertTrue(account.isClosed());
    }

    // ── Parameterized tests ────────────────────────────────────────────────

    @ParameterizedTest
    @ValueSource(doubles = {0.01, 1.0, 100.0, 999.99, 1000.0})
    @DisplayName("Valid deposit amounts all succeed")
    void deposit_variousValidAmounts_allSucceed(double amount) {
        assertDoesNotThrow(() -> account.deposit(BigDecimal.valueOf(amount)));
    }

    @ParameterizedTest
    @ValueSource(doubles = {1001.0, 5000.0, 999999.99})
    @DisplayName("Withdrawal exceeding balance always throws")
    void withdraw_amountsExceedingBalance_alwaysThrow(double amount) {
        assertThrows(InsufficientFundsException.class,
                () -> account.withdraw(BigDecimal.valueOf(amount)));
    }
}