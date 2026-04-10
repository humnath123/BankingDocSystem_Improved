package com.banking.view;

import com.banking.model.Document;
import com.banking.util.AlertHelper;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;

/**
 * Document Image Viewer - allows customers to preview document images
 * Supports: JPG, PNG, PDF (thumbnail), and other image formats
 */
public class DocumentImageViewer {

    public static void viewDocument(Document document) {
        if (document == null) {
            AlertHelper.showError("Error", "No document selected");
            return;
        }

        File file = new File(document.getFilePath());
        if (!file.exists()) {
            AlertHelper.showError("Error", "Document file not found");
            return;
        }

        try {
            // Create viewer window
            Stage viewerStage = new Stage();
            viewerStage.setTitle("Document Viewer - " + document.getFileName());
            viewerStage.setWidth(800);
            viewerStage.setHeight(600);

            VBox root = new VBox(15);
            root.setPadding(new Insets(20));
            root.setStyle("-fx-background-color: #FFFFFF;");

            // Header with document info
            VBox header = createHeader(document);
            root.getChildren().add(header);

            // Image viewer area
            VBox viewerArea = createViewerArea(file, document);
            VBox.setVgrow(viewerArea, javafx.scene.layout.Priority.ALWAYS);
            root.getChildren().add(viewerArea);

            // Buttons
            VBox buttons = createButtonBar(viewerStage, file);
            root.getChildren().add(buttons);

            javafx.scene.Scene scene = new javafx.scene.Scene(root);
            viewerStage.setScene(scene);
            viewerStage.show();

        } catch (Exception e) {
            AlertHelper.showError("Error", "Failed to open viewer: " + e.getMessage());
        }
    }

    private static VBox createHeader(Document document) {
        VBox header = new VBox(6);
        header.setStyle("-fx-border-color: #E0E0E0; -fx-border-radius: 5; -fx-padding: 12; -fx-background-color: #F5F5F5;");

        Label title = new Label("📄 " + document.getFileName());
        title.setStyle("-fx-font-size: 14; -fx-font-weight: bold;");

        String info = String.format("Type: %s | Size: %s KB | Status: %s | Date: %s",
            document.getDocumentType(),
            document.getFileSizeKb() != null ? document.getFileSizeKb() : "Unknown",
            document.getVerifiedLabel(),
            document.getUploadDate());

        Label details = new Label(info);
        details.setStyle("-fx-font-size: 11; -fx-text-fill: #666666;");

        if (document.getReviewNotes() != null && !document.getReviewNotes().isEmpty()) {
            Label notes = new Label("Notes: " + document.getReviewNotes());
            notes.setStyle("-fx-font-size: 11; -fx-text-fill: #E74C3C; -fx-wrap-text: true;");
            header.getChildren().addAll(title, details, notes);
        } else {
            header.getChildren().addAll(title, details);
        }

        return header;
    }

    private static VBox createViewerArea(File file, Document document) {
        VBox area = new VBox();
        area.setAlignment(Pos.CENTER);
        area.setStyle("-fx-border-color: #D0D0D0; -fx-border-radius: 5; -fx-background-color: #EEEEEE;");
        area.setPadding(new Insets(10));

        String fileName = file.getName().toLowerCase();

        try {
            if (fileName.endsWith(".pdf")) {
                // PDF handling
                Label pdfLabel = new Label("📄 PDF Document");
                pdfLabel.setStyle("-fx-font-size: 14; -fx-font-weight: bold;");
                
                Label pdfNote = new Label("PDF preview not available.\nClick 'Download' to view the full document.");
                pdfNote.setStyle("-fx-font-size: 12; -fx-text-fill: #666666; -fx-text-alignment: center;");
                
                area.getChildren().addAll(pdfLabel, pdfNote);

            } else if (fileName.endsWith(".txt") || fileName.endsWith(".log") || fileName.endsWith(".md")) {
                // Text file handling
                String content = java.nio.file.Files.readString(file.toPath());
                TextArea textArea = new TextArea(content);
                textArea.setEditable(false);
                textArea.setWrapText(true);
                textArea.setPrefWidth(700);
                textArea.setPrefHeight(500);
                textArea.setStyle("-fx-font-family: 'Consolas', 'Monaco', monospace; -fx-font-size: 12;");

                area.getChildren().add(textArea);

            } else if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") || 
                      fileName.endsWith(".png") || fileName.endsWith(".gif") || 
                      fileName.endsWith(".bmp")) {
                // Image handling
                try (FileInputStream fis = new FileInputStream(file)) {
                    Image image = new Image(fis);
                    ImageView imageView = new ImageView(image);
                    imageView.setPreserveRatio(true);
                    imageView.setFitWidth(700);
                    imageView.setFitHeight(500);

                    area.getChildren().add(imageView);
                }
            } else {
                // Unsupported format
                Label unsupported = new Label("📦 Unsupported Format");
                unsupported.setStyle("-fx-font-size: 14; -fx-font-weight: bold;");
                
                Label note = new Label("Preview not available for this file type.\nClick 'Download' to view the document.");
                note.setStyle("-fx-font-size: 12; -fx-text-fill: #666666; -fx-text-alignment: center;");
                
                area.getChildren().addAll(unsupported, note);
            }
        } catch (Exception e) {
            Label error = new Label("❌ Error Loading Document");
            error.setStyle("-fx-font-size: 14; -fx-font-weight: bold; -fx-text-fill: #E74C3C;");
            
            Label errorMsg = new Label(e.getMessage());
            errorMsg.setStyle("-fx-font-size: 11; -fx-text-fill: #666666;");
            
            area.getChildren().addAll(error, errorMsg);
        }

        return area;
    }

    private static VBox createButtonBar(Stage stage, File file) {
        VBox buttonBox = new VBox(10);
        buttonBox.setAlignment(Pos.CENTER);

        Button downloadBtn = new Button("⬇️ Download File");
        downloadBtn.getStyleClass().add("primary-btn");
        downloadBtn.setPrefWidth(150);
        downloadBtn.setOnAction(e -> {
            javafx.stage.FileChooser fileChooser = new javafx.stage.FileChooser();
            fileChooser.setTitle("Save Document");
            fileChooser.setInitialFileName(file.getName());
            File saveLocation = fileChooser.showSaveDialog(stage);

            if (saveLocation != null) {
                try {
                    java.nio.file.Files.copy(
                        file.toPath(),
                        saveLocation.toPath(),
                        java.nio.file.StandardCopyOption.REPLACE_EXISTING
                    );
                    AlertHelper.showInfo("Success", "Document saved successfully!");
                } catch (Exception ex) {
                    AlertHelper.showError("Error", "Failed to save: " + ex.getMessage());
                }
            }
        });

        Button closeBtn = new Button("❌ Close");
        closeBtn.getStyleClass().addAll("primary-btn", "btn-secondary");
        closeBtn.setPrefWidth(150);
        closeBtn.setOnAction(e -> stage.close());

        buttonBox.getChildren().addAll(downloadBtn, closeBtn);
        return buttonBox;
    }
}

