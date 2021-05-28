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

    @FXML
    void quitGame(MouseEvent event) {

    }

    @FXML
    void startGame(MouseEvent event) {
        backendInterface.getMainpanel().setDisable(false);
        backendInterface.resetCoreGame();
        backendInterface.drawFigures();


        //Close window
        Stage stage = (Stage) startButton.getScene().getWindow();
        stage.close();

    }

    public void setControllerInterface(MainFrame backendInterface) {
        this.backendInterface = backendInterface;
    }
}
