package chess.gui;

import chess.controller.*;
import chess.ki.Computer;
import chess.model.*;
import chess.util.Observer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
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
    /*private Computer computer;*/
    private Rectangle startField;
    private boolean blacksTurn = false;
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
    private CheckBox turnBoard;
    @FXML
    private CheckBox possibleFieldsButton;
    @FXML
    private CheckBox singleSelect;
    private int gameMode = 0;


    /*public void init(ActionEvent actionEvent) {
        coreGame = new CoreGame();
        computer = new Computer(true);
        Rules.addObserver(this);
    }*/
    /*public void init() {
        coreGame = new CoreGame();
        *//*computer = new Computer(true);*//*
        Rules.addObserver(this);
    }*/

    public SampleController(int gameMode) {
        coreGame = new CoreGame();
        /*computer = new Computer(true);*/
        Rules.addObserver(this);
        this.gameMode = gameMode;
    }


    //--------------------------------------Field----------------------------------------------------------------------------------------------
    public void handleFieldClick(MouseEvent mouseEvent) {
        if (mouseEvent.getTarget() instanceof Rectangle) {

            Rectangle targetField = (Rectangle) mouseEvent.getTarget();

            // erstes Feld angeklickt: markiere Feld
            if (startField == null) {
                startField = targetField;
                ImageView selectedFigure = (ImageView) getImageByRowColumnIndex(GridPane.getColumnIndex(startField), GridPane.getRowIndex(startField));
                markField(startField, CYAN);

                // auf dem Feld steht eine eigene Figur und AnzeigeMöglicherFelder ist eingeschaltet: markiere mögliche Felder
                if (selectedFigure != null && imageIsBlack(selectedFigure) == blacksTurn && possibleFieldsButton.isSelected()) {
                    for (Rectangle field : getPossibleFields(startField)) {
                        markField(field, BLUEVIOLET);
                    }
                }
            }

            // zweites Feld angeklickt
            else {
                ImageView selectedFigure = (ImageView) getImageByRowColumnIndex(GridPane.getColumnIndex(startField), GridPane.getRowIndex(startField));
                // auf dem ersten Feld stand eine eigene Figur
                if (selectedFigure != null && imageIsBlack(selectedFigure) == blacksTurn) {

                    // demarkiere mögliche Felder (muss vor dem Ausführen des Zuges aufgerufen werden)
                    for (Rectangle field : getPossibleFields(startField)) {
                        unmarkField(field);
                    }
                    // führe Zug aus (wenn möglich) und update Scene
                    Position startPosition = new Position(GridPane.getColumnIndex(startField) - 1, 8 - GridPane.getRowIndex(startField));
                    Position targetPosition = new Position(GridPane.getColumnIndex(targetField) - 1, 8 - GridPane.getRowIndex(targetField));
                    Move move = new Move(startPosition, targetPosition);
                    if (coreGame.chessMove(move)) {
                        updateScene(targetField, move);
                    }
                    /*//Check Computer play
                    if(gameMode == 2){
                        coreGame.chessMove(computer.makeMove(coreGame.getCurrentBoard()));
                        updateScene(targetField, move);
                    }*/
                }
                // eigene Figur ist ausgewählt und MehrfachAuswahl ist nicht erlaubt: demarkiere nicht und schalte kein neues Feld frei
                if (!(selectedFigure != null && imageIsBlack(selectedFigure) == blacksTurn && singleSelect.isSelected())) {
                    unmarkField(startField);
                    startField = null;
                }
                // aktuelles Feld ist noch vorhanden (und markiert) und AnzeigeMöglicherFelder ist eingeschaltet: markiere mögliche Felder
                if (startField != null && possibleFieldsButton.isSelected()) {
                    for (Rectangle field : getPossibleFields(startField)) {
                        markField(field, BLUEVIOLET);
                    }
                }
            }
        }
    }

    private ArrayList<Rectangle> getPossibleFields(Rectangle actualField) {

        Position actualPosition = new Position(GridPane.getColumnIndex(actualField) - 1, 8 - GridPane.getRowIndex(actualField));
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
        field.setStrokeWidth(3);
        field.setStrokeType(StrokeType.INSIDE);
    }

    private void unmarkField(Rectangle field) {
        field.setStrokeWidth(0);
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
        blacksTurn = !blacksTurn;
        updatePlayer(blacksTurn);
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
        history.add(t, 2, indexHistory);
        history.add((new Text("   " + (indexHistory + 1))), 0, indexHistory);
        indexHistory += 1;
    }

    private void updatePlayer(boolean black) {
        if (black) {
            player.setText("schwarz");
        } else {
            player.setText("weiß");
        }
    }


    @Override
    public void updateBeatenFigures(int posX, int posY) {
        ImageView iv = (ImageView) getImageByRowColumnIndex(posX + 1, 8 - posY);
        iv.setFitHeight(55.0);
        iv.setFitWidth(25.0);
        beatenFigures.getChildren().add(iv);

        if (blacksTurn) {
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

    private boolean imageIsBlack(ImageView iv) {
        return iv.getImage().getUrl().equals(ImageHandler.getInstance().getImage("RookBlack").getUrl()) ||
                iv.getImage().getUrl().equals(ImageHandler.getInstance().getImage("KnightBlack").getUrl()) ||
                iv.getImage().getUrl().equals(ImageHandler.getInstance().getImage("BishopBlack").getUrl()) ||
                iv.getImage().getUrl().equals(ImageHandler.getInstance().getImage("KingBlack").getUrl()) ||
                iv.getImage().getUrl().equals(ImageHandler.getInstance().getImage("QueenBlack").getUrl()) ||
                iv.getImage().getUrl().equals(ImageHandler.getInstance().getImage("PawnBlack").getUrl());
    }


    /*public void setGameMode(int mode){
        this.gameMode = mode;
    }*/
}