package chess.gui;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class Menu {

    private int gameMode = 1;

    @FXML
    void quitGame(MouseEvent event) {
        System.exit(0);
    }

    /*@FXML
    void startGame(MouseEvent event) {
        //unlock Mainframe
        backendInterface.getMainpanel().setDisable(false);
        backendInterface.resetCoreGame();
        backendInterface.setGameStart(true);
        backendInterface.renderBoard();

        //setGameMode
        backendInterface.setGameMode(gameMode);

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("schachbrett2.fxml"));

            //SampleController controller = new SampleController(gameMode);
            //fxmlLoader.setController(controller);

            Parent root = fxmlLoader.load();

            Stage stage = new Stage();
            stage.setTitle("Schach");

            stage.setScene(new Scene(root));
            stage.show();

        //Close window
        Stage stage = (Stage) startButton.getScene().getWindow();
        stage.close();

    }*/

    @FXML
    private void startGame(MouseEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("schachbrett2.fxml"));
            Parent root = fxmlLoader.load();

            SampleController controller = fxmlLoader.getController();
            controller.init(gameMode);

            Stage stage = new Stage();
            stage.setTitle("Schachspiel");
            stage.setScene(new Scene(root));
            stage.show();

            // Hide this current window
            ((Node) (event.getSource())).getScene().getWindow().hide();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void setMode01(MouseEvent event) {
        gameMode = 1;
    }

    @FXML
    private void setMode02(MouseEvent event) {
        gameMode = 2;
    }

    @FXML
    private void setMode03(MouseEvent event) {
        gameMode = 3;
    }

    /*public void setControllerInterface(MainFrame backendInterface) {
        this.backendInterface = backendInterface;
    }*/
}
