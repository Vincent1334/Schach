package chess.gui;

import chess.controller.CoreGame;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class Main extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("schachbrett2.fxml"));
        Parent root = fxmlLoader.load();

        primaryStage.setTitle("Schach");
        primaryStage.setScene(new Scene(root, 900, 1000));
        primaryStage.show();


    }

    public static void main(String[] args) {
        launch(args);
    }

}
