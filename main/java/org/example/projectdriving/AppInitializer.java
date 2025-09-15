package org.example.projectdriving;

import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AppInitializer extends Application {
    public static void main(String[] args) {
       launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
//        stage.setScene(new Scene(
//                new FXMLLoader(getClass().getResource("/view/LoginPage.fxml")).load()
//        ));
//        stage.show();
//
//        Task<Scene> loadingTask = new Task<>() {
//            @Override
//            protected Scene call() throws Exception {
//                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/LoginPage.fxml"));
//                return new Scene(fxmlLoader.load());
//            }
//        };
//
//        loadingTask.setOnSucceeded(event -> {
//            Scene value = loadingTask.getValue();
//            stage.setTitle("Login...");
//            stage.setScene(value);
//        });
//
//        new Thread(loadingTask).start();

        stage.setScene(new Scene(
                FXMLLoader.load(getClass().getResource("/view/LoginPage.fxml"))
        ));
        stage.setTitle("Login Page");
        stage.show();
    }
}
