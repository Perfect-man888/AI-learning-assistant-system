package com.scms.learningassistantclient.view;

import com.scms.learningassistantclient.HelloApplication;
import com.scms.learningassistantclient.api.AuthApi;
import com.scms.learningassistantclient.model.LoginUser;
import com.scms.learningassistantclient.model.RegisterRequest;
import com.scms.learningassistantclient.util.AppContext;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class RegisterView {

    private final VBox root = new VBox();
    private final AuthApi authApi = new AuthApi();

    public RegisterView() {
        createView();
    }

    private void createView() {
        root.setSpacing(14);
        root.setPadding(new Insets(30));
        root.setAlignment(Pos.CENTER);

        Label titleLabel = new Label("用户注册");
        titleLabel.setFont(new Font(24));

        TextField usernameField = new TextField();
        usernameField.setPromptText("请输入用户名");
        usernameField.setMaxWidth(280);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("请输入密码，至少6位");
        passwordField.setMaxWidth(280);

        TextField realNameField = new TextField();
        realNameField.setPromptText("请输入真实姓名");
        realNameField.setMaxWidth(280);

        TextField classNameField = new TextField();
        classNameField.setPromptText("请输入班级，例如：软件2301班");
        classNameField.setMaxWidth(280);

        ComboBox<String> roleComboBox = new ComboBox<>();
        roleComboBox.getItems().addAll("student", "teacher");
        roleComboBox.setValue("student");
        roleComboBox.setMaxWidth(280);

        Button registerButton = new Button("注册");
        registerButton.setMaxWidth(280);

        Button backButton = new Button("返回登录");
        backButton.setMaxWidth(280);

        Label messageLabel = new Label();
        messageLabel.setStyle("-fx-text-fill: red;");

        registerButton.setOnAction(event -> {
            String username = usernameField.getText() == null ? "" : usernameField.getText().trim();
            String password = passwordField.getText() == null ? "" : passwordField.getText().trim();
            String realName = realNameField.getText() == null ? "" : realNameField.getText().trim();
            String className = classNameField.getText() == null ? "" : classNameField.getText().trim();
            String role = roleComboBox.getValue();

            if (username.isBlank()) {
                messageLabel.setText("请输入用户名");
                return;
            }

            if (password.isBlank()) {
                messageLabel.setText("请输入密码");
                return;
            }

            if (realName.isBlank()) {
                messageLabel.setText("请输入真实姓名");
                return;
            }

            if (role == null || role.isBlank()) {
                messageLabel.setText("请选择身份");
                return;
            }

            registerButton.setDisable(true);
            backButton.setDisable(true);
            messageLabel.setStyle("-fx-text-fill: #333333;");
            messageLabel.setText("正在注册，请稍候...");

            RegisterRequest request = new RegisterRequest(
                    username,
                    password,
                    realName,
                    role,
                    className
            );

            new Thread(() -> {
                try {
                    LoginUser user = authApi.register(request);
                    AppContext.setCurrentUser(user);

                    Platform.runLater(() -> {
                        if ("teacher".equals(user.getRole())) {
                            HelloApplication.showTeacherHomeView();
                        } else {
                            HelloApplication.showStudentHomeView();
                        }
                    });
                } catch (Exception e) {
                    Platform.runLater(() -> {
                        messageLabel.setStyle("-fx-text-fill: red;");
                        messageLabel.setText("注册失败：" + e.getMessage());
                        registerButton.setDisable(false);
                        backButton.setDisable(false);
                    });
                }
            }).start();
        });

        backButton.setOnAction(event -> HelloApplication.showLoginView());

        root.getChildren().addAll(
                titleLabel,
                usernameField,
                passwordField,
                realNameField,
                classNameField,
                roleComboBox,
                registerButton,
                backButton,
                messageLabel
        );
    }

    public Parent getView() {
        return root;
    }
}