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
    private CheckBox rotateBoard;
    @FXML
    private CheckBox possibleFieldsButton;
    @FXML
    private CheckBox singleSelect;
    @FXML
    private ChoiceBox conversion;
    @FXML
    private Label checkLabel;
    @FXML
    private Label calculating;

    /**
     * initializes gameMode,coreGame,computer,beatenFigureList and conversion
     * @param gameMode against a local friend (0) or a networkegame (1) or against the computer (2)
     * @param playerColorBlack the colour you want to play
     */
    public void init(int gameMode, boolean playerColorBlack) {
        this.gameMode = gameMode;

        coreGame = new CoreGame();
        computer = new Computer(!playerColorBlack);
        beatenFigureList = new ArrayList<>();

        conversion.getItems().addAll("Queen", "Bishop", "Rook", "Knight");
        conversion.getSelectionModel().select("Queen");

        if (gameMode == 2) {
            rotateBoard.setDisable(true);
            if (playerColorBlack) {
                computerMove();
            }
        }
    }

    /**
     * loads the menu
     * @param event the window you want to hide
     */
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


    //--------------------------------------Field----------------------------------------------------------------------------------------------

    /**
     * performs a move if first the start position is clicked and second the target position and if the move is allowed
     * @param mouseEvent the clicked field
     */
    public void handleFieldClick(MouseEvent mouseEvent) {
        if (mouseEvent.getTarget() instanceof Rectangle) {
            Rectangle clickedField = (Rectangle) mouseEvent.getTarget();
            if (startField == null && getFigure(clickedField)!= null && imageIsBlack(getFigure(clickedField)) == blacksTurn) {
                startField = clickedField;
                mark(startField);
            }
            else if(startField != null && getFigure(startField) != null ) {
                performMove(getMove(startField, clickedField));
            }
        }
    }

    /**
     * performs the move if possible and updates scene
     * @param move the move which should be performed
     */
    private void performMove(Move move) {
        unmark(startField);
        if(coreGame.chessMove(move)){
            updateScene(move);
            startField = null;
            if (gameMode == 2) {
                computerMove();
            }
        }else if(singleSelect.isSelected() && !getPossibleFields(startField).isEmpty()){
            mark(startField);
        }else{
            startField = null;
        }
    }

    /**
     * waits until the computer makes a move
     */
    private void computerMove(){
        computer.makeMove(coreGame.getCurrentBoard());
        while(computer.getMove() == null){
            calculating.setVisible(true);
            gridPane.setDisable(true);
        }
        calculating.setVisible(false);
        gridPane.setDisable(false);
        Move computerMove = computer.getMove();
        coreGame.chessMove(computerMove);
        updateScene(computerMove);
    }

    /**
     * Returns the move from the startField to the targetField
     * @return move from the startField to the targetField
     */
    private Move getMove(Rectangle startField, Rectangle targetField){
        Position startPosition = new Position(GridPane.getColumnIndex(startField) - 1, 8 - GridPane.getRowIndex(startField));
        Position targetPosition = new Position(GridPane.getColumnIndex(targetField) - 1, 8 - GridPane.getRowIndex(targetField));
        return new Move(startPosition, targetPosition, getConversionFigure());
    }

    /**
     * returns the field with the rowIndex row and the columnIndex column
     * @param row rowIndex of the field you want to get
     * @param column columnIndex of the field you want to get
     * @return the field with the rowIndex row and the columnIndex column
     */
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

    /**
     * Marks the field and if selected the possible moves of the figure on the field
     * @param field the field you want to mark
     */
    private void mark(Rectangle field){
        markField(field, CORNFLOWERBLUE);
        if (possibleFieldsButton.isSelected()) {
            for (Rectangle f : getPossibleFields(field)) {
                markField(f, CYAN);
            }
        }
    }

    /**
     * unmarks field and the possible target fields of the Figure
     * @param field of the Figure
     */
    private void unmark(Rectangle field){
        field.setStrokeWidth(0);
        for (Rectangle f: getPossibleFields(field)) {
            f.setStrokeWidth(0);
        }
    }

    /**
     * marks a field
     * @param field the field you want to mark
     * @param color the color in which you want to mark the field
     */
    private void markField(Rectangle field, Color color) {
        field.setStroke(color);
        field.setStrokeWidth(4);
        field.setStrokeType(StrokeType.INSIDE);
    }




    //----------------------------------Update----------------------------------------------------------------------------------------------

    /**
     * updates the scene
     * @param move ,the move you want to make
     */
    public void updateScene(Move move) {
        drawBoard();
        updateHistory(move);
        updateBeatenFigures(coreGame.getCurrentBoard().getBeatenFigures());
        updateNotifications();


        if (rotateBoard.isSelected()) {
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

    /**
     * Sets a label if a player is in check or checkmate or stalemate
     */
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

    /**
     * draws the chessboard
     */
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


    /**
     * turns the chessboard that the figures of the actualPlayer are always on the bottom
     */
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

    /**
     * rotates the figures with the angle angle
     * @param angle the angle around which the figures are rotated
     */
    private  void turnFigures(int angle){
        ObservableList<Node> children = gridPane.getChildren();
        for (Node node : children) {
            node.setRotate(angle);
        }
    }

    /**
     * swaps the labeling of the rows and columns
     */
    private void swapLabeling(){
        swap(rowsRight.getChildren());
        swap(rowsLeft.getChildren());
        swap(columnsBottom.getChildren());
        swap(columnsTop.getChildren());
    }

    /**
     * swaps every node of the given list
     * @param children you want to swap
     */
    private void swap(ObservableList<Node> children) {
        ObservableList<Node> workingCollection = FXCollections.observableArrayList(children);
        Collections.swap(workingCollection, 0, 7);
        Collections.swap(workingCollection, 1, 6);
        Collections.swap(workingCollection, 2, 5);
        Collections.swap(workingCollection, 3, 4);
        children.setAll(workingCollection);
    }

    /**
     * updates the history
     * @param move the move you want to add to the history
     */
    public void updateHistory(Move move) {
        Text t = new Text(move.toString());
        history.add(t, 2, indexHistory);
        history.add((new Text("   " + (indexHistory + 1))), 0, indexHistory);
        indexHistory += 1;
    }

    /**
     * updates the label with the actual player
     * @param black should be true if the actual player is black
     */
    private void updatePlayer(boolean black) {
        if (black) {
            player.setText("black");
        } else {
            player.setText("white");
        }
    }

    /**
     * updates the beaten figures
     * @param beatenFigures a list with all beaten figures
     */
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

    /**
     * returns the ImageView with the columnIndex column and the rowIndex row in the gridPane
     * @param column of the ImageView you want to get
     * @param row of the ImageView you want to get
     * @return the ImageView with the columnIndex column and the rowIndex row in the gridPane
     */
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

    /**
     * returns the Image of a figure
     * @param symbol of the figure you want the image from
     * @return the image of the figure
     */
    private Image getImageBySymbol(char symbol) {
        String color = "Black";
        if(Character.isUpperCase(symbol)){
            color = "White";
        }

        switch (Character.toUpperCase(symbol)) {
            // Rook
            case 'R' :
                return ImageHandler.getInstance().getImage("Rook" + color);
            // Knight
            case 'N':
                return ImageHandler.getInstance().getImage("Knight" + color);
            // Bishop
            case 'B':
                return ImageHandler.getInstance().getImage("Bishop" + color);
            // Queen
            case 'Q':
                return ImageHandler.getInstance().getImage("Queen" + color);
            // King
            case 'K':
                return ImageHandler.getInstance().getImage("King" + color);
            // Pawn
            case 'P':
                return ImageHandler.getInstance().getImage("Pawn" + color);
            // None
            default:
                return null;
        }
    }

    /**
     * returns if the image iv shows a black figure
     * @param iv the image of the figure you want to know th color
     * @return if the image iv shows a black figure
     */
    private boolean imageIsBlack(ImageView iv) {
        return iv.getImage().getUrl().equals(ImageHandler.getInstance().getImage("RookBlack").getUrl()) ||
                iv.getImage().getUrl().equals(ImageHandler.getInstance().getImage("KnightBlack").getUrl()) ||
                iv.getImage().getUrl().equals(ImageHandler.getInstance().getImage("BishopBlack").getUrl()) ||
                iv.getImage().getUrl().equals(ImageHandler.getInstance().getImage("KingBlack").getUrl()) ||
                iv.getImage().getUrl().equals(ImageHandler.getInstance().getImage("QueenBlack").getUrl()) ||
                iv.getImage().getUrl().equals(ImageHandler.getInstance().getImage("PawnBlack").getUrl());
    }

    //--------------getter/setter---------------------------------------------------------------------------------------------------------------

    /**
     * returns the ID-number of the conversionFigure
     * @return the ID-number of the conversionFigure
     */
    private int getConversionFigure() {
        String item = (String) conversion.getSelectionModel().getSelectedItem();
        if (item.equals("Queen")) {
            return 5;
        }
        if (item.equals("Bishop")) {
            return 4;
        }
        if (item.equals("Rook")) {
            return 2;
        }
        if (item.equals("Knight")) {
            return 3;
        }
        return 5;
    }

    /**
     * returns the figure of the field
     * @param field of the figure you want to get
     * @return the figure of the field
     */
    private ImageView getFigure(Rectangle field){
        return (ImageView) getImageViewByIndex(GridPane.getColumnIndex(field), GridPane.getRowIndex(field));
    }

    /**
     * returns a array list with all possible fields the figure of the actualField can move
     * @param actualField the field of the figure you want to know the possible moves
     * @return a array list with all possible fields the figure of the actualField can move
     */
    private List<Rectangle> getPossibleFields(Rectangle actualField) {

        Position actualPosition = new Position(GridPane.getColumnIndex(actualField) - 1, 8 - GridPane.getRowIndex(actualField));
        Board board = coreGame.getCurrentBoard();

        List<Position> positions = Rules.possibleTargetFields(actualPosition, board);
        List<Rectangle> fields = new ArrayList<>();

        for (Position position : positions) {
            fields.add((Rectangle) getFieldByRowColumnIndex(8 - position.getPosY(), position.getPosX() + 1));
        }

        return fields;
    }
}