package chess.gui;


import chess.Main;
import chess.controller.CoreGame;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class MenuFrame {

    @FXML
    private Button startButton;

    private MainFrame backendInterface;
    private int gameMode = 1;

    @FXML
    void quitGame(MouseEvent event) {
        System.exit(0);
    }

    @FXML
    void startGame(MouseEvent event) {
        //unlock Mainframe
        backendInterface.getMainpanel().setDisable(false);
        backendInterface.resetCoreGame();
        backendInterface.setGameStart(true);
        backendInterface.renderBoard();

        //setGameMode
        backendInterface.setGameMode(gameMode);


        //Close window
        Stage stage = (Stage) startButton.getScene().getWindow();
        stage.close();

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

    public void setControllerInterface(MainFrame backendInterface) {
        this.backendInterface = backendInterface;
    }
}
