package chess.gui;

import chess.managers.LanguageManager;
import chess.model.Move;
import chess.model.Position;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

/**
 * This class contains information about the pawn conversion menu, it´s structure and functions
 *
 * @author Lydia Engelhardt, Sophia Kuhlmann, Vincent Schiller, Friederike Weilbeer
 * 2021-07-02
 */
@SuppressWarnings({"PMD.UnusedPrivateMethod"})
// the methods getPromotionFigure and setLanguage are used by the gui but PMD does not recognize this
public class Promotion {

    private Logic logic;
    private Position startPosition;
    private Position targetPosition;
    @FXML
    public Pane promotionPane;

    /**
     * initializes the promotion
     *
     * @param startPosition  the start position
     * @param targetPosition the target position
     * @param logic          the logic
     */
    protected void init(Position startPosition, Position targetPosition, Logic logic) {
        this.startPosition = startPosition;
        this.targetPosition = targetPosition;
        this.logic = logic;
    }

    /**
     * converts the pawn into the clicked figure
     *
     * @param event the mouseclick event, used to know which figure was clicked
     */
    @FXML
    private void getPromotionFigure(MouseEvent event) {
        int id = 0;
        String item = ((Button) event.getSource()).getText();
        if (item.equals(LanguageManager.getText("queen_label"))) {
            id = 5;
        }
        if (item.equals(LanguageManager.getText("bishop_label"))) {
            id = 4;
        }
        if (item.equals(LanguageManager.getText("rook_label"))) {
            id = 2;
        }
        if (item.equals(LanguageManager.getText("knight_label"))) {
            id = 3;
        }
        ((Node) (event.getSource())).getScene().getWindow().hide();
        logic.performMove(new Move(startPosition, targetPosition, id));
    }

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
}
