package chess.gui;

import chess.enums.GameMode;
import chess.managers.LanguageManager;
import chess.managers.WindowManager;
import chess.network.NetworkPlayer;
import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import static javafx.scene.paint.Color.*;

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
     * TODO
     */
    @FXML
    private void start() {
        try {
            NetworkPlayer network;
            if (getButtonJoinGame().isSelected()) {
                network = new NetworkPlayer(getIPInput().getText(), Integer.parseInt(getPortInput().getText()));
            } else {
                network = new NetworkPlayer(Integer.parseInt(getPortInput().getText()), isBlack);
            }

            WindowManager.initialWindow("GameStage", "game_title");
            LanguageManager.networkID = " - " + network.getIpAddress() + ":" + network.getPort();
            WindowManager.getStage("GameStage").setTitle(WindowManager.getStage("GameStage").getTitle() + " - " + network.getIpAddress() + ":" + network.getPort());
            ((Controller) WindowManager.getController("GameStage")).init(GameMode.NETWORK, isBlack, network);
            WindowManager.showStage("GameStage");

            // Hide this current window
            WindowManager.closeStage("NetworkStage");
        } catch (Exception x) {
            getNetworkError().setVisible(true);
        }

    }

    /**
     * TODO
     */
    @FXML
    private void backToMenu() {
        WindowManager.initialWindow("MenuStage", "menu_title");
        WindowManager.showStage("MenuStage");

        // Hide this current window
        WindowManager.closeStage("NetworkStage");
    }

    /**
     * TODO
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
     * TODO
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


    @FXML
    private void setLanguage(MouseEvent event) {
        LanguageManager.setLanguage(((ImageView) event.getTarget()).getImage().getUrl().substring(64,66));
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
