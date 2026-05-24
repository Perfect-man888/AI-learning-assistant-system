package com.scms.learningassistantclient.view;

import com.scms.learningassistantclient.api.ChapterApi;
import com.scms.learningassistantclient.model.CourseChapter;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.Scene;

import java.util.List;

public class StudentCourseDetailView extends BorderPane {

    private final Long courseId;
    private final String courseName;
    private final Runnable onBack;

    private final ChapterApi chapterApi = new ChapterApi();

    private final ListView<CourseChapter> chapterListView = new ListView<>();
    private final Label messageLabel = new Label();

    public StudentCourseDetailView(Long courseId, String courseName, Runnable onBack) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.onBack = onBack;

        initView();
        loadChapters();
    }

    private void initView() {
        setPadding(new Insets(30));

        Label titleLabel = new Label("学生端 - 课程学习");
        titleLabel.setStyle("-fx-font-size: 28px; -fx-font-weight: bold;");

        Label courseLabel = new Label("当前课程：" + courseName + "  /  课程ID：" + courseId);
        courseLabel.setStyle("-fx-font-size: 17px;");

        VBox topBox = new VBox(12, titleLabel, courseLabel);
        topBox.setAlignment(Pos.CENTER);
        topBox.setPadding(new Insets(0, 0, 25, 0));

        setTop(topBox);

        Label listTitle = new Label("章节列表：");
        listTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        chapterListView.setPrefHeight(420);
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

        Button enterChapterButton = new Button("进入选中章节");
        Button refreshButton = new Button("刷新章节");
        Button backButton = new Button("返回我的课程");

        enterChapterButton.setOnAction(e -> enterSelectedChapter());
        refreshButton.setOnAction(e -> loadChapters());

        backButton.setOnAction(e -> {
            if (onBack != null) {
                onBack.run();
            } else {
                messageLabel.setStyle("-fx-font-size: 15px; -fx-text-fill: red;");
                messageLabel.setText("当前是测试页面，暂时没有可返回的我的课程页");
            }
        });

        HBox buttonBox = new HBox(15, enterChapterButton, refreshButton, backButton);
        buttonBox.setAlignment(Pos.CENTER);

        messageLabel.setStyle("-fx-font-size: 15px; -fx-text-fill: green;");

        VBox centerBox = new VBox(15, listTitle, chapterListView, buttonBox, messageLabel);
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

    private void enterSelectedChapter() {
        CourseChapter selectedChapter = chapterListView.getSelectionModel().getSelectedItem();

        if (selectedChapter == null) {
            messageLabel.setStyle("-fx-font-size: 15px; -fx-text-fill: red;");
            messageLabel.setText("请先选中一个章节");
            return;
        }

        javafx.scene.Scene currentScene = this.getScene();

        StudentChapterDetailView detailView = new StudentChapterDetailView(
                courseId,
                selectedChapter.getId(),
                selectedChapter.getChapterTitle(),
                () -> currentScene.setRoot(new StudentCourseDetailView(courseId, courseName, onBack))
        );

        currentScene.setRoot(detailView);
    }
}