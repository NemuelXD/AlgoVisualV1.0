package com.nemuel.controller;
/*Hola Nemuel*/
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    public static Scene scene;
    public static Stage stage;

    @Override
    public void start(Stage stg) throws Exception {
        scene = new Scene(loadFXML("/com/nemuel/view/MainWindowView"));

        stage = stg;
        stage.setTitle("AlgoVisual V1.0");
        stage.getIcons().
                add(new Image(getClass()
                        .getResource("/com/nemuel/resources/icons/IconoAlgoVisual2.png")
                        .toExternalForm()));
        stage.setMaxWidth(1920);
        stage.setMaxHeight(1080);
        stage.setWidth(1280);
        stage.setHeight(720);
        stage.setMinWidth(854);
        stage.setMinHeight(480);
        stage.centerOnScreen();
        stage.setScene(scene);
        stage.show();
    }

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    public static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader FXML = new FXMLLoader(Main.class.getResource(fxml + ".fxml"));
        return FXML.load();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
