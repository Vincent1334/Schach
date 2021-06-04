package chess.gui;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class Menu {

    private int gameMode = 1;
    private boolean playerColorBlack;

    @FXML
    void quitGame() {

        System.exit(0);
    }


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

    @FXML
    private void setMode01() {
        gameMode = 1;
    }
    @FXML
    private void setMode02() {
        gameMode = 2;
    }
    @FXML
    private void setMode03() {
        gameMode = 3;
    }

    public void setBlack() {
        playerColorBlack = true;
    }

    public void setWhite() {
        playerColorBlack = false;
    }
}
