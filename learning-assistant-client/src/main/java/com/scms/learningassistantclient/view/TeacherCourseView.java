package com.scms.learningassistantclient.view;

import com.scms.learningassistantclient.HelloApplication;
import com.scms.learningassistantclient.api.CourseApi;
import com.scms.learningassistantclient.model.Course;
import com.scms.learningassistantclient.model.CreateCourseRequest;
import com.scms.learningassistantclient.model.LoginUser;
import com.scms.learningassistantclient.util.AppContext;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.util.List;

public class TeacherCourseView {

    private final VBox root = new VBox();

    private final CourseApi courseApi = new CourseApi();

    private final ListView<Course> courseListView = new ListView<>();

    private final Label messageLabel = new Label();

    public TeacherCourseView() {
        createView();
        loadTeacherCourses();
    }

    private void createView() {
        root.setSpacing(14);
        root.setPadding(new Insets(25));
        root.setAlignment(Pos.TOP_CENTER);

        LoginUser user = AppContext.getCurrentUser();

        Label titleLabel = new Label("教师端 - 课程管理");
        titleLabel.setFont(new Font(24));

        Label userLabel = new Label("当前教师：" + user.getRealName() + " / " + user.getClassName());

        TextField courseNameField = new TextField();
        courseNameField.setPromptText("请输入课程名称，例如：概率论与数理统计");
        courseNameField.setMaxWidth(420);

        TextArea descriptionArea = new TextArea();
        descriptionArea.setPromptText("请输入课程简介");
        descriptionArea.setMaxWidth(420);
        descriptionArea.setPrefRowCount(3);

        Button createButton = new Button("创建课程");
        Button refreshButton = new Button("刷新课程列表");
        Button enterCourseButton = new Button("进入选中课程");
        Button backButton = new Button("返回教师首页");

        HBox buttonBox = new HBox(12);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(
                createButton,
                refreshButton,
                enterCourseButton,
                backButton
        );

        courseListView.setPrefWidth(600);
        courseListView.setPrefHeight(240);

        courseListView.setCellFactory(listView -> new ListCell<>() {
            @Override
            protected void updateItem(Course course, boolean empty) {
                super.updateItem(course, empty);

                if (empty || course == null) {
                    setText(null);
                } else {
                    setText(
                            "课程ID：" + course.getId()
                                    + "\n课程名称：" + course.getCourseName()
                                    + "\n课程简介：" + course.getDescription()
                                    + "\n创建时间：" + course.getCreateTime()
                    );
                }
            }
        });

        messageLabel.setStyle("-fx-text-fill: red;");

        createButton.setOnAction(event -> {
            String courseName = courseNameField.getText() == null ? "" : courseNameField.getText().trim();
            String description = descriptionArea.getText() == null ? "" : descriptionArea.getText().trim();

            if (courseName.isBlank()) {
                messageLabel.setStyle("-fx-text-fill: red;");
                messageLabel.setText("请输入课程名称");
                return;
            }

            createButton.setDisable(true);
            messageLabel.setStyle("-fx-text-fill: #333333;");
            messageLabel.setText("正在创建课程...");

            new Thread(() -> {
                try {
                    courseApi.createCourse(new CreateCourseRequest(courseName, description, user.getId()));

                    Platform.runLater(() -> {
                        messageLabel.setStyle("-fx-text-fill: green;");
                        messageLabel.setText("课程创建成功");
                        courseNameField.clear();
                        descriptionArea.clear();
                        createButton.setDisable(false);
                        loadTeacherCourses();
                    });
                } catch (Exception e) {
                    Platform.runLater(() -> {
                        messageLabel.setStyle("-fx-text-fill: red;");
                        messageLabel.setText("创建失败：" + e.getMessage());
                        createButton.setDisable(false);
                    });
                }
            }).start();
        });

        refreshButton.setOnAction(event -> loadTeacherCourses());

        enterCourseButton.setOnAction(event -> enterSelectedCourse());

        backButton.setOnAction(event -> HelloApplication.showTeacherHomeView());

        root.getChildren().addAll(
                titleLabel,
                userLabel,
                courseNameField,
                descriptionArea,
                buttonBox,
                new Label("我创建的课程："),
                courseListView,
                messageLabel
        );
    }

    private void loadTeacherCourses() {
        LoginUser user = AppContext.getCurrentUser();

        messageLabel.setStyle("-fx-text-fill: #333333;");
        messageLabel.setText("正在加载课程...");

        new Thread(() -> {
            try {
                List<Course> courses = courseApi.listTeacherCourses(user.getId());

                Platform.runLater(() -> {
                    courseListView.setItems(FXCollections.observableArrayList(courses));
                    messageLabel.setStyle("-fx-text-fill: green;");
                    messageLabel.setText("课程加载成功，共 " + courses.size() + " 门课程");
                });
            } catch (Exception e) {
                Platform.runLater(() -> {
                    messageLabel.setStyle("-fx-text-fill: red;");
                    messageLabel.setText("课程加载失败：" + e.getMessage());
                });
            }
        }).start();
    }

    private void enterSelectedCourse() {
        Course selectedCourse = courseListView.getSelectionModel().getSelectedItem();

        if (selectedCourse == null) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("请先选中一门课程");
            return;
        }

        TeacherCourseDetailView detailView = new TeacherCourseDetailView(
                selectedCourse.getId(),
                selectedCourse.getCourseName(),
                () -> HelloApplication.showTeacherCourseView()
        );

        root.getScene().setRoot(detailView);
    }

    public Parent getView() {
        return root;
    }
}