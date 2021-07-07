package chess.gui;

import chess.enums.GameMode;
import chess.managers.LanguageManager;
import chess.managers.WindowManager;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

/**
 * manages the main menu options
 *
 * @author Lydia Engelhardt, Sophia Kuhlmann, Vincent Schiller, Friederike Weilbeer
 * 2021-06-09
 */
@SuppressWarnings({"PMD.UnusedPrivateMethod"})
// the methods setMode, ... and startGame are used by the gui but PMD does not recognize this
public class MainMenu {
    private GameMode gameMode;
    private boolean playerColorBlack;
    @FXML
    public Pane pane;

    /**
     * starts the game
     */
    @FXML
    private void startGame() {
        if (gameMode == GameMode.NETWORK) {
            WindowManager.initialWindow("NetworkStage", "network_title");
            WindowManager.getStage("NetworkStage").show();
        } else {
            WindowManager.initialWindow("GameStage", "game_title");
            ((Controller) WindowManager.getController("GameStage")).init(gameMode, playerColorBlack, null);
            WindowManager.getStage("GameStage").show();
        }
        // Hide this current window
        WindowManager.closeStage("MenuStage");
    }

    /**
     * quits the game
     */
    @FXML
    void quitGame() {
        System.exit(0);
    }


    //--------------getter / setter---------------------------------------------------------------------------------------------------------------


    @FXML
    private void setLanguage(MouseEvent event) {
        String url = ((ImageView) event.getTarget()).getImage().getUrl();
        LanguageManager.setLanguage(url.substring(url.length()-6,url.length()-4));
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
