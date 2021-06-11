package chess.gui;

import chess.controller.*;
import chess.figures.Figure;
import chess.model.*;
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
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
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
    private List<Figure> beatenFigureList;
    private Logic logic;

    @FXML
    private GridPane gridPane;
    @FXML
    private GridPane beatenFigures;
    @FXML
    private GridPane history;
    @FXML
    private Pane menu;
    @FXML
    private VBox settings;



    public void init(int gameMode, boolean playerColorBlack){
        beatenFigureList = new ArrayList<>();
        getChoiceBoxConversion().getItems().addAll("Queen", "Bishop", "Rook", "Knight");
        getChoiceBoxConversion().getSelectionModel().select("Queen");

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

        if (getRotateBoard().isSelected()) {
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
        getLabelCheck().setVisible(false);
        if (board.getCheckFlag(true)) {
            getLabelCheck().setVisible(true);
            getLabelCheck().setText("Black is in check");
        }
        if (board.getCheckFlag(false)) {
            getLabelCheck().setVisible(true);
            getLabelCheck().setText("White is in check");
        }
        if (board.getCheckMateFlag(true)) {
            getLabelCheck().setVisible(true);
            getLabelCheck().setText("Player black is checkmate!");
        }
        if (board.getCheckMateFlag(false)) {
            getLabelCheck().setVisible(true);
            getLabelCheck().setText("Player white is checkmate!");
        }
        if (board.getStaleMateFlag()) {
            getLabelCheck().setVisible(true);
            getLabelCheck().setText("Game ends because stalemate!");
        }
    }

    /**
     * updates the history
     * @param move the move you want to add to the history
     */
    public void updateHistory(Move move) {
        Text t = new Text(move.toString());

        history.add(t, 2, history.getRowCount());
        history.add(new Text("   " + history.getRowCount()), 0, history.getRowCount()-1);
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
        gridPane.setRotate(angle);
        turnFigures(angle);

    }

    /**
     * rotates the figures with the angle angle
     * @param angle the angle around which the figures are rotated
     */
    private  void turnFigures(int angle){
        for (Node node : gridPane.getChildren()) {
            node.setRotate(angle);
        }
    }

    /**
     * updates the label with the actual player
     * @param black should be true if the actual player is black
     */
    private void setPlayerLabel(boolean black) {
        if (black) {
//            getLabelPlayer().setText("black");
            getRectangleBlack().setStroke(Color.valueOf("#00A8C6"));
            getRectangleBlack().setStrokeWidth(3);
            getRectangleWhite().setStroke(BLACK);
            getRectangleWhite().setStrokeWidth(1);
        } else {
//            getLabelPlayer().setText("white");
            getRectangleWhite().setStroke(Color.valueOf("#00A8C6"));
            getRectangleWhite().setStrokeWidth(3);
            getRectangleBlack().setStroke(BLACK);
            getRectangleBlack().setStrokeWidth(1);
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
                int indexBeatenFiguresBlack = 0;
                for (Figure figure : beatenFigures) {
                    if (figure.isBlackTeam()) {
                        indexBeatenFiguresBlack += 1;
                    }
                }
                GridPane.setColumnIndex(iv, indexBeatenFiguresBlack);
                GridPane.setRowIndex(iv, 1);
            } else {
                int indexBeatenFiguresWhite = 0;
                for (Figure figure : beatenFigures) {
                    if (!figure.isBlackTeam()) {
                        indexBeatenFiguresWhite += 1;
                    }
                }
                GridPane.setColumnIndex(iv, indexBeatenFiguresWhite);
                GridPane.setRowIndex(iv, 0);
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
        String item = (String) getChoiceBoxConversion().getSelectionModel().getSelectedItem();
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
            fields.add((Rectangle) getField(8 - position.getPosY(), position.getPosX() + 1));
        }

        return fields;
    }

    /**
     * returns the field with the rowIndex row and the columnIndex column
     * @param row rowIndex of the field you want to get
     * @param column columnIndex of the field you want to get
     * @return the field with the rowIndex row and the columnIndex column
     */
    private Node getField(int row, int column) {
        for (Node node : gridPane.getChildren()) {
            if(GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == column) {
                return node;
            }
        }
        return null;
    }

    /**
     * Marks or unmarks the field and if selected the possible moves of the figure on the field
     * @param field the field you want to mark
     * @param mark if the field should be marked or unmarked
     */
    protected void setMark(Rectangle field,boolean mark,Board board){
        if(mark){
            field.setStroke(Color.valueOf("#00A8C6"));
            field.setStrokeWidth(5);
            field.setStrokeType(StrokeType.INSIDE);
            if (((CheckBox) settings.getChildren().get(1)).isSelected()) {
                for (Rectangle f : getPossibleFields(field,board)) {
                    f.setStroke(Color.valueOf("#8fbe00"));
                    f.setStrokeWidth(5);
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
        return (CheckBox) settings.getChildren().get(2);
    }

    protected boolean isSingleSelect(){
        return ((CheckBox) settings.getChildren().get(0)).isSelected();
    }

    protected void setCalculating(boolean isCalculating){
        if(isCalculating){
            getLabelCalculating().setVisible(true);
            gridPane.setMouseTransparent(true);
        }else{
            getLabelCalculating().setVisible(false);
            gridPane.setMouseTransparent(false);
        }

    }

    private ChoiceBox getChoiceBoxConversion(){
        return (ChoiceBox) menu.getChildren().get(4);
    }

    private Label getLabelCheck(){
        return (Label) menu.getChildren().get(2);
    }

    private Label getLabelCalculating(){
        return (Label) menu.getChildren().get(1);
    }

    /*private Label getLabelPlayer(){
        return (Label) menu.getChildren().get(1);
    }*/

    private Rectangle getRectangleWhite(){
        return (Rectangle) menu.getChildren().get(7);
    }

    private Rectangle getRectangleBlack(){
        return (Rectangle) menu.getChildren().get(8);
    }




}