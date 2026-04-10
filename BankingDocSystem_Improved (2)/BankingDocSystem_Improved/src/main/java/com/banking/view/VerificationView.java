package com.banking.view;

import com.banking.controller.VerificationController;
import com.banking.service.VerificationService;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * VerificationView - Two-step authentication screen for Admin and Staff users
 * Features modern card layout, passkey input, and attempt tracking
 */
public class VerificationView {

    private BorderPane root;
    private PasswordField passkeyField;
    private Label errorLabel;
    private Label attemptsLabel;
    private Button verifyBtn;

    public VerificationView(Stage stage) {
        root = new BorderPane();
        root.getStyleClass().add("login-root");

        // Add animated background effect
        addAnimatedBackground();

        // ── Left: branding panel ─────────────────────────────
        VBox brandPanel = createBrandPanel();
        root.setLeft(brandPanel);

        // ── Right: verification form ─────────────────────────
        VBox formPanel = createVerificationForm(stage);
        root.setCenter(formPanel);

        // Add entrance animations
        addEntranceAnimations(brandPanel, formPanel);
    }

    private void addAnimatedBackground() {
        // Create animated background rectangles for visual effect
        Rectangle bgRect1 = new Rectangle(200, 200);
        bgRect1.setFill(Color.web("#ff6b6b"));
        bgRect1.setOpacity(0.03);
        bgRect1.setArcWidth(50);
        bgRect1.setArcHeight(50);

        Rectangle bgRect2 = new Rectangle(150, 150);
        bgRect2.setFill(Color.web("#4ecdc4"));
        bgRect2.setOpacity(0.03);
        bgRect2.setArcWidth(75);
        bgRect2.setArcHeight(75);

        Rectangle bgRect3 = new Rectangle(100, 100);
        bgRect3.setFill(Color.web("#45b7d1"));
        bgRect3.setOpacity(0.03);
        bgRect3.setArcWidth(25);
        bgRect3.setArcHeight(25);

        Pane backgroundPane = new Pane(bgRect1, bgRect2, bgRect3);
        backgroundPane.setMouseTransparent(true);

        // Position the background elements
        bgRect1.setLayoutX(100);
        bgRect1.setLayoutY(100);
        bgRect2.setLayoutX(600);
        bgRect2.setLayoutY(300);
        bgRect3.setLayoutX(300);
        bgRect3.setLayoutY(500);

        root.getChildren().add(0, backgroundPane);
    }

    private VBox createBrandPanel() {
        VBox brandPanel = new VBox(20);
        brandPanel.getStyleClass().add("brand-panel");
        brandPanel.setAlignment(Pos.CENTER);
        brandPanel.setPrefWidth(380);

        Text securityIcon = new Text("🔐");
        securityIcon.setStyle("-fx-font-size: 72px;");
        securityIcon.getStyleClass().add("fade-in");

        Label bankName = new Label("Two-Step\nVerification");
        bankName.getStyleClass().add("brand-title");
        bankName.setAlignment(Pos.CENTER);

        Label tagline = new Label("Enhanced Security for Administrative Access");
        tagline.getStyleClass().add("brand-tagline");

        Label roleLabel = new Label("Admin & Staff Verification Required");
        roleLabel.getStyleClass().add("brand-course");

        brandPanel.getChildren().addAll(securityIcon, bankName, tagline, roleLabel);
        return brandPanel;
    }

    private VBox createVerificationForm(Stage stage) {
        VBox formPanel = new VBox(18);
        formPanel.getStyleClass().add("login-form-panel");
        formPanel.setAlignment(Pos.CENTER);
        formPanel.setPadding(new Insets(60, 60, 60, 60));
        formPanel.getStyleClass().add("slide-up");

        // Main card container
        VBox card = new VBox(20);
        card.getStyleClass().add("verification-card");
        card.setPadding(new Insets(30));
        card.setMaxWidth(400);
        card.setAlignment(Pos.CENTER);

        Label verificationTitle = new Label("🔐 Security Verification");
        verificationTitle.getStyleClass().add("login-title");

        Label verificationSubtitle = new Label("Enter your administrative passkey to continue");
        verificationSubtitle.getStyleClass().add("login-subtitle");

        // Passkey input section
        VBox inputSection = new VBox(12);
        inputSection.setAlignment(Pos.CENTER_LEFT);

        Label passkeyLabel = new Label("Administrative Passkey");
        passkeyLabel.getStyleClass().add("field-label");

        passkeyField = new PasswordField();
        passkeyField.setPromptText("Enter 10-digit passkey");
        passkeyField.getStyleClass().add("login-field");
        passkeyField.setMaxWidth(300);

        // Attempts indicator
        attemptsLabel = new Label("Attempts remaining: 3");
        attemptsLabel.getStyleClass().add("attempts-label");
        attemptsLabel.setStyle("-fx-text-fill: #27ae60; -fx-font-weight: bold;");

        inputSection.getChildren().addAll(passkeyLabel, passkeyField, attemptsLabel);

        // Error message
        errorLabel = new Label();
        errorLabel.getStyleClass().add("error-label");
        errorLabel.setWrapText(true);
        errorLabel.setVisible(false);
        errorLabel.setMaxWidth(300);

        // Buttons
        verifyBtn = new Button("🔓 Verify & Continue");
        verifyBtn.getStyleClass().add("login-btn");
        verifyBtn.setPrefWidth(300);

        Button backBtn = new Button("⬅️ Back to Login");
        backBtn.getStyleClass().add("btn-secondary");
        backBtn.setPrefWidth(300);
        backBtn.setOnAction(e -> navigateBackToLogin(stage));

        // Security info box
        VBox securityBox = new VBox(8);
        securityBox.getStyleClass().add("security-info-box");
        securityBox.setPadding(new Insets(15));
        securityBox.setMaxWidth(300);

        Label securityTitle = new Label("🔒 Security Information");
        securityTitle.setStyle("-fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        Label securityInfo = new Label(
            "• Maximum 3 verification attempts\n" +
            "• Passkey is required for Admin/Staff access\n" +
            "• Contact administrator if access is blocked"
        );
        securityInfo.setStyle("-fx-font-size: 11px; -fx-text-fill: #7f8c8d;");
        securityInfo.setWrapText(true);

        securityBox.getChildren().addAll(securityTitle, securityInfo);

        card.getChildren().addAll(
            verificationTitle, verificationSubtitle,
            inputSection, errorLabel, verifyBtn, backBtn, securityBox
        );

        formPanel.getChildren().add(card);

        // ── Wire controller ──────────────────────────────────
        VerificationController controller = new VerificationController(stage);

        Runnable doVerification = () -> {
            String passkey = passkeyField.getText();
            VerificationService.VerificationResult result = controller.verifyPasskey(passkey);

            if (result.isSuccess()) {
                errorLabel.setVisible(false);
                attemptsLabel.setText("Verification successful!");
                attemptsLabel.setStyle("-fx-text-fill: #27ae60; -fx-font-weight: bold;");
                // Add success animation
                addSuccessAnimation(verifyBtn);
            } else {
                errorLabel.setText(result.getMessage());
                errorLabel.setVisible(true);

                // Update attempts label
                int remaining = controller.getRemainingAttempts();
                if (remaining > 0) {
                    attemptsLabel.setText("Attempts remaining: " + remaining);
                    attemptsLabel.setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold;");
                } else {
                    attemptsLabel.setText("Access blocked");
                    attemptsLabel.setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold;");
                    verifyBtn.setDisable(true);
                }

                // Add shake animation for error
                addShakeAnimation(card);
            }
        };

        verifyBtn.setOnAction(e -> doVerification.run());
        passkeyField.setOnAction(e -> doVerification.run()); // Enter key triggers verification

        return formPanel;
    }

    private void navigateBackToLogin(Stage stage) {
        LoginView loginView = new LoginView(stage);
        Scene loginScene = new Scene(loginView.getRoot(), 1200, 700);
        loginScene.getStylesheets().add(
            getClass().getResource("/com/banking/css/style.css").toExternalForm());
        stage.setScene(loginScene);
        stage.setWidth(1200);
        stage.setHeight(700);
        stage.centerOnScreen();
    }

    private void addEntranceAnimations(VBox brandPanel, VBox formPanel) {
        // Fade in brand panel
        FadeTransition brandFade = new FadeTransition(Duration.seconds(1), brandPanel);
        brandFade.setFromValue(0);
        brandFade.setToValue(1);
        brandFade.setDelay(Duration.seconds(0.2));
        brandFade.play();

        // Slide up form panel
        formPanel.setTranslateY(50);
        formPanel.setOpacity(0);

        FadeTransition formFade = new FadeTransition(Duration.seconds(1), formPanel);
        formFade.setFromValue(0);
        formFade.setToValue(1);
        formFade.setDelay(Duration.seconds(0.5));

        javafx.animation.TranslateTransition formSlide = new javafx.animation.TranslateTransition(Duration.seconds(0.8), formPanel);
        formSlide.setFromY(50);
        formSlide.setToY(0);
        formSlide.setDelay(Duration.seconds(0.5));

        formFade.play();
        formSlide.play();
    }

    private void addShakeAnimation(VBox card) {
        javafx.animation.TranslateTransition shake = new javafx.animation.TranslateTransition(Duration.seconds(0.1), card);
        shake.setFromX(0);
        shake.setByX(10);
        shake.setCycleCount(6);
        shake.setAutoReverse(true);
        shake.play();
    }

    private void addSuccessAnimation(Button verifyBtn) {
        ScaleTransition scale = new ScaleTransition(Duration.seconds(0.2), verifyBtn);
        scale.setFromX(1);
        scale.setFromY(1);
        scale.setToX(1.05);
        scale.setToY(1.05);
        scale.setCycleCount(2);
        scale.setAutoReverse(true);
        scale.play();
    }

    public BorderPane getRoot() { return root; }
}
