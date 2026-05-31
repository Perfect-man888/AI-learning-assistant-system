package com.scms.learningassistantclient.view;

import com.scms.learningassistantclient.api.ReviewTaskApi;
import com.scms.learningassistantclient.api.WrongQuestionApi;
import com.scms.learningassistantclient.model.LearningTask;
import com.scms.learningassistantclient.model.LoginUser;
import com.scms.learningassistantclient.model.WrongQuestion;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StudentWrongBookView {

    private final VBox root = new VBox();

    private final WrongQuestionApi wrongQuestionApi = new WrongQuestionApi();
    private final ReviewTaskApi reviewTaskApi = new ReviewTaskApi();

    private final Long courseId;
    private final String courseName;

    private final TextField reviewCountField = new TextField("5");

    private final Label summaryLabel = new Label("暂无错题统计");
    private final ListView<WrongQuestion> wrongListView = new ListView<>();
    private final TextArea detailArea = new TextArea("请选择一条错题记录");
    private final Label messageLabel = new Label();

    private WrongQuestion selectedWrongQuestion;

    public StudentWrongBookView(Long courseId, String courseName) {
        this.courseId = courseId;
        this.courseName = courseName;
        createView();
        loadWrongBook();
    }

    private void createView() {
        root.setSpacing(16);
        root.setPadding(new Insets(30));
        root.setAlignment(Pos.TOP_CENTER);

        Label titleLabel = new Label("学生端 - 我的错题本 / 个性化复习");
        titleLabel.setFont(new Font(26));

        Label courseLabel = new Label("当前课程：" + courseName + " / 课程ID：" + courseId);
        courseLabel.setStyle("-fx-font-size: 16px;");

        reviewCountField.setPromptText("复习题数量");
        reviewCountField.setMaxWidth(120);

        Button refreshButton = new Button("刷新错题本");
        Button markMasteredButton = new Button("标记为已掌握");
        Button generateReviewTaskButton = new Button("生成个性化复习任务");
        Button backButton = new Button("返回课程学习");

        refreshButton.setMinWidth(130);
        markMasteredButton.setMinWidth(130);
        generateReviewTaskButton.setMinWidth(180);
        backButton.setMinWidth(130);

        HBox paramBox = new HBox(
                12,
                new Label("复习题数量："),
                reviewCountField
        );
        paramBox.setAlignment(Pos.CENTER);

        HBox buttonBox = new HBox(
                12,
                refreshButton,
                markMasteredButton,
                generateReviewTaskButton,
                backButton
        );
        buttonBox.setAlignment(Pos.CENTER);

        summaryLabel.setPrefWidth(920);
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
        wrongListView.setPrefHeight(360);

        wrongListView.setCellFactory(listView -> new ListCell<>() {
            @Override
            protected void updateItem(WrongQuestion item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                    return;
                }

                setText(buildListText(item));
                setWrapText(true);
                setPrefWidth(wrongListView.getPrefWidth() - 30);
                setMinHeight(Region.USE_PREF_SIZE);

                if (item.mastered()) {
                    setStyle(
                            "-fx-font-size: 15px;" +
                                    "-fx-padding: 12px;" +
                                    "-fx-background-color: #f6ffed;" +
                                    "-fx-border-color: #b7eb8f;" +
                                    "-fx-border-width: 0 0 1 0;"
                    );
                } else {
                    setStyle(
                            "-fx-font-size: 15px;" +
                                    "-fx-padding: 12px;" +
                                    "-fx-background-color: #fff7e6;" +
                                    "-fx-border-color: #ffd591;" +
                                    "-fx-border-width: 0 0 1 0;"
                    );
                }
            }
        });

        wrongListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            selectedWrongQuestion = newVal;
            showDetail(newVal);
        });

        detailArea.setPrefWidth(920);
        detailArea.setPrefHeight(260);
        detailArea.setWrapText(true);
        detailArea.setEditable(false);
        detailArea.setStyle(
                "-fx-font-size: 15px;" +
                        "-fx-control-inner-background: #eef8ff;" +
                        "-fx-border-color: #b9def5;"
        );

        messageLabel.setWrapText(true);
        messageLabel.setPrefWidth(920);
        messageLabel.setMaxWidth(920);
        messageLabel.setMinHeight(70);
        messageLabel.setAlignment(Pos.CENTER_LEFT);
        messageLabel.setStyle(
                "-fx-font-size: 15px;" +
                        "-fx-text-fill: green;" +
                        "-fx-padding: 10px;" +
                        "-fx-background-color: #f6ffed;" +
                        "-fx-border-color: #b7eb8f;"
        );

        refreshButton.setOnAction(e -> loadWrongBook());
        markMasteredButton.setOnAction(e -> markSelectedMastered());
        generateReviewTaskButton.setOnAction(e -> generateReviewTask());

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
                paramBox,
                buttonBox,
                new Label("错题统计："),
                summaryLabel,
                new Label("错题记录："),
                wrongListView,
                new Label("错题详情："),
                detailArea,
                messageLabel
        );
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
                List<WrongQuestion> wrongList =
                        wrongQuestionApi.getWrongQuestionsByStudentId(studentId);

                Platform.runLater(() -> {
                    wrongListView.setItems(FXCollections.observableArrayList(wrongList));
                    updateSummary(wrongList);

                    if (wrongList == null || wrongList.isEmpty()) {
                        selectedWrongQuestion = null;
                        detailArea.setText("当前暂无错题记录");
                        setMessage("当前暂无错题记录", "green");
                    } else {
                        wrongListView.getSelectionModel().select(0);
                        setMessage("错题本加载成功，共 " + wrongList.size() + " 条错题记录", "green");
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
                Platform.runLater(() -> setMessage("错题本加载失败：" + e.getMessage(), "red"));
            }
        }).start();
    }

    private void markSelectedMastered() {
        if (selectedWrongQuestion == null || selectedWrongQuestion.getId() == null) {
            setMessage("请先选择一条错题记录", "red");
            return;
        }

        if (selectedWrongQuestion.mastered()) {
            setMessage("该错题已经是已掌握状态", "green");
            return;
        }

        Long wrongQuestionId = selectedWrongQuestion.getId();

        setMessage("正在标记为已掌握...", "#333333");

        new Thread(() -> {
            try {
                wrongQuestionApi.markMastered(wrongQuestionId);

                Platform.runLater(() -> {
                    setMessage("标记成功，正在刷新错题本...", "green");
                    loadWrongBook();
                });

            } catch (Exception e) {
                e.printStackTrace();
                Platform.runLater(() -> setMessage("标记失败：" + e.getMessage(), "red"));
            }
        }).start();
    }

    private void generateReviewTask() {
        LoginUser user = AppContext.getCurrentUser();

        if (user == null || user.getId() == null) {
            setMessage("未获取到当前学生信息，请重新登录", "red");
            return;
        }

        if (courseId == null) {
            setMessage("课程ID为空，无法生成复习任务", "red");
            return;
        }

        Integer count = parseInt(reviewCountField.getText(), "复习题数量");

        if (count == null) {
            return;
        }

        setMessage("正在根据错题本生成个性化复习任务...", "#333333");

        new Thread(() -> {
            try {
                LearningTask task = reviewTaskApi.generateReviewTask(user.getId(), courseId, count);

                Platform.runLater(() -> setMessage(
                        "个性化复习任务生成成功！\n"
                                + "任务ID：" + task.getId() + "\n"
                                + "任务标题：" + task.getTaskTitle() + "\n"
                                + "请进入【课程测验】，系统会自动加载当前课程任务，或使用任务ID："
                                + task.getId() + " 开始复习。",
                        "green"
                ));

            } catch (Exception e) {
                e.printStackTrace();
                Platform.runLater(() -> setMessage(e.getMessage(), "red"));
            }
        }).start();
    }

    private void updateSummary(List<WrongQuestion> wrongList) {
        if (wrongList == null || wrongList.isEmpty()) {
            summaryLabel.setText(
                    "当前暂无错题记录。\n" +
                            "继续保持！如果后续答错题目，系统会自动记录到这里。"
            );
            return;
        }

        int total = wrongList.size();
        int masteredCount = 0;
        int notMasteredCount = 0;
        int totalWrongTimes = 0;

        Map<String, Integer> knowledgeCountMap = new HashMap<>();

        for (WrongQuestion item : wrongList) {
            if (item.mastered()) {
                masteredCount++;
            } else {
                notMasteredCount++;
            }

            if (item.getWrongCount() != null) {
                totalWrongTimes += item.getWrongCount();
            }

            String point = item.getKnowledgePointName();

            if (point == null || point.trim().isEmpty()) {
                point = "未标注知识点";
            }

            knowledgeCountMap.put(point, knowledgeCountMap.getOrDefault(point, 0) + 1);
        }

        StringBuilder builder = new StringBuilder();

        builder.append("错题记录数量：").append(total).append(" 条\n");
        builder.append("历史错误总次数：").append(totalWrongTimes).append(" 次\n");
        builder.append("未掌握错题：").append(notMasteredCount).append(" 条\n");
        builder.append("已掌握错题：").append(masteredCount).append(" 条\n\n");

        builder.append("薄弱知识点分布：\n");

        for (Map.Entry<String, Integer> entry : knowledgeCountMap.entrySet()) {
            builder.append(" - ")
                    .append(entry.getKey())
                    .append("：")
                    .append(entry.getValue())
                    .append(" 条错题记录\n");
        }

        builder.append("\n学习建议：优先复习未掌握错题对应的知识点，再进行同类题训练。");

        summaryLabel.setText(builder.toString());
    }

    private String buildListText(WrongQuestion item) {
        return "错题ID：" + safe(item.getId())
                + "    题目ID：" + safe(item.getQuestionId())
                + "    课程ID：" + safe(item.getCourseId())
                + "\n知识点：" + safe(item.getKnowledgePointName())
                + "    错误次数：" + safe(item.getWrongCount())
                + "    状态：" + (item.mastered() ? "已掌握" : "未掌握")
                + "\n最近错误时间：" + safe(item.getLastWrongTime());
    }

    private void showDetail(WrongQuestion item) {
        if (item == null) {
            detailArea.setText("请选择一条错题记录");
            return;
        }

        detailArea.setText(buildDetailText(item));
    }

    private String buildDetailText(WrongQuestion item) {
        return "错题记录ID：" + safe(item.getId())
                + "\n学生ID：" + safe(item.getStudentId())
                + "\n题目ID：" + safe(item.getQuestionId())
                + "\n课程ID：" + safe(item.getCourseId())
                + "\n章节ID：" + safe(item.getChapterId())
                + "\n小节ID：" + safe(item.getSectionId())
                + "\n知识点ID：" + safe(item.getKnowledgePointId())
                + "\n知识点名称：" + safe(item.getKnowledgePointName())
                + "\n错误次数：" + safe(item.getWrongCount())
                + "\n掌握状态：" + (item.mastered() ? "已掌握" : "未掌握")
                + "\n最近错误时间：" + safe(item.getLastWrongTime())
                + "\n更新时间：" + safe(item.getUpdateTime())
                + "\n\n复习建议：请重新复习【" + safe(item.getKnowledgePointName()) + "】相关知识点，并完成同类题巩固。";
    }

    private Integer parseInt(String text, String fieldName) {
        if (text == null || text.trim().isEmpty()) {
            setMessage("请输入" + fieldName, "red");
            return null;
        }

        try {
            return Integer.parseInt(text.trim());
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
        messageLabel.setWrapText(true);
        messageLabel.setPrefWidth(920);
        messageLabel.setMaxWidth(920);
        messageLabel.setMinHeight(70);
        messageLabel.setAlignment(Pos.CENTER_LEFT);
        messageLabel.setStyle(
                "-fx-font-size: 15px;" +
                        "-fx-text-fill: " + color + ";" +
                        "-fx-padding: 10px;" +
                        "-fx-background-color: #f6ffed;" +
                        "-fx-border-color: #b7eb8f;"
        );
        messageLabel.setText(text);
    }

    public Parent getView() {
        return root;
    }
}