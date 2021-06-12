package chess.gui;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * manages the main menu options
 *
 * @author Lydia Engelhardt, Sophia Kuhlmann, Vincent Schiller, Friederike Weilbeer
 * 2021-06-09
 */
public class Menu {

    private int gameMode = 1;
    private boolean playerColorBlack;

    /**
     * quits the game
     */
    @FXML
    void quitGame() {
        System.exit(0);
    }

    // the methods setMode... and startGame are used by the gui but PMD didn't recognized
    /**
     * starts the game
     * @param event
     */
    @SuppressWarnings({"PMD.UnusedPrivateMethod"})
    @FXML
    private void startGame(MouseEvent event) {
        try {

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("schachbrett.fxml"));
            Parent root = fxmlLoader.load();

            Controller controller = fxmlLoader.getController();
            controller.init(gameMode, playerColorBlack);

            Stage stage = new Stage();
            stage.setTitle("chess");
            stage.setY(20);
            stage.setScene(new Scene(root));
            stage.show();

            // Hide this current window
            ((Node) (event.getSource())).getScene().getWindow().hide();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * sets the game mode for a local game against a friend
     */
    @SuppressWarnings({"PMD.UnusedPrivateMethod"})
    @FXML
    private void setMode01() {
        gameMode = 1;
    }

    /**
     * sets the game mode for a local game against the computer
     */
    @SuppressWarnings({"PMD.UnusedPrivateMethod"})
    @FXML
    private void setMode02() {
        gameMode = 2;
    }

    /**
     * sets the game mode for a network game
     */
    @SuppressWarnings({"PMD.UnusedPrivateMethod"})
    @FXML
    private void setMode03() {
        gameMode = 3;
    }

    /**
     * sets the playerColor black
     */
    public void setBlack() {
        playerColorBlack = true;
    }

    /**
     * sets the playerColor White
     */
    public void setWhite() {
        playerColorBlack = false;
    }
}
