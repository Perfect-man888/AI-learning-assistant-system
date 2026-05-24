package com.scms.learningassistantclient.view;

import com.scms.learningassistantclient.api.SectionApi;
import com.scms.learningassistantclient.model.CourseSection;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.util.List;

public class TeacherChapterDetailView extends BorderPane {

    private final Long chapterId;
    private final String chapterTitle;
    private final Runnable onBack;

    private final SectionApi sectionApi = new SectionApi();

    private final ListView<CourseSection> sectionListView = new ListView<>();
    private final TextField sectionTitleField = new TextField();
    private final TextArea contentArea = new TextArea();
    private final TextField knowledgePointsField = new TextField();
    private final TextField sortOrderField = new TextField();
    private final Label messageLabel = new Label();

    public TeacherChapterDetailView(Long chapterId, String chapterTitle, Runnable onBack) {
        this.chapterId = chapterId;
        this.chapterTitle = chapterTitle;
        this.onBack = onBack;

        initView();
        loadSections();
    }

    private void initView() {
        setPadding(new Insets(30));

        Label titleLabel = new Label("教师端 - 章节详情");
        titleLabel.setStyle("-fx-font-size: 28px; -fx-font-weight: bold;");

        Label chapterLabel = new Label("当前章节：" + chapterTitle + "  /  章节ID：" + chapterId);
        chapterLabel.setStyle("-fx-font-size: 17px;");

        VBox topBox = new VBox(12, titleLabel, chapterLabel);
        topBox.setAlignment(Pos.CENTER);
        topBox.setPadding(new Insets(0, 0, 20, 0));
        setTop(topBox);

        sectionTitleField.setPromptText("请输入小节标题，例如：函数的概念");
        sectionTitleField.setPrefWidth(520);

        sortOrderField.setPromptText("排序号，例如：1");
        sortOrderField.setPrefWidth(160);

        contentArea.setPromptText("请输入小节学习内容");
        contentArea.setPrefRowCount(4);
        contentArea.setPrefWidth(780);

        knowledgePointsField.setPromptText("请输入知识点，例如：函数定义,定义域,值域");
        knowledgePointsField.setPrefWidth(780);

        Button addButton = new Button("新增小节");
        Button refreshButton = new Button("刷新小节");
        Button backButton = new Button("返回课程详情");

        addButton.setMinWidth(110);
        refreshButton.setMinWidth(110);
        backButton.setMinWidth(140);

        addButton.setOnAction(e -> addSection());
        refreshButton.setOnAction(e -> loadSections());

        backButton.setOnAction(e -> {
            if (onBack != null) {
                onBack.run();
            } else {
                messageLabel.setStyle("-fx-font-size: 15px; -fx-text-fill: red;");
                messageLabel.setText("当前是测试页面，暂时没有可返回的课程详情页");
            }
        });

        HBox sectionFieldBox = new HBox(15, sectionTitleField, sortOrderField);
        sectionFieldBox.setAlignment(Pos.CENTER);

        HBox buttonBox = new HBox(15, addButton, refreshButton, backButton);
        buttonBox.setAlignment(Pos.CENTER);

        VBox inputBox = new VBox(
                12,
                sectionFieldBox,
                contentArea,
                knowledgePointsField,
                buttonBox
        );
        inputBox.setAlignment(Pos.CENTER);

        Label listTitle = new Label("小节列表：");
        listTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        sectionListView.setPrefHeight(300);
        sectionListView.setCellFactory(listView -> new ListCell<>() {
            @Override
            protected void updateItem(CourseSection section, boolean empty) {
                super.updateItem(section, empty);

                if (empty || section == null) {
                    setText(null);
                } else {
                    setText(
                            "小节ID：" + section.getId()
                                    + "\n小节标题：" + section.getSectionTitle()
                                    + "\n知识点：" + section.getKnowledgePoints()
                                    + "\n排序：" + section.getSortOrder()
                    );
                }
            }
        });

        messageLabel.setStyle("-fx-font-size: 15px; -fx-text-fill: green;");

        VBox centerBox = new VBox(15, inputBox, listTitle, sectionListView, messageLabel);
        centerBox.setPadding(new Insets(10));
        setCenter(centerBox);
    }

    private void loadSections() {
        messageLabel.setStyle("-fx-font-size: 15px; -fx-text-fill: #333333;");
        messageLabel.setText("正在加载小节...");

        new Thread(() -> {
            try {
                List<CourseSection> sections = sectionApi.getSectionsByChapterId(chapterId);

                Platform.runLater(() -> {
                    sectionListView.setItems(FXCollections.observableArrayList(sections));
                    messageLabel.setStyle("-fx-font-size: 15px; -fx-text-fill: green;");
                    messageLabel.setText("小节加载成功，共 " + sections.size() + " 个小节");
                });

            } catch (Exception e) {
                e.printStackTrace();

                Platform.runLater(() -> {
                    messageLabel.setStyle("-fx-font-size: 15px; -fx-text-fill: red;");
                    messageLabel.setText("小节加载失败：" + e.getMessage());
                });
            }
        }).start();
    }

    private void addSection() {
        String sectionTitle = sectionTitleField.getText() == null ? "" : sectionTitleField.getText().trim();
        String content = contentArea.getText() == null ? "" : contentArea.getText().trim();
        String knowledgePoints = knowledgePointsField.getText() == null ? "" : knowledgePointsField.getText().trim();

        if (sectionTitle.isBlank()) {
            messageLabel.setStyle("-fx-font-size: 15px; -fx-text-fill: red;");
            messageLabel.setText("小节标题不能为空");
            return;
        }

        Integer sortOrder;

        try {
            String sortText = sortOrderField.getText();

            if (sortText == null || sortText.trim().isEmpty()) {
                sortOrder = sectionListView.getItems().size() + 1;
            } else {
                sortOrder = Integer.parseInt(sortText.trim());
            }

        } catch (Exception e) {
            messageLabel.setStyle("-fx-font-size: 15px; -fx-text-fill: red;");
            messageLabel.setText("排序号必须是数字");
            return;
        }

        messageLabel.setStyle("-fx-font-size: 15px; -fx-text-fill: #333333;");
        messageLabel.setText("正在新增小节...");

        new Thread(() -> {
            try {
                sectionApi.createSection(
                        chapterId,
                        sectionTitle,
                        content,
                        knowledgePoints,
                        sortOrder
                );

                Platform.runLater(() -> {
                    sectionTitleField.clear();
                    contentArea.clear();
                    knowledgePointsField.clear();
                    sortOrderField.clear();

                    messageLabel.setStyle("-fx-font-size: 15px; -fx-text-fill: green;");
                    messageLabel.setText("新增小节成功");
                    loadSections();
                });

            } catch (Exception e) {
                e.printStackTrace();

                Platform.runLater(() -> {
                    messageLabel.setStyle("-fx-font-size: 15px; -fx-text-fill: red;");
                    messageLabel.setText("新增小节失败：" + e.getMessage());
                });
            }
        }).start();
    }
}