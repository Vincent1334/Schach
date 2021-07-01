package chess.gui;

import chess.GameMode;
import chess.managers.LanguageManager;
import chess.managers.WindowManager;
import chess.network.NetworkPlayer;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import static javafx.scene.paint.Color.*;

public class NetworkMenu {

    private boolean isBlack = true;
    private Stage stage = new Stage();

    @FXML
    public Pane menu;
    @FXML
    private Rectangle white;
    @FXML
    private Rectangle black;


    @FXML
    private void start(MouseEvent event){
        try{
            NetworkPlayer network;
            if(getButtonJoinGame().isSelected()) network = new NetworkPlayer(getIPInput().getText(), Integer.valueOf(getPortInput().getText()));
            else network = new NetworkPlayer(Integer.valueOf(getPortInput().getText()), isBlack);

            WindowManager.initialWindow("GameStage", "game_title");
            ((Controller) WindowManager.getController("GameStage")).init(GameMode.NETWORK, isBlack, network);
            WindowManager.showStage("GameStage");

            // Hide this current window
            WindowManager.closeStage("NetworkStage");
        }catch(Exception x){
            getNetworkError().setVisible(true);
        }

    }

    @FXML
    private void backToMenu(MouseEvent event){
        WindowManager.initialWindow("MenuStage", "menu_title");
        WindowManager.showStage("MenuStage");

        // Hide this current window
        WindowManager.closeStage("NetworkStage");
    }

    @FXML
    private void setColor(MouseEvent event){
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
        LanguageManager.nextLocale();
    }

    @FXML
    private void setDissableIP(){
        if(getButtonNewGame().isSelected()){
            getIPInput().setDisable(true);
            getTextIP().setOpacity(0.5);
            getTextColor().setOpacity(1.0);
            black.setDisable(false);
            black.setOpacity(1.0);
            white.setDisable(false);
            white.setOpacity(1.0);
        }else{
            getIPInput().setDisable(false);
            getTextIP().setOpacity(1);
            getTextColor().setOpacity(0.5);
            black.setDisable(true);
            black.setOpacity(0.5);
            white.setDisable(true);
            white.setOpacity(0.5);
        }
    }

    private Text getTextColor(){
        return (Text) menu.getChildren().get(7);
    }
    private Text getTextIP(){
        return (Text) menu.getChildren().get(11);
    }
    private RadioButton getButtonNewGame(){
        return (RadioButton) menu.getChildren().get(13);
    }
    private RadioButton getButtonJoinGame(){
        return (RadioButton) menu.getChildren().get(14);
    }
    private TextField getPortInput() {return (TextField) menu.getChildren().get(3);}
    private TextField getIPInput() {return (TextField) menu.getChildren().get(10);}
    private Text getNetworkError(){
        return (Text) menu.getChildren().get(15);
    }
}
