package chess.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Gui extends Application {

//Kommentar
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("schachbrett2.fxml"));
        Parent root = (Parent)fxmlLoader.load();

        primaryStage.setTitle("Schach");

        primaryStage.setScene(new Scene(root, 1115, 1148));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
