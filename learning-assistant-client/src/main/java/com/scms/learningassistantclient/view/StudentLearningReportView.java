package com.scms.learningassistantclient.view;

import com.scms.learningassistantclient.api.ReportApi;
import com.scms.learningassistantclient.model.KnowledgePointStat;
import com.scms.learningassistantclient.model.LoginUser;
import com.scms.learningassistantclient.model.StudentReport;
import com.scms.learningassistantclient.util.AppContext;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class StudentLearningReportView {

    private final VBox root = new VBox();
    private final ScrollPane scrollPane = new ScrollPane(root);

    private final ReportApi reportApi = new ReportApi();

    private final Long courseId;
    private final String courseName;

    private final Label baseInfoLabel = new Label("暂无学生信息");
    private final Label progressLabel = new Label("暂无学习进度");
    private final Label quizLabel = new Label("暂无测验表现");
    private final Label weakPointLabel = new Label("暂无薄弱知识点");
    private final Label suggestionLabel = new Label("暂无学习建议");

    private final ListView<Object> progressListView = new ListView<>();
    private final ListView<Object> wrongListView = new ListView<>();

    private final Label messageLabel = new Label();

    public StudentLearningReportView(Long courseId, String courseName) {
        this.courseId = courseId;
        this.courseName = courseName;
        createView();
        loadReport();
    }

    private void createView() {
        scrollPane.setFitToWidth(true);
        scrollPane.setPannable(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        root.setSpacing(14);
        root.setPadding(new Insets(26));
        root.setAlignment(Pos.TOP_CENTER);

        Label titleLabel = new Label("学生端 - 个人学习报告");
        titleLabel.setFont(new Font(26));

        Label courseLabel = new Label("当前课程：" + courseName + " / 课程ID：" + courseId);
        courseLabel.setStyle("-fx-font-size: 16px;");

        Button loadButton = new Button("生成学习报告");
        Button refreshButton = new Button("刷新");
        Button backButton = new Button("返回课程学习");

        loadButton.setMinWidth(130);
        refreshButton.setMinWidth(90);
        backButton.setMinWidth(130);

        HBox buttonBox = new HBox(12, loadButton, refreshButton, backButton);
        buttonBox.setAlignment(Pos.CENTER);

        configCard(baseInfoLabel, "#eef8ff", "#b9def5", 140);
        configCard(progressLabel, "#f6ffed", "#b7eb8f", 150);
        configCard(quizLabel, "#fff7e6", "#ffd591", 180);
        configCard(weakPointLabel, "#fff1f0", "#ffa39e", 260);
        configCard(suggestionLabel, "#f9f0ff", "#d3adf7", 240);

        progressListView.setPrefWidth(920);
        progressListView.setPrefHeight(120);
        progressListView.setPlaceholder(new Label("学习进度明细已整合到上方报告概览中"));

        wrongListView.setPrefWidth(920);
        wrongListView.setPrefHeight(120);
        wrongListView.setPlaceholder(new Label("错题统计已整合到上方薄弱知识点分析中"));

        messageLabel.setStyle("-fx-font-size: 15px; -fx-text-fill: green;");

        loadButton.setOnAction(e -> loadReport());
        refreshButton.setOnAction(e -> loadReport());

        backButton.setOnAction(e -> {
            Scene currentScene = root.getScene();
            currentScene.setRoot(new StudentCourseDetailView(
                    courseId,
                    courseName,
                    () -> currentScene.setRoot(new StudentCourseView().getView())
            ));
        });

        root.getChildren().addAll(
                titleLabel,
                courseLabel,
                buttonBox,
                new Label("基本信息："),
                baseInfoLabel,
                new Label("学习进度概览："),
                progressLabel,
                new Label("测验表现："),
                quizLabel,
                new Label("薄弱知识点："),
                weakPointLabel,
                new Label("综合学习建议："),
                suggestionLabel,
                new Label("小节学习明细："),
                progressListView,
                new Label("错题明细："),
                wrongListView,
                messageLabel
        );
    }

    private void configCard(Label label, String backgroundColor, String borderColor, int height) {
        label.setPrefWidth(900);
        label.setMaxWidth(900);
        label.setMinHeight(height);
        label.setPrefHeight(Region.USE_COMPUTED_SIZE);
        label.setMaxHeight(Double.MAX_VALUE);
        label.setWrapText(true);
        label.setAlignment(Pos.CENTER_LEFT);

        label.setStyle(
                "-fx-font-size: 15px;" +
                        "-fx-padding: 14px;" +
                        "-fx-background-color: " + backgroundColor + ";" +
                        "-fx-border-color: " + borderColor + ";"
        );
    }

    private void loadReport() {
        LoginUser user = AppContext.getCurrentUser();

        if (user == null || user.getId() == null) {
            setMessage("未获取到当前学生信息，请重新登录", "red");
            return;
        }

        if (courseId == null) {
            setMessage("课程ID为空，无法生成学习报告", "red");
            return;
        }

        Long studentId = user.getId();

        setMessage("正在从后端生成个人学习报告...", "#333333");

        new Thread(() -> {
            try {
                StudentReport report = reportApi.getStudentCourseReport(studentId, courseId);

                Platform.runLater(() -> {
                    updateBaseInfo(user);
                    updateProgressByReport(report);
                    updateQuizByReport(report);
                    updateWeakPointsByReport(report);
                    updateSuggestionByReport(report);

                    progressListView.setItems(FXCollections.observableArrayList());
                    wrongListView.setItems(FXCollections.observableArrayList());

                    setMessage("个人学习报告生成成功", "green");
                });

            } catch (Exception e) {
                e.printStackTrace();
                Platform.runLater(() -> setMessage("个人学习报告生成失败：" + e.getMessage(), "red"));
            }
        }).start();
    }

    private void updateBaseInfo(LoginUser user) {
        baseInfoLabel.setText(
                "学生ID：" + safe(user.getId())
                        + "\n学生姓名：" + safe(user.getRealName())
                        + "\n账号：" + safe(user.getUsername())
                        + "\n班级：" + safe(user.getClassName())
                        + "\n当前课程：" + safe(courseName)
                        + "\n当前报告课程ID：" + safe(courseId)
        );
    }

    private void updateProgressByReport(StudentReport report) {
        if (report == null) {
            progressLabel.setText("暂无学习进度数据。");
            return;
        }

        progressLabel.setText(
                "课程小节总数：" + safe(report.getTotalSections())
                        + "\n已完成小节：" + safe(report.getCompletedSections())
                        + "\n未完成小节：" + safe(report.getUnfinishedSections())
                        + "\n学习完成率：" + safe(report.getProgressPercent()) + "%"
        );
    }

    private void updateQuizByReport(StudentReport report) {
        if (report == null) {
            quizLabel.setText("暂无测验表现数据。");
            return;
        }

        quizLabel.setText(
                "答题提交次数：" + safe(report.getTotalAnswers())
                        + "\n答对数量：" + safe(report.getCorrectAnswers())
                        + "\n答错数量：" + safe(report.getWrongAnswers())
                        + "\n测验正确率：" + safe(report.getAccuracy()) + "%"
                        + "\n平均得分：" + safe(report.getAverageScore())
                        + "\nAI个性化复习任务数量：" + safe(report.getReviewTaskCount())
        );
    }

    private void updateWeakPointsByReport(StudentReport report) {
        if (report == null) {
            weakPointLabel.setText("暂无薄弱知识点数据。");
            return;
        }

        StringBuilder builder = new StringBuilder();

        builder.append("错题本总数：").append(safe(report.getWrongQuestionTotal())).append(" 条\n");
        builder.append("已掌握错题：").append(safe(report.getMasteredWrongQuestions())).append(" 条\n");
        builder.append("未掌握错题：").append(safe(report.getUnmasteredWrongQuestions())).append(" 条\n");
        builder.append("重点薄弱知识点：").append(safe(report.getMainWeakPoint())).append("\n\n");

        builder.append("薄弱知识点分布：\n");

        if (report.getWeakKnowledgePoints() == null || report.getWeakKnowledgePoints().isEmpty()) {
            builder.append("暂无明显薄弱知识点。");
        } else {
            for (KnowledgePointStat stat : report.getWeakKnowledgePoints()) {
                builder.append(" - ")
                        .append(safe(stat.getKnowledgePoint()))
                        .append("：")
                        .append(safe(stat.getWrongCount()))
                        .append(" 次\n");
            }
        }

        weakPointLabel.setText(builder.toString());
    }

    private void updateSuggestionByReport(StudentReport report) {
        if (report == null || report.getLearningSuggestion() == null) {
            suggestionLabel.setText("暂无学习建议。");
            return;
        }

        suggestionLabel.setText(report.getLearningSuggestion());
    }

    private String safe(Object value) {
        if (value == null) {
            return "暂无";
        }

        String text = String.valueOf(value);
        return text.trim().isEmpty() ? "暂无" : text;
    }

    private void setMessage(String text, String color) {
        messageLabel.setStyle("-fx-font-size: 15px; -fx-text-fill: " + color + ";");
        messageLabel.setText(text);
    }

    public Parent getView() {
        return scrollPane;
    }
}