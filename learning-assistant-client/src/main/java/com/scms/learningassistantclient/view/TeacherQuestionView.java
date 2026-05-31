package com.scms.learningassistantclient.view;

import com.scms.learningassistantclient.HelloApplication;
import com.scms.learningassistantclient.api.AiQuestionApi;
import com.scms.learningassistantclient.api.QuestionApi;
import com.scms.learningassistantclient.api.TaskApi;
import com.scms.learningassistantclient.api.TaskQuestionApi;
import com.scms.learningassistantclient.model.AiGenerateQuestionRequest;
import com.scms.learningassistantclient.model.LearningTask;
import com.scms.learningassistantclient.model.LoginUser;
import com.scms.learningassistantclient.model.Question;
import com.scms.learningassistantclient.util.AppContext;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TeacherQuestionView {

    private final VBox root = new VBox();

    private final QuestionApi questionApi = new QuestionApi();
    private final TaskApi taskApi = new TaskApi();
    private final TaskQuestionApi taskQuestionApi = new TaskQuestionApi();
    private final AiQuestionApi aiQuestionApi = new AiQuestionApi();

    private final Long courseId;
    private final String courseName;

    private final TextField chapterIdField = new TextField("1");
    private final TextField sectionIdField = new TextField("1");
    private final TextField aiCountField = new TextField("2");

    private final TextArea questionTextArea = new TextArea();
    private final TextArea optionsArea = new TextArea();

    private final ComboBox<String> answerBox = new ComboBox<>();
    private final TextField knowledgePointField = new TextField();
    private final ComboBox<String> difficultyBox = new ComboBox<>();

    private final TextField taskTitleField = new TextField("课堂测验");

    private final ListView<Question> questionListView = new ListView<>();
    private final Label messageLabel = new Label();

    public TeacherQuestionView(Long courseId, String courseName) {
        this.courseId = courseId;
        this.courseName = courseName;
        createView();
        loadQuestions();
    }

    private void createView() {
        root.setSpacing(14);
        root.setPadding(new Insets(24));
        root.setAlignment(Pos.TOP_CENTER);

        Label titleLabel = new Label("教师端 - 测验题目管理");
        titleLabel.setFont(new Font(26));

        Label courseLabel = new Label("当前课程：" + courseName + " / 课程ID：" + courseId);
        courseLabel.setStyle("-fx-font-size: 16px;");

        chapterIdField.setPromptText("章节ID");
        sectionIdField.setPromptText("小节ID");
        aiCountField.setPromptText("AI生成数量");
        aiCountField.setMaxWidth(120);

        questionTextArea.setPromptText("请输入题目内容，例如：函数的概念主要研究什么？");
        questionTextArea.setPrefRowCount(2);
        questionTextArea.setPrefWidth(520);
        questionTextArea.setWrapText(true);

        optionsArea.setPromptText("请输入选项，例如：\nA. 变量之间的对应关系\nB. 随机事件的概率\nC. 矩阵的运算\nD. 导数的几何意义");
        optionsArea.setPrefRowCount(5);
        optionsArea.setPrefWidth(520);
        optionsArea.setWrapText(true);

        answerBox.getItems().addAll("A", "B", "C", "D");
        answerBox.setValue("A");

        knowledgePointField.setPromptText("知识点，例如：函数的概念");

        difficultyBox.getItems().addAll("easy", "normal", "hard");
        difficultyBox.setValue("easy");

        taskTitleField.setPrefWidth(300);

        GridPane formGrid = new GridPane();
        formGrid.setHgap(12);
        formGrid.setVgap(10);
        formGrid.setAlignment(Pos.CENTER);

        formGrid.add(new Label("章节ID："), 0, 0);
        formGrid.add(chapterIdField, 1, 0);

        formGrid.add(new Label("小节ID："), 2, 0);
        formGrid.add(sectionIdField, 3, 0);

        formGrid.add(new Label("AI数量："), 4, 0);
        formGrid.add(aiCountField, 5, 0);

        formGrid.add(new Label("题目内容："), 0, 1);
        formGrid.add(questionTextArea, 1, 1, 5, 1);

        formGrid.add(new Label("题目选项："), 0, 2);
        formGrid.add(optionsArea, 1, 2, 5, 1);

        formGrid.add(new Label("正确答案："), 0, 3);
        formGrid.add(answerBox, 1, 3);

        formGrid.add(new Label("知识点："), 2, 3);
        formGrid.add(knowledgePointField, 3, 3);

        formGrid.add(new Label("难度："), 4, 3);
        formGrid.add(difficultyBox, 5, 3);

        formGrid.add(new Label("任务标题："), 0, 4);
        formGrid.add(taskTitleField, 1, 4, 5, 1);

        Button queryButton = new Button("查询题目");
        Button addButton = new Button("新增题目");
        Button aiGenerateButton = new Button("AI生成题目");
        Button createTaskButton = new Button("发布任务并加入题目");
        Button clearButton = new Button("清空输入");
        Button backButton = new Button("返回课程详情");

        aiGenerateButton.setMinWidth(120);
        queryButton.setMinWidth(110);
        addButton.setMinWidth(110);
        createTaskButton.setMinWidth(170);
        clearButton.setMinWidth(110);
        backButton.setMinWidth(130);

        HBox buttonBox = new HBox(
                12,
                queryButton,
                addButton,
                aiGenerateButton,
                createTaskButton,
                clearButton,
                backButton
        );
        buttonBox.setAlignment(Pos.CENTER);

        questionListView.setPrefWidth(920);
        questionListView.setPrefHeight(320);
        questionListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        questionListView.setCellFactory(listView -> new ListCell<>() {
            @Override
            protected void updateItem(Question item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                    return;
                }

                setText(buildQuestionText(item));
                setWrapText(true);
                setStyle(
                        "-fx-font-size: 14px;" +
                                "-fx-padding: 12px;" +
                                "-fx-background-color: #f7fbff;" +
                                "-fx-border-color: #d0e6f7;" +
                                "-fx-border-width: 0 0 1 0;"
                );
            }
        });

        messageLabel.setStyle("-fx-font-size: 15px; -fx-text-fill: green;");

        queryButton.setOnAction(e -> loadQuestions());
        aiGenerateButton.setOnAction(e -> generateQuestionsByAi());
        addButton.setOnAction(e -> addQuestion());
        createTaskButton.setOnAction(e -> createTask());
        clearButton.setOnAction(e -> clearForm());

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
                formGrid,
                buttonBox,
                new Label("题目列表："),
                questionListView,
                messageLabel
        );
    }

    private void loadQuestions() {
        if (courseId == null) {
            setMessage("课程ID为空，无法加载题目", "red");
            return;
        }

        Long chapterId = parseLong(chapterIdField.getText(), "章节ID");
        Long sectionId = parseLong(sectionIdField.getText(), "小节ID");

        if (chapterId == null || sectionId == null) {
            return;
        }

        setMessage("正在加载当前章节/小节题目...", "#333333");

        new Thread(() -> {
            try {
                List<Question> allQuestions = questionApi.getQuestionsByCourseId(courseId);

                List<Question> filteredQuestions = allQuestions.stream()
                        .filter(q -> q != null)
                        .filter(q -> courseId.equals(q.getCourseId()))
                        .filter(q -> chapterId.equals(q.getChapterId()))
                        .filter(q -> sectionId.equals(q.getSectionId()))
                        .toList();

                Platform.runLater(() -> {
                    questionListView.setItems(FXCollections.observableArrayList(filteredQuestions));
                    setMessage(
                            "题目加载成功，当前章节ID：" + chapterId
                                    + "，小节ID：" + sectionId
                                    + "，共 " + filteredQuestions.size() + " 道题",
                            "green"
                    );
                });

            } catch (Exception e) {
                e.printStackTrace();
                Platform.runLater(() -> setMessage("题目加载失败：" + e.getMessage(), "red"));
            }
        }).start();
    }

    private void addQuestion() {
        Long chapterId = parseLong(chapterIdField.getText(), "章节ID");
        Long sectionId = parseLong(sectionIdField.getText(), "小节ID");

        if (courseId == null || chapterId == null || sectionId == null) {
            return;
        }

        String questionText = questionTextArea.getText();
        String options = optionsArea.getText();
        String answer = answerBox.getValue();
        String knowledgePoint = knowledgePointField.getText();
        String difficulty = difficultyBox.getValue();

        if (isBlank(questionText)) {
            setMessage("请输入题目内容", "red");
            return;
        }

        if (isBlank(options)) {
            setMessage("请输入题目选项", "red");
            return;
        }

        if (isBlank(answer)) {
            setMessage("请选择正确答案", "red");
            return;
        }

        Question question = new Question();
        question.setCourseId(courseId);
        question.setChapterId(chapterId);
        question.setSectionId(sectionId);
        question.setQuestionType("single");
        question.setQuestionText(questionText.trim());
        question.setOptions(options.trim());
        question.setAnswer(answer.trim().toUpperCase());
        question.setKnowledgePoint(isBlank(knowledgePoint) ? "未标注" : knowledgePoint.trim());
        question.setDifficulty(isBlank(difficulty) ? "normal" : difficulty);

        setMessage("正在新增题目...", "#333333");

        new Thread(() -> {
            try {
                Question saved = questionApi.createQuestion(question);

                Platform.runLater(() -> {
                    setMessage("题目新增成功，题目ID：" + saved.getId(), "green");
                    clearQuestionInputsOnly();
                    loadQuestions();
                });

            } catch (Exception e) {
                e.printStackTrace();
                Platform.runLater(() -> setMessage("新增题目失败：" + e.getMessage(), "red"));
            }
        }).start();
    }

    private void generateQuestionsByAi() {
        Long chapterId = parseLong(chapterIdField.getText(), "章节ID");
        Long sectionId = parseLong(sectionIdField.getText(), "小节ID");

        if (courseId == null || chapterId == null || sectionId == null) {
            return;
        }

        String knowledgePoint = knowledgePointField.getText();
        String difficulty = difficultyBox.getValue();

        if (isBlank(knowledgePoint)) {
            setMessage("请输入知识点，例如：函数的概念", "red");
            return;
        }

        Integer count;

        try {
            count = Integer.parseInt(aiCountField.getText().trim());
        } catch (Exception e) {
            setMessage("AI生成数量必须是数字", "red");
            return;
        }

        if (count <= 0) {
            setMessage("AI生成数量必须大于0", "red");
            return;
        }

        if (count > 10) {
            setMessage("一次最多生成10道题", "red");
            return;
        }

        AiGenerateQuestionRequest request = new AiGenerateQuestionRequest();
        request.setCourseId(courseId);
        request.setChapterId(chapterId);
        request.setSectionId(sectionId);
        request.setKnowledgePoint(knowledgePoint.trim());
        request.setQuestionType("single");
        request.setDifficulty(isBlank(difficulty) ? "easy" : difficulty);
        request.setCount(count);

        setMessage("AI正在生成题目，请稍等...", "#333333");

        new Thread(() -> {
            try {
                List<Question> generatedQuestions = aiQuestionApi.generateQuestions(request);

                Platform.runLater(() -> {
                    setMessage("AI生成题目成功，共生成 " + generatedQuestions.size() + " 道题", "green");
                    loadQuestions();
                });

            } catch (Exception e) {
                e.printStackTrace();

                Platform.runLater(() -> setMessage(
                        "AI生成题目失败：" + e.getMessage(),
                        "red"
                ));
            }
        }).start();
    }

    private void createTask() {
        Long chapterId = parseLong(chapterIdField.getText(), "章节ID");
        Long sectionId = parseLong(sectionIdField.getText(), "小节ID");

        if (courseId == null || chapterId == null || sectionId == null) {
            return;
        }

        String taskTitle = taskTitleField.getText();

        if (isBlank(taskTitle)) {
            setMessage("请输入测验任务标题", "red");
            return;
        }

        List<Question> selectedQuestions = new ArrayList<>(
                questionListView.getSelectionModel().getSelectedItems()
        );

        if (selectedQuestions.isEmpty()) {
            selectedQuestions = questionListView.getItems()
                    .stream()
                    .filter(q -> Objects.equals(q.getCourseId(), courseId))
                    .filter(q -> Objects.equals(q.getChapterId(), chapterId))
                    .filter(q -> Objects.equals(q.getSectionId(), sectionId))
                    .toList();
        }

        if (selectedQuestions.isEmpty()) {
            setMessage("没有可加入任务的题目。请先查询/生成题目，或在题目列表中选中题目。", "red");
            return;
        }

        LearningTask task = new LearningTask();
        task.setCourseId(courseId);
        task.setChapterId(chapterId);
        task.setSectionId(sectionId);

        LoginUser currentUser = AppContext.getCurrentUser();
        if (currentUser != null && currentUser.getId() != null) {
            task.setTeacherId(currentUser.getId());
        } else {
            task.setTeacherId(1L);
        }

        task.setTaskTitle(taskTitle.trim());
        task.setTaskType("quiz");
        task.setStatus("published");

        setMessage("正在发布任务并绑定题目...", "#333333");

        List<Question> finalSelectedQuestions = selectedQuestions;

        new Thread(() -> {
            try {
                LearningTask saved = taskApi.createTask(task);

                int bindCount = 0;

                for (int i = 0; i < finalSelectedQuestions.size(); i++) {
                    Question question = finalSelectedQuestions.get(i);

                    if (question != null && question.getId() != null) {
                        taskQuestionApi.addQuestionToTask(saved.getId(), question.getId(), i + 1);
                        bindCount++;
                    }
                }

                int finalBindCount = bindCount;

                Platform.runLater(() -> setMessage(
                        "任务发布成功，任务ID：" + saved.getId()
                                + "，已加入题目 " + finalBindCount + " 道。学生端可以开始答题。",
                        "green"
                ));

            } catch (Exception e) {
                e.printStackTrace();
                Platform.runLater(() -> setMessage("发布任务或绑定题目失败：" + e.getMessage(), "red"));
            }
        }).start();
    }

    private String buildQuestionText(Question q) {
        StringBuilder builder = new StringBuilder();

        builder.append("题目ID：").append(safe(q.getId())).append("\n");
        builder.append("课程ID：").append(safe(q.getCourseId()))
                .append("    章节ID：").append(safe(q.getChapterId()))
                .append("    小节ID：").append(safe(q.getSectionId()))
                .append("\n");

        builder.append("题型：").append(safe(q.getQuestionType()))
                .append("    难度：").append(safe(q.getDifficulty()))
                .append("\n");

        builder.append("题目：").append(safe(q.getQuestionText())).append("\n");

        if (!isBlank(q.getOptions())) {
            builder.append("选项：\n").append(q.getOptions()).append("\n");
        }

        builder.append("正确答案：").append(safe(q.getAnswer())).append("\n");
        builder.append("知识点：").append(safe(q.getKnowledgePoint()));

        if (!isBlank(q.getAnalysis())) {
            builder.append("\n解析：").append(q.getAnalysis());
        }

        return builder.toString();
    }

    private void clearForm() {
        questionTextArea.clear();
        optionsArea.clear();
        answerBox.setValue("A");
        knowledgePointField.clear();
        difficultyBox.setValue("easy");
        taskTitleField.setText("课堂测验");
        setMessage("输入已清空", "#333333");
    }

    private void clearQuestionInputsOnly() {
        questionTextArea.clear();
        optionsArea.clear();
        answerBox.setValue("A");
        knowledgePointField.clear();
        difficultyBox.setValue("easy");
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
        return root;
    }
}