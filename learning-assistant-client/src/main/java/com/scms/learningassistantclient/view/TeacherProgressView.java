package com.scms.learningassistantclient.view;

import com.scms.learningassistantclient.HelloApplication;
import com.scms.learningassistantclient.api.StudyProgressApi;
import com.scms.learningassistantclient.model.StudentProgressSummary;
import com.scms.learningassistantclient.model.StudyProgress;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.util.Collections;
import java.util.List;

public class TeacherProgressView {

    private final VBox root = new VBox();

    private final StudyProgressApi studyProgressApi = new StudyProgressApi();

    private final ListView<StudentProgressSummary> progressListView = new ListView<>();
    private final TextField courseIdField = new TextField();
    private final Label messageLabel = new Label();

    public TeacherProgressView() {
        createView();
    }

    private void createView() {
        root.setSpacing(16);
        root.setPadding(new Insets(30));
        root.setAlignment(Pos.TOP_CENTER);

        Label titleLabel = new Label("教师端 - 学习进度统计");
        titleLabel.setFont(new Font(26));

        courseIdField.setPromptText("请输入课程ID，例如：2");
        courseIdField.setText("2");
        courseIdField.setMaxWidth(300);

        Button queryButton = new Button("查询学习进度");
        Button refreshButton = new Button("刷新");
        Button backButton = new Button("返回教师首页");

        queryButton.setMinWidth(120);
        refreshButton.setMinWidth(90);
        backButton.setMinWidth(120);

        HBox buttonBox = new HBox(12, queryButton, refreshButton, backButton);
        buttonBox.setAlignment(Pos.CENTER);

        progressListView.setPrefWidth(900);
        progressListView.setPrefHeight(460);

        progressListView.setCellFactory(listView -> new ListCell<>() {
            @Override
            protected void updateItem(StudentProgressSummary item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                    return;
                }

                setText(buildProgressText(item));
                setWrapText(true);
                setStyle(
                        "-fx-font-size: 15px;" +
                                "-fx-padding: 12px;" +
                                "-fx-background-color: #08a0c8;" +
                                "-fx-text-fill: white;" +
                                "-fx-border-color: white;" +
                                "-fx-border-width: 0 0 2 0;"
                );
            }
        });

        messageLabel.setStyle("-fx-font-size: 15px; -fx-text-fill: green;");

        queryButton.setOnAction(e -> loadProgress());
        refreshButton.setOnAction(e -> loadProgress());
        backButton.setOnAction(e -> HelloApplication.showTeacherHomeView());

        root.getChildren().addAll(
                titleLabel,
                new Label("课程ID："),
                courseIdField,
                buttonBox,
                new Label("学习进度列表："),
                progressListView,
                messageLabel
        );
    }

    private void loadProgress() {
        String courseIdText = courseIdField.getText();

        if (courseIdText == null || courseIdText.trim().isEmpty()) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("请输入课程ID");
            return;
        }

        Long courseId;

        try {
            courseId = Long.parseLong(courseIdText.trim());
        } catch (Exception e) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("课程ID必须是数字");
            return;
        }

        messageLabel.setStyle("-fx-text-fill: #333333;");
        messageLabel.setText("正在加载学习进度和小节明细...");

        new Thread(() -> {
            try {
                List<StudentProgressSummary> summaries =
                        studyProgressApi.getCourseProgressSummary(courseId);

                if (summaries == null) {
                    summaries = Collections.emptyList();
                }

                for (StudentProgressSummary summary : summaries) {
                    if (summary == null || summary.getStudentId() == null) {
                        continue;
                    }

                    try {
                        List<StudyProgress> detailList =
                                studyProgressApi.getStudentCourseProgress(summary.getStudentId(), courseId);

                        summary.setProgressList(detailList);
                    } catch (Exception detailException) {
                        detailException.printStackTrace();
                        summary.setProgressList(Collections.emptyList());
                    }
                }

                List<StudentProgressSummary> finalSummaries = summaries;

                Platform.runLater(() -> {
                    progressListView.setItems(FXCollections.observableArrayList(finalSummaries));
                    progressListView.refresh();

                    messageLabel.setStyle("-fx-text-fill: green;");
                    messageLabel.setText("学习进度加载成功，共 " + finalSummaries.size() + " 名学生");
                });

            } catch (Exception e) {
                e.printStackTrace();

                Platform.runLater(() -> {
                    messageLabel.setStyle("-fx-text-fill: red;");
                    messageLabel.setText("学习进度加载失败：" + e.getMessage());
                });
            }
        }).start();
    }

    private String buildProgressText(StudentProgressSummary item) {
        int completedSections = safeInt(item.getCompletedSections());
        int totalSections = safeInt(item.getTotalSections());
        int progressPercent = safeInt(item.getProgressPercent());

        StringBuilder builder = new StringBuilder();

        builder.append("学生ID：").append(safeText(item.getStudentId())).append("\n");
        builder.append("学生姓名：").append(safeText(item.getStudentName())).append("\n");
        builder.append("班级：").append(safeText(item.getClassName())).append("\n");
        builder.append("课程ID：").append(safeText(item.getCourseId())).append("\n");
        builder.append("已学习小节：").append(completedSections).append(" / ").append(totalSections).append("\n");
        builder.append("完成率：").append(progressPercent).append("%");

        builder.append("\n\n小节学习明细：");

        List<StudyProgress> progressList = item.getProgressList();

        if (progressList == null || progressList.isEmpty()) {
            builder.append("\n暂无小节学习记录");
        } else {
            for (int i = 0; i < progressList.size(); i++) {
                StudyProgress progress = progressList.get(i);

                builder.append("\n")
                        .append(i + 1)
                        .append(". ")
                        .append(progress.getDisplaySectionName())
                        .append("：")
                        .append(progress.getDisplayStatus());

                if (progress.getProgressPercent() != null) {
                    builder.append("，进度 ")
                            .append(progress.getProgressPercent())
                            .append("%");
                }

                if (progress.getUpdateTime() != null && !progress.getUpdateTime().trim().isEmpty()) {
                    builder.append("，更新时间：")
                            .append(progress.getUpdateTime());
                }
            }
        }

        int unfinishedCount = totalSections - completedSections;
        if (unfinishedCount > 0) {
            builder.append("\n\n未完成小节数量：").append(unfinishedCount).append(" 个");
        }

        return builder.toString();
    }

    private int safeInt(Integer value) {
        return value == null ? 0 : value;
    }

    private String safeText(Object value) {
        if (value == null) {
            return "暂无";
        }
        return String.valueOf(value);
    }

    public Parent getView() {
        return root;
    }
}