package com.scms.learningassistantclient.view;

import com.scms.learningassistantclient.HelloApplication;
import com.scms.learningassistantclient.api.ClassReportApi;
import com.scms.learningassistantclient.model.ClassLearningReport;
import com.scms.learningassistantclient.model.ClassStudentReport;
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

import java.util.List;
import java.util.Map;

public class TeacherClassReportView {

    private final VBox root = new VBox();
    private final ScrollPane scrollPane = new ScrollPane(root);

    private final ClassReportApi classReportApi = new ClassReportApi();

    private final TextField courseIdField = new TextField("2");

    private final Label overviewLabel = new Label("暂无班级概览");
    private final Label weakPointLabel = new Label("暂无薄弱知识点");
    private final Label suggestionLabel = new Label("暂无教学建议");

    private final ListView<ClassStudentReport> studentListView = new ListView<>();
    private final Label messageLabel = new Label();

    public TeacherClassReportView() {
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

        Label titleLabel = new Label("教师端 - 班级综合学习报告");
        titleLabel.setFont(new Font(26));

        courseIdField.setPromptText("请输入课程ID，例如：2");
        courseIdField.setMaxWidth(260);

        Button loadButton = new Button("生成班级报告");
        Button refreshButton = new Button("刷新");
        Button backButton = new Button("返回教师首页");

        loadButton.setMinWidth(130);
        refreshButton.setMinWidth(90);
        backButton.setMinWidth(130);

        HBox inputBox = new HBox(12, new Label("课程ID："), courseIdField);
        inputBox.setAlignment(Pos.CENTER);

        HBox buttonBox = new HBox(12, loadButton, refreshButton, backButton);
        buttonBox.setAlignment(Pos.CENTER);

        configCard(overviewLabel, "#eef8ff", "#b9def5", 190);
        configCard(weakPointLabel, "#fff1f0", "#ffa39e", 150);
        configCard(suggestionLabel, "#f9f0ff", "#d3adf7", 150);

        studentListView.setPrefWidth(930);
        studentListView.setPrefHeight(420);

        studentListView.setCellFactory(listView -> new ListCell<>() {
            @Override
            protected void updateItem(ClassStudentReport item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                    return;
                }

                setText(buildStudentText(item));
                setWrapText(true);
                setPrefWidth(studentListView.getPrefWidth() - 30);
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

        loadButton.setOnAction(e -> loadClassReport());
        refreshButton.setOnAction(e -> loadClassReport());
        backButton.setOnAction(e -> HelloApplication.showTeacherHomeView());

        root.getChildren().addAll(
                titleLabel,
                inputBox,
                buttonBox,
                new Label("班级整体概览："),
                overviewLabel,
                new Label("班级薄弱知识点："),
                weakPointLabel,
                new Label("教学建议："),
                suggestionLabel,
                new Label("学生学习概览："),
                studentListView,
                messageLabel
        );

        loadClassReport();
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

    private void loadClassReport() {
        Long courseId = parseLong(courseIdField.getText(), "课程ID");

        if (courseId == null) {
            return;
        }

        setMessage("正在生成班级综合学习报告...", "#333333");

        new Thread(() -> {
            try {
                ClassLearningReport report = classReportApi.getClassLearningReport(courseId);

                Platform.runLater(() -> {
                    updateOverview(report);
                    updateWeakPoints(report);
                    updateSuggestion(report);

                    List<ClassStudentReport> students = report.getStudents();
                    studentListView.setItems(FXCollections.observableArrayList(students));

                    int count = students == null ? 0 : students.size();
                    setMessage("班级综合学习报告生成成功，共 " + count + " 名学生", "green");
                });

            } catch (Exception e) {
                e.printStackTrace();
                Platform.runLater(() -> setMessage("班级综合学习报告生成失败：" + e.getMessage(), "red"));
            }
        }).start();
    }

    private void updateOverview(ClassLearningReport report) {
        if (report == null) {
            overviewLabel.setText("暂无班级报告数据");
            return;
        }

        overviewLabel.setText(
                "课程ID：" + safe(report.getCourseId())
                        + "\n参与学生数：" + safe(report.getStudentCount())
                        + "\n班级平均学习完成率：" + safe(report.getAverageStudyProgress()) + "%"
                        + "\n班级答题总次数：" + safe(report.getTotalAnswerCount())
                        + "\n班级答对数量：" + safe(report.getTotalCorrectCount())
                        + "\n班级答错数量：" + safe(report.getTotalWrongCount())
                        + "\n班级平均测验正确率：" + safe(report.getAverageQuizAccuracy()) + "%"
                        + "\n班级平均得分：" + safe(report.getAverageScore())
        );
    }

    private void updateWeakPoints(ClassLearningReport report) {
        if (report == null || report.getWeakKnowledgePoints() == null || report.getWeakKnowledgePoints().isEmpty()) {
            weakPointLabel.setText("当前暂无明显班级薄弱知识点。");
            return;
        }

        StringBuilder builder = new StringBuilder();
        builder.append("薄弱知识点分布：\n");

        for (Map.Entry<String, Integer> entry : report.getWeakKnowledgePoints().entrySet()) {
            builder.append(" - ")
                    .append(entry.getKey())
                    .append("：")
                    .append(entry.getValue())
                    .append(" 次\n");
        }

        weakPointLabel.setText(builder.toString());
    }

    private void updateSuggestion(ClassLearningReport report) {
        if (report == null || report.getTeachingSuggestion() == null) {
            suggestionLabel.setText("暂无教学建议。");
            return;
        }

        suggestionLabel.setText(report.getTeachingSuggestion());
    }

    private String buildStudentText(ClassStudentReport item) {
        return "学生ID：" + safe(item.getStudentId())
                + "\n学生姓名：" + safe(item.getStudentName())
                + "\n班级：" + safe(item.getClassName())
                + "\n课程ID：" + safe(item.getCourseId())
                + "\n学习进度：" + safe(item.getCompletedSections()) + " / " + safe(item.getTotalSections())
                + "\n学习完成率：" + safe(item.getStudyProgressPercent()) + "%"
                + "\n答题次数：" + safe(item.getAnswerCount())
                + "\n答对数量：" + safe(item.getCorrectCount())
                + "\n答错数量：" + safe(item.getWrongCount())
                + "\n测验正确率：" + safe(item.getQuizAccuracy()) + "%"
                + "\n平均得分：" + safe(item.getAverageScore())
                + "\n错题数量：" + safe(item.getWrongQuestionCount());
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