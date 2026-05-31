package com.scms.learningassistantclient.view;

import com.scms.learningassistantclient.HelloApplication;
import com.scms.learningassistantclient.model.LoginUser;
import com.scms.learningassistantclient.util.AppContext;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class StudentHomeView {

    private final VBox root = new VBox();

    public StudentHomeView() {
        createView();
    }

    private void createView() {
        root.setSpacing(18);
        root.setPadding(new Insets(40));
        root.setAlignment(Pos.CENTER);

        LoginUser user = AppContext.getCurrentUser();

        Label titleLabel = new Label("学生端首页");
        titleLabel.setFont(new Font(26));

        Label userInfoLabel = new Label(
                "欢迎你，" + user.getRealName()
                        + "\n账号：" + user.getUsername()
                        + "\n班级：" + user.getClassName()
        );

        Button courseButton = new Button("我的课程");
        courseButton.setMaxWidth(220);
        courseButton.setOnAction(event -> HelloApplication.showStudentCourseView());

        Button taskButton = new Button("学习任务");
        taskButton.setMaxWidth(220);
        taskButton.setOnAction(event ->
                showAlert("请先进入课程中心，选择具体课程后再查看学习任务。")
        );

        Button reportButton = new Button("我的学习报告");
        reportButton.setMaxWidth(220);
        reportButton.setOnAction(event ->
                showAlert("请先进入课程中心，选择具体课程后再查看学习报告。")
        );

        Button quizButton = new Button("课程测验答题");
        quizButton.setMaxWidth(220);
        quizButton.setOnAction(event ->
                showAlert("请先进入课程中心，选择具体课程后再使用测验答题功能。")
        );

        Button wrongBookButton = new Button("我的错题本");
        wrongBookButton.setMaxWidth(220);
        wrongBookButton.setOnAction(event ->
                showAlert("请先进入课程中心，选择具体课程后再查看错题本。")
        );

        Button reviewButton = new Button("个性化复习");
        reviewButton.setMaxWidth(220);
        reviewButton.setOnAction(event ->
                showAlert("请先进入课程中心，选择具体课程后再生成个性化复习。")
        );

        Button logoutButton = new Button("退出登录");
        logoutButton.setMaxWidth(220);
        logoutButton.setOnAction(event -> {
            AppContext.clear();
            HelloApplication.showLoginView();
        });

        root.getChildren().addAll(
                titleLabel,
                userInfoLabel,
                courseButton,
                taskButton,
                reportButton,
                quizButton,
                wrongBookButton,
                reviewButton,
                logoutButton
        );
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("操作提示");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public Parent getView() {
        return root;
    }
}