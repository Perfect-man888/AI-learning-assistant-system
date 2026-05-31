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

public class TeacherHomeView {

    private final VBox root = new VBox();

    public TeacherHomeView() {
        createView();
    }

    private void createView() {
        root.setSpacing(18);
        root.setPadding(new Insets(40));
        root.setAlignment(Pos.CENTER);

        LoginUser user = AppContext.getCurrentUser();

        Label titleLabel = new Label("教师端首页");
        titleLabel.setFont(new Font(26));

        Label userInfoLabel = new Label(
                "欢迎您，" + user.getRealName()
                        + "\n账号：" + user.getUsername()
                        + "\n班级：" + user.getClassName()
        );

        Button courseButton = new Button("课程管理");
        courseButton.setMaxWidth(220);
        courseButton.setOnAction(event -> HelloApplication.showTeacherCourseView());

        Button questionButton = new Button("测验题目管理");
        questionButton.setMaxWidth(220);
        questionButton.setOnAction(event ->
                showAlert("请先进入课程管理，选择具体课程后，再进行测验题目管理。")
        );

        Button taskButton = new Button("学习任务管理");
        taskButton.setMaxWidth(220);
        taskButton.setOnAction(event ->
                showAlert("请先进入课程管理，选择具体课程后，再进行学习任务管理。")
        );

        Button reportButton = new Button("学习数据报告");
        reportButton.setMaxWidth(220);
        reportButton.setOnAction(event ->
                showAlert("请先进入课程管理，选择具体课程后，再查看学习数据报告。")
        );

        Button answerStatsButton = new Button("答题统计");
        answerStatsButton.setMaxWidth(220);
        answerStatsButton.setOnAction(event ->
                showAlert("请先进入课程管理，选择具体课程后，再查看答题统计。")
        );

        Button classReportButton = new Button("班级综合报告");
        classReportButton.setMaxWidth(220);
        classReportButton.setOnAction(event ->
                showAlert("请先进入课程管理，选择具体课程后，再查看班级综合报告。")
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
                questionButton,
                taskButton,
                reportButton,
                answerStatsButton,
                classReportButton,
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