package chess.gui;

import chess.controller.*;
import chess.model.*;
import chess.util.Observer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.Collections;

import static javafx.scene.paint.Color.*;

public class SampleController implements Observer {

    private static CoreGame coreGame;
    private Rectangle startField;
    private boolean black = true;
    private int indexBeatenFiguresBlack = 1;
    private int indexBeatenFiguresWhite = 1;
    private int indexHistory = 0;
    private boolean even = true;
    @FXML
    private Label player;
    @FXML
    private GridPane gridPane;
    @FXML
    private GridPane beatenFigures;
    @FXML
    private GridPane history;
    @FXML
    private VBox rowsLeft;
    @FXML
    private VBox rowsRight;
    @FXML
    private HBox columnsTop;
    @FXML
    private HBox columnsBottom;
    @FXML
    private ToggleButton turnBoard;
    @FXML
    private ToggleButton possibleFieldsButton;


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
                if (getImageByRowColumnIndex(GridPane.getColumnIndex(startField), GridPane.getRowIndex(startField)) != null
                        && possibleFieldsButton.isSelected()) {
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
                        updateScene(targetField, move);
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
    public void updateScene(Rectangle targetField, Move move) {
        // get image on clicked field
        ImageView iv = (ImageView) getImageByRowColumnIndex(GridPane.getColumnIndex(startField), GridPane.getRowIndex(startField));
        GridPane.setColumnIndex(iv, GridPane.getColumnIndex(targetField));
        GridPane.setRowIndex(iv, GridPane.getRowIndex(targetField));
        updateHistory(move);
        updatePlayer(black);
        black = !black;
        if (turnBoard.isSelected()) {
            turnBoard();
        }
    }

    private void turnBoard() {
        gridPane.setRotate(even ? 180 : 0);
        columnsTop.setRotate(even ? 180 : 0);
        columnsBottom.setRotate(even ? 180 : 0);
        rowsLeft.setRotate(even ? 180 : 0);
        rowsRight.setRotate(even ? 180 : 0);
        ObservableList<Node> children = gridPane.getChildren();
        for (Node node : children) {
            node.setRotate(even ? 180 : 0);
        }
        even = !even;

        swap(rowsRight.getChildren());
        swap(rowsLeft.getChildren());
        swap(columnsBottom.getChildren());
        swap(columnsTop.getChildren());
    }

    private void swap(ObservableList<Node> children) {
        ObservableList<Node> workingCollection = FXCollections.observableArrayList(children);
        Collections.swap(workingCollection, 0, 7);
        Collections.swap(workingCollection, 1, 6);
        Collections.swap(workingCollection, 2, 5);
        Collections.swap(workingCollection, 3, 4);
        children.setAll(workingCollection);
    }

    public void updateHistory(Move move) {
        Text t = new Text(move.toString());
        history.add(t, 1, indexHistory);
        history.add(new Text(" " + (indexHistory + 1)), 0, indexHistory);
        indexHistory += 1;
    }

    private void updatePlayer(boolean black) {
        if (black) {
            player.setText("black");
        } else {
            player.setText("white");
        }
    }


    @Override
    public void updateBeatenFigures(int posX, int posY) {
        ImageView iv = (ImageView) getImageByRowColumnIndex(posX + 1, 8 - posY);
        iv.setFitHeight(65.0);
        iv.setFitWidth(45.0);
        beatenFigures.getChildren().add(iv);

        if (black) {
            GridPane.setColumnIndex(iv, indexBeatenFiguresBlack);
            GridPane.setRowIndex(iv, 1);
            indexBeatenFiguresBlack += 1;
        } else {
            GridPane.setColumnIndex(iv, indexBeatenFiguresWhite);
            GridPane.setRowIndex(iv, 0);
            indexBeatenFiguresWhite += 1;
        }

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
}