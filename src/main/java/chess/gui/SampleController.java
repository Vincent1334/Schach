package chess.gui;

import chess.controller.CoreGame;
import chess.model.Move;
import chess.model.Position;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;

import static javafx.scene.paint.Color.BLACK;

public class SampleController {

    private static CoreGame coreGame;

    public void handleFieldClick(MouseEvent mouseEvent) {
        if (mouseEvent.getTarget() instanceof Rectangle) {
            Rectangle r = (Rectangle) mouseEvent.getTarget();
            r.setStroke(BLACK);
        }
    }

    /*public static void enterGame() {

        Move move = new Move(new Position(posX, posY), new Position(newX, newY));

        coreGame = new CoreGame();
        coreGame.chessMove();
    }*/

}