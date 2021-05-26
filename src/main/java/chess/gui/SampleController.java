package chess.gui;

import chess.controller.*;
import chess.model.*;
import chess.util.Observer;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import java.util.ArrayList;
import static javafx.scene.paint.Color.*;

public class SampleController implements Observer {

    private static CoreGame coreGame;
    private Rectangle startField;
    private boolean black = true;
    @FXML
    private Label player;
    @FXML
    private GridPane gridPane;



    public void init(ActionEvent actionEvent) {
        coreGame = new CoreGame();
        Rules.addObserver(this);
    }


    //--------------------------------------Field----------------------------------------------------------------------------------------------
    public void handleFieldClick(MouseEvent mouseEvent) {
        if (mouseEvent.getTarget() instanceof Rectangle) {

            Rectangle targetField = (Rectangle) mouseEvent.getTarget();

            // erstes Feld angeklickt
            if (startField == null) {
                startField = targetField;
                markField(startField, CYAN);

                // auf dem Feld steht eine Figur
                if (getImageByRowColumnIndex(GridPane.getColumnIndex(startField), GridPane.getRowIndex(startField)) != null) {
                    for (Rectangle field : getPossibleFields(startField)) {
                        markField(field, BLUEVIOLET);
                    }
                }
            }

            // zweites Feld angeklickt
            else {
                // auf dem ersten Feld stand eine Figur
                if (getImageByRowColumnIndex(GridPane.getColumnIndex(startField), GridPane.getRowIndex(startField)) != null) {

                    Position startPosition = new Position(GridPane.getColumnIndex(startField) - 1, 8 - GridPane.getRowIndex(startField));
                    Position targetPosition = new Position(GridPane.getColumnIndex(targetField) - 1, 8 - GridPane.getRowIndex(targetField));

                    for (Rectangle field : getPossibleFields(startField)) {
                        unmarkField(field);
                    }

                    Move move = new Move(startPosition, targetPosition);
                    if (coreGame.chessMove(move)) {
                        updateScene(targetField);
                    }

                }
                unmarkField(startField);
                startField = null;
            }
        }
    }

    private ArrayList<Rectangle> getPossibleFields(Rectangle actualField) {

        Position actualPosition = new Position(GridPane.getColumnIndex(startField) - 1, 8 - GridPane.getRowIndex(startField));
        Board board = coreGame.getCurrentBoard();

        ArrayList<Position> positions = Rules.possibleTargetFields(actualPosition, board);
        ArrayList<Rectangle> fields = new ArrayList<>();

        for (Position position : positions) {
            fields.add((Rectangle) getFieldByRowColumnIndex(8 - position.getPosY(), position.getPosX() + 1));
        }

        return fields;
    }

    private void markField(Rectangle field, Color color) {
        field.setStroke(color);
        field.setStrokeWidth(5);
        field.setStrokeType(StrokeType.INSIDE);
    }

    private void unmarkField(Rectangle field) {
        field.setStroke(color(0.97, 0.69, 0.53));
        field.setStrokeWidth(1);
        field.setStrokeType(StrokeType.OUTSIDE);
    }

    private Node getFieldByRowColumnIndex(int row, int column) {
        Node result = null;
        ObservableList<Node> children = gridPane.getChildren();

        for (Node node : children) {
            if (GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == column) {
                result = node;
                break;
            }
        }
        return result;
    }
//----------------------------------Update----------------------------------------------------------------------------------------------
    public void updateScene(Rectangle targetField) {
        // get image on clicked field
        ImageView iv = (ImageView) getImageByRowColumnIndex(GridPane.getColumnIndex(startField), GridPane.getRowIndex(startField));
        GridPane.setColumnIndex(iv, GridPane.getColumnIndex(targetField));
        GridPane.setRowIndex(iv, GridPane.getRowIndex(targetField));
        updatePlayer(black);
        black = !black;
    }

    private void updatePlayer(boolean black){
        if(black){
            player.setText("black");
        }else{
            player.setText("white");
        }
    }

    @Override
    public void updateBeatenFigures(int posX, int posY) {
        ImageView iv = (ImageView) getImageByRowColumnIndex(posX + 1, 8 - posY);
        gridPane.getChildren().remove(iv);
    }

    @Override
    public void updateExtraMoves(int posX, int posY, int newX, int newY) {
        // Castling
        ImageView iv = (ImageView) getImageByRowColumnIndex(posX + 1, 8 - posY);
        GridPane.setColumnIndex(iv, newX + 1);
        GridPane.setRowIndex(iv, 8 - newY);
    }

    @Override
    public void updateChange(int posX, int posY, int changeTo, boolean isBlackTeam) {
        // PawnConversion
        ImageView iv = (ImageView) getImageByRowColumnIndex(posX + 1, 8 - posY);
        iv.setImage(getImage(changeTo, isBlackTeam));
    }

//--------------------------------Image----------------------------------------------------------------------------------------------

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

    private Image getImage(int symbol, boolean isBlackTeam) {
        switch (symbol) {
            // Rook
            case 2:
                return isBlackTeam ? ImageHandler.getInstance().getImage("RookBlack") : ImageHandler.getInstance().getImage("RookWhite");
            // Knight
            case 3:
                return isBlackTeam ? ImageHandler.getInstance().getImage("KnightBlack") : ImageHandler.getInstance().getImage("KnightWhite");
            // Bishop
            case 4:
                return isBlackTeam ? ImageHandler.getInstance().getImage("BishopBlack") : ImageHandler.getInstance().getImage("BishopWhite");
            // Queen
            default:
                return isBlackTeam ? ImageHandler.getInstance().getImage("QueenBlack") : ImageHandler.getInstance().getImage("QueenWhite");
        }
    }

//----------------------------------Sonstiges------------------------------------------------------------------------------
    // zu Testzwecken
    public void removeFigure(ActionEvent actionEvent) {
        ImageView iv = (ImageView) getImageByRowColumnIndex(GridPane.getColumnIndex(startField), GridPane.getRowIndex(startField));
        gridPane.getChildren().remove(iv);
    }

}