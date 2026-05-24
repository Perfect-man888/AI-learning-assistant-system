package com.scms.learningassistantclient.view;

import com.scms.learningassistantclient.HelloApplication;
import com.scms.learningassistantclient.api.CourseApi;
import com.scms.learningassistantclient.model.Course;
import com.scms.learningassistantclient.model.JoinCourseRequest;
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
import javafx.scene.Scene;

import java.util.List;

public class StudentCourseView {

    private final VBox root = new VBox();

    private final CourseApi courseApi = new CourseApi();

    private final ListView<Course> allCourseListView = new ListView<>();

    private final ListView<Course> myCourseListView = new ListView<>();

    private final Label messageLabel = new Label();

    public StudentCourseView() {
        createView();
        loadAllCourses();
        loadMyCourses();
    }

    private void createView() {
        root.setSpacing(12);
        root.setPadding(new Insets(25));
        root.setAlignment(Pos.TOP_CENTER);

        LoginUser user = AppContext.getCurrentUser();

        Label titleLabel = new Label("学生端 - 我的课程");
        titleLabel.setFont(new Font(24));

        Label userLabel = new Label("当前学生：" + user.getRealName() + " / " + user.getClassName());

        allCourseListView.setPrefWidth(620);
        allCourseListView.setPrefHeight(210);

        myCourseListView.setPrefWidth(620);
        myCourseListView.setPrefHeight(180);

        allCourseListView.setCellFactory(listView -> new ListCell<>() {
            @Override
            protected void updateItem(Course course, boolean empty) {
                super.updateItem(course, empty);

                if (empty || course == null) {
                    setText(null);
                } else {
                    setText(
                            "课程ID：" + course.getId()
                                    + "\n课程名称：" + course.getCourseName()
                                    + "\n教师：" + course.getTeacherName()
                                    + "\n班级：" + course.getClassName()
                                    + "\n简介：" + course.getDescription()
                    );
                }
            }
        });

        myCourseListView.setCellFactory(listView -> new ListCell<>() {
            @Override
            protected void updateItem(Course course, boolean empty) {
                super.updateItem(course, empty);

                if (empty || course == null) {
                    setText(null);
                } else {
                    setText(
                            "课程ID：" + course.getId()
                                    + "\n课程名称：" + course.getCourseName()
                                    + "\n教师：" + course.getTeacherName()
                                    + "\n班级：" + course.getClassName()
                                    + "\n状态：已加入"
                    );
                }
            }
        });

        Button refreshAllButton = new Button("刷新全部课程");
        Button joinButton = new Button("加入选中课程");
        Button refreshMyButton = new Button("刷新我的课程");
        Button enterCourseButton = new Button("进入选中课程");
        Button backButton = new Button("返回学生首页");

        HBox buttonBox = new HBox(12);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(
                refreshAllButton,
                joinButton,
                refreshMyButton,
                enterCourseButton,
                backButton
        );

        messageLabel.setStyle("-fx-text-fill: red;");

        refreshAllButton.setOnAction(event -> loadAllCourses());

        refreshMyButton.setOnAction(event -> loadMyCourses());

        enterCourseButton.setOnAction(event -> enterSelectedCourse());

        joinButton.setOnAction(event -> {
            Course selectedCourse = allCourseListView.getSelectionModel().getSelectedItem();

            if (selectedCourse == null) {
                messageLabel.setStyle("-fx-text-fill: red;");
                messageLabel.setText("请先选择一门课程");
                return;
            }

            joinButton.setDisable(true);
            messageLabel.setStyle("-fx-text-fill: #333333;");
            messageLabel.setText("正在加入课程...");

            new Thread(() -> {
                try {
                    courseApi.joinCourse(new JoinCourseRequest(selectedCourse.getId(), user.getId()));

                    Platform.runLater(() -> {
                        messageLabel.setStyle("-fx-text-fill: green;");
                        messageLabel.setText("加入课程成功");
                        joinButton.setDisable(false);
                        loadAllCourses();
                        loadMyCourses();
                    });
                } catch (Exception e) {
                    Platform.runLater(() -> {
                        messageLabel.setStyle("-fx-text-fill: red;");
                        messageLabel.setText("加入失败：" + e.getMessage());
                        joinButton.setDisable(false);
                    });
                }
            }).start();
        });

        backButton.setOnAction(event -> HelloApplication.showStudentHomeView());

        root.getChildren().addAll(
                titleLabel,
                userLabel,
                new Label("全部课程："),
                allCourseListView,
                buttonBox,
                new Label("我已加入的课程："),
                myCourseListView,
                messageLabel
        );
    }

    private void loadAllCourses() {
        messageLabel.setStyle("-fx-text-fill: #333333;");
        messageLabel.setText("正在加载全部课程...");

        new Thread(() -> {
            try {
                List<Course> courses = courseApi.listAllCourses();

                Platform.runLater(() -> {
                    allCourseListView.setItems(FXCollections.observableArrayList(courses));
                    messageLabel.setStyle("-fx-text-fill: green;");
                    messageLabel.setText("全部课程加载成功，共 " + courses.size() + " 门课程");
                });
            } catch (Exception e) {
                Platform.runLater(() -> {
                    messageLabel.setStyle("-fx-text-fill: red;");
                    messageLabel.setText("全部课程加载失败：" + e.getMessage());
                });
            }
        }).start();
    }

    private void loadMyCourses() {
        LoginUser user = AppContext.getCurrentUser();

        new Thread(() -> {
            try {
                List<Course> courses = courseApi.listMyCourses(user.getId());

                Platform.runLater(() -> myCourseListView.setItems(FXCollections.observableArrayList(courses)));
            } catch (Exception e) {
                Platform.runLater(() -> {
                    messageLabel.setStyle("-fx-text-fill: red;");
                    messageLabel.setText("我的课程加载失败：" + e.getMessage());
                });
            }
        }).start();
    }

    private void enterSelectedCourse() {
        Course selectedCourse = myCourseListView.getSelectionModel().getSelectedItem();

        if (selectedCourse == null) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("请先在“我已加入的课程”中选中一门课程");
            return;
        }

        Scene currentScene = root.getScene();

        StudentCourseDetailView detailView = new StudentCourseDetailView(
                selectedCourse.getId(),
                selectedCourse.getCourseName(),
                () -> currentScene.setRoot(new StudentCourseView().getView())
        );

        currentScene.setRoot(detailView);
    }

    public Parent getView() {
        return root;
    }
}