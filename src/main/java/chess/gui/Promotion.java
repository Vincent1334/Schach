package chess.gui;

import chess.managers.LanguageManager;
import chess.model.Move;
import chess.model.Position;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

/**
 * TODO: JavaDoc
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
     * @param startPosition
     * @param targetPosition
     * @param logic
     */
    protected void init(Position startPosition, Position targetPosition, Logic logic) {
        this.startPosition = startPosition;
        this.targetPosition = targetPosition;
        this.logic = logic;
    }

    /**
     * @param event
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

    @FXML
    private void setLanguage() {
        LanguageManager.nextLocale();
    }
}
