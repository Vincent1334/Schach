package chess.gui;

import chess.controller.CoreGame;
import chess.model.Move;
import chess.model.Position;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Node;
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

    /*private Node getFieldByRowColumnIndex(int row, int column) {
        Node result = null;
        ObservableList<Node> children = gridPane.getChildren();

        for (Node node : children) {
            if (GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == column) {
                result = node;
                break;
            }
        }
        return result;
    }*/

    private Node getImageByRowColumnIndex(int column, int row) {
        Node result = null;
        ObservableList<Node> children = gridPane.getChildren();

        for (Node node : children) {
            if (GridPane.getRowIndex(node) != null && GridPane.getColumnIndex(node) != null &&
                    GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == column && node instanceof ImageView) {
                result = node;
                break;
            }
        }
        return result;
    }

    public void handleFieldClick(MouseEvent mouseEvent) {
        if (mouseEvent.getTarget() instanceof Rectangle) {

            Rectangle targetField = (Rectangle) mouseEvent.getTarget();

            // erstes Feld angeklickt
            if (startField == null) {
                startField = targetField;
                startField.setStroke(CYAN);
                startField.setStrokeWidth(5);
                startField.setStrokeType(StrokeType.INSIDE);
            }

            // zweites Feld angeklickt
            else {
                // auf dem ersten Feld stand eine Figur
                if (getImageByRowColumnIndex(GridPane.getColumnIndex(startField), GridPane.getRowIndex(startField)) != null) {

                    Position startPosition = new Position(GridPane.getColumnIndex(startField) - 1, 8 - GridPane.getRowIndex(startField));
                    Position targetPosition = new Position(GridPane.getColumnIndex(targetField) - 1, 8 - GridPane.getRowIndex(targetField));

                    Move move = new Move(startPosition, targetPosition);
                    if (coreGame.chessMove(move)) {
                        updateScene(targetField);
                    }
                }
                startField.setStroke(color(0.97, 0.69, 0.53));
                startField.setStrokeWidth(1);
                startField.setStrokeType(StrokeType.OUTSIDE);
                startField = null;
            }
        }
    }

    public void updateScene(Rectangle targetField) {
        /*// alle Bilder entfernen / clearBoard
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                drawFigure(x, y);
            }
        }*/

        // get image on clicked field
        ImageView iv = (ImageView) getImageByRowColumnIndex(GridPane.getColumnIndex(startField), GridPane.getRowIndex(startField));
        GridPane.setColumnIndex(iv, GridPane.getColumnIndex(targetField));
        GridPane.setRowIndex(iv, GridPane.getRowIndex(targetField));

        /*// Animation funktioniert noch nicht (Zielposition falsch?)
        TranslateTransition tt = new TranslateTransition(Duration.millis(1000), iv);
        tt.setToX(targetField.localToScene(targetField.getBoundsInLocal()).getMinX());
        tt.setToY(targetField.localToScene(targetField.getBoundsInLocal()).getMinY());
        tt.play();*/
    }

    // zu Testzwecken
    public void removeFigure(ActionEvent actionEvent) {
        ImageView iv = (ImageView) getImageByRowColumnIndex(GridPane.getColumnIndex(startField), GridPane.getRowIndex(startField));
        gridPane.getChildren().remove(iv);
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
            gridPane.add(iv, x + 1, 8 - y);
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