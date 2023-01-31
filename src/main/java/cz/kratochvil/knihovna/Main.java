package cz.kratochvil.knihovna;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Main extends Application {
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Main.fxml")));
        stage.setScene(new Scene(root, 1080, 720));
        stage.getIcons().add(new Image("file:src/main/resources/cz/kratochvil/knihovna/ikona.png"));
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
