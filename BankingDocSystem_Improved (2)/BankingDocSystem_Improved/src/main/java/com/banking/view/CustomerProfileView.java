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
import com.banking.util.ValidationUtil;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.scene.shape.Circle;

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

    private final BorderPane root;
    private final CustomerController customerController;
    private final UserController userController;
    private final UserDAO userDAO;
    private final CustomerDAO customerDAO;
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
        mainContainer.getStyleClass().add("profile-card");

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
        scrollPane.getStyleClass().add("scroll-pane");
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
                               "-fx-background-color: #F9F9F9; " +
                               "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 5);");
        photoContainer.setAlignment(Pos.CENTER);

        photoImageView = new ImageView();
        photoImageView.setFitWidth(190);
        photoImageView.setFitHeight(190);
        photoImageView.setPreserveRatio(true);
        photoImageView.setClip(new Circle(95, 95, 95));

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
        uploadBtn.getStyleClass().add("action-btn-primary");
        uploadBtn.setPrefWidth(140);

        Button removeBtn = new Button("🗑️ Remove");
        removeBtn.getStyleClass().add("action-btn-close");
        removeBtn.setPrefWidth(140);

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
        section.getStyleClass().add("profile-section");
        section.setPadding(new Insets(25));

        Label sectionTitle = new Label("ℹ️ Personal Information");
        sectionTitle.getStyleClass().add("profile-section-title");

        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(15);

        // Username (Read-only)
        addFormField(grid, 0, "👤 Username", 
            currentUser != null ? currentUser.getUsername() : "", true);

        // Email (Editable)
        TextField emailField = addFormField(grid, 1, "✉️ Email", 
            currentCustomer != null ? currentCustomer.getEmail() : "", false);

        // Full Name (Editable)
        TextField fullNameField = addFormField(grid, 2, "📝 Full Name", 
            currentCustomer != null ? currentCustomer.getName() : "", false);

        // Phone (Editable)
        TextField phoneField = addFormField(grid, 3, "📞 Phone", 
            currentCustomer != null ? currentCustomer.getPhone() : "", false);

        // Address (Editable)
        TextField addressField = addFormField(grid, 4, "🏠 Address", 
            currentCustomer != null ? currentCustomer.getAddress() : "", false);

        // Store fields for validation
        grid.setUserData(new Object[]{emailField, fullNameField, phoneField, addressField});
        section.getChildren().addAll(sectionTitle, grid);

        return section;
    }

    private TextField addFormField(GridPane grid, int row, String label, String value, boolean readOnly) {
        Label labelNode = new Label(label);
        labelNode.getStyleClass().add("field-label");

        TextField field = new TextField(value);
        field.getStyleClass().add("text-field");
        field.setPrefWidth(250);

        if (readOnly) {
            field.setEditable(false);
            field.getStyleClass().add("read-only");
        }

        grid.add(labelNode, 0, row);
        grid.add(field, 1, row);

        return field;
    }

    private VBox buildPasswordChangeSection() {
        VBox section = new VBox(15);
        section.getStyleClass().add("profile-section");
        section.setPadding(new Insets(25));

        Label sectionTitle = new Label("🔒 Change Password");
        sectionTitle.getStyleClass().add("profile-section-title");

        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(15);

        HBox currentPwdContainer = addPasswordFieldWithToggle(grid, 0, "Current Password:");
        HBox newPwdContainer = addPasswordFieldWithToggle(grid, 1, "New Password:");
        HBox confirmPwdContainer = addPasswordFieldWithToggle(grid, 2, "Confirm Password:");

        Label note = new Label("Leave password fields empty to keep current password");
        note.setStyle("-fx-font-size: 10; -fx-text-fill: #888888; -fx-italic: true;");

        grid.setUserData(new Object[]{currentPwdContainer, newPwdContainer, confirmPwdContainer});
        section.getChildren().addAll(sectionTitle, grid, note);

        return section;
    }

    private HBox addPasswordFieldWithToggle(GridPane grid, int row, String label) {
        Label labelNode = new Label(label);
        labelNode.getStyleClass().add("field-label");

        HBox container = new HBox(10);
        container.getStyleClass().add("password-field-container");

        PasswordField passwordField = new PasswordField();
        passwordField.getStyleClass().add("password-field");
        passwordField.setPrefWidth(220);

        TextField textField = new TextField();
        textField.getStyleClass().add("text-field");
        textField.setPrefWidth(220);
        textField.setManaged(false);
        textField.setVisible(false);

        // Bind text properties
        passwordField.textProperty().bindBidirectional(textField.textProperty());

        Button toggleBtn = new Button("👁️");
        toggleBtn.getStyleClass().add("button");
        toggleBtn.setOnAction(e -> {
            boolean show = textField.isVisible();
            textField.setVisible(!show);
            textField.setManaged(!show);
            passwordField.setVisible(show);
            passwordField.setManaged(show);
            toggleBtn.setText(show ? "👁️" : "🙈");
        });

        container.getChildren().addAll(passwordField, textField, toggleBtn);

        grid.add(labelNode, 0, row);
        grid.add(container, 1, row);

        return container;
    }

    private HBox buildButtonBar() {
        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(20, 0, 0, 0));

        Button saveBtn = new Button("💾 Save All Changes");
        saveBtn.getStyleClass().add("btn-success");
        saveBtn.setPrefWidth(180);
        saveBtn.setOnAction(e -> handleSaveAll());

        Button cancelBtn = new Button("❌ Cancel");
        cancelBtn.getStyleClass().add("btn-secondary");
        cancelBtn.setPrefWidth(180);
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
                                    else if (arr.length == 3 && arr[0] instanceof HBox) passwordData = arr;
                                }
                            }
                        }
                    }
                }
            }
        }

        if (personalData != null && personalData.length >= 4) {
            String email = ((TextField) personalData[0]).getText();
            String fullName = ((TextField) personalData[1]).getText();
            String phone = ((TextField) personalData[2]).getText();
            String address = ((TextField) personalData[3]).getText();

            if (!validateAndSavePersonal(email, fullName, phone, address)) {
                return;
            }
        }

        if (passwordData != null && passwordData.length == 3) {
            HBox currentContainer = (HBox) passwordData[0];
            HBox newContainer = (HBox) passwordData[1];
            HBox confirmContainer = (HBox) passwordData[2];

            PasswordField currentPwdField = (PasswordField) currentContainer.getChildren().getFirst();
            PasswordField newPwdField = (PasswordField) newContainer.getChildren().getFirst();
            PasswordField confirmPwdField = (PasswordField) confirmContainer.getChildren().getFirst();

            String currentPwd = currentPwdField.getText();
            String newPwd = newPwdField.getText();
            String confirmPwd = confirmPwdField.getText();

            if (!newPwd.isEmpty() && !validateAndSavePassword(currentPwd, newPwd, confirmPwd)) {
                return;
            }
        }

        AlertHelper.showInfo("Success", "All changes saved successfully!");
        loadCustomerData();
    }

    private boolean validateAndSavePersonal(String email, String name, String phone, String address) {
        // Validate name (no numbers allowed)
        String nameErr = ValidationUtil.validateName(name);
        if (nameErr != null) {
            AlertHelper.showError("Validation", nameErr);
            return false;
        }
        
        // Validate email
        String emailErr = ValidationUtil.validateEmail(email);
        if (emailErr != null) {
            AlertHelper.showError("Validation", emailErr);
            return false;
        }
        
        // Validate phone (numeric, exactly 10 characters)
        String phoneErr = ValidationUtil.validatePhone(phone);
        if (phoneErr != null) {
            AlertHelper.showError("Validation", phoneErr);
            return false;
        }
        
        // Validate address
        String addressErr = ValidationUtil.validateAddress(address);
        if (addressErr != null) {
            AlertHelper.showError("Validation", addressErr);
            return false;
        }

        currentCustomer.setEmail(email.trim());
        currentCustomer.setName(name.trim());
        currentCustomer.setPhone(phone.trim());
        currentCustomer.setAddress(address.trim());

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
