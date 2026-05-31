package com.scms.learningassistantclient.view;

import com.scms.learningassistantclient.HelloApplication;
import com.scms.learningassistantclient.api.TaskApi;
import com.scms.learningassistantclient.model.LearningTask;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.util.List;

public class TeacherTaskView {

    private final VBox root = new VBox();

    private final TaskApi taskApi = new TaskApi();

    private final Long courseId;
    private final String courseName;
    private final ListView<LearningTask> taskListView = new ListView<>();
    private final Label messageLabel = new Label();

    private LearningTask selectedTask;

    public TeacherTaskView(Long courseId, String courseName) {
        this.courseId = courseId;
        this.courseName = courseName;
        createView();
        loadTasks();
    }

    private void createView() {
        root.setSpacing(16);
        root.setPadding(new Insets(30));
        root.setAlignment(Pos.TOP_CENTER);

        Label titleLabel = new Label("教师端 - 测验任务管理");
        titleLabel.setFont(new Font(26));

//        courseIdField.setPromptText("请输入课程ID，例如：2");
//        courseIdField.setMaxWidth(260);

        Button queryButton = new Button("查询任务");
        Button refreshButton = new Button("刷新");
        Button statsButton = new Button("查看答题统计");
        Button deleteButton = new Button("删除选中任务");
        Button backButton = new Button("返回教师首页");

        queryButton.setMinWidth(120);
        refreshButton.setMinWidth(90);
        statsButton.setMinWidth(130);
        deleteButton.setMinWidth(130);
        backButton.setMinWidth(130);

        Label courseLabel = new Label("当前课程：" + courseName + " / 课程ID：" + courseId);
        courseLabel.setStyle("-fx-font-size: 16px;");

        HBox buttonBox = new HBox(12, queryButton, refreshButton, statsButton, deleteButton, backButton);
        buttonBox.setAlignment(Pos.CENTER);

        taskListView.setPrefWidth(900);
        taskListView.setPrefHeight(460);

        taskListView.setCellFactory(listView -> new ListCell<>() {
            @Override
            protected void updateItem(LearningTask item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                    return;
                }

                setText(buildTaskText(item));
                setWrapText(true);
                setPrefWidth(taskListView.getPrefWidth() - 30);
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

        taskListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            selectedTask = newVal;
        });

        taskListView.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY
                    && event.getClickCount() == 2
                    && selectedTask != null) {
                HelloApplication.showTeacherAnswerStatsView(selectedTask.getId());
            }
        });

        messageLabel.setStyle("-fx-font-size: 15px; -fx-text-fill: green;");

        queryButton.setOnAction(e -> loadTasks());
        refreshButton.setOnAction(e -> loadTasks());
        statsButton.setOnAction(e -> openAnswerStats());
        deleteButton.setOnAction(e -> deleteSelectedTask());
        backButton.setOnAction(e -> {
            Parent detailView = new TeacherCourseDetailView(
                    courseId,
                    courseName,
                    HelloApplication::showTeacherCourseView
            );
            root.getScene().setRoot(detailView);
        });

        root.getChildren().addAll(
                titleLabel,
                courseLabel,
                buttonBox,
                new Label("测验任务列表："),
                taskListView,
                new Label("提示：双击某个任务，也可以直接查看该任务答题统计。"),
                messageLabel
        );

        loadTasks();
    }

    private void loadTasks() {

        if (courseId == null) {
            return;
        }

        setMessage("正在加载测验任务...", "#333333");

        new Thread(() -> {
            try {
                List<LearningTask> tasks = taskApi.getTasksByCourseId(courseId);

                Platform.runLater(() -> {
                    taskListView.setItems(FXCollections.observableArrayList(tasks));
                    selectedTask = null;
                    setMessage("测验任务加载成功，共 " + tasks.size() + " 个任务", "green");
                });

            } catch (Exception e) {
                e.printStackTrace();
                Platform.runLater(() -> setMessage("测验任务加载失败：" + e.getMessage(), "red"));
            }
        }).start();
    }

    private void openAnswerStats() {
        if (selectedTask == null) {
            setMessage("请先选择一个测验任务", "red");
            return;
        }

        HelloApplication.showTeacherAnswerStatsView(selectedTask.getId());
    }

    private void deleteSelectedTask() {
        if (selectedTask == null) {
            setMessage("请先选择一个要删除的测验任务", "red");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("确认删除");
        alert.setHeaderText("确定要删除这个测验任务吗？");
        alert.setContentText(
                "任务ID：" + selectedTask.getId()
                        + "\n任务标题：" + selectedTask.getTaskTitle()
                        + "\n\n注意：如果该任务已有学生答题记录，数据库可能不允许删除。"
        );

        ButtonType result = alert.showAndWait().orElse(ButtonType.CANCEL);

        if (result != ButtonType.OK) {
            return;
        }

        Long taskId = selectedTask.getId();

        setMessage("正在删除测验任务...", "#333333");

        new Thread(() -> {
            try {
                taskApi.deleteTask(taskId);

                Platform.runLater(() -> {
                    setMessage("测验任务删除成功，任务ID：" + taskId, "green");
                    loadTasks();
                });

            } catch (Exception e) {
                e.printStackTrace();

                Platform.runLater(() -> setMessage(
                        "删除测验任务失败：" + e.getMessage()
                                + "。如果该任务已有答题记录，建议保留任务，不要删除。",
                        "red"
                ));
            }
        }).start();
    }

    private String buildTaskText(LearningTask task) {
        return "任务ID：" + safe(task.getId())
                + "\n课程ID：" + safe(task.getCourseId())
                + "\n教师ID：" + safe(task.getTeacherId())
                + "\n任务标题：" + safe(task.getTaskTitle())
                + "\n任务类型：" + safe(task.getTaskType())
                + "\n开始时间：" + safe(task.getStartTime())
                + "\n结束时间：" + safe(task.getEndTime())
                + "\n创建时间：" + safe(task.getCreateTime());
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
        return root;
    }
}