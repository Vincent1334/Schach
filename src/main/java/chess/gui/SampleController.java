package chess.gui;

import chess.controller.CoreGame;
import chess.model.Move;
import chess.model.Position;
import javafx.event.ActionEvent;
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
                Position startPosition = new Position(GridPane.getColumnIndex(startField) - 1, 8 - GridPane.getRowIndex(startField));
                Position targetPosition = new Position(GridPane.getColumnIndex(r) - 1, 8 - GridPane.getRowIndex(r));

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
        // TODO: alle Bilder entfernen / clearBoard
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
        if (image != null) {
            ImageView iv = new ImageView();
            iv.setImage(image);
            iv.setScaleX(0.3);
            iv.setScaleY(0.3);
            //gridPane.getChildren().add(iv);
            gridPane.add(iv, x+1, 8-y);
        }

    }

    private Image getImage(char symbol) {
        switch (symbol) {
            case 'P':
                return ImageHandler.getInstance().getImage("PawnWhite");
            case 'B':
                return ImageHandler.getInstance().getImage("BishopWhite");
            case 'K':
                return ImageHandler.getInstance().getImage("KingWhite");
            case 'Q':
                return ImageHandler.getInstance().getImage("QueenWhite");
            case 'N':
                return ImageHandler.getInstance().getImage("KnightWhite");
            case 'R':
                return ImageHandler.getInstance().getImage("RookWhite");
            case 'p':
                return ImageHandler.getInstance().getImage("PawnBlack");
            case 'b':
                return ImageHandler.getInstance().getImage("BishopBlack");
            case 'k':
                return ImageHandler.getInstance().getImage("KingBlack");
            case 'q':
                return ImageHandler.getInstance().getImage("QueenBlack");
            case 'n':
                return ImageHandler.getInstance().getImage("KnightBlack");
            case 'r':
                return ImageHandler.getInstance().getImage("RookBlack");
            default:
                return null;
        }
    }

    public void init(ActionEvent actionEvent) {
        coreGame = new CoreGame();
    }
}