package com.banking.view;

import com.banking.controller.AccountController;
import com.banking.controller.TransactionController;
import com.banking.dao.CustomerDAO;
import com.banking.model.Account;
import com.banking.model.Customer;
import com.banking.model.Transaction;
import com.banking.util.Session;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;

import java.util.List;

/**
 * CustomerTransactionsView — allows customers to view their own transactions
 * Customers can only see transactions for accounts linked to their customer account
 */
public class CustomerTransactionsView {

    private final BorderPane root;
    private final TableView<Transaction> table;
    private final TransactionController txnController;
    private final AccountController accountController;
    private final CustomerDAO customerDAO;
    private final ComboBox<Account> accountFilter;

    public CustomerTransactionsView() {
        txnController = new TransactionController();
        accountController = new AccountController();
        customerDAO = new CustomerDAO();
        root = new BorderPane();
        root.getStyleClass().add("page-content");
        root.setPadding(new Insets(30));

        // Header
        VBox header = new VBox(4);
        Label title = new Label("My Transactions");
        title.getStyleClass().add("page-title");
        Label subtitle = new Label("View your account transactions and activity");
        subtitle.getStyleClass().add("page-subtitle");
        header.getChildren().addAll(title, subtitle);

        // Toolbar
        HBox toolbar = new HBox(10);
        toolbar.setAlignment(Pos.CENTER_LEFT);
        toolbar.setPadding(new Insets(14, 0, 14, 0));

        Label accountLabel = new Label("Select Account:");
        accountLabel.setPrefWidth(120);

        accountFilter = new ComboBox<>();
        accountFilter.setPrefWidth(250);
        accountFilter.setPromptText("All Accounts");

        toolbar.getChildren().addAll(accountLabel, accountFilter);

        // Build table
        table = buildTable();

        // Account filter listener
        accountFilter.setOnAction(e -> refreshTable());

        VBox content = new VBox(0, header, toolbar, table);
        VBox.setVgrow(table, Priority.ALWAYS);
        root.setCenter(content);

        loadCustomerAccounts();
        refreshTable();
    }

    private void loadCustomerAccounts() {
        try {
            int userId = Session.getInstance().getCurrentUser().getUserId();
            Customer customer = customerDAO.getCustomerByUserId(userId);

            if (customer == null) return;

            List<Account> accounts = accountController.getAccountsByCustomer(customer.getCustomerId());
            accountFilter.getItems().setAll(accounts);

            if (!accounts.isEmpty()) {
                accountFilter.getSelectionModel().selectFirst();
            }
        } catch (Exception e) {
            System.err.println("Error loading customer accounts: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private TableView<Transaction> buildTable() {
        TableView<Transaction> tv = new TableView<>();
        tv.getStyleClass().add("table-view");

        TableColumn<Transaction, Integer> idCol = col("TXN ID", "transactionId", 70);
        TableColumn<Transaction, String> accCol = col("Account Number", "accountNumber", 150);
        TableColumn<Transaction, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(new PropertyValueFactory<>("transactionType"));
        typeCol.setPrefWidth(120);
        typeCol.setCellFactory(c -> new TableCell<>() {
            @Override protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(item);
                getStyleClass().removeAll("transaction-deposit", "transaction-withdrawal", "transaction-other");
                if (!empty && item != null) {
                    switch (item) {
                        case "Deposit" -> getStyleClass().add("transaction-deposit");
                        case "Withdrawal" -> getStyleClass().add("transaction-withdrawal");
                        default -> getStyleClass().add("transaction-other");
                    }
                }
            }
        });

        TableColumn<Transaction, Double> amountCol = col("Amount (Rs.)", "amount", 120);
        TableColumn<Transaction, String> dateCol = col("Date", "transactionDate", 160);
        TableColumn<Transaction, String> descCol = col("Description", "description", 200);

        tv.getColumns().addAll(idCol, accCol, typeCol, amountCol, dateCol, descCol);
        return tv;
    }

    @SuppressWarnings("unchecked")
    private <T> TableColumn<Transaction, T> col(String title, String prop, double width) {
        TableColumn<Transaction, T> c = new TableColumn<>(title);
        c.setCellValueFactory(new PropertyValueFactory<>(prop));
        c.setPrefWidth(width);
        return c;
    }

    private void refreshTable() {
        try {
            Account selectedAccount = accountFilter.getSelectionModel().getSelectedItem();

            if (selectedAccount == null) {
                table.getItems().clear();
                return;
            }

            List<Transaction> txns = txnController.getTransactionsByAccount(selectedAccount.getAccountNumber());
            table.getItems().setAll(txns);
        } catch (Exception e) {
            System.err.println("Error loading transactions: " + e.getMessage());
            table.getItems().clear();
        }
    }

    public BorderPane getRoot() {

        return root;
    }
}
