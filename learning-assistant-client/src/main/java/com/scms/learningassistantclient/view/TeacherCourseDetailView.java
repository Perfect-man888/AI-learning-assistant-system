package com.scms.learningassistantclient.view;

import com.scms.learningassistantclient.api.ChapterApi;
import com.scms.learningassistantclient.model.CourseChapter;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.util.List;

public class TeacherCourseDetailView extends BorderPane {

    private final Long courseId;
    private final String courseName;

    private final ChapterApi chapterApi = new ChapterApi();

    private final ListView<CourseChapter> chapterListView = new ListView<>();
    private final TextField chapterTitleField = new TextField();
    private final TextField sortOrderField = new TextField();
    private final Label messageLabel = new Label();

    private Runnable onBack;

    public TeacherCourseDetailView(Long courseId, String courseName) {
        this(courseId, courseName, null);
    }

    public TeacherCourseDetailView(Long courseId, String courseName, Runnable onBack) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.onBack = onBack;

        initView();
        loadChapters();
    }

    private void initView() {
        setPadding(new Insets(30));

        Label titleLabel = new Label("教师端 - 课程详情");
        titleLabel.setStyle("-fx-font-size: 28px; -fx-font-weight: bold;");

        Label courseLabel = new Label("当前课程：" + courseName + "  /  课程ID：" + courseId);
        courseLabel.setStyle("-fx-font-size: 17px;");

        VBox topBox = new VBox(12, titleLabel, courseLabel);
        topBox.setAlignment(Pos.CENTER);
        topBox.setPadding(new Insets(0, 0, 25, 0));
        setTop(topBox);

        chapterTitleField.setPromptText("请输入章节名称，例如：第一章 函数与极限");
        chapterTitleField.setPrefWidth(520);

        sortOrderField.setPromptText("排序号，例如：1");
        sortOrderField.setPrefWidth(160);

        Button addButton = new Button("新增章节");
        Button refreshButton = new Button("刷新章节");
        Button enterChapterButton = new Button("进入选中章节");

        Button questionButton = new Button("题库管理");
        Button taskButton = new Button("测验任务");
        Button progressButton = new Button("学习进度");
        Button classReportButton = new Button("班级报告");

        Button backButton = new Button("返回课程管理");

        questionButton.setOnAction(e -> {
            Scene currentScene = this.getScene();
            currentScene.setRoot(new TeacherQuestionView(courseId, courseName).getView());
        });

        taskButton.setOnAction(e -> {
            Scene currentScene = this.getScene();
            currentScene.setRoot(new TeacherTaskView(courseId, courseName).getView());
        });

        progressButton.setOnAction(e -> {
            Scene currentScene = this.getScene();
            currentScene.setRoot(new TeacherProgressView(courseId, courseName).getView());
        });

        classReportButton.setOnAction(e -> {
            Scene currentScene = this.getScene();
            currentScene.setRoot(new TeacherClassReportView(courseId, courseName).getView());
        });

        addButton.setMinWidth(110);
        refreshButton.setMinWidth(110);
        enterChapterButton.setMinWidth(140);
        backButton.setMinWidth(140);

        addButton.setOnAction(e -> addChapter());
        refreshButton.setOnAction(e -> loadChapters());
        enterChapterButton.setOnAction(e -> enterSelectedChapter());

        backButton.setOnAction(e -> {
            if (onBack != null) {
                onBack.run();
            } else {
                messageLabel.setStyle("-fx-font-size: 15px; -fx-text-fill: red;");
                messageLabel.setText("当前是测试页面，暂时没有可返回的课程管理页");
            }
        });

        HBox inputFieldBox = new HBox(15, chapterTitleField, sortOrderField);
        inputFieldBox.setAlignment(Pos.CENTER);

        questionButton.setMinWidth(120);
        taskButton.setMinWidth(120);
        progressButton.setMinWidth(120);
        classReportButton.setMinWidth(120);

        HBox buttonBox1 = new HBox(
                12,
                addButton,
                refreshButton,
                enterChapterButton,
                backButton
        );
        buttonBox1.setAlignment(Pos.CENTER);

        HBox buttonBox2 = new HBox(
                12,
                questionButton,
                taskButton,
                progressButton,
                classReportButton
        );
        buttonBox2.setAlignment(Pos.CENTER);

        VBox inputBox = new VBox(12, inputFieldBox, buttonBox1, buttonBox2);
        inputBox.setAlignment(Pos.CENTER);
        inputBox.setPadding(new Insets(10));

        Label listTitle = new Label("章节列表：");
        listTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        chapterListView.setPrefHeight(360);
        chapterListView.setCellFactory(listView -> new ListCell<>() {
            @Override
            protected void updateItem(CourseChapter chapter, boolean empty) {
                super.updateItem(chapter, empty);

                if (empty || chapter == null) {
                    setText(null);
                } else {
                    setText(
                            "章节ID：" + chapter.getId()
                                    + "\n章节名称：" + chapter.getChapterTitle()
                                    + "\n排序：" + chapter.getSortOrder()
                    );
                }
            }
        });

        messageLabel.setStyle("-fx-font-size: 15px; -fx-text-fill: green;");

        VBox centerBox = new VBox(15, inputBox, listTitle, chapterListView, messageLabel);
        centerBox.setPadding(new Insets(10));
        setCenter(centerBox);
    }

    private void loadChapters() {
        messageLabel.setStyle("-fx-font-size: 15px; -fx-text-fill: #333333;");
        messageLabel.setText("正在加载章节...");

        new Thread(() -> {
            try {
                List<CourseChapter> chapters = chapterApi.getChaptersByCourseId(courseId);

                Platform.runLater(() -> {
                    chapterListView.setItems(FXCollections.observableArrayList(chapters));
                    messageLabel.setStyle("-fx-font-size: 15px; -fx-text-fill: green;");
                    messageLabel.setText("章节加载成功，共 " + chapters.size() + " 个章节");
                });

            } catch (Exception e) {
                e.printStackTrace();

                Platform.runLater(() -> {
                    messageLabel.setStyle("-fx-font-size: 15px; -fx-text-fill: red;");
                    messageLabel.setText("章节加载失败：" + e.getMessage());
                });
            }
        }).start();
    }

    private void addChapter() {
        String chapterTitle = chapterTitleField.getText();

        if (chapterTitle == null || chapterTitle.trim().isEmpty()) {
            messageLabel.setStyle("-fx-font-size: 15px; -fx-text-fill: red;");
            messageLabel.setText("章节名称不能为空");
            return;
        }

        Integer sortOrder;

        try {
            String sortText = sortOrderField.getText();

            if (sortText == null || sortText.trim().isEmpty()) {
                sortOrder = chapterListView.getItems().size() + 1;
            } else {
                sortOrder = Integer.parseInt(sortText.trim());
            }

        } catch (Exception e) {
            messageLabel.setStyle("-fx-font-size: 15px; -fx-text-fill: red;");
            messageLabel.setText("排序号必须是数字");
            return;
        }

        messageLabel.setStyle("-fx-font-size: 15px; -fx-text-fill: green;");
        messageLabel.setText("正在新增章节...");

        new Thread(() -> {
            try {
                chapterApi.createChapter(courseId, chapterTitle.trim(), sortOrder);

                Platform.runLater(() -> {
                    chapterTitleField.clear();
                    sortOrderField.clear();
                    messageLabel.setText("新增章节成功");
                    loadChapters();
                });

            } catch (Exception e) {
                e.printStackTrace();

                Platform.runLater(() -> {
                    messageLabel.setStyle("-fx-font-size: 15px; -fx-text-fill: red;");
                    messageLabel.setText("新增章节失败：" + e.getMessage());
                });
            }
        }).start();
    }

    private void enterSelectedChapter() {
        CourseChapter selectedChapter = chapterListView.getSelectionModel().getSelectedItem();

        if (selectedChapter == null) {
            messageLabel.setStyle("-fx-font-size: 15px; -fx-text-fill: red;");
            messageLabel.setText("请先选中一个章节");
            return;
        }

        Scene currentScene = this.getScene();

        TeacherChapterDetailView detailView = new TeacherChapterDetailView(
                selectedChapter.getId(),
                selectedChapter.getChapterTitle(),
                () -> currentScene.setRoot(new TeacherCourseDetailView(courseId, courseName, onBack))
        );

        currentScene.setRoot(detailView);
    }
}