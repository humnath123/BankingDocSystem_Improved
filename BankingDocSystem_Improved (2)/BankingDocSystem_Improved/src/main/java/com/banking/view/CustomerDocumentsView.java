package com.banking.view;

import com.banking.controller.DocumentController;
import com.banking.dao.CustomerDAO;
import com.banking.model.Customer;
import com.banking.model.Document;
import com.banking.util.AlertHelper;
import com.banking.util.Session;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.List;

/**
 * CustomerDocumentsView — enhanced with document viewing/downloading
 * Customers can:
 * - View their documents in a table
 * - See document status (Approved/Rejected/Pending)
 * - Download/Open documents
 * - Search documents by type or filename
 */
public class CustomerDocumentsView {

    private BorderPane root;
    private TableView<Document> table;
    private DocumentController controller;
    private CustomerDAO customerDAO;

    public CustomerDocumentsView() {
        controller = new DocumentController();
        customerDAO = new CustomerDAO();
        root = new BorderPane();
        root.getStyleClass().add("page-content");
        root.setPadding(new Insets(30));

        // Header
        VBox header = new VBox(4);
        Label title = new Label("📄 My Documents");
        title.getStyleClass().add("page-title");
        Label subtitle = new Label("View your uploaded documents, status, and download them");
        subtitle.getStyleClass().add("page-subtitle");
        header.getChildren().addAll(title, subtitle);

        // Toolbar
        HBox toolbar = new HBox(10);
        toolbar.setAlignment(Pos.CENTER_LEFT);
        toolbar.setPadding(new Insets(14, 0, 14, 0));

        TextField searchField = new TextField();
        searchField.setPromptText("🔍  Search by document type or filename...");
        searchField.setPrefWidth(350);
        searchField.getStyleClass().add("search-field");

        toolbar.getChildren().add(searchField);

        // Build table with download button
        table = buildTable();

        // Search listener
        searchField.textProperty().addListener((obs, o, n) -> {
            refreshTable(n);
        });

        VBox content = new VBox(0, header, toolbar, table);
        VBox.setVgrow(table, Priority.ALWAYS);
        root.setCenter(content);

        refreshTable(null);
    }

    private TableView<Document> buildTable() {
        TableView<Document> tv = new TableView<>();
        tv.getStyleClass().add("data-table");

        TableColumn<Document, Integer> idCol = col("Doc ID", "documentId", 60);
        TableColumn<Document, String> typeCol = col("Document Type", "documentType", 140);
        TableColumn<Document, String> fileCol = col("File Name", "fileName", 180);
        TableColumn<Document, String> dateCol = col("Upload Date", "uploadDate", 140);
        
        // Status Column with color coding
        TableColumn<Document, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("verifiedLabel"));
        statusCol.setPrefWidth(110);
        statusCol.setCellFactory(c -> new TableCell<>() {
            @Override protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(item);
                if (!empty && item != null) {
                    setStyle("Approved".equals(item)
                        ? "-fx-text-fill: #27AE60; -fx-font-weight: bold;"
                        : "Rejected".equals(item)
                        ? "-fx-text-fill: #E74C3C; -fx-font-weight: bold;"
                        : "-fx-text-fill: #F39C12; -fx-font-weight: bold;");
                } else setStyle("");
            }
        });

        TableColumn<Document, String> sizeCol = new TableColumn<>("Size");
        sizeCol.setCellValueFactory(new PropertyValueFactory<>("fileSizeKb"));
        sizeCol.setPrefWidth(80);
        sizeCol.setCellFactory(c -> new TableCell<>() {
            @Override protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(item != null ? item + " KB" : "");
            }
        });

        TableColumn<Document, String> notesCol = col("Review Notes", "reviewNotes", 150);

        // Action Column with Download button
        TableColumn<Document, Void> actionCol = new TableColumn<>("Actions");
        actionCol.setPrefWidth(120);
        actionCol.setCellFactory(col -> new TableCell<>() {
            final Button downloadBtn = new Button("⬇️ Download");
            final Button viewBtn = new Button("👁️ View");
            final HBox box = new HBox(6, viewBtn, downloadBtn);
            {
                downloadBtn.getStyleClass().add("primary-btn");
                downloadBtn.setPrefWidth(90);
                viewBtn.getStyleClass().addAll("primary-btn", "btn-info");
                viewBtn.setPrefWidth(60);
                box.setAlignment(Pos.CENTER);
                
                downloadBtn.setOnAction(e -> {
                    Document doc = getTableView().getItems().get(getIndex());
                    handleDownloadDocument(doc);
                });
                
                viewBtn.setOnAction(e -> {
                    Document doc = getTableView().getItems().get(getIndex());
                    DocumentImageViewer.viewDocument(doc);
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : box);
            }
        });

        tv.getColumns().addAll(idCol, typeCol, fileCol, dateCol, statusCol, sizeCol, notesCol, actionCol);
        return tv;
    }

    @SuppressWarnings("unchecked")
    private <T> TableColumn<Document, T> col(String title, String prop, double width) {
        TableColumn<Document, T> c = new TableColumn<>(title);
        c.setCellValueFactory(new PropertyValueFactory<>(prop));
        c.setPrefWidth(width);
        return c;
    }

    private void handleDownloadDocument(Document document) {
        if (document == null) {
            AlertHelper.showError("Error", "No document selected");
            return;
        }

        File file = new File(document.getFilePath());
        if (!file.exists()) {
            AlertHelper.showError("Error", "Document file not found: " + document.getFilePath());
            return;
        }

        try {
            // Open file save dialog
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Download Document");
            fileChooser.setInitialFileName(document.getFileName());
            File saveLocation = fileChooser.showSaveDialog(null);

            if (saveLocation != null) {
                // Copy file to selected location
                java.nio.file.Files.copy(
                    file.toPath(),
                    saveLocation.toPath(),
                    java.nio.file.StandardCopyOption.REPLACE_EXISTING
                );
                AlertHelper.showInfo("Success", "Document downloaded successfully!");
            }
        } catch (Exception e) {
            AlertHelper.showError("Error", "Failed to download document: " + e.getMessage());
        }
    }

    private void handleViewDocument(Document document) {
        if (document == null) {
            AlertHelper.showError("Error", "No document selected");
            return;
        }

        File file = new File(document.getFilePath());
        if (!file.exists()) {
            AlertHelper.showError("Error", "Document file not found: " + document.getFilePath());
            return;
        }

        try {
            // Create a preview dialog showing document info
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Document Preview");
            alert.setHeaderText("Document Information");
            
            String content = "Document ID: " + document.getDocumentId() + "\n" +
                           "Type: " + document.getDocumentType() + "\n" +
                           "File Name: " + document.getFileName() + "\n" +
                           "File Size: " + document.getFileSizeKb() + " KB\n" +
                           "Status: " + document.getVerifiedLabel() + "\n" +
                           "Upload Date: " + document.getUploadDate() + "\n" +
                           "File Path: " + document.getFilePath() + "\n\n" +
                           "Note: Download the document to view it with your system's default application.";
            
            alert.setContentText(content);
            alert.setWidth(500);
            alert.setHeight(400);
            alert.showAndWait();
            
        } catch (Exception e) {
            AlertHelper.showError("Error", "Failed to display document info: " + e.getMessage());
        }
    }

    private void refreshTable(String searchTerm) {
        try {
            // Get current logged-in user's customer record
            int userId = Session.getInstance().getCurrentUser().getUserId();
            Customer customer = customerDAO.getCustomerByUserId(userId);

            if (customer == null) {
                table.getItems().clear();
                return;
            }

            // Get documents for this customer
            List<Document> docs = controller.getDocumentsByCustomer(customer.getCustomerId());

            // Filter by search term if provided
            if (searchTerm != null && !searchTerm.isBlank()) {
                docs = docs.stream()
                    .filter(d -> d.getDocumentType().toLowerCase().contains(searchTerm.toLowerCase())
                        || d.getFileName().toLowerCase().contains(searchTerm.toLowerCase()))
                    .toList();
            }

            table.getItems().setAll(docs);
        } catch (Exception e) {
            System.err.println("Error loading customer documents: " + e.getMessage());
            table.getItems().clear();
        }
    }

    public BorderPane getRoot() {
        return root;
    }
}



