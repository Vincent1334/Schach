package chess.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Gui extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        /*FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MainFrame.fxml"));
        Parent root = (Parent)fxmlLoader.load();

        primaryStage.setTitle("Schach");

        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.show();*/

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Menu.fxml"));
        Parent root = fxmlLoader.load();

        primaryStage.setTitle("Schach");

        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        primaryStage.show();


    }

    public static void main(String[] args) {
        launch(args);
    }

}
