package com.scms.learningassistantclient.view;

import com.scms.learningassistantclient.HelloApplication;
import com.scms.learningassistantclient.api.AnswerApi;
import com.scms.learningassistantclient.api.StudyProgressApi;
//import com.scms.learningassistantclient.model.AnswerRecord;
import com.scms.learningassistantclient.model.AnswerRecordDetail;
import com.scms.learningassistantclient.model.LoginUser;
import com.scms.learningassistantclient.model.StudyProgress;
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
import javafx.scene.control.ScrollPane;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StudentLearningReportView {

    private final VBox root = new VBox();
    private final ScrollPane scrollPane = new ScrollPane(root);

    private final StudyProgressApi studyProgressApi = new StudyProgressApi();
    private final AnswerApi answerApi = new AnswerApi();

    private final TextField courseIdField = new TextField("2");

    private final Label baseInfoLabel = new Label("暂无学生信息");
    private final Label progressLabel = new Label("暂无学习进度");
    private final Label quizLabel = new Label("暂无测验表现");
    private final Label weakPointLabel = new Label("暂无薄弱知识点");
    private final Label suggestionLabel = new Label("暂无学习建议");

    private final ListView<StudyProgress> progressListView = new ListView<>();
    private final ListView<AnswerRecordDetail> wrongListView = new ListView<>();

    private final Label messageLabel = new Label();

    public StudentLearningReportView() {
        createView();
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

        courseIdField.setPromptText("请输入课程ID，例如：2");
        courseIdField.setMaxWidth(260);

        Button loadButton = new Button("生成学习报告");
        Button refreshButton = new Button("刷新");
        Button backButton = new Button("返回学生首页");

        loadButton.setMinWidth(130);
        refreshButton.setMinWidth(90);
        backButton.setMinWidth(130);

        HBox inputBox = new HBox(12, new Label("课程ID："), courseIdField);
        inputBox.setAlignment(Pos.CENTER);

        HBox buttonBox = new HBox(12, loadButton, refreshButton, backButton);
        buttonBox.setAlignment(Pos.CENTER);

        configCard(baseInfoLabel, "#eef8ff", "#b9def5", 140);
        configCard(progressLabel, "#f6ffed", "#b7eb8f", 150);
        configCard(quizLabel, "#fff7e6", "#ffd591", 170);
        configCard(weakPointLabel, "#fff1f0", "#ffa39e", 160);
        configCard(suggestionLabel, "#f9f0ff", "#d3adf7", 180);

        progressListView.setPrefWidth(920);
        progressListView.setPrefHeight(210);
        progressListView.setCellFactory(listView -> new ListCell<>() {
            @Override
            protected void updateItem(StudyProgress item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                    return;
                }

                setText(buildProgressText(item));
                setWrapText(true);
                setPrefWidth(progressListView.getPrefWidth() - 30);
                setMinHeight(Region.USE_PREF_SIZE);
                setStyle(
                        "-fx-font-size: 14px;" +
                                "-fx-padding: 10px;" +
                                "-fx-background-color: #fbfffb;" +
                                "-fx-border-color: #d9f7be;" +
                                "-fx-border-width: 0 0 1 0;"
                );
            }
        });

        wrongListView.setPrefWidth(920);
        wrongListView.setPrefHeight(230);
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
                        "-fx-font-size: 14px;" +
                                "-fx-padding: 10px;" +
                                "-fx-background-color: #fffdf7;" +
                                "-fx-border-color: #ffe0a3;" +
                                "-fx-border-width: 0 0 1 0;"
                );
            }
        });

        messageLabel.setStyle("-fx-font-size: 15px; -fx-text-fill: green;");

        loadButton.setOnAction(e -> loadReport());
        refreshButton.setOnAction(e -> loadReport());
        backButton.setOnAction(e -> HelloApplication.showStudentHomeView());

        root.getChildren().addAll(
                titleLabel,
                inputBox,
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

        loadReport();
    }

    private void configCard(Label label, String backgroundColor, String borderColor, int height) {
        label.setPrefWidth(900);
        label.setMinHeight(height);
        label.setPrefHeight(height);
        label.setMaxHeight(Region.USE_PREF_SIZE);
        label.setWrapText(true);
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

        Long courseId = parseLong(courseIdField.getText(), "课程ID");
        if (courseId == null) {
            return;
        }

        Long studentId = user.getId();

        setMessage("正在生成个人学习报告...", "#333333");

        new Thread(() -> {
            try {
                List<StudyProgress> progressList =
                        studyProgressApi.getStudentCourseProgress(studentId, courseId);

                List<AnswerRecordDetail> answerRecords =
                        answerApi.getStudentAnswerDetailsByCourse(studentId, courseId);

                List<AnswerRecordDetail> wrongList =
                        answerApi.getStudentWrongAnswerDetailsByCourse(studentId, courseId);

                Platform.runLater(() -> {
                    updateBaseInfo(user, courseId);
                    updateProgress(progressList);
                    updateQuiz(answerRecords);
                    updateWeakPoints(wrongList);
                    updateSuggestion(progressList, answerRecords, wrongList);

                    progressListView.setItems(FXCollections.observableArrayList(progressList));
                    wrongListView.setItems(FXCollections.observableArrayList(wrongList));

                    setMessage("个人学习报告生成成功", "green");
                });

            } catch (Exception e) {
                e.printStackTrace();
                Platform.runLater(() -> setMessage("个人学习报告生成失败：" + e.getMessage(), "red"));
            }
        }).start();
    }

    private void updateBaseInfo(LoginUser user, Long courseId) {
        baseInfoLabel.setText(
                "学生ID：" + safe(user.getId())
                        + "\n学生姓名：" + safe(user.getRealName())
                        + "\n账号：" + safe(user.getUsername())
                        + "\n班级：" + safe(user.getClassName())
                        + "\n当前报告课程ID：" + courseId
        );
    }

    private void updateProgress(List<StudyProgress> progressList) {
        if (progressList == null || progressList.isEmpty()) {
            progressLabel.setText("当前课程暂无小节学习记录。");
            return;
        }

        int total = progressList.size();
        int completed = 0;

        for (StudyProgress progress : progressList) {
            if (progress != null && progress.isCompleted()) {
                completed++;
            }
        }

        int unfinished = total - completed;
        int percent = total == 0 ? 0 : completed * 100 / total;

        progressLabel.setText(
                "课程小节总数：" + total
                        + "\n已完成小节：" + completed
                        + "\n未完成小节：" + unfinished
                        + "\n学习完成率：" + percent + "%"
        );
    }

    private void updateQuiz(List<AnswerRecordDetail> answerRecords) {
        if (answerRecords == null || answerRecords.isEmpty()) {
            quizLabel.setText("当前暂无测验答题记录。");
            return;
        }

        int total = answerRecords.size();
        int correctCount = 0;
        int totalScore = 0;

        for (AnswerRecordDetail record : answerRecords)  {
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

        quizLabel.setText(
                "答题提交次数：" + total
                        + "\n答对数量：" + correctCount
                        + "\n答错数量：" + wrongCount
                        + "\n测验正确率：" + accuracy + "%"
                        + "\n平均得分：" + averageScore
        );
    }

    private void updateWeakPoints(List<AnswerRecordDetail> wrongList) {
        if (wrongList == null || wrongList.isEmpty()) {
            weakPointLabel.setText("当前暂无错题，暂未发现明显薄弱知识点。");
            return;
        }

        Map<String, Integer> map = countKnowledgePoints(wrongList);

        StringBuilder builder = new StringBuilder();
        builder.append("错题数量：").append(wrongList.size()).append(" 道\n");
        builder.append("薄弱知识点分布：\n");

        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            builder.append(" - ")
                    .append(entry.getKey())
                    .append("：")
                    .append(entry.getValue())
                    .append(" 次\n");
        }

        weakPointLabel.setText(builder.toString());
    }

    private void updateSuggestion(
            List<StudyProgress> progressList,
            List<AnswerRecordDetail> answerRecords,
            List<AnswerRecordDetail> wrongList
    ) {
        StringBuilder builder = new StringBuilder();

        int totalSections = progressList == null ? 0 : progressList.size();
        int completedSections = 0;

        if (progressList != null) {
            for (StudyProgress progress : progressList) {
                if (progress != null && progress.isCompleted()) {
                    completedSections++;
                }
            }
        }

        if (totalSections > 0 && completedSections < totalSections) {
            builder.append("1. 你还有 ")
                    .append(totalSections - completedSections)
                    .append(" 个小节未完成，建议先补齐课程学习进度。\n");
        } else if (totalSections > 0) {
            builder.append("1. 课程小节已全部完成，可以进入巩固复习阶段。\n");
        } else {
            builder.append("1. 当前课程暂无小节数据，请先完成课程学习。\n");
        }

        if (answerRecords == null || answerRecords.isEmpty()) {
            builder.append("2. 暂无测验记录，建议完成课程测验以检测掌握情况。\n");
        } else {
            int total = answerRecords.size();
            int correct = 0;

            for (AnswerRecordDetail record : answerRecords) {
                if (record.getIsCorrect() != null && record.getIsCorrect() == 1) {
                    correct++;
                }
            }

            int accuracy = total == 0 ? 0 : correct * 100 / total;

            if (accuracy >= 85) {
                builder.append("2. 当前测验正确率较高，可以继续挑战更高难度题目。\n");
            } else if (accuracy >= 60) {
                builder.append("2. 当前测验表现中等，建议针对错题知识点进行复习。\n");
            } else {
                builder.append("2. 当前测验正确率偏低，建议重新学习相关章节后再答题。\n");
            }
        }

        if (wrongList == null || wrongList.isEmpty()) {
            builder.append("3. 当前暂无错题记录，继续保持良好学习状态。");
        } else {
            Map<String, Integer> map = countKnowledgePoints(wrongList);
            String mainWeakPoint = findTopKnowledgePoint(map);

            builder.append("3. 重点薄弱知识点为【")
                    .append(mainWeakPoint)
                    .append("】，建议优先复习相关小节，并重新完成对应测验。");
        }

        suggestionLabel.setText(builder.toString());
    }

    private Map<String, Integer> countKnowledgePoints(List<AnswerRecordDetail> wrongList) {
        Map<String, Integer> map = new HashMap<>();

        if (wrongList == null) {
            return map;
        }

        for (AnswerRecordDetail item : wrongList) {
            String point = item.getKnowledgePoint();

            if (point == null || point.trim().isEmpty()) {
                point = "未标注知识点";
            }

            map.put(point, map.getOrDefault(point, 0) + 1);
        }

        return map;
    }

    private String findTopKnowledgePoint(Map<String, Integer> map) {
        String topPoint = "暂无";
        int maxCount = 0;

        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            if (entry.getValue() > maxCount) {
                maxCount = entry.getValue();
                topPoint = entry.getKey();
            }
        }

        return topPoint;
    }

    private String buildProgressText(StudyProgress progress) {
        return "章节：" + safe(progress.getChapterTitle())
                + "\n小节：" + safe(progress.getSectionTitle())
                + "\n学习状态：" + progress.getDisplayStatus()
                + "\n学习进度：" + safe(progress.getProgressPercent()) + "%"
                + "\n更新时间：" + safe(progress.getUpdateTime());
    }

    private String buildWrongText(AnswerRecordDetail item) {
        return "错题记录ID：" + safe(item.getId())
                + "\n任务ID：" + safe(item.getTaskId())
                + "\n题目内容：" + safe(item.getQuestionText())
                + "\n我的答案：" + safe(item.getStudentAnswer())
                + "\n正确答案：" + safe(item.getCorrectAnswer())
                + "\n知识点：" + safe(item.getKnowledgePoint())
                + "\n得分：" + safe(item.getScore())
                + "\n提交时间：" + safe(item.getSubmitTime());
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