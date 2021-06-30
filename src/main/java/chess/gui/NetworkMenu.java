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
    private Pane menu;
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

            Stage stage = new Stage();
            stage.setTitle(LanguageManager.getText("game_title"));
            stage.setScene(new Scene(WindowManager.createWindow("schachbrett.fxml")));
            stage.centerOnScreen();

            Controller controller = (Controller) WindowManager.getController();
            controller.init(GameMode.NETWORK,isBlack, network);

            stage.show();

            // Hide this current window
            ((Node) (event.getSource())).getScene().getWindow().hide();
        }catch(Exception x){
            getNetworkError().setVisible(true);
        }

    }

    @FXML
    private void backToMenu(MouseEvent event){
        stage.setTitle(LanguageManager.getText("menu_title"));
        stage.setScene(new Scene(WindowManager.createWindow("MainMenu.fxml")));
        stage.show();

        // Hide this current window
        ((Node) (event.getSource())).getScene().getWindow().hide();
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

        getButtonLanguage().setText(LanguageManager.getText("language"));
        getTitle().setText(LanguageManager.getText("networkSettingsTitle"));
        getButtonNewGame().setText(LanguageManager.getText("newGame"));
        getTextColor().setText(LanguageManager.getText("yourColor"));
        getButtonJoinGame().setText(LanguageManager.getText("joinGame"));
        getTextIP().setText(LanguageManager.getText("ip"));
        getTextPort().setText(LanguageManager.getText("port"));
        getButtonMenu().setText(LanguageManager.getText("menu_button"));
        getButtonStart().setText(LanguageManager.getText("start_button"));
        getNetworkError().setText(LanguageManager.getText("network_error"));
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

    private Text getTitle() {
        return (Text) menu.getChildren().get(2);
    }
    private Text getTextPort(){
        return (Text) menu.getChildren().get(4);
    }
    private Button getButtonStart(){
        return (Button) menu.getChildren().get(5);
    }
    private Button getButtonMenu(){
        return (Button) menu.getChildren().get(6);
    }
    private Text getTextColor(){
        return (Text) menu.getChildren().get(7);
    }
    private Text getTextIP(){
        return (Text) menu.getChildren().get(11);
    }
    private Button getButtonLanguage(){
        return (Button) menu.getChildren().get(12);
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
