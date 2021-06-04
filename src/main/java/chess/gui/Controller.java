package chess.gui;

import chess.controller.*;
import chess.figures.Figure;
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
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.*;

import static javafx.scene.paint.Color.*;

public class Controller {
    private boolean firstTurn = true;
    private boolean blacksTurn = false;
    private boolean blackDown = false;
    private int indexBeatenFiguresBlack = 1;
    private int indexBeatenFiguresWhite = 1;
    private int indexHistory = 0;
    private List<Figure> beatenFigureList;
    private Logic logic;

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

    public void init(int gameMode, boolean playerColorBlack){
        beatenFigureList = new ArrayList<>();
        conversion.getItems().addAll("Queen", "Bishop", "Rook", "Knight");
        conversion.getSelectionModel().select("Queen");

        logic = new Logic(gameMode, playerColorBlack,this);
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

    //--------------------------------------Move----------------------------------------------------------------------------------------------

    /**
     * performs a move if first the start position is clicked and second the target position and if the move is allowed
     * @param mouseEvent the clicked field
     */
    public void isFieldClicked(MouseEvent mouseEvent) {
        if (mouseEvent.getTarget() instanceof Rectangle) {
            Rectangle clickedField = (Rectangle) mouseEvent.getTarget();
            logic.handleFieldClick(clickedField,blacksTurn);
        }
    }

    //----------------------------------Update----------------------------------------------------------------------------------------------

    /**
     * updates the scene
     * @param move ,the move you want to make
     */
    public void updateScene(Move move,CoreGame coreGame) {
        drawBoard(coreGame.getCurrentBoard());
        updateHistory(move);
        setBeatenFigures(coreGame.getCurrentBoard().getBeatenFigures());
        updateNotifications(coreGame.getCurrentBoard());


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
        setPlayerLabel(blacksTurn);
    }

    /**
     * Sets a label if a player is in check or checkmate or stalemate
     */
    private void updateNotifications(Board board) {
        checkLabel.setVisible(false);
        if (board.getCheckFlag(true)) {
            checkLabel.setVisible(true);
            checkLabel.setText("Black is in check");
        }
        if (board.getCheckFlag(false)) {
            checkLabel.setVisible(true);
            checkLabel.setText("White is in check");
        }
        if (board.getCheckMateFlag(true)) {
            checkLabel.setVisible(true);
            checkLabel.setText("Player black is checkmate!");
        }
        if (board.getCheckMateFlag(false)) {
            checkLabel.setVisible(true);
            checkLabel.setText("Player white is checkmate!");
        }
        if (board.getStaleMateFlag()) {
            checkLabel.setVisible(true);
            checkLabel.setText("Game ends because stalemate!");
        }
    }

    /**
     * updates the history
     * @param move the move you want to add to the history
     */
    public void updateHistory(Move move) {
        Text t = new Text(move.toString());
        history.add(t, 2, indexHistory);
        history.add((new Text("   " + indexHistory + 1)), 0, indexHistory);
        indexHistory += 1;
    }

    /**
     * draws the chessboard
     */
    private void drawBoard(Board board) {
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                gridPane.getChildren().remove(getImageViewByIndex(x + 1, 8 - y));
                if (getImageBySymbol(board.getFigure(x, y).getSymbol()) != null) {
                    ImageView iv = new ImageView(getImageBySymbol(board.getFigure(x, y).getSymbol()));
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
     * updates the label with the actual player
     * @param black should be true if the actual player is black
     */
    private void setPlayerLabel(boolean black) {
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
    public void setBeatenFigures(List<Figure> beatenFigures) {

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
    protected boolean isImageBlack(ImageView iv) {
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
    protected int getConversionFigure() {
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
    protected ImageView getFigure(Rectangle field){
        return (ImageView) getImageViewByIndex(GridPane.getColumnIndex(field), GridPane.getRowIndex(field));
    }

    /**
     * returns a array list with all possible fields the figure of the actualField can move
     * @param actualField the field of the figure you want to know the possible moves
     * @return a array list with all possible fields the figure of the actualField can move
     */
    protected List<Rectangle> getPossibleFields(Rectangle actualField,Board board) {

        Position actualPosition = new Position(GridPane.getColumnIndex(actualField) - 1, 8 - GridPane.getRowIndex(actualField));

        List<Position> positions = Rules.possibleTargetFields(actualPosition, board);
        List<Rectangle> fields = new ArrayList<>();

        for (Position position : positions) {
            fields.add((Rectangle) getFieldByRowColumnIndex(8 - position.getPosY(), position.getPosX() + 1));
        }

        return fields;
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
     * Marks or unmarks the field and if selected the possible moves of the figure on the field
     * @param field the field you want to mark
     * @param mark if the field should be marked or unmarked
     */
    protected void setMark(Rectangle field,boolean mark,Board board){
        if(mark){
            field.setStroke(CORNFLOWERBLUE);
            field.setStrokeWidth(4);
            field.setStrokeType(StrokeType.INSIDE);
            if (possibleFieldsButton.isSelected()) {
                for (Rectangle f : getPossibleFields(field,board)) {
                    f.setStroke(CYAN);
                    f.setStrokeWidth(6);
                    f.setStrokeType(StrokeType.INSIDE);
                }
            }
        }else{
            field.setStrokeWidth(0);
            for (Rectangle f: getPossibleFields(field,board)) {
                f.setStrokeWidth(0);
            }
        }
    }


    protected CheckBox getRotateBoard(){
        return rotateBoard;
    }

    protected boolean isSingleSelect(){
        return singleSelect.isSelected();
    }

    protected void setCalculating(boolean isCalculating){
        if(isCalculating){
            calculating.setVisible(true);
            gridPane.setMouseTransparent(true);
        }else{
            calculating.setVisible(false);
            gridPane.setMouseTransparent(false);
        }

    }



}