package com.scms.learningassistantclient;

import com.scms.learningassistantclient.view.TeacherCourseDetailView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class TestTeacherCourseDetailApp extends Application {

    @Override
    public void start(Stage stage) {
        TeacherCourseDetailView view = new TeacherCourseDetailView(2L, "高等数学");

        Scene scene = new Scene(view, 900, 650);

        stage.setTitle("教师端 - 课程详情");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}