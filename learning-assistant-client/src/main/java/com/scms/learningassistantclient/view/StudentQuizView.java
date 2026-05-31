package com.scms.learningassistantclient.view;

import com.scms.learningassistantclient.api.AnswerApi;
import com.scms.learningassistantclient.api.QuestionApi;
import com.scms.learningassistantclient.api.TaskApi;
import com.scms.learningassistantclient.model.AnswerRecord;
import com.scms.learningassistantclient.model.LearningTask;
import com.scms.learningassistantclient.model.LoginUser;
import com.scms.learningassistantclient.model.Question;
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

import java.util.List;

public class StudentQuizView {

    private final VBox root = new VBox();
    private final ScrollPane scrollPane = new ScrollPane(root);

    private final QuestionApi questionApi = new QuestionApi();
    private final TaskApi taskApi = new TaskApi();
    private final AnswerApi answerApi = new AnswerApi();

    private final Long courseId;
    private final String courseName;

    private final TextField taskIdField = new TextField();

    private final ListView<Question> questionListView = new ListView<>();
    private final Label questionDetailLabel = new Label("请选择一道题目");
    private final ComboBox<String> answerBox = new ComboBox<>();

    private final Label messageLabel = new Label();

    private Question selectedQuestion;
    private long questionStartMillis = System.currentTimeMillis();

    public StudentQuizView(Long courseId, String courseName) {
        this.courseId = courseId;
        this.courseName = courseName;
        createView();
        loadTasks();
    }

    private void createView() {
        scrollPane.setFitToWidth(true);
        scrollPane.setPannable(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        root.setSpacing(16);
        root.setPadding(new Insets(28));
        root.setAlignment(Pos.TOP_CENTER);

        Label titleLabel = new Label("学生端 - 任务测验答题");
        titleLabel.setFont(new Font(26));

        Label courseLabel = new Label("当前课程：" + courseName + " / 课程ID：" + courseId);
        courseLabel.setStyle("-fx-font-size: 16px;");

        taskIdField.setPromptText("任务ID");
        taskIdField.setMaxWidth(220);

        Button loadTasksButton = new Button("查询课程任务");
        Button loadQuestionsButton = new Button("查询任务题目");
        Button submitButton = new Button("提交答案");
        Button backButton = new Button("返回课程学习");

        loadTasksButton.setMinWidth(120);
        loadQuestionsButton.setMinWidth(120);
        submitButton.setMinWidth(120);
        backButton.setMinWidth(120);

        HBox inputBox = new HBox(12,
                new Label("任务ID："), taskIdField
        );
        inputBox.setAlignment(Pos.CENTER);

        HBox buttonBox = new HBox(12, loadTasksButton, loadQuestionsButton, submitButton, backButton);
        buttonBox.setAlignment(Pos.CENTER);

        questionListView.setPrefWidth(880);
        questionListView.setPrefHeight(320);

        questionListView.setCellFactory(listView -> new ListCell<>() {
            @Override
            protected void updateItem(Question item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                    return;
                }

                setText(
                        "题目ID：" + item.getId()
                                + "\n题目：" + safe(item.getQuestionText())
                                + "\n知识点：" + safe(item.getKnowledgePoint())
                                + "    难度：" + safe(item.getDifficulty())
                );
                setWrapText(true);
                setPrefWidth(questionListView.getPrefWidth() - 30);
                setMinHeight(Region.USE_PREF_SIZE);
            }
        });

        questionListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            selectedQuestion = newVal;
            questionStartMillis = System.currentTimeMillis();
            showQuestionDetail(newVal);
        });

        questionDetailLabel.setWrapText(true);
        questionDetailLabel.setPrefWidth(880);
        questionDetailLabel.setMinHeight(360);
        questionDetailLabel.setPrefHeight(360);
        questionDetailLabel.setMaxHeight(Region.USE_PREF_SIZE);
        questionDetailLabel.setStyle(
                "-fx-font-size: 15px;" +
                        "-fx-padding: 14px;" +
                        "-fx-background-color: #eef8ff;" +
                        "-fx-border-color: #b9def5;"
        );

        answerBox.getItems().addAll("A", "B", "C", "D");
        answerBox.setValue("A");
        answerBox.setEditable(true);

        HBox answerBoxArea = new HBox(12, new Label("填写/选择答案："), answerBox);
        answerBoxArea.setAlignment(Pos.CENTER);

        messageLabel.setStyle("-fx-font-size: 15px; -fx-text-fill: green;");

        loadTasksButton.setOnAction(e -> loadTasks());
        loadQuestionsButton.setOnAction(e -> loadQuestions());
        submitButton.setOnAction(e -> submitAnswer());

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
                inputBox,
                buttonBox,
                new Label("题目列表："),
                questionListView,
                new Label("题目详情："),
                questionDetailLabel,
                answerBoxArea,
                messageLabel
        );
    }

    private void loadTasks() {
        if (courseId == null) {
            setMessage("课程ID为空，无法查询课程任务", "red");
            return;
        }

        setMessage("正在查询课程任务...", "#333333");

        new Thread(() -> {
            try {
                List<LearningTask> tasks = taskApi.getTasksByCourseId(courseId);

                Platform.runLater(() -> {
                    if (tasks == null || tasks.isEmpty()) {
                        setMessage("当前课程暂无任务，请教师先发布测验任务", "red");
                        questionListView.setItems(FXCollections.observableArrayList());
                        questionDetailLabel.setText("当前课程暂无任务");
                        return;
                    }

                    LearningTask latestTask = tasks.get(0);
                    taskIdField.setText(String.valueOf(latestTask.getId()));

                    setMessage(
                            "任务加载成功，当前任务ID：" + latestTask.getId()
                                    + "，任务标题：" + latestTask.getTaskTitle()
                                    + "。正在加载该任务题目...",
                            "green"
                    );

                    loadQuestions();
                });

            } catch (Exception e) {
                e.printStackTrace();
                Platform.runLater(() -> setMessage("查询课程任务失败：" + e.getMessage(), "red"));
            }
        }).start();
    }

    private void loadQuestions() {
        Long taskId = parseLong(taskIdField.getText(), "任务ID");
        if (taskId == null) {
            return;
        }

        setMessage("正在加载任务题目...", "#333333");

        new Thread(() -> {
            try {
                List<Question> questions = questionApi.getQuestionsByTaskId(taskId);

                Platform.runLater(() -> {
                    questionListView.setItems(FXCollections.observableArrayList(questions));

                    if (questions == null || questions.isEmpty()) {
                        selectedQuestion = null;
                        questionDetailLabel.setText("当前任务暂无绑定题目，请教师先在任务中加入题目。");
                        setMessage("当前任务暂无绑定题目", "red");
                    } else {
                        questionListView.getSelectionModel().select(0);
                        questionStartMillis = System.currentTimeMillis();
                        setMessage("任务题目加载成功，共 " + questions.size() + " 道题", "green");
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
                Platform.runLater(() -> setMessage("加载任务题目失败：" + e.getMessage(), "red"));
            }
        }).start();
    }

    private void submitAnswer() {
        if (selectedQuestion == null) {
            setMessage("请先选择一道题目", "red");
            return;
        }

        Long taskId = parseLong(taskIdField.getText(), "任务ID");
        if (taskId == null) {
            return;
        }

        LoginUser user = AppContext.getCurrentUser();
        if (user == null || user.getId() == null) {
            setMessage("未获取到当前学生信息，请重新登录", "red");
            return;
        }

        String studentAnswer = getAnswerText();
        if (studentAnswer == null || studentAnswer.trim().isEmpty()) {
            setMessage("请填写或选择答案", "red");
            return;
        }

        int answerDuration = calcAnswerDurationSeconds();

        AnswerRecord record = new AnswerRecord();
        record.setTaskId(taskId);
        record.setQuestionId(selectedQuestion.getId());
        record.setStudentId(user.getId());
        record.setStudentAnswer(studentAnswer.trim().toUpperCase());
        record.setAnswerDuration(answerDuration);

        setMessage("正在提交答案...", "#333333");

        new Thread(() -> {
            try {
                AnswerRecord saved = answerApi.submitAnswer(record);

                Platform.runLater(() -> {
                    if (saved.answerCorrect()) {
                        setMessage("提交成功，回答正确，得分：" + saved.getScore()
                                + "，用时：" + answerDuration + " 秒", "green");
                    } else {
                        setMessage("提交成功，回答错误，得分：" + saved.getScore()
                                + "。正确答案：" + selectedQuestion.getAnswer()
                                + "，用时：" + answerDuration + " 秒。该题已进入错题本。", "red");
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
                Platform.runLater(() -> setMessage("提交答案失败：" + e.getMessage(), "red"));
            }
        }).start();
    }

    private String getAnswerText() {
        String text = null;

        if (answerBox.isEditable() && answerBox.getEditor() != null) {
            text = answerBox.getEditor().getText();
        }

        if (isBlank(text)) {
            text = answerBox.getValue();
        }

        return text == null ? "" : text.trim();
    }

    private int calcAnswerDurationSeconds() {
        long now = System.currentTimeMillis();
        long seconds = (now - questionStartMillis) / 1000;

        if (seconds <= 0) {
            return 1;
        }

        if (seconds > 3600) {
            return 3600;
        }

        return (int) seconds;
    }

    private void showQuestionDetail(Question q) {
        if (q == null) {
            questionDetailLabel.setText("请选择一道题目");
            return;
        }

        questionDetailLabel.setText(buildQuestionDetailText(q));

        String type = safe(q.getQuestionType()).toLowerCase();

        answerBox.getItems().clear();

        if ("judge".equals(type) || "判断题".equals(type)) {
            answerBox.getItems().addAll("A", "B", "正确", "错误");
            answerBox.setValue("A");
            answerBox.getEditor().setText("A");
        } else {
            answerBox.getItems().addAll("A", "B", "C", "D");
            answerBox.setValue("A");
            answerBox.getEditor().setText("A");
        }
    }

    private String buildQuestionDetailText(Question q) {
        if (q == null) {
            return "请选择一道题目";
        }

        StringBuilder builder = new StringBuilder();

        builder.append("题目ID：").append(safe(q.getId())).append("\n");
        builder.append("题型：").append(safe(q.getQuestionType())).append("\n");
        builder.append("题目：").append(safe(q.getQuestionText())).append("\n\n");

        builder.append("选项：\n");
        builder.append(formatOptions(q.getOptions())).append("\n\n");

        builder.append("知识点：").append(safe(q.getKnowledgePoint())).append("\n");
        builder.append("知识点ID：").append(safe(q.getKnowledgePointId())).append("\n");
        builder.append("难度：").append(safe(q.getDifficulty()));

        return builder.toString();
    }

    private String formatOptions(String options) {
        if (options == null || options.trim().isEmpty()) {
            return "暂无选项";
        }

        String text = options.trim();

        text = text.replace("\\n", "\n");

        text = text.replace("A.", "\nA.");
        text = text.replace("B.", "\nB.");
        text = text.replace("C.", "\nC.");
        text = text.replace("D.", "\nD.");

        return text.trim();
    }

    private Long parseLong(String text, String fieldName) {
        if (isBlank(text)) {
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

    private boolean isBlank(String text) {
        return text == null || text.trim().isEmpty();
    }

    private String safe(Object value) {
        return value == null ? "暂无" : String.valueOf(value);
    }

    private void setMessage(String text, String color) {
        messageLabel.setStyle("-fx-font-size: 15px; -fx-text-fill: " + color + ";");
        messageLabel.setText(text);
    }

    public Parent getView() {
        return scrollPane;
    }
}