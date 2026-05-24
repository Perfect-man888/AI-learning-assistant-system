package com.scms.learningassistantclient.view;

import com.scms.learningassistantclient.HelloApplication;
import com.scms.learningassistantclient.api.AnswerApi;
import com.scms.learningassistantclient.model.AnswerRecordDetail;
import com.scms.learningassistantclient.model.LoginUser;
import com.scms.learningassistantclient.util.AppContext;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StudentWrongBookView {

    private final VBox root = new VBox();

    private final AnswerApi answerApi = new AnswerApi();

    private final Label summaryLabel = new Label("暂无错题统计");
    private final ListView<AnswerRecordDetail> wrongListView = new ListView<>();
    private final Label messageLabel = new Label();

    public StudentWrongBookView() {
        createView();
    }

    private void createView() {
        root.setSpacing(16);
        root.setPadding(new Insets(30));
        root.setAlignment(Pos.TOP_CENTER);

        Label titleLabel = new Label("学生端 - 我的错题本");
        titleLabel.setFont(new Font(26));

        Button refreshButton = new Button("刷新错题本");
        Button backButton = new Button("返回学生首页");

        refreshButton.setMinWidth(130);
        backButton.setMinWidth(130);

        HBox buttonBox = new HBox(12, refreshButton, backButton);
        buttonBox.setAlignment(Pos.CENTER);

        summaryLabel.setPrefWidth(880);
        summaryLabel.setMinHeight(150);
        summaryLabel.setPrefHeight(150);
        summaryLabel.setWrapText(true);
        summaryLabel.setStyle(
                "-fx-font-size: 16px;" +
                        "-fx-padding: 14px;" +
                        "-fx-background-color: #fff7e6;" +
                        "-fx-border-color: #ffd591;"
        );

        wrongListView.setPrefWidth(920);
        wrongListView.setPrefHeight(460);

        wrongListView.setCellFactory(listView -> new ListCell<>() {
            @Override
            protected void updateItem(AnswerRecordDetail item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                    return;
                }

                setText(buildWrongText(item));
                setWrapText(true);
                setPrefWidth(wrongListView.getPrefWidth() - 30);
                setMinHeight(Region.USE_PREF_SIZE);
                setStyle(
                        "-fx-font-size: 15px;" +
                                "-fx-padding: 12px;" +
                                "-fx-background-color: #fffdf7;" +
                                "-fx-border-color: #ffe0a3;" +
                                "-fx-border-width: 0 0 1 0;"
                );
            }
        });

        messageLabel.setStyle("-fx-font-size: 15px; -fx-text-fill: green;");

        refreshButton.setOnAction(e -> loadWrongBook());
        backButton.setOnAction(e -> HelloApplication.showStudentHomeView());

        root.getChildren().addAll(
                titleLabel,
                buttonBox,
                new Label("错题统计："),
                summaryLabel,
                new Label("错题记录："),
                wrongListView,
                messageLabel
        );

        loadWrongBook();
    }

    private void loadWrongBook() {
        LoginUser user = AppContext.getCurrentUser();

        if (user == null || user.getId() == null) {
            setMessage("未获取到当前学生信息，请重新登录", "red");
            return;
        }

        Long studentId = user.getId();

        setMessage("正在加载错题本...", "#333333");

        new Thread(() -> {
            try {
                List<AnswerRecordDetail> wrongList =
                        answerApi.getStudentWrongAnswerDetails(studentId);

                Platform.runLater(() -> {
                    wrongListView.setItems(FXCollections.observableArrayList(wrongList));
                    updateSummary(wrongList);
                    setMessage("错题本加载成功，共 " + wrongList.size() + " 道错题", "green");
                });

            } catch (Exception e) {
                e.printStackTrace();
                Platform.runLater(() -> setMessage("错题本加载失败：" + e.getMessage(), "red"));
            }
        }).start();
    }

    private void updateSummary(List<AnswerRecordDetail> wrongList) {
        if (wrongList == null || wrongList.isEmpty()) {
            summaryLabel.setText(
                    "当前暂无错题记录。\n" +
                            "继续保持！如果后续答错题目，系统会自动记录到这里。"
            );
            return;
        }

        Map<String, Integer> knowledgeCountMap = new HashMap<>();

        for (AnswerRecordDetail item : wrongList) {
            String point = item.getKnowledgePoint();

            if (point == null || point.trim().isEmpty()) {
                point = "未标注知识点";
            }

            knowledgeCountMap.put(point, knowledgeCountMap.getOrDefault(point, 0) + 1);
        }

        StringBuilder builder = new StringBuilder();

        builder.append("错题数量：").append(wrongList.size()).append(" 道\n");
        builder.append("薄弱知识点：\n");

        for (Map.Entry<String, Integer> entry : knowledgeCountMap.entrySet()) {
            builder.append(" - ")
                    .append(entry.getKey())
                    .append("：")
                    .append(entry.getValue())
                    .append(" 次\n");
        }

        builder.append("\n学习建议：请优先复习出现次数较多的知识点，并重新完成相关测验。");

        summaryLabel.setText(builder.toString());
    }

    private String buildWrongText(AnswerRecordDetail item) {
        return "错题记录ID：" + safe(item.getId())
                + "\n任务ID：" + safe(item.getTaskId())
                + "\n题目ID：" + safe(item.getQuestionId())
                + "\n学生姓名：" + safe(item.getStudentName())
                + "\n班级：" + safe(item.getClassName())
                + "\n题目内容：" + safe(item.getQuestionText())
                + "\n我的答案：" + safe(item.getStudentAnswer())
                + "\n正确答案：" + safe(item.getCorrectAnswer())
                + "\n知识点：" + safe(item.getKnowledgePoint())
                + "\n得分：" + safe(item.getScore())
                + "\n提交时间：" + safe(item.getSubmitTime())
                + "\n\n复习建议：请重新理解【" + safe(item.getKnowledgePoint()) + "】相关内容。";
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
        return root;
    }
}