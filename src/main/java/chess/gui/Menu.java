package chess.gui;


import chess.GameMode;
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

import java.io.IOException;
import java.util.Locale;
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
    private static ResourceBundle messages = ResourceBundle.getBundle("/languages/MessagesBundle", Gui.locale);
    private GameMode gameMode;
    private boolean playerColorBlack;
    @FXML
    private Pane pane;


    /**
     * sets the language
     */
    @FXML
    private void setLanguage(){
        if(messages.getLocale().getCountry().equals("DE")){
            messages = ResourceBundle.getBundle("/languages/MessagesBundle", new Locale("en", "US"));
        }else if(messages.getLocale().getCountry().equals("US")){
            messages = ResourceBundle.getBundle("/languages/MessagesBundle", new Locale("fr", "FR"));
        }else if(messages.getLocale().getCountry().equals("FR")){
            messages = ResourceBundle.getBundle("/languages/MessagesBundle", new Locale("de", "DE"));
        }
        System.out.println(messages.getLocale().getCountry());
        ((Label) pane.getChildren().get(3)).setText(messages.getString("game_title"));
        ((RadioButton) pane.getChildren().get(4)).setText(messages.getString("gamemode01"));
        ((RadioButton) pane.getChildren().get(5)).setText(messages.getString("gamemode02"));
        ((Label) pane.getChildren().get(6)).setText(messages.getString("team_label"));
        ((RadioButton) pane.getChildren().get(7)).setText(messages.getString("black_label"));
        ((RadioButton) pane.getChildren().get(8)).setText(messages.getString("white_label"));
        ((RadioButton) pane.getChildren().get(9)).setText(messages.getString("gamemode03"));
        ((Button) pane.getChildren().get(10)).setText(messages.getString("start_button"));
        ((Button) pane.getChildren().get(11)).setText(messages.getString("quit_button"));
        ((Button) pane.getChildren().get(13)).setText(messages.getString("language"));
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
        try {
            FXMLLoader fxmlLoader;
            Stage stage = new Stage();
            Parent root;
            if(gameMode== GameMode.NETWORK){
                fxmlLoader = new FXMLLoader(getClass().getResource("Network.fxml"));
                //fxmlLoader.setResources(ResourceBundle.getBundle("/languages/MessagesBundle", messages.getLocale()));
                root = fxmlLoader.load();
                stage.setTitle("Netzwerkmenü");
            }else{
                fxmlLoader = new FXMLLoader(getClass().getResource("schachbrett.fxml"));
                fxmlLoader.setResources(ResourceBundle.getBundle("/languages/MessagesBundle", messages.getLocale()));
                root = fxmlLoader.load();
                Controller controller = fxmlLoader.getController();
                controller.init(gameMode, playerColorBlack, null);
                stage.setTitle(ResourceBundle.getBundle("/languages/MessagesBundle", Gui.locale).getString("game_title"));
            }
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
