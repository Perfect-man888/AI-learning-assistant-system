package com.scms.learningassistantclient.view;

import com.scms.learningassistantclient.HelloApplication;
import com.scms.learningassistantclient.api.AnswerApi;
import com.scms.learningassistantclient.model.AnswerRecordDetail;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.layout.Region;

import java.util.List;

public class TeacherAnswerStatsView {

    private final VBox root = new VBox();

    private final AnswerApi answerApi = new AnswerApi();

    private final TextField taskIdField = new TextField("2");
    private final Label summaryLabel = new Label("暂无统计数据");
    private final ListView<AnswerRecordDetail> recordListView = new ListView<>();
    private final Label messageLabel = new Label();

    public TeacherAnswerStatsView() {
        createView();
    }

    public TeacherAnswerStatsView(Long taskId) {
        if (taskId != null) {
            taskIdField.setText(String.valueOf(taskId));
        }

        createView();
    }

    private void createView() {
        root.setSpacing(16);
        root.setPadding(new Insets(30));
        root.setAlignment(Pos.TOP_CENTER);

        Label titleLabel = new Label("教师端 - 答题统计");
        titleLabel.setFont(new Font(26));

        taskIdField.setPromptText("请输入任务ID，例如：2");
        taskIdField.setMaxWidth(260);

        Button queryButton = new Button("查询答题统计");
        Button refreshButton = new Button("刷新");
        Button backButton = new Button("返回教师首页");

        queryButton.setMinWidth(130);
        refreshButton.setMinWidth(90);
        backButton.setMinWidth(130);

        HBox inputBox = new HBox(12, new Label("任务ID："), taskIdField);
        inputBox.setAlignment(Pos.CENTER);

        HBox buttonBox = new HBox(12, queryButton, refreshButton, backButton);
        buttonBox.setAlignment(Pos.CENTER);

        summaryLabel.setPrefWidth(850);
        summaryLabel.setMinHeight(220);
        summaryLabel.setPrefHeight(220);
        summaryLabel.setMaxHeight(220);
        summaryLabel.setWrapText(true);
        summaryLabel.setStyle(
                "-fx-font-size: 16px;" +
                        "-fx-padding: 14px;" +
                        "-fx-background-color: #eef8ff;" +
                        "-fx-border-color: #b9def5;"
        );

        recordListView.setPrefWidth(900);
        recordListView.setPrefHeight(420);

        recordListView.setCellFactory(listView -> new ListCell<>() {
            @Override
            protected void updateItem(AnswerRecordDetail item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                    return;
                }

                setText(buildRecordText(item));
                setWrapText(true);
                setPrefWidth(recordListView.getPrefWidth() - 30);
                setMinHeight(Region.USE_PREF_SIZE);
                setStyle(
                        "-fx-font-size: 15px;" +
                                "-fx-padding: 12px;" +
                                "-fx-background-color: #f7fbff;" +
                                "-fx-border-color: #d0e6f7;" +
                                "-fx-border-width: 0 0 1 0;"
                );
            }
        });

        messageLabel.setStyle("-fx-font-size: 15px; -fx-text-fill: green;");

        queryButton.setOnAction(e -> loadAnswerStats());
        refreshButton.setOnAction(e -> loadAnswerStats());
        backButton.setOnAction(e -> HelloApplication.showTeacherHomeView());

        root.getChildren().addAll(
                titleLabel,
                inputBox,
                buttonBox,
                new Label("统计概览："),
                summaryLabel,
                new Label("答题记录列表："),
                recordListView,
                messageLabel
        );

        loadAnswerStats();
    }

    private void loadAnswerStats() {
        Long taskId = parseLong(taskIdField.getText(), "任务ID");

        if (taskId == null) {
            return;
        }

        setMessage("正在加载答题统计详情...", "#333333");

        new Thread(() -> {
            try {
                List<AnswerRecordDetail> records = answerApi.getTaskAnswerRecordDetails(taskId);

                Platform.runLater(() -> {
                    recordListView.setItems(FXCollections.observableArrayList(records));
                    updateSummary(records);
                    setMessage("答题统计加载成功，共 " + records.size() + " 条记录", "green");
                });

            } catch (Exception e) {
                e.printStackTrace();
                Platform.runLater(() -> setMessage("答题统计加载失败：" + e.getMessage(), "red"));
            }
        }).start();
    }

    private void updateSummary(List<AnswerRecordDetail> records) {
        if (records == null || records.isEmpty()) {
            summaryLabel.setText("当前任务暂无学生答题记录");
            return;
        }

        int total = records.size();
        int correctCount = 0;
        int totalScore = 0;

        for (AnswerRecordDetail record : records) {
            if (record.getIsCorrect() != null && record.getIsCorrect() == 1) {
                correctCount++;
            }

            if (record.getScore() != null) {
                totalScore += record.getScore();
            }
        }

        int wrongCount = total - correctCount;
        int accuracy = total == 0 ? 0 : correctCount * 100 / total;
        int averageScore = total == 0 ? 0 : totalScore / total;

        summaryLabel.setText(
                "任务ID：" + taskIdField.getText().trim()
                        + "\n提交记录数：" + total
                        + "\n答对数量：" + correctCount
                        + "\n答错数量：" + wrongCount
                        + "\n正确率：" + accuracy + "%"
                        + "\n平均分：" + averageScore
        );
    }

    private String buildRecordText(AnswerRecordDetail record) {
        String status = record.getIsCorrect() != null && record.getIsCorrect() == 1
                ? "正确"
                : "错误";

        return "答题记录ID：" + safe(record.getId())
                + "\n任务ID：" + safe(record.getTaskId())
                + "\n学生ID：" + safe(record.getStudentId())
                + "\n学生姓名：" + safe(record.getStudentName())
                + "\n班级：" + safe(record.getClassName())
                + "\n题目ID：" + safe(record.getQuestionId())
                + "\n题目内容：" + safe(record.getQuestionText())
                + "\n学生答案：" + safe(record.getStudentAnswer())
                + "\n正确答案：" + safe(record.getCorrectAnswer())
                + "\n判题结果：" + status
                + "\n得分：" + safe(record.getScore())
                + "\n知识点：" + safe(record.getKnowledgePoint())
                + "\n提交时间：" + safe(record.getSubmitTime());
    }

    private Long parseLong(String text, String fieldName) {
        if (text == null || text.trim().isEmpty()) {
            setMessage("请输入" + fieldName, "red");
            return null;
        }

        try {
            return Long.parseLong(text.trim());
        } catch (Exception e) {
            setMessage(fieldName + "必须是数字", "red");
            return null;
        }
    }

    private String safe(Object value) {
        return value == null ? "暂无" : String.valueOf(value);
    }

    private void setMessage(String text, String color) {
        messageLabel.setStyle("-fx-font-size: 15px; -fx-text-fill: " + color + ";");
        messageLabel.setText(text);
    }

    public Parent getView() {
        return root;
    }
}