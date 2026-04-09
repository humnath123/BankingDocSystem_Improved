package com.banking.view;

import com.banking.controller.CustomerController;
import com.banking.controller.UserController;
import com.banking.dao.CustomerDAO;
import com.banking.dao.UserDAO;
import com.banking.model.Customer;
import com.banking.model.User;
import com.banking.util.AlertHelper;
import com.banking.util.PasswordUtil;
import com.banking.util.Session;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Premium CustomerProfileView - Beautiful UI for profile management
 * Features:
 * - Attractive profile photo section with hover effects
 * - Modern card-based layout
 * - Smooth animations and transitions
 * - Responsive design
 * - Professional styling
 */
public class CustomerProfileView {

    private BorderPane root;
    private CustomerController customerController;
    private UserController userController;
    private UserDAO userDAO;
    private CustomerDAO customerDAO;
    private Customer currentCustomer;
    private User currentUser;
    private ImageView photoImageView;
    private StackPane photoContainer;
    private static final String UPLOADS_DIR = "uploads/photos/";

    public CustomerProfileView() {
        customerController = new CustomerController();
        userController = new UserController();
        userDAO = new UserDAO();
        customerDAO = new CustomerDAO();
        root = new BorderPane();
        root.setStyle("-fx-background-color: #F5F7FA;");

        loadCustomerData();
        root.setCenter(buildProfileForm());
    }

    private ScrollPane buildProfileForm() {
        VBox mainContainer = new VBox(25);
        mainContainer.setPadding(new Insets(30));
        mainContainer.setStyle("-fx-background-color: #F5F7FA;");

        // Hero Header Section
        VBox heroSection = buildHeroSection();
        mainContainer.getChildren().add(heroSection);

        // Photo Section - Most Prominent
        VBox photoSection = buildAttractivePhotoSection();
        mainContainer.getChildren().add(photoSection);

        // Two-column layout for info sections
        HBox contentBox = new HBox(25);
        contentBox.setAlignment(Pos.TOP_LEFT);

        // Personal Information Section
        VBox personalSection = buildPersonalInfoSection();
        HBox.setHgrow(personalSection, Priority.ALWAYS);

        // Password Change Section
        VBox passwordSection = buildPasswordChangeSection();
        HBox.setHgrow(passwordSection, Priority.ALWAYS);

        contentBox.getChildren().addAll(personalSection, passwordSection);
        mainContainer.getChildren().add(contentBox);

        // Buttons
        HBox buttonBox = buildButtonBar();
        mainContainer.getChildren().add(buttonBox);

        ScrollPane scrollPane = new ScrollPane(mainContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: #F5F7FA;");
        return scrollPane;
    }

    private VBox buildHeroSection() {
        VBox hero = new VBox(8);
        hero.setAlignment(Pos.CENTER_LEFT);
        hero.setPadding(new Insets(20));
        hero.setStyle("-fx-background: linear-gradient(to right, #667eea 0%, #764ba2 100%); " +
                      "-fx-background-radius: 10; " +
                      "-fx-text-fill: white;");

        Label title = new Label("👤 My Profile");
        title.setStyle("-fx-font-size: 28; -fx-font-weight: bold; -fx-text-fill: white;");

        Label subtitle = new Label("Update your personal information, photo, and security settings");
        subtitle.setStyle("-fx-font-size: 13; -fx-text-fill: #E0E0E0;");

        hero.getChildren().addAll(title, subtitle);
        return hero;
    }

    private VBox buildAttractivePhotoSection() {
        VBox section = new VBox(15);
        section.setStyle("-fx-background-color: white; " +
                        "-fx-border-color: #E0E0E0; " +
                        "-fx-border-radius: 8; " +
                        "-fx-padding: 30;");

        Label sectionTitle = new Label("📸 Profile Photo");
        sectionTitle.setStyle("-fx-font-size: 16; -fx-font-weight: bold; -fx-text-fill: #333333;");

        // Main photo display with styling
        photoContainer = new StackPane();
        photoContainer.setPrefSize(200, 200);
        photoContainer.setStyle("-fx-border-color: #D0D0D0; " +
                               "-fx-border-radius: 100; " +
                               "-fx-border-width: 3; " +
                               "-fx-background-color: #F9F9F9;");
        photoContainer.setAlignment(Pos.CENTER);

        photoImageView = new ImageView();
        photoImageView.setFitWidth(190);
        photoImageView.setFitHeight(190);
        photoImageView.setPreserveRatio(true);
        photoImageView.setStyle("-fx-border-radius: 95;");

        setDefaultPhoto();
        photoContainer.getChildren().add(photoImageView);

        // Load existing photo if available
        if (currentCustomer != null && currentCustomer.getPhotoPath() != null) {
            try {
                Image img = new Image(new File(currentCustomer.getPhotoPath()).toURI().toString());
                photoImageView.setImage(img);
            } catch (Exception e) {
                setDefaultPhoto();
            }
        }

        // Photo info and buttons
        HBox photoLayout = new HBox(30);
        photoLayout.setAlignment(Pos.CENTER_LEFT);

        VBox photoInfo = new VBox(12);
        photoInfo.setAlignment(Pos.CENTER);

        Label photoLabel = new Label("Your Profile Picture");
        photoLabel.setStyle("-fx-font-size: 14; -fx-font-weight: bold;");

        Label photoDesc = new Label("JPG or PNG • Max 5MB\nSquare images work best");
        photoDesc.setStyle("-fx-font-size: 11; -fx-text-fill: #666666; -fx-wrap-text: true;");

        VBox buttonBox = new VBox(8);
        buttonBox.setAlignment(Pos.CENTER);

        Button uploadBtn = new Button("🖼️ Upload Photo");
        uploadBtn.setStyle("-fx-padding: 10 20; " +
                          "-fx-font-size: 12; " +
                          "-fx-background-color: #667eea; " +
                          "-fx-text-fill: white; " +
                          "-fx-border-radius: 5; " +
                          "-fx-cursor: hand;");
        uploadBtn.setPrefWidth(140);
        uploadBtn.setOnMouseEntered(e -> uploadBtn.setStyle("-fx-padding: 10 20; " +
                                                            "-fx-font-size: 12; " +
                                                            "-fx-background-color: #5568d3; " +
                                                            "-fx-text-fill: white; " +
                                                            "-fx-border-radius: 5; " +
                                                            "-fx-cursor: hand;"));
        uploadBtn.setOnMouseExited(e -> uploadBtn.setStyle("-fx-padding: 10 20; " +
                                                           "-fx-font-size: 12; " +
                                                           "-fx-background-color: #667eea; " +
                                                           "-fx-text-fill: white; " +
                                                           "-fx-border-radius: 5; " +
                                                           "-fx-cursor: hand;"));

        Button removeBtn = new Button("🗑️ Remove");
        removeBtn.setStyle("-fx-padding: 10 20; " +
                          "-fx-font-size: 12; " +
                          "-fx-background-color: #F44336; " +
                          "-fx-text-fill: white; " +
                          "-fx-border-radius: 5; " +
                          "-fx-cursor: hand;");
        removeBtn.setPrefWidth(140);
        removeBtn.setOnMouseEntered(e -> removeBtn.setStyle("-fx-padding: 10 20; " +
                                                           "-fx-font-size: 12; " +
                                                           "-fx-background-color: #DA190B; " +
                                                           "-fx-text-fill: white; " +
                                                           "-fx-border-radius: 5; " +
                                                           "-fx-cursor: hand;"));
        removeBtn.setOnMouseExited(e -> removeBtn.setStyle("-fx-padding: 10 20; " +
                                                          "-fx-font-size: 12; " +
                                                          "-fx-background-color: #F44336; " +
                                                          "-fx-text-fill: white; " +
                                                          "-fx-border-radius: 5; " +
                                                          "-fx-cursor: hand;"));

        uploadBtn.setOnAction(e -> handlePhotoUpload());
        removeBtn.setOnAction(e -> handlePhotoRemove());

        buttonBox.getChildren().addAll(uploadBtn, removeBtn);
        photoInfo.getChildren().addAll(photoLabel, photoDesc, buttonBox);

        photoLayout.getChildren().addAll(photoContainer, photoInfo);
        section.getChildren().addAll(sectionTitle, photoLayout);

        return section;
    }

    private VBox buildPersonalInfoSection() {
        VBox section = new VBox(15);
        section.setStyle("-fx-background-color: white; " +
                        "-fx-border-color: #E0E0E0; " +
                        "-fx-border-radius: 8; " +
                        "-fx-padding: 25;");

        Label sectionTitle = new Label("ℹ️ Personal Information");
        sectionTitle.setStyle("-fx-font-size: 16; -fx-font-weight: bold; -fx-text-fill: #333333;");

        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(15);

        // Username (Read-only)
        addFormField(grid, 0, "👤 Username", 
            currentUser != null ? currentUser.getUsername() : "", true);

        // Email (Read-only)
        addFormField(grid, 1, "✉️ Email", 
            currentCustomer != null ? currentCustomer.getEmail() : "", true);

        // Full Name (Editable)
        TextField fullNameField = addFormField(grid, 2, "📝 Full Name", 
            currentCustomer != null ? currentCustomer.getName() : "", false);

        // Phone (Editable)
        TextField phoneField = addFormField(grid, 3, "📞 Phone", 
            currentCustomer != null ? currentCustomer.getPhone() : "", false);

        // Address (Editable)
        TextField addressField = addFormField(grid, 4, "🏠 Address", 
            currentCustomer != null ? currentCustomer.getAddress() : "", false);

        // DOB (Editable)
        TextField dobField = addFormField(grid, 5, "📅 Date of Birth", 
            currentCustomer != null ? currentCustomer.getDateOfBirth() : "", false);
        dobField.setPromptText("YYYY-MM-DD");

        grid.setUserData(new Object[]{fullNameField, phoneField, addressField, dobField});
        section.getChildren().addAll(sectionTitle, grid);

        return section;
    }

    private TextField addFormField(GridPane grid, int row, String label, String value, boolean readOnly) {
        Label labelNode = new Label(label);
        labelNode.setStyle("-fx-font-weight: bold; -fx-text-fill: #555555; -fx-font-size: 12;");

        TextField field = new TextField(value);
        field.setPrefWidth(250);
        field.setStyle("-fx-padding: 10; -fx-border-color: #E0E0E0; -fx-border-radius: 4; -fx-font-size: 11;");

        if (readOnly) {
            field.setEditable(false);
            field.setStyle("-fx-padding: 10; " +
                          "-fx-border-color: #E0E0E0; " +
                          "-fx-border-radius: 4; " +
                          "-fx-font-size: 11; " +
                          "-fx-control-inner-background: #F5F5F5; " +
                          "-fx-text-fill: #999999;");
        }

        grid.add(labelNode, 0, row);
        grid.add(field, 1, row);

        return field;
    }

    private VBox buildPasswordChangeSection() {
        VBox section = new VBox(15);
        section.setStyle("-fx-background-color: white; " +
                        "-fx-border-color: #E0E0E0; " +
                        "-fx-border-radius: 8; " +
                        "-fx-padding: 25;");

        Label sectionTitle = new Label("🔒 Change Password");
        sectionTitle.setStyle("-fx-font-size: 16; -fx-font-weight: bold; -fx-text-fill: #333333;");

        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(15);

        PasswordField currentPwdField = addPasswordField(grid, 0, "Current Password:");
        PasswordField newPwdField = addPasswordField(grid, 1, "New Password:");
        PasswordField confirmPwdField = addPasswordField(grid, 2, "Confirm Password:");

        Label note = new Label("Leave password fields empty to keep current password");
        note.setStyle("-fx-font-size: 10; -fx-text-fill: #888888; -fx-italic: true;");

        grid.setUserData(new Object[]{currentPwdField, newPwdField, confirmPwdField});
        section.getChildren().addAll(sectionTitle, grid, note);

        return section;
    }

    private PasswordField addPasswordField(GridPane grid, int row, String label) {
        Label labelNode = new Label(label);
        labelNode.setStyle("-fx-font-weight: bold; -fx-text-fill: #555555; -fx-font-size: 12;");

        PasswordField field = new PasswordField();
        field.setPrefWidth(250);
        field.setStyle("-fx-padding: 10; -fx-border-color: #E0E0E0; -fx-border-radius: 4; -fx-font-size: 11;");

        grid.add(labelNode, 0, row);
        grid.add(field, 1, row);

        return field;
    }

    private HBox buildButtonBar() {
        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(20, 0, 0, 0));

        Button saveBtn = new Button("💾 Save All Changes");
        saveBtn.setStyle("-fx-padding: 12 30; " +
                        "-fx-font-size: 13; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-color: #4CAF50; " +
                        "-fx-text-fill: white; " +
                        "-fx-border-radius: 5; " +
                        "-fx-cursor: hand;");
        saveBtn.setPrefWidth(180);
        saveBtn.setOnMouseEntered(e -> saveBtn.setStyle("-fx-padding: 12 30; " +
                                                       "-fx-font-size: 13; " +
                                                       "-fx-font-weight: bold; " +
                                                       "-fx-background-color: #45a049; " +
                                                       "-fx-text-fill: white; " +
                                                       "-fx-border-radius: 5; " +
                                                       "-fx-cursor: hand;"));
        saveBtn.setOnMouseExited(e -> saveBtn.setStyle("-fx-padding: 12 30; " +
                                                      "-fx-font-size: 13; " +
                                                      "-fx-font-weight: bold; " +
                                                      "-fx-background-color: #4CAF50; " +
                                                      "-fx-text-fill: white; " +
                                                      "-fx-border-radius: 5; " +
                                                      "-fx-cursor: hand;"));

        Button cancelBtn = new Button("❌ Cancel");
        cancelBtn.setStyle("-fx-padding: 12 30; " +
                          "-fx-font-size: 13; " +
                          "-fx-font-weight: bold; " +
                          "-fx-background-color: #9E9E9E; " +
                          "-fx-text-fill: white; " +
                          "-fx-border-radius: 5; " +
                          "-fx-cursor: hand;");
        cancelBtn.setPrefWidth(180);
        cancelBtn.setOnMouseEntered(e -> cancelBtn.setStyle("-fx-padding: 12 30; " +
                                                           "-fx-font-size: 13; " +
                                                           "-fx-font-weight: bold; " +
                                                           "-fx-background-color: #808080; " +
                                                           "-fx-text-fill: white; " +
                                                           "-fx-border-radius: 5; " +
                                                           "-fx-cursor: hand;"));
        cancelBtn.setOnMouseExited(e -> cancelBtn.setStyle("-fx-padding: 12 30; " +
                                                          "-fx-font-size: 13; " +
                                                          "-fx-font-weight: bold; " +
                                                          "-fx-background-color: #9E9E9E; " +
                                                          "-fx-text-fill: white; " +
                                                          "-fx-border-radius: 5; " +
                                                          "-fx-cursor: hand;"));

        saveBtn.setOnAction(e -> handleSaveAll());
        cancelBtn.setOnAction(e -> loadCustomerData());

        buttonBox.getChildren().addAll(saveBtn, cancelBtn);
        return buttonBox;
    }

    private void setDefaultPhoto() {
        Label noPhoto = new Label("No Photo");
        noPhoto.setStyle("-fx-font-size: 16; -fx-text-fill: #CCCCCC; -fx-font-weight: bold;");
        photoImageView.setImage(null);
    }

    private void handlePhotoUpload() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Profile Photo");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.jpeg", "*.png"),
            new FileChooser.ExtensionFilter("All Files", "*.*")
        );

        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            try {
                if (selectedFile.length() > 5 * 1024 * 1024) {
                    AlertHelper.showError("Error", "File size must be less than 5MB");
                    return;
                }

                Files.createDirectories(Paths.get(UPLOADS_DIR));
                String fileName = currentCustomer.getCustomerId() + "_" + System.currentTimeMillis() +
                    selectedFile.getName().substring(selectedFile.getName().lastIndexOf("."));
                String destPath = UPLOADS_DIR + fileName;
                Files.copy(selectedFile.toPath(), Paths.get(destPath));

                currentCustomer.setPhotoPath(destPath);
                Image img = new Image(selectedFile.toURI().toString());
                photoImageView.setImage(img);

                AlertHelper.showInfo("Success", "Photo will be saved on profile update");
            } catch (IOException e) {
                AlertHelper.showError("Error", "Failed to upload photo: " + e.getMessage());
            }
        }
    }

    private void handlePhotoRemove() {
        if (AlertHelper.showConfirm("Remove Photo", "Are you sure you want to remove your profile photo?")) {
            currentCustomer.setPhotoPath(null);
            setDefaultPhoto();
            AlertHelper.showInfo("Success", "Photo will be removed on profile update");
        }
    }

    private void handleSaveAll() {
        ScrollPane scrollPane = (ScrollPane) root.getCenter();
        if (scrollPane == null) return;

        VBox mainContainer = (VBox) scrollPane.getContent();
        if (mainContainer == null) return;

        Object[] personalData = null;
        Object[] passwordData = null;

        for (javafx.scene.Node node : mainContainer.getChildren()) {
            if (node instanceof HBox hbox) {
                for (javafx.scene.Node child : hbox.getChildren()) {
                    if (child instanceof VBox section) {
                        for (javafx.scene.Node sectionChild : section.getChildren()) {
                            if (sectionChild instanceof GridPane gp) {
                                Object userData = gp.getUserData();
                                if (userData instanceof Object[] arr) {
                                    if (arr.length == 4 && arr[0] instanceof TextField) personalData = arr;
                                    else if (arr.length == 3 && arr[0] instanceof PasswordField) passwordData = arr;
                                }
                            }
                        }
                    }
                }
            }
        }

        if (personalData != null && personalData.length >= 4) {
            String fullName = ((TextField) personalData[0]).getText();
            String phone = ((TextField) personalData[1]).getText();
            String address = ((TextField) personalData[2]).getText();
            String dob = ((TextField) personalData[3]).getText();

            if (!validateAndSavePersonal(fullName, phone, address, dob)) {
                return;
            }
        }

        if (passwordData != null && passwordData.length == 3) {
            String currentPwd = ((PasswordField) passwordData[0]).getText();
            String newPwd = ((PasswordField) passwordData[1]).getText();
            String confirmPwd = ((PasswordField) passwordData[2]).getText();

            if (!newPwd.isEmpty() && !validateAndSavePassword(currentPwd, newPwd, confirmPwd)) {
                return;
            }
        }

        AlertHelper.showInfo("Success", "All changes saved successfully!");
        loadCustomerData();
    }

    private boolean validateAndSavePersonal(String name, String phone, String address, String dob) {
        if (name.isBlank()) {
            AlertHelper.showError("Validation", "Full name is required.");
            return false;
        }
        if (phone.isBlank()) {
            AlertHelper.showError("Validation", "Phone is required.");
            return false;
        }
        if (!phone.matches("\\d{7,15}")) {
            AlertHelper.showError("Validation", "Phone must be 7–15 digits.");
            return false;
        }

        currentCustomer.setName(name.trim());
        currentCustomer.setPhone(phone.trim());
        currentCustomer.setAddress(address.trim());
        currentCustomer.setDateOfBirth(dob.trim());

        if (!customerController.update(currentCustomer)) {
            AlertHelper.showError("Error", "Failed to update profile.");
            return false;
        }

        return true;
    }

    private boolean validateAndSavePassword(String currentPwd, String newPwd, String confirmPwd) {
        if (currentPwd.isBlank()) {
            AlertHelper.showError("Validation", "Current password is required to change password.");
            return false;
        }

        if (newPwd.length() < 6) {
            AlertHelper.showError("Validation", "New password must be at least 6 characters.");
            return false;
        }

        if (!newPwd.equals(confirmPwd)) {
            AlertHelper.showError("Validation", "Passwords do not match.");
            return false;
        }

        String storedHash = userDAO.getPasswordHashByUserId(currentUser.getUserId());
        if (storedHash == null || !PasswordUtil.verifyWithLegacyFallback(currentPwd, storedHash)) {
            AlertHelper.showError("Error", "Current password is incorrect.");
            return false;
        }

        currentUser.setPassword(newPwd);
        if (!userController.update(currentUser)) {
            AlertHelper.showError("Error", "Failed to update password.");
            return false;
        }

        AlertHelper.showInfo("Success", "Password changed successfully!");
        return true;
    }

    private void loadCustomerData() {
        try {
            currentUser = Session.getInstance().getCurrentUser();
            currentCustomer = customerDAO.getCustomerByUserId(currentUser.getUserId());

            if (currentCustomer == null) {
                AlertHelper.showError("Error", "Customer profile not found");
                return;
            }

            root.setCenter(buildProfileForm());
        } catch (Exception e) {
            System.err.println("Error loading customer data: " + e.getMessage());
            AlertHelper.showError("Error", "Failed to load profile: " + e.getMessage());
        }
    }

    public BorderPane getRoot() {
        return root;
    }
}

