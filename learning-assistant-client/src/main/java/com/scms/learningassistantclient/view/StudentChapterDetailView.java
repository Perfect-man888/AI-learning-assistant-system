package com.scms.learningassistantclient.view;

import com.scms.learningassistantclient.api.SectionApi;
import com.scms.learningassistantclient.api.StudyProgressApi;
import com.scms.learningassistantclient.model.CourseSection;
import com.scms.learningassistantclient.model.LoginUser;
import com.scms.learningassistantclient.model.StudyProgress;
import com.scms.learningassistantclient.util.AppContext;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class StudentChapterDetailView extends BorderPane {

    private final Long courseId;
    private final Long chapterId;
    private final String chapterTitle;
    private final Runnable onBack;

    private final SectionApi sectionApi = new SectionApi();
    private final StudyProgressApi studyProgressApi = new StudyProgressApi();

    private final ListView<CourseSection> sectionListView = new ListView<>();
    private final TextArea contentArea = new TextArea();
    private final Label knowledgePointsLabel = new Label();
    private final Label messageLabel = new Label();

    // 保存当前学生已经学过的小节ID
    private final Set<Long> completedSectionIds = new HashSet<>();

    public StudentChapterDetailView(Long courseId, Long chapterId, String chapterTitle, Runnable onBack) {
        this.courseId = courseId;
        this.chapterId = chapterId;
        this.chapterTitle = chapterTitle;
        this.onBack = onBack;

        initView();
        loadSections();
    }

    private void initView() {
        setPadding(new Insets(30));

        Label titleLabel = new Label("学生端 - 章节学习");
        titleLabel.setStyle("-fx-font-size: 28px; -fx-font-weight: bold;");

        Label chapterLabel = new Label("当前章节：" + chapterTitle + "  /  章节ID：" + chapterId);
        chapterLabel.setStyle("-fx-font-size: 17px;");

        VBox topBox = new VBox(12, titleLabel, chapterLabel);
        topBox.setAlignment(Pos.CENTER);
        topBox.setPadding(new Insets(0, 0, 20, 0));
        setTop(topBox);

        Label listTitle = new Label("小节列表：");
        listTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        sectionListView.setPrefHeight(260);
        sectionListView.setCellFactory(listView -> new ListCell<>() {
            @Override
            protected void updateItem(CourseSection section, boolean empty) {
                super.updateItem(section, empty);

                if (empty || section == null) {
                    setText(null);
                } else {
                    boolean completed = completedSectionIds.contains(section.getId());
                    String statusText = completed ? "已学习" : "未学习";

                    setText(
                            "小节ID：" + section.getId()
                                    + "\n小节标题：" + section.getSectionTitle()
                                    + "\n知识点：" + section.getKnowledgePoints()
                                    + "\n状态：" + statusText
                                    + "\n排序：" + section.getSortOrder()
                    );
                }
            }
        });

        Button viewSectionButton = new Button("查看选中小节");
        Button refreshButton = new Button("刷新小节");
        Button backButton = new Button("返回课程学习");

        viewSectionButton.setMinWidth(130);
        refreshButton.setMinWidth(100);
        backButton.setMinWidth(130);

        viewSectionButton.setOnAction(e -> viewSelectedSection());
        refreshButton.setOnAction(e -> loadSections());

        backButton.setOnAction(e -> {
            if (onBack != null) {
                onBack.run();
            } else {
                messageLabel.setStyle("-fx-font-size: 15px; -fx-text-fill: red;");
                messageLabel.setText("当前是测试页面，暂时没有可返回的课程学习页");
            }
        });

        HBox buttonBox = new HBox(15, viewSectionButton, refreshButton, backButton);
        buttonBox.setAlignment(Pos.CENTER);

        Label contentTitle = new Label("小节内容：");
        contentTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        contentArea.setEditable(false);
        contentArea.setWrapText(true);
        contentArea.setPrefHeight(160);
        contentArea.setPromptText("请选择小节后点击“查看选中小节”");

        knowledgePointsLabel.setStyle("-fx-font-size: 15px; -fx-text-fill: #333333;");
        messageLabel.setStyle("-fx-font-size: 15px; -fx-text-fill: green;");

        VBox centerBox = new VBox(
                15,
                listTitle,
                sectionListView,
                buttonBox,
                contentTitle,
                contentArea,
                knowledgePointsLabel,
                messageLabel
        );

        centerBox.setPadding(new Insets(10));
        setCenter(centerBox);
    }

    private void loadSections() {
        messageLabel.setStyle("-fx-font-size: 15px; -fx-text-fill: #333333;");
        messageLabel.setText("正在加载小节和学习状态...");

        LoginUser user = AppContext.getCurrentUser();

        new Thread(() -> {
            try {
                List<CourseSection> sections = sectionApi.getSectionsByChapterId(chapterId);

                Set<Long> completedIds = new HashSet<>();

                if (user != null && user.getId() != null) {
                    List<StudyProgress> progressList =
                            studyProgressApi.getStudentCourseProgress(user.getId(), courseId);

                    for (StudyProgress progress : progressList) {
                        if (progress.getSectionId() != null
                                && "completed".equalsIgnoreCase(progress.getStatus())) {
                            completedIds.add(progress.getSectionId());
                        }
                    }
                }

                Platform.runLater(() -> {
                    completedSectionIds.clear();
                    completedSectionIds.addAll(completedIds);

                    sectionListView.setItems(FXCollections.observableArrayList(sections));
                    sectionListView.refresh();

                    messageLabel.setStyle("-fx-font-size: 15px; -fx-text-fill: green;");
                    messageLabel.setText("小节加载成功，共 " + sections.size() + " 个小节");
                });

            } catch (Exception e) {
                e.printStackTrace();

                Platform.runLater(() -> {
                    messageLabel.setStyle("-fx-font-size: 15px; -fx-text-fill: red;");
                    messageLabel.setText("小节或学习状态加载失败：" + e.getMessage());
                });
            }
        }).start();
    }

    private void viewSelectedSection() {
        CourseSection selectedSection = sectionListView.getSelectionModel().getSelectedItem();

        if (selectedSection == null) {
            messageLabel.setStyle("-fx-font-size: 15px; -fx-text-fill: red;");
            messageLabel.setText("请先选中一个小节");
            return;
        }

        contentArea.setText(selectedSection.getContent() == null ? "" : selectedSection.getContent());

        knowledgePointsLabel.setText(
                "知识点：" + (selectedSection.getKnowledgePoints() == null ? "暂无" : selectedSection.getKnowledgePoints())
        );

        messageLabel.setStyle("-fx-font-size: 15px; -fx-text-fill: #333333;");
        messageLabel.setText("正在记录学习进度...");

        recordStudyProgress(selectedSection);
    }

    private void recordStudyProgress(CourseSection selectedSection) {
        LoginUser user = AppContext.getCurrentUser();

        if (user == null || user.getId() == null) {
            messageLabel.setStyle("-fx-font-size: 15px; -fx-text-fill: red;");
            messageLabel.setText("当前学生信息为空，无法记录学习进度");
            return;
        }

        new Thread(() -> {
            try {
                studyProgressApi.completeSection(
                        courseId,
                        chapterId,
                        selectedSection.getId(),
                        user.getId()
                );

                Platform.runLater(() -> {
                    completedSectionIds.add(selectedSection.getId());
                    sectionListView.refresh();

                    messageLabel.setStyle("-fx-font-size: 15px; -fx-text-fill: green;");
                    messageLabel.setText(
                            "正在学习：" + selectedSection.getSectionTitle()
                                    + "，学习进度已记录"
                    );
                });

            } catch (Exception e) {
                e.printStackTrace();

                Platform.runLater(() -> {
                    messageLabel.setStyle("-fx-font-size: 15px; -fx-text-fill: red;");
                    messageLabel.setText("学习进度记录失败：" + e.getMessage());
                });
            }
        }).start();
    }
}