package com.scms.learningassistantclient;

import com.scms.learningassistantclient.view.LoginView;
import com.scms.learningassistantclient.view.RegisterView;
import com.scms.learningassistantclient.view.StudentCourseView;
import com.scms.learningassistantclient.view.StudentHomeView;
import com.scms.learningassistantclient.view.TeacherCourseView;
import com.scms.learningassistantclient.view.TeacherHomeView;
import com.scms.learningassistantclient.view.TeacherProgressView;
import com.scms.learningassistantclient.view.TeacherQuestionView;
import com.scms.learningassistantclient.view.StudentQuizView;
import com.scms.learningassistantclient.view.TeacherAnswerStatsView;
import com.scms.learningassistantclient.view.StudentWrongBookView;
import com.scms.learningassistantclient.view.StudentLearningReportView;
import com.scms.learningassistantclient.view.TeacherClassReportView;
import com.scms.learningassistantclient.view.TeacherTaskView;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class HelloApplication extends Application {

    private static Stage primaryStage;

    @Override
    public void start(Stage stage) {
        primaryStage = stage;
        showLoginView();
    }

    private static void setScene(Parent root, String title, int width, int height) {
        Scene scene = new Scene(root, width, height);
        primaryStage.setTitle(title);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void showLoginView() {
        setScene(new LoginView().getView(), "智能学习助手 - 登录", 420, 360);
    }

    public static void showRegisterView() {
        setScene(new RegisterView().getView(), "智能学习助手 - 注册", 460, 460);
    }

    public static void showTeacherHomeView() {
        setScene(new TeacherHomeView().getView(), "智能学习助手 - 教师端", 700, 500);
    }

    public static void showStudentHomeView() {
        setScene(new StudentHomeView().getView(), "智能学习助手 - 学生端", 700, 500);
    }

    public static void showTeacherCourseView() {
        setScene(new TeacherCourseView().getView(), "智能学习助手 - 教师课程管理", 900, 650);
    }

    public static void showStudentCourseView() {
        setScene(new StudentCourseView().getView(), "智能学习助手 - 学生课程中心", 900, 720);
    }

    public static void showTeacherProgressView() {
        setScene(new TeacherProgressView().getView(), "智能学习助手 - 教师学习进度统计", 900, 650);
    }

    public static void showTeacherQuestionView() {
        setScene(new TeacherQuestionView().getView(), "教师端 - 测验题目管理", 1000, 720);
    }

    public static void showStudentQuizView() {
        setScene(new StudentQuizView().getView(), "学生端 - 课程测验答题", 980, 720);
    }

    public static void showTeacherAnswerStatsView() {
        setScene(new TeacherAnswerStatsView().getView(), "教师端 - 答题统计", 980, 720);
    }

    public static void showStudentWrongBookView() {
        setScene(new StudentWrongBookView().getView(), "学生端 - 我的错题本", 980, 720);
    }

    public static void showStudentLearningReportView() {
        setScene(new StudentLearningReportView().getView(), "学生端 - 个人学习报告", 1050, 820);
    }

    public static void showTeacherClassReportView() {
        setScene(new TeacherClassReportView().getView(), "教师端 - 班级综合学习报告", 1050, 820);
    }

    public static void showTeacherTaskView() {
        setScene(new TeacherTaskView().getView(), "教师端 - 测验任务管理", 980, 700);
    }

    public static void showTeacherAnswerStatsView(Long taskId) {
        setScene(new TeacherAnswerStatsView(taskId).getView(), "教师端 - 答题统计", 980, 720);
    }

    public static void main(String[] args) {
        launch();
    }
}