package chess.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * This class ist the starting point for the graphical interface
 *
 * @author Lydia Engelhardt, Sophia Kuhlmann, Vincent Schiller, Friederike Weilbeer
 * 2021-06-09
 */
public class Gui extends Application {

    public static Locale locale = new Locale("en", "US");

    /**
     * opens the menu
     * @param primaryStage the stage on which the menu should be opened
     * @throws Exception contains exception type
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Menu.fxml"));
        fxmlLoader.setResources(ResourceBundle.getBundle("/languages/MessagesBundle", Gui.locale));
        Parent root = fxmlLoader.load();

        primaryStage.setTitle(ResourceBundle.getBundle("/languages/MessagesBundle", Gui.locale).getString("menu_title"));

        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    /**
     * The entry point of the GUI application.
     * @param args The command line arguments passed to the application
     */
    public static void main(String[] args) {
        launch(args);
    }

}
