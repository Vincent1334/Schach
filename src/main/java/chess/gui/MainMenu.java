package chess.gui;


import chess.GameMode;
import chess.managers.LanguageManager;
import chess.managers.WindowManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * manages the main menu options
 *
 * @author Lydia Engelhardt, Sophia Kuhlmann, Vincent Schiller, Friederike Weilbeer
 * 2021-06-09
 */
@SuppressWarnings({"PMD.UnusedPrivateMethod"})
// the methods setMode... and startGame are used by the gui but PMD didn't recognize
public class MainMenu {
    private GameMode gameMode;
    private boolean playerColorBlack;
    @FXML
    public Pane pane;


    /**
     * sets the language
     */
    @FXML
    private void setLanguage(){
        LanguageManager.nextLocale();
    }

    /**
     * quits the game
     */
    @FXML
    void quitGame() {
        System.exit(0);
    }

    /**
     * starts the game
     *
     * @param event the mouse event
     */
    @FXML
    private void startGame(MouseEvent event) {
        FXMLLoader fxmlLoader;
        Stage stage = new Stage();
        Parent root;
        if(gameMode== GameMode.NETWORK){
            WindowManager.initialWindow("NetworkStage", "network_title");
            WindowManager.getStage("NetworkStage").show();
        }else{
            WindowManager.initialWindow("GameStage", "game_title");
            ((Controller) WindowManager.getController("GameStage")).init(gameMode, playerColorBlack, null);
            WindowManager.getStage("GameStage").show();
        }

        // Hide this current window
        WindowManager.closeStage("MenuStage");
    }


    /**
     * sets the game mode for a local game against a friend
     */
    @FXML
    private void setMode01() {
        gameMode = GameMode.NORMAL;
    }

    /**
     * sets the game mode for a local game against the computer
     */
    @FXML
    private void setMode02() {
        gameMode = GameMode.COMPUTER;
    }

    /**
     * sets the game mode for a network game
     */
    @FXML
    private void setMode03() {
        gameMode = GameMode.NETWORK;
    }

    /**
     * sets the playerColor to black
     */
    public void setBlack() {
        playerColorBlack = true;
    }

    /**
     * sets the playerColor to white
     */
    public void setWhite() {
        playerColorBlack = false;
    }
}
