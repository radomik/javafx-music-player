package com.mc;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class App extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/com/mc/gui/sample.fxml"));
        stage.setTitle("Test Javafx application");
        stage.setScene(new Scene(root));
        stage.setOnCloseRequest((WindowEvent t) -> {
            MusicService.getInstance().stop();
        });
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
