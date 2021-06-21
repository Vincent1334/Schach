package chess.gui;

import chess.GameMode;
import chess.network.NetworkPlayer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import static javafx.scene.paint.Color.*;
import java.io.IOException;
import java.util.ResourceBundle;

public class NetworkMenu {

    private boolean isBlack = true;

    @FXML
    private TextField port;
    @FXML
    private TextField opponentIP;
    @FXML
    private RadioButton joinGame;
    @FXML
    private RadioButton newGame;
    @FXML
    private Rectangle white;
    @FXML
    private Rectangle black;
    @FXML
    private ProgressBar progressBar;
    private Stage stage = new Stage();

    @FXML
    private void start(MouseEvent event){
        try {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("schachbrett.fxml"));
                    fxmlLoader.setResources(ResourceBundle.getBundle("/languages/MessagesBundle", Gui.locale));
                    Parent root = fxmlLoader.load();
        
                    Controller controller = fxmlLoader.getController();
                    controller.init(GameMode.NETWORK,isBlack,joinGame.isSelected(),opponentIP.getText(),Integer.getInteger(port.getText()));
        
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

        // Hide this current window
        ((Node) (event.getSource())).getScene().getWindow().hide();

    }

    @FXML
    private void backToMenu(MouseEvent event){
    try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Menu.fxml"));
                fxmlLoader.setResources(ResourceBundle.getBundle("/languages/MessagesBundle", Gui.locale));
                Parent root = fxmlLoader.load();

                Stage stage = new Stage();
                stage.setTitle("Menu");
                stage.setScene(new Scene(root));
                stage.show();

                // Hide this current window
                ((Node) (event.getSource())).getScene().getWindow().hide();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    @FXML
    private void setColour(MouseEvent event){
        Rectangle clickedField = (Rectangle) event.getTarget();
        if(clickedField == black){
            isBlack = true;
            black.setStroke(valueOf("#8fbe00"));
            black.setStrokeWidth(2.5);
            white.setStroke(Color.BLACK);
            white.setStrokeWidth(1.0);
            }
        else{
            isBlack = false;
            black.setStroke(Color.BLACK);
            black.setStrokeWidth(1.0);
            white.setStroke(valueOf("#8fbe00"));
            white.setStrokeWidth(2.5);
        }
    }

    @FXML
    private void setLanguage(){

    }
}
