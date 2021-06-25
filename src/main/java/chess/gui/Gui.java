package chess.gui;

import chess.managers.LanguageManager;
import chess.managers.WindowManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * This class is the starting point for the graphical interface
 *
 * @author Lydia Engelhardt, Sophia Kuhlmann, Vincent Schiller, Friederike Weilbeer
 * 2021-06-09
 */
public class Gui extends Application {

    /**
     * opens the menu
     * @param primaryStage the stage on which the menu should be opened
     * @throws Exception contains exception type
     */
    @Override
    public void start(Stage primaryStage) throws Exception {

        primaryStage.setTitle(LanguageManager.getText("menu_title"));
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(WindowManager.createWindow("Menu.fxml")));
        primaryStage.show();
    }

    /**
     * The entry point of the GUI application.
     * @param args The command line arguments passed to the application
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void stop(){
        System.exit(0);
    }
}
