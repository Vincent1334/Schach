package chess.gui;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ResourceBundle;

/**
 * manages the main menu options
 *
 * @author Lydia Engelhardt, Sophia Kuhlmann, Vincent Schiller, Friederike Weilbeer
 * 2021-06-09
 */
@SuppressWarnings({"PMD.UnusedPrivateMethod"})
// the methods setMode... and startGame are used by the gui but PMD didn't recognize
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

    /**
     * starts the game
     *
     * @param event the mouse event
     */
    @FXML
    private void startGame(MouseEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("schachbrett.fxml"));
            fxmlLoader.setResources(ResourceBundle.getBundle("/languages/MessagesBundle", Gui.locale));
            Parent root = fxmlLoader.load();

            Controller controller = fxmlLoader.getController();
            controller.init(gameMode, playerColorBlack);

            Stage stage = new Stage();
            stage.setTitle(ResourceBundle.getBundle("/languages/MessagesBundle", Gui.locale).getString("game_title"));
            stage.setScene(new Scene(root));
            stage.centerOnScreen();
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
    @FXML
    private void setMode01() {
        gameMode = 1;
    }

    /**
     * sets the game mode for a local game against the computer
     */
    @FXML
    private void setMode02() {
        gameMode = 2;
    }

    /**
     * sets the game mode for a network game
     */
    @FXML
    private void setMode03() {
        gameMode = 3;
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
