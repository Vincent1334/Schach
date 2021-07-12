package chess.gui;

import chess.enums.GameMode;
import chess.managers.LanguageManager;
import chess.managers.WindowManager;
import chess.network.NetworkPlayer;
import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import static javafx.scene.paint.Color.BLACK;
import static javafx.scene.paint.Color.valueOf;

/**
 * This class contains information about the network menu, it´s structure and functions
 *
 * @author Lydia Engelhardt, Sophia Kuhlmann, Vincent Schiller, Friederike Weilbeer
 * 2021-07-02
 */
@SuppressWarnings({"PMD.UnusedPrivateMethod"})
// the methods backToMenu, setColor, setLanguage and setDisableIP are used by the gui but PMD does not recognize this
public class NetworkMenu {

    private boolean isBlack = true;
    @FXML
    public Pane menu;
    @FXML
    private Rectangle white;
    @FXML
    private Rectangle black;

    /**
     * starts the networkgame
     */
    @FXML
    private void start() {
        try {
            NetworkPlayer network;
            network = new NetworkPlayer(Integer.parseInt(getPortInput().getText()), getIPInput().getText(), !getButtonJoinGame().isSelected(), isBlack);

            String stage = "GameStage";
            WindowManager.initialWindow(stage, "game_title");
            ((Controller) WindowManager.getController(stage)).init(GameMode.NETWORK, isBlack, network);
            WindowManager.showStage(stage);

            // Hide this current window
            WindowManager.closeStage("NetworkStage");
        } catch (Exception x) {
            getNetworkError().setVisible(true);
        }

    }

    /**
     * returns to the main menu
     */
    @FXML
    private void backToMenu() {
        WindowManager.initialWindow("MenuStage", "menu_title");
        WindowManager.showStage("MenuStage");

        // Hide this current window
        WindowManager.closeStage("NetworkStage");
    }

    /**
     * marks the clicked player color
     *
     * @param event the mouseEvent
     */
    @FXML
    private void setColor(MouseEvent event) {
        Rectangle clickedField = (Rectangle) event.getTarget();
        if (clickedField == black) {
            isBlack = true;
            black.setStroke(valueOf("#8fbe00"));
            black.setStrokeWidth(2.5);
            white.setStroke(BLACK);
            white.setStrokeWidth(1.0);
        } else {
            isBlack = false;
            black.setStroke(BLACK);
            black.setStrokeWidth(1.0);
            white.setStroke(valueOf("#8fbe00"));
            white.setStrokeWidth(2.5);
        }
    }

    /**
     * Disables the IP textfield if a new networkgame is started
     */
    @FXML
    private void setDisableIP() {
        if (getButtonNewGame().isSelected()) {
            getIPInput().setDisable(true);
            getTextIP().setOpacity(0.5);
            getTextColor().setOpacity(1.0);
            black.setDisable(false);
            black.setOpacity(1.0);
            white.setDisable(false);
            white.setOpacity(1.0);
        } else {
            getIPInput().setDisable(false);
            getTextIP().setOpacity(1);
            getTextColor().setOpacity(0.5);
            black.setDisable(true);
            black.setOpacity(0.5);
            white.setDisable(true);
            white.setOpacity(0.5);
        }
    }


    //--------------getter / setter---------------------------------------------------------------------------------------------------------------


    /**
     * switches the language of the gui elements
     *
     * @param event the mouse event, used to know which flag was clicked
     */
    @FXML
    private void setLanguage(MouseEvent event) {
        //the url of the clicked image
        String url = ((ImageView) event.getTarget()).getImage().getUrl();
        //sets the language after the name of the image
        LanguageManager.setLanguage(url.substring(url.length() - 6, url.length() - 4));
    }

    private Text getTextColor() {
        return (Text) menu.getChildren().get(7);
    }

    private Text getTextIP() {
        return (Text) menu.getChildren().get(11);
    }

    private RadioButton getButtonNewGame() {
        return (RadioButton) menu.getChildren().get(12);
    }

    private RadioButton getButtonJoinGame() {
        return (RadioButton) menu.getChildren().get(13);
    }

    private TextField getPortInput() {
        return (TextField) menu.getChildren().get(3);
    }

    private TextField getIPInput() {
        return (TextField) menu.getChildren().get(10);
    }

    private Text getNetworkError() {
        return (Text) menu.getChildren().get(14);
    }
}
