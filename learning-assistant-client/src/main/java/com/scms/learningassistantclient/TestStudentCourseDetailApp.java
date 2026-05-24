package com.scms.learningassistantclient;

import com.scms.learningassistantclient.view.StudentCourseDetailView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class TestStudentCourseDetailApp extends Application {

    @Override
    public void start(Stage stage) {
        StudentCourseDetailView view = new StudentCourseDetailView(
                2L,
                "高等数学",
                null
        );

        Scene scene = new Scene(view, 900, 650);

        stage.setTitle("学生端 - 课程学习");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}