package chess.gui;

import chess.controller.*;
import chess.figures.Figure;
import chess.ki.Computer;
import chess.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.effect.Effect;
import javafx.scene.effect.Reflection;
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
import javafx.stage.Modality;
import javafx.stage.Popup;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.*;

import static javafx.scene.paint.Color.*;

public class SampleController {

    private static CoreGame coreGame;
    private Computer computer;
    private Rectangle startField;
    private boolean blacksTurn = false;
    private int indexBeatenFiguresBlack = 1;
    private int indexBeatenFiguresWhite = 1;
    private int indexHistory = 0;
    private int gameMode = 0;
    private List<Figure> beatenFigureList;
    private boolean playerColorBlack = false;
    private boolean firstTurn = true;
    private boolean blackDown = false;

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
    @FXML
    private ChoiceBox conversion;
    @FXML
    private VBox vbox;
    @FXML
    private Label checkLabel;


    public void init(int gameMode, boolean playerColorBlack) {
        this.playerColorBlack = playerColorBlack;
        this.gameMode = gameMode;

        coreGame = new CoreGame();
        computer = new Computer(!playerColorBlack);
        beatenFigureList = new ArrayList<>();

        conversion.getItems().addAll("Dame", "Läufer", "Turm", "Springer");
        conversion.getSelectionModel().select("Dame");

        if (gameMode == 2) {
            turnBoard.setDisable(true);
            if (playerColorBlack) {
                Move computerMove = computer.makeMove(coreGame.getCurrentBoard());
                coreGame.chessMove(computerMove);
                updateScene(computerMove);
            }
        }
    }

    //--------------------------------------Field----------------------------------------------------------------------------------------------
    public void handleFieldClick(MouseEvent mouseEvent) {
        if (mouseEvent.getTarget() instanceof Rectangle) {

            Rectangle clickedField = (Rectangle) mouseEvent.getTarget();

            // erstes Feld angeklickt: markiere Feld
            if (startField == null) {
                startField = clickedField;
                ImageView selectedFigure = (ImageView) getImageViewByIndex(GridPane.getColumnIndex(startField), GridPane.getRowIndex(startField));
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
                ImageView selectedFigure = (ImageView) getImageViewByIndex(GridPane.getColumnIndex(startField), GridPane.getRowIndex(startField));
                // auf dem ersten Feld stand eine eigene Figur
                if (selectedFigure != null && imageIsBlack(selectedFigure) == blacksTurn) {

                    // demarkiere mögliche Felder (muss vor dem Ausführen des Zuges aufgerufen werden)
                    for (Rectangle field : getPossibleFields(startField)) {
                        unmarkField(field);
                    }
                    // führe Zug aus (wenn möglich) und update Scene
                    Position startPosition = new Position(GridPane.getColumnIndex(startField) - 1, 8 - GridPane.getRowIndex(startField));
                    Position targetPosition = new Position(GridPane.getColumnIndex(clickedField) - 1, 8 - GridPane.getRowIndex(clickedField));
                    Move move = new Move(startPosition, targetPosition, getConversionFigure());
                    if (coreGame.chessMove(move)) {
                        updateScene(move);
                        // wenn Spiel gegen den Computer
                        if (gameMode == 2) {
                            Move computerMove = computer.makeMove(coreGame.getCurrentBoard());
                            coreGame.chessMove(computerMove);
                            updateScene(computerMove);
                        }
                    }
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
    public void updateScene(Move move) {
        drawBoard();
        updateHistory(move);
        updateBeatenFigures(coreGame.getCurrentBoard().getBeatenFigures());
        updateNotifications();


        if (turnBoard.isSelected()) {
            turnBoard();
            if(firstTurn){
                firstTurn = false;
            }
        }else{
            if(!firstTurn){
                firstTurn = true;
                blackDown = blacksTurn;
            }
            if(blackDown){
                turnFigures(180);
            }
        }

        blacksTurn = !blacksTurn;
        updatePlayer(blacksTurn);
    }

    private void updateNotifications() {
        checkLabel.setVisible(false);
        if (coreGame.getCurrentBoard().getCheckFlag(true)) {
            checkLabel.setVisible(true);
            checkLabel.setText("Black is in check");
        }
        if (coreGame.getCurrentBoard().getCheckFlag(false)) {
            checkLabel.setVisible(true);
            checkLabel.setText("White is in check");
        }
        if (coreGame.getCurrentBoard().getCheckMateFlag(true)) {
            checkLabel.setVisible(true);
            checkLabel.setText("Player black is checkmate!");
        }
        if (coreGame.getCurrentBoard().getCheckMateFlag(false)) {
            checkLabel.setVisible(true);
            checkLabel.setText("Player white is checkmate!");
        }
        if (coreGame.getCurrentBoard().getStaleMateFlag()) {
            checkLabel.setVisible(true);
            checkLabel.setText("Game ends because stalemate!");
        }
    }

    private void drawBoard() {
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                gridPane.getChildren().remove(getImageViewByIndex(x + 1, 8 - y));
                if (getImageBySymbol(coreGame.getCurrentBoard().getFigure(x, y).getSymbol()) != null) {
                    ImageView iv = new ImageView(getImageBySymbol(coreGame.getCurrentBoard().getFigure(x, y).getSymbol()));
                    iv.preserveRatioProperty().setValue(true);
                    iv.setFitHeight(50.0);
                    iv.setMouseTransparent(true);
                    iv.setEffect(new Reflection(0.0, 0.12, 0.24, 0.0));
                    gridPane.add(iv, x + 1, 8 - y);
                }
            }
        }
    }



    private void turnBoard() {
        int angle;
        if(blacksTurn){
            angle = 0;
        }else{
            angle = 180;
        }
        if(gridPane.getRotate() != angle){
            swapLabeling();
        }
        gridPane.setRotate(angle);
        columnsTop.setRotate(angle);
        columnsBottom.setRotate(angle);
        rowsLeft.setRotate(angle);
        rowsRight.setRotate(angle);
        turnFigures(angle);

    }

    private  void turnFigures(int angle){
        ObservableList<Node> children = gridPane.getChildren();
        for (Node node : children) {
            node.setRotate(angle);
        }
    }

    private void swapLabeling(){
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

    public void updateBeatenFigures(List<Figure> beatenFigures) {

        if (beatenFigures.size() != this.beatenFigureList.size() && beatenFigures.size() > 0) {
            ImageView iv = new ImageView(getImageBySymbol(beatenFigures.get(beatenFigures.size() - 1).getSymbol()));
            iv.preserveRatioProperty().setValue(true);
            iv.setFitHeight(50.0);
            iv.setRotate(0);
            if (!blacksTurn) {
                GridPane.setColumnIndex(iv, indexBeatenFiguresBlack);
                GridPane.setRowIndex(iv, 1);
                indexBeatenFiguresBlack += 1;
            } else {
                GridPane.setColumnIndex(iv, indexBeatenFiguresWhite);
                GridPane.setRowIndex(iv, 0);
                indexBeatenFiguresWhite += 1;
            }
            this.beatenFigures.getChildren().add(iv);
            this.beatenFigureList.add(beatenFigures.get(beatenFigures.size() - 1));
        }
    }

//--------------------------------Image----------------------------------------------------------------------------------------------

    private Node getImageViewByIndex(int column, int row) {
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

    private Image getImageBySymbol(char symbol) {
        switch (symbol) {
            // Rook
            case 'R':
                return ImageHandler.getInstance().getImage("RookWhite");
            case 'r':
                return ImageHandler.getInstance().getImage("RookBlack");
            // Knight
            case 'N':
                return ImageHandler.getInstance().getImage("KnightWhite");
            case 'n':
                return ImageHandler.getInstance().getImage("KnightBlack");
            // Bishop
            case 'B':
                return ImageHandler.getInstance().getImage("BishopWhite");
            case 'b':
                return ImageHandler.getInstance().getImage("BishopBlack");
            // Queen
            case 'Q':
                return ImageHandler.getInstance().getImage("QueenWhite");
            case 'q':
                return ImageHandler.getInstance().getImage("QueenBlack");
            // King
            case 'K':
                return ImageHandler.getInstance().getImage("KingWhite");
            case 'k':
                return ImageHandler.getInstance().getImage("KingBlack");
            // Pawn
            case 'P':
                return ImageHandler.getInstance().getImage("PawnWhite");
            case 'p':
                return ImageHandler.getInstance().getImage("PawnBlack");
            // None
            default:
                return null;
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

    //--------------getter/setter---------------------------------------------------------------------------------------------------------------
    private int getConversionFigure() {
        String item = (String) conversion.getSelectionModel().getSelectedItem();
        if (item.equals("Dame")) {
            return 5;
        }
        if (item.equals("Läufer")) {
            return 4;
        }
        if (item.equals("Turm")) {
            return 2;
        }
        if (item.equals("Springer")) {
            return 3;
        }
        return 5;
    }

    public void backToMenu(MouseEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Menu.fxml"));
            Parent root = fxmlLoader.load();

            Stage stage = new Stage();
            stage.setTitle("Menu");
            stage.setScene(new Scene(root));
            stage.show();

            // Hide this current window
            ((Node) (event.getSource())).getScene().getWindow().hide();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    /*@Override
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
        iv.setImage(getImageByInt(changeTo, isBlackTeam));
    }*/

    /*private Image getImageByInt(int symbol, boolean isBlackTeam) {
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
    }*/
}