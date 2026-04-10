package com.banking.view;

import com.banking.controller.DocumentController;
import com.banking.controller.TransactionController;
import com.banking.dao.AccountDAO;
import com.banking.dao.CustomerDAO;
import com.banking.dao.DocumentDAO;
import com.banking.model.Account;
import com.banking.model.Customer;
import com.banking.model.Document;
import com.banking.model.Transaction;
import com.banking.util.AlertHelper;
import com.banking.util.Session;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.util.List;

/**
 * Enhanced Customer Dashboard with ID Proof Section
 * Features:
 * - Welcome message with customer name
 * - Personal information overview
 * - Bank accounts with balances
 * - ID PROOF section (highlighted)
 * - Document verification status
 * - Quick access to essential features
 * - Profile completion indicator
 */
public class CustomerDashboardView {

    private BorderPane root;
    private CustomerDAO customerDAO;
    private AccountDAO accountDAO;
    private DocumentDAO documentDAO;
    private DocumentController documentController;
    private TransactionController transactionController;
    private Customer currentCustomer;

    public CustomerDashboardView() {
        customerDAO = new CustomerDAO();
        accountDAO = new AccountDAO();
        documentDAO = new DocumentDAO();
        documentController = new DocumentController();
        transactionController = new TransactionController();
        root = new BorderPane();
        root.getStyleClass().add("dashboard-root");

        loadData();
        buildDashboard();
    }

    private void loadData() {
        try {
            int userId = Session.getInstance().getCurrentUser().getUserId();
            currentCustomer = customerDAO.getCustomerByUserId(userId);
            if (currentCustomer == null) {
                AlertHelper.showError("Error", "Customer profile not found");
            }
        } catch (Exception e) {
            System.err.println("Error loading customer data: " + e.getMessage());
        }
    }

    private void buildDashboard() {
        VBox mainContent = new VBox(20);
        mainContent.setPadding(new Insets(30));
        mainContent.getStyleClass().add("page-content");

        // Header
        VBox header = buildHeader();
        mainContent.getChildren().add(header);

        // Profile Completion Status
        VBox profileStatus = buildProfileCompletionStatus();
        mainContent.getChildren().add(profileStatus);

        // ID PROOF Section (Highlighted)
        VBox idProofSection = buildIDProofSection();
        mainContent.getChildren().add(idProofSection);

        // Customer Info Section
        VBox customerInfo = buildCustomerInfoSection();
        mainContent.getChildren().add(customerInfo);

        // Accounts Section
        VBox accountsSection = buildAccountsSection();
        mainContent.getChildren().add(accountsSection);

        // Recent Transactions Section
        VBox transactionsSection = buildRecentTransactionsSection();
        mainContent.getChildren().add(transactionsSection);

        // Quick Actions
        HBox quickActions = buildQuickActions();
        mainContent.getChildren().add(quickActions);

        // Quick Stats
        HBox statsBox = buildQuickStats();
        mainContent.getChildren().add(statsBox);

        ScrollPane scrollPane = new ScrollPane(mainContent);
        scrollPane.getStyleClass().add("scroll-pane");
        scrollPane.setFitToWidth(true);
        root.setCenter(scrollPane);
    }

    private VBox buildHeader() {
        VBox header = new VBox(8);
        Label title = new Label("🏠 Dashboard");
        title.getStyleClass().add("page-title");

        String customerName = currentCustomer != null ? currentCustomer.getName() : "Customer";
        Label welcome = new Label("Welcome back, " + customerName + "!");
        welcome.getStyleClass().add("page-subtitle");

        Label date = new Label("📅 " + new java.text.SimpleDateFormat("EEEE, MMMM dd, yyyy").format(new java.util.Date()));
        date.getStyleClass().add("hint-text");

        header.getChildren().addAll(title, welcome, date);

        // Add fade-in animation
        header.getStyleClass().add("fade-in");

        return header;
    }

    private VBox buildProfileCompletionStatus() {
        VBox section = new VBox(10);
        section.getStyleClass().add("card");
        section.setPadding(new Insets(20));

        Label statusTitle = new Label("✅ Profile Status");
        statusTitle.getStyleClass().add("section-title");

        int completionPercent = calculateProfileCompletion();
        ProgressBar progressBar = new ProgressBar(completionPercent / 100.0);
        progressBar.setPrefWidth(500);
        progressBar.setStyle("-fx-accent: linear-gradient(to right, #28a745, #20c997);");

        Label statusLabel = new Label("Profile " + completionPercent + "% Complete - " +
            (completionPercent == 100 ? "Fully Updated" : "Please complete your profile"));
        statusLabel.getStyleClass().add("hint-text");

        section.getChildren().addAll(statusTitle, progressBar, statusLabel);

        // Add slide-up animation
        section.getStyleClass().add("slide-up");

        return section;
    }

    private VBox buildIDProofSection() {
        VBox section = new VBox(12);
        section.getStyleClass().add("card");
        section.getStyleClass().add("gradient-bg");
        section.setPadding(new Insets(20));

        Label sectionTitle = new Label("🆔 ID PROOF - Important Document");
        sectionTitle.getStyleClass().add("section-title");
        sectionTitle.setStyle("-fx-text-fill: white;");

        HBox idProofContent = new HBox(20);
        idProofContent.setAlignment(Pos.CENTER_LEFT);

        VBox idProofInfo = new VBox(8);
        idProofInfo.setStyle("-fx-text-fill: white;");

        Label idProofStatus = new Label("📄 Your ID Proof Document");
        idProofStatus.getStyleClass().add("section-title");
        idProofStatus.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");

        // Get ID proof document
        Document idProof = getIDProofDocument();
        String statusText;
        String statusColor;

        if (idProof != null) {
            statusText = "Status: " + idProof.getVerifiedLabel();
            statusColor = "Approved".equals(idProof.getVerifiedLabel()) ? "#90EE90" :
                          "Rejected".equals(idProof.getVerifiedLabel()) ? "#FFB6C6" : "#FFE082";
        } else {
            statusText = "Status: Not Uploaded";
            statusColor = "#FFE082";
        }

        Label statusLabel = new Label(statusText);
        statusLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: " + statusColor + "; -fx-font-weight: bold;");

        Label infoLabel = new Label("ID proof is required for account verification and banking services.");
        infoLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #E0E0E0; -fx-wrap-text: true;");

        idProofInfo.getChildren().addAll(idProofStatus, statusLabel, infoLabel);

        Button viewBtn = new Button(idProof != null ? "👁️ View ID Proof" : "📤 Upload ID Proof");
        viewBtn.getStyleClass().add("primary-btn");
        viewBtn.setPrefWidth(160);

        if (idProof != null) {
            viewBtn.setOnAction(e -> DocumentImageViewer.viewDocument(idProof));
        } else {
            viewBtn.setOnAction(e -> AlertHelper.showInfo("Upload",
                "Please go to 'My Documents' section to upload your ID proof."));
        }

        idProofContent.getChildren().addAll(idProofInfo, new Region(), viewBtn);
        HBox.setHgrow(idProofContent.lookup("javafx.scene.layout.Region"), Priority.ALWAYS);

        section.getChildren().addAll(sectionTitle, idProofContent);

        // Add slide-up animation with delay
        section.getStyleClass().add("slide-up");

        return section;
    }

    private Document getIDProofDocument() {
        try {
            if (currentCustomer == null) return null;
            List<Document> docs = documentController.getDocumentsByCustomer(currentCustomer.getCustomerId());
            return docs.stream()
                .filter(d -> "ID Proof".equalsIgnoreCase(d.getDocumentType()) ||
                            "ID".equalsIgnoreCase(d.getDocumentType()) ||
                            "Passport".equalsIgnoreCase(d.getDocumentType()))
                .findFirst()
                .orElse(null);
        } catch (Exception e) {
            System.err.println("Error getting ID proof: " + e.getMessage());
            return null;
        }
    }

    private int calculateProfileCompletion() {
        if (currentCustomer == null) return 0;
        int completion = 0;

        if (currentCustomer.getName() != null && !currentCustomer.getName().isEmpty()) completion += 20;
        if (currentCustomer.getPhone() != null && !currentCustomer.getPhone().isEmpty()) completion += 20;
        if (currentCustomer.getAddress() != null && !currentCustomer.getAddress().isEmpty()) completion += 20;
        if (currentCustomer.getDateOfBirth() != null && !currentCustomer.getDateOfBirth().isEmpty()) completion += 20;
        if (currentCustomer.getPhotoPath() != null && !currentCustomer.getPhotoPath().isEmpty()) completion += 20;

        return Math.min(completion, 100);
    }

    private VBox buildCustomerInfoSection() {
        VBox section = new VBox(12);
        section.getStyleClass().add("section-box");
        section.setPadding(new Insets(20));

        Label sectionTitle = new Label("👤 Your Information");
        sectionTitle.getStyleClass().add("section-title");

        GridPane infoGrid = new GridPane();
        infoGrid.setHgap(20);
        infoGrid.setVgap(15);
        infoGrid.setPadding(new Insets(15));
        infoGrid.getStyleClass().add("card");

        if (currentCustomer != null) {
            addInfoRow(infoGrid, 0, "Customer ID:", String.valueOf(currentCustomer.getCustomerId()));
            addInfoRow(infoGrid, 1, "Full Name:", currentCustomer.getName());
            addInfoRow(infoGrid, 2, "Phone:", currentCustomer.getPhone() != null ? currentCustomer.getPhone() : "N/A");
            addInfoRow(infoGrid, 3, "Email:", currentCustomer.getEmail() != null ? currentCustomer.getEmail() : "N/A");
            addInfoRow(infoGrid, 4, "Address:", currentCustomer.getAddress() != null ? currentCustomer.getAddress() : "N/A");
            addInfoRow(infoGrid, 5, "Date of Birth:", currentCustomer.getDateOfBirth() != null ? currentCustomer.getDateOfBirth() : "N/A");
        }

        section.getChildren().addAll(sectionTitle, infoGrid);
        return section;
    }

    private void addInfoRow(GridPane grid, int row, String label, String value) {
        Label labelNode = new Label(label);
        labelNode.getStyleClass().add("field-label");
        Label valueNode = new Label(value);
        valueNode.setStyle("-fx-text-fill: #333; -fx-font-weight: 500;");
        grid.add(labelNode, 0, row);
        grid.add(valueNode, 1, row);
    }

    private VBox buildAccountsSection() {
        VBox section = new VBox(12);
        section.getStyleClass().add("section-box");
        section.setPadding(new Insets(20));

        Label sectionTitle = new Label("🏦 Your Bank Accounts");
        sectionTitle.getStyleClass().add("section-title");

        VBox accountsBox = new VBox(10);
        accountsBox.getStyleClass().add("card");
        accountsBox.setPadding(new Insets(15));

        try {
            if (currentCustomer != null) {
                List<Account> accounts = accountDAO.getAccountsByCustomer(currentCustomer.getCustomerId());

                if (accounts.isEmpty()) {
                    Label noAccounts = new Label("No accounts found");
                    noAccounts.getStyleClass().add("hint-text");
                    accountsBox.getChildren().add(noAccounts);
                } else {
                    for (Account account : accounts) {
                        HBox accountCard = createAccountCard(account);
                        accountsBox.getChildren().add(accountCard);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error loading accounts: " + e.getMessage());
        }

        section.getChildren().addAll(sectionTitle, accountsBox);
        return section;
    }

    private HBox createAccountCard(Account account) {
        HBox card = new HBox(20);
        card.getStyleClass().add("card");
        card.setPadding(new Insets(15));
        card.setAlignment(Pos.CENTER_LEFT);

        VBox details = new VBox(4);
        Label accountNum = new Label("Account: " + account.getAccountNumber());
        accountNum.setStyle("-fx-font-weight: 600; -fx-font-size: 14px;");

        Label accountType = new Label("Type: " + account.getAccountType());
        accountType.getStyleClass().add("hint-text");

        Label status = new Label("Status: " + account.getStatus());
        status.setStyle("-fx-font-size: 12px; -fx-text-fill: " +
            ("Active".equals(account.getStatus()) ? "#28a745" : "#dc3545") + "; -fx-font-weight: 500;");

        details.getChildren().addAll(accountNum, accountType, status);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label balance = new Label("Rs. " + String.format("%.2f", account.getBalance()));
        balance.setStyle("-fx-font-size: 16px; -fx-font-weight: 700; -fx-text-fill: #28a745;");

        card.getChildren().addAll(details, spacer, balance);
        return card;
    }

    private VBox buildRecentTransactionsSection() {
        VBox section = new VBox(12);
        section.getStyleClass().add("card");
        section.setPadding(new Insets(20));

        Label sectionTitle = new Label("📈 Recent Transactions");
        sectionTitle.getStyleClass().add("section-title");

        TableView<Transaction> tableView = new TableView<>();
        tableView.getStyleClass().add("table-view");

        TableColumn<Transaction, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getTransactionId()).asObject());
        idCol.setPrefWidth(50);

        TableColumn<Transaction, String> accountCol = new TableColumn<>("Account No");
        accountCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getAccountNumber()));
        accountCol.setPrefWidth(120);

        TableColumn<Transaction, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getTransactionType()));
        typeCol.setPrefWidth(100);

        TableColumn<Transaction, Double> amountCol = new TableColumn<>("Amount");
        amountCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleDoubleProperty(cellData.getValue().getAmount()).asObject());
        amountCol.setCellFactory(column -> new TableCell<Transaction, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    getStyleClass().removeAll("deposit-amount", "withdrawal-amount");
                } else {
                    setText("Rs. " + String.format("%.2f", item));
                    Transaction transaction = getTableView().getItems().get(getIndex());
                    if ("Deposit".equals(transaction.getTransactionType())) {
                        getStyleClass().add("deposit-amount");
                    } else {
                        getStyleClass().add("withdrawal-amount");
                    }
                }
            }
        });
        amountCol.setPrefWidth(100);

        TableColumn<Transaction, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getTransactionDate().toString()));
        dateCol.setPrefWidth(120);

        TableColumn<Transaction, String> descCol = new TableColumn<>("Description");
        descCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getDescription()));
        descCol.setPrefWidth(200);

        tableView.getColumns().addAll(idCol, accountCol, typeCol, amountCol, dateCol, descCol);

        try {
            if (currentCustomer != null) {
                List<Account> accounts = accountDAO.getAccountsByCustomer(currentCustomer.getCustomerId());
                List<Transaction> allTransactions = new java.util.ArrayList<>();
                for (Account account : accounts) {
                    List<Transaction> txns = transactionController.getTransactionsByAccount(account.getAccountNumber());
                    allTransactions.addAll(txns);
                }
                // Sort by date descending and take top 5
                allTransactions.sort((t1, t2) -> t2.getTransactionDate().compareTo(t1.getTransactionDate()));
                List<Transaction> recentTransactions = allTransactions.stream().limit(5).toList();

                tableView.getItems().addAll(recentTransactions);

                if (recentTransactions.isEmpty()) {
                    Label noTransactions = new Label("No transactions available");
                    noTransactions.getStyleClass().add("hint-text");
                    section.getChildren().addAll(sectionTitle, noTransactions);
                    return section;
                }
            }
        } catch (Exception e) {
            System.err.println("Error loading transactions: " + e.getMessage());
        }

        section.getChildren().addAll(sectionTitle, tableView);
        return section;
    }


    private HBox buildQuickActions() {
        HBox actions = new HBox(15);
        actions.getStyleClass().add("card");
        actions.setPadding(new Insets(20));
        actions.setAlignment(Pos.CENTER_LEFT);

        Label title = new Label("⚡ Quick Actions");
        title.getStyleClass().add("section-title");
        actions.getChildren().add(title);

        Region vSpacer = new Region();
        vSpacer.setPrefWidth(20);
        actions.getChildren().add(vSpacer);

        Button editProfile = new Button("✏️ Edit Profile");
        editProfile.getStyleClass().add("primary-btn");
        editProfile.setOnAction(e -> System.out.println("Edit Profile clicked"));

        Button viewDocuments = new Button("📄 View Documents");
        viewDocuments.getStyleClass().add("btn-info");
        viewDocuments.setOnAction(e -> System.out.println("View Documents clicked"));

        Button viewTransactions = new Button("💸 Transactions");
        viewTransactions.getStyleClass().add("btn-warning");
        viewTransactions.setOnAction(e -> System.out.println("View Transactions clicked"));

        actions.getChildren().addAll(editProfile, viewDocuments, viewTransactions);
        return actions;
    }

    private HBox buildQuickStats() {
        HBox stats = new HBox(15);
        stats.setPadding(new Insets(15));

        stats.getChildren().addAll(
            createStatCard("📄 Documents", getDocumentCount()),
            createStatCard("🏦 Accounts", getAccountCount()),
            createStatCard("💰 Total Balance", getTotalBalance()),
            createStatCard("✅ Verified", getVerifiedDocCount())
        );

        return stats;
    }

    private VBox createStatCard(String label, String value) {
        VBox card = new VBox(8);
        card.getStyleClass().add("stat-card");
        card.setPadding(new Insets(15));
        card.setAlignment(Pos.CENTER);
        card.setPrefWidth(140);

        Label title = new Label(label);
        title.getStyleClass().add("stat-label");

        Label val = new Label(value);
        val.setStyle("-fx-font-size: 18px; -fx-font-weight: 700; -fx-text-fill: #2c3e50;");

        card.getChildren().addAll(title, val);
        return card;
    }

    private String getDocumentCount() {
        try {
            if (currentCustomer != null) {
                List<Document> docs = documentController.getDocumentsByCustomer(currentCustomer.getCustomerId());
                return String.valueOf(docs.size());
            }
        } catch (Exception e) {
            System.err.println("Error getting document count: " + e.getMessage());
        }
        return "0";
    }

    private String getAccountCount() {
        try {
            if (currentCustomer != null) {
                List<Account> accounts = accountDAO.getAccountsByCustomer(currentCustomer.getCustomerId());
                return String.valueOf(accounts.size());
            }
        } catch (Exception e) {
            System.err.println("Error getting account count: " + e.getMessage());
        }
        return "0";
    }

    private String getTotalBalance() {
        try {
            if (currentCustomer != null) {
                List<Account> accounts = accountDAO.getAccountsByCustomer(currentCustomer.getCustomerId());
                double total = accounts.stream().mapToDouble(Account::getBalance).sum();
                return String.format("Rs. %.2f", total);
            }
        } catch (Exception e) {
            System.err.println("Error getting total balance: " + e.getMessage());
        }
        return "Rs. 0.00";
    }

    private String getVerifiedDocCount() {
        try {
            if (currentCustomer != null) {
                List<Document> docs = documentController.getDocumentsByCustomer(currentCustomer.getCustomerId());
                long verified = docs.stream().filter(d -> "Approved".equals(d.getVerifiedLabel())).count();
                return String.valueOf(verified);
            }
        } catch (Exception e) {
            System.err.println("Error getting verified count: " + e.getMessage());
        }
        return "0";
    }

    public BorderPane getRoot() {
        return root;
    }
}
