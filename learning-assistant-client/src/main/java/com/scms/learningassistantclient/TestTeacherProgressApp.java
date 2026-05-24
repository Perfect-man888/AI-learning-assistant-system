package com.scms.learningassistantclient;

import com.scms.learningassistantclient.view.TeacherProgressView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class TestTeacherProgressApp extends Application {

    @Override
    public void start(Stage stage) {
        TeacherProgressView view = new TeacherProgressView();

        Scene scene = new Scene(view.getView(), 900, 650);

        stage.setTitle("教师端 - 学习进度统计");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}