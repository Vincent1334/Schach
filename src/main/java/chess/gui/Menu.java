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
public class Menu {
    private GameMode gameMode;
    private boolean playerColorBlack;
    @FXML
    private Pane pane;


    /**
     * sets the language
     */
    @FXML
    private void setLanguage(){
        LanguageManager.nextLocale();

        ((Label) pane.getChildren().get(3)).setText(LanguageManager.getText("game_title"));
        ((RadioButton) pane.getChildren().get(4)).setText(LanguageManager.getText("gamemode01"));
        ((RadioButton) pane.getChildren().get(5)).setText(LanguageManager.getText("gamemode02"));
        ((Label) pane.getChildren().get(6)).setText(LanguageManager.getText("team_label"));
        ((RadioButton) pane.getChildren().get(7)).setText(LanguageManager.getText("black_label"));
        ((RadioButton) pane.getChildren().get(8)).setText(LanguageManager.getText("white_label"));
        ((RadioButton) pane.getChildren().get(9)).setText(LanguageManager.getText("gamemode03"));
        ((Button) pane.getChildren().get(10)).setText(LanguageManager.getText("start_button"));
        ((Button) pane.getChildren().get(11)).setText(LanguageManager.getText("quit_button"));
        ((Button) pane.getChildren().get(13)).setText(LanguageManager.getText("language"));
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
            stage.setScene(new Scene(WindowManager.createWindow("Network.fxml")));
            stage.setTitle(LanguageManager.getText("network_title"));
        }else{
            stage.setScene(new Scene(WindowManager.createWindow("schachbrett.fxml")));
            stage.setTitle(LanguageManager.getText("game_title"));
            Controller controller = (Controller) WindowManager.getController();
            controller.init(gameMode, playerColorBlack, null);
        }
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.show();

        // Hide this current window
        ((Node) (event.getSource())).getScene().getWindow().hide();
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
