package chess.gui;

import chess.enums.GameMode;
import chess.managers.WindowManager;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * This class is the starting point for the graphical interface
 *
 * @author Lydia Engelhardt, Sophia Kuhlmann, Vincent Schiller, Friederike Weilbeer
 * 2021-06-09
 */
public class Gui extends Application {

    /**
     * opens the menu
     *
     * @param primaryStage the stage on which the menu should be opened
     * @throws Exception contains exception type
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        WindowManager.initialWindow("MenuStage", "menu_title");
        WindowManager.showStage("MenuStage");
    }

    /**
     * The entry point of the GUI application.
     *
     * @param args The command line arguments passed to the application
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void stop() {
        if(((ChessBoard)WindowManager.getController("GameStage")).getLogic().getGameMode() == GameMode.NETWORK){
            ((ChessBoard)WindowManager.getController("GameStage")).getLogic().getNetwork().killNetwork();
        }
        System.exit(0);
    }
}
