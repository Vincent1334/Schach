package chess.gui;

import chess.controller.CoreGame;
import chess.model.Move;
import chess.model.Position;
import javafx.event.ActionEvent;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;

import static javafx.scene.paint.Color.*;

public class SampleController {

    private static CoreGame coreGame;
    private Rectangle startField;

    public GridPane gridPane;

    public Move handleFieldClick(MouseEvent mouseEvent) {
        if (mouseEvent.getTarget() instanceof Rectangle) {

            Rectangle r = (Rectangle) mouseEvent.getTarget();
            if (startField == null) {
                startField = r;
                startField.setStroke(CYAN);
                startField.setStrokeWidth(5);
                startField.setStrokeType(StrokeType.INSIDE);
            } else {
                // Problem: Grid Pane Koordinatenursprung ist oben links, Board Koordinatenursprung ist unten links
                Position startPosition = new Position(GridPane.getColumnIndex(startField)-1, 8-GridPane.getRowIndex(startField));
                Position targetPosition = new Position(GridPane.getColumnIndex(r)-1, 8-GridPane.getRowIndex(r));

                Move move = new Move(startPosition, targetPosition);
                coreGame.chessMove(move);
                updateScene();

                startField.setStroke(color(0.97, 0.69, 0.53));
                startField.setStrokeWidth(1);
                startField.setStrokeType(StrokeType.OUTSIDE);
                startField = null;

                return move;
            }
        }
        return null;
    }

    public void updateScene() {
        //drawBoard();
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                drawFigure(x, y);
            }
        }
    }

    private void drawFigure(int x, int y) {
        Image image = getImage(coreGame.getCurrentBoard().getFigure(x, y).getSymbol());
        //controller.gridPane(x+1,y+1).setImage(image);
        //gridPane.add(image,x+1,y+1);

        ImageView iv = new ImageView();
        iv.setImage(image);
        gridPane.getChildren().add(iv);
    }

    private Image getImage(char symbol) {
        switch (symbol) {
            case 'p':
                return ImageHandler.getInstance().getImage("PawnBlack");
            case 'b':
                return ImageHandler.getInstance().getImage("BishopBlack");
            //...
            default:
                return ImageHandler.getInstance().getImage("KingBlack");
        }
    }

    public void init(ActionEvent actionEvent) {
        coreGame = new CoreGame();
    }
}