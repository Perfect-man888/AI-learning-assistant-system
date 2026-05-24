package com.scms.learningassistantclient.view;

import com.scms.learningassistantclient.HelloApplication;
import com.scms.learningassistantclient.api.AuthApi;
import com.scms.learningassistantclient.model.LoginRequest;
import com.scms.learningassistantclient.model.LoginUser;
import com.scms.learningassistantclient.util.AppContext;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class LoginView {

    private final VBox root = new VBox();
    private final AuthApi authApi = new AuthApi();

    public LoginView() {
        createView();
    }

    private void createView() {
        root.setSpacing(15);
        root.setPadding(new Insets(30));
        root.setAlignment(Pos.CENTER);

        Label titleLabel = new Label("智能学习助手");
        titleLabel.setFont(new Font(24));

        TextField usernameField = new TextField();
        usernameField.setPromptText("请输入账号");
        usernameField.setMaxWidth(260);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("请输入密码");
        passwordField.setMaxWidth(260);

        Button loginButton = new Button("登录");
        loginButton.setMaxWidth(260);

        Button registerButton = new Button("注册");
        registerButton.setMaxWidth(260);

        Label messageLabel = new Label();
        messageLabel.setStyle("-fx-text-fill: red;");

        loginButton.setOnAction(event -> {
            // 重点：去掉账号中的所有空格，防止变成 t e a c h e r 0 0 1
            String account = usernameField.getText() == null
                    ? ""
                    : usernameField.getText().trim().replaceAll("\\s+", "");

            // 这里也去掉密码中的空格，防止变成 1 2 3 4 5 6
            String password = passwordField.getText() == null
                    ? ""
                    : passwordField.getText().trim().replaceAll("\\s+", "");

            if (account.isBlank()) {
                messageLabel.setStyle("-fx-text-fill: red;");
                messageLabel.setText("请输入账号");
                return;
            }

            if (password.isBlank()) {
                messageLabel.setStyle("-fx-text-fill: red;");
                messageLabel.setText("请输入密码");
                return;
            }

            loginButton.setDisable(true);
            registerButton.setDisable(true);
            messageLabel.setStyle("-fx-text-fill: #333333;");
            messageLabel.setText("正在登录，请稍候...");

            new Thread(() -> {
                try {
                    System.out.println("登录账号：" + account);
                    System.out.println("登录密码：" + password);

                    LoginRequest loginRequest = new LoginRequest(account, password);
                    LoginUser user = authApi.login(loginRequest);

                    AppContext.setCurrentUser(user);

                    Platform.runLater(() -> {
                        if ("teacher".equals(user.getRole())) {
                            HelloApplication.showTeacherHomeView();
                        } else if ("student".equals(user.getRole())) {
                            HelloApplication.showStudentHomeView();
                        } else {
                            messageLabel.setStyle("-fx-text-fill: red;");
                            messageLabel.setText("未知身份：" + user.getRole());
                            loginButton.setDisable(false);
                            registerButton.setDisable(false);
                        }
                    });
                } catch (Exception e) {
                    Platform.runLater(() -> {
                        messageLabel.setStyle("-fx-text-fill: red;");
                        messageLabel.setText("登录失败：" + e.getMessage());
                        loginButton.setDisable(false);
                        registerButton.setDisable(false);
                    });
                }
            }).start();
        });

        registerButton.setOnAction(event -> HelloApplication.showRegisterView());

        root.getChildren().addAll(
                titleLabel,
                usernameField,
                passwordField,
                loginButton,
                registerButton,
                messageLabel
        );
    }

    public Parent getView() {
        return root;
    }
}