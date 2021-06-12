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
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.*;

import static javafx.scene.paint.Color.*;

/**
 * This class connects the internal game logic to the graphical user interface.
 * Every time the user interacts with the gui, this class will react (e.g. it will start a new chess game or perform a move).
 *
 * @author Lydia Engelhardt, Sophia Kuhlmann, Vincent Schiller, Friederike Weilbeer
 * 2021-06-09
 */
public class Controller {
    private boolean firstTurn = true;
    private boolean blacksTurn = false;
    private boolean blackDown = false;
    private List<Figure> beatenFigureList;
    private Logic logic;
    final private static String queen = "Queen";

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

    /**
     * initiates the controller and the logic
     *
     * @param gameMode         against a local friend (0) or a network game (1) or against the computer (2)
     * @param playerColorBlack the color you want to play
     */
    public void init(int gameMode, boolean playerColorBlack) {
        beatenFigureList = new ArrayList<>();
        getChoiceBoxConversion().getItems().addAll(queen, "Bishop", "Rook", "Knight");
        getChoiceBoxConversion().getSelectionModel().select(queen);

        logic = new Logic(gameMode, playerColorBlack, this);
    }

    /**
     * closes the actual window and opens the menu-window
     *
     * @param event the mouse event
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
     * performs a move if the target position is clicked after the start position was clicked and if the move is allowed
     *
     * @param mouseEvent the clicked field
     */
    public void isFieldClicked(MouseEvent mouseEvent) {
        if (mouseEvent.getTarget() instanceof Rectangle) {
            Rectangle clickedField = (Rectangle) mouseEvent.getTarget();
            logic.handleFieldClick(clickedField, blacksTurn);
        }
    }

    //----------------------------------Update----------------------------------------------------------------------------------------------

    /**
     * updates the scene (the board, the history, the beaten-figure-list, possible notifications, possible board turns)
     *
     * @param move     the move the player wants to make
     * @param coreGame the current coreGame
     */
    public void updateScene(Move move, CoreGame coreGame) {
        drawBoard(coreGame.getCurrentBoard());
        updateHistory(move);
        setBeatenFigures(coreGame.getCurrentBoard().getBeatenFigures());
        updateNotifications(coreGame.getCurrentBoard());

        if (getRotateBoard().isSelected()) {
            turnBoard();
            if (firstTurn) {
                firstTurn = false;
            }
        } else {
            if (!firstTurn) {
                firstTurn = true;
                blackDown = blacksTurn;
            }
            if (blackDown) {
                turnFigures(180);
            }
        }

        blacksTurn = !blacksTurn;
        setPlayerLabel(blacksTurn);
    }

    /**
     * Sets a label in the gui if a player is in check, checkmate or stalemate
     *
     * @param board the current chessboard
     */
    private void updateNotifications(Board board) {
        getLabelCheck().setVisible(false);
        if (board.isCheckFlag(true)) {
            getLabelCheck().setVisible(true);
            getLabelCheck().setText("Black is in check");
        }
        if (board.isCheckFlag(false)) {
            getLabelCheck().setVisible(true);
            getLabelCheck().setText("White is in check");
        }
        if (board.isCheckMateFlag(true)) {
            getLabelCheck().setVisible(true);
            getLabelCheck().setText("Player black is checkmate!");
        }
        if (board.isCheckMateFlag(false)) {
            getLabelCheck().setVisible(true);
            getLabelCheck().setText("Player white is checkmate!");
        }
        if (board.isStaleMateFlag()) {
            getLabelCheck().setVisible(true);
            getLabelCheck().setText("Game ends because stalemate!");
        }
    }

    /**
     * updates the history in the gui
     *
     * @param move the move that should be added to the history
     */
    public void updateHistory(Move move) {
        Text t = new Text(move.toString());

        history.add(t, 2, history.getRowCount());
        history.add(new Text("   " + history.getRowCount()), 0, history.getRowCount() - 1);
    }

    /**
     * draws the chess board
     *
     * @param board the current chessboard
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
     * turns the chessboard so that the figures of the actualPlayer are always on the bottom
     */
    private void turnBoard() {
        int angle;
        if (blacksTurn) {
            angle = 0;
        } else {
            angle = 180;
        }
        gridPane.setRotate(angle);
        turnFigures(angle);
    }

    /**
     * rotates the figures themselves
     *
     * @param angle the angle around which the figures are rotated
     */
    private void turnFigures(int angle) {
        for (Node node : gridPane.getChildren()) {
            node.setRotate(angle);
        }
    }

    /**
     * updates the label that shows which player's turn it is.
     *
     * @param black true if the actual player is black
     */
    private void setPlayerLabel(boolean black) {
        if (black) {
            getRectangleBlack().setStroke(valueOf("#00A8C6"));
            getRectangleBlack().setStrokeWidth(3);
            getRectangleWhite().setStroke(BLACK);
            getRectangleWhite().setStrokeWidth(1);
        } else {
            getRectangleWhite().setStroke(valueOf("#00A8C6"));
            getRectangleWhite().setStrokeWidth(3);
            getRectangleBlack().setStroke(BLACK);
            getRectangleBlack().setStrokeWidth(1);
        }
    }

    /**
     * updates the beaten figure list in the gui
     *
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
     * returns the ImageView that is at the given position in the gridPane
     *
     * @param column of the ImageView you want to get
     * @param row    of the ImageView you want to get
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
     * returns the Image of a figure by its char-value
     *
     * @param symbol of the figure you want the image from
     * @return the image of the figure
     */
    private Image getImageBySymbol(char symbol) {
        String color = "Black";
        if (Character.isUpperCase(symbol)) {
            color = "White";
        }
        switch (Character.toUpperCase(symbol)) {
            // Rook
            case 'R':
                return ImageHandler.getInstance().getImage("Rook" + color);
            // Knight
            case 'N':
                return ImageHandler.getInstance().getImage("Knight" + color);
            // Bishop
            case 'B':
                return ImageHandler.getInstance().getImage("Bishop" + color);
            // Queen
            case 'Q':
                return ImageHandler.getInstance().getImage(queen + color);
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
     * whether the imageView contains an image of a black figure
     *
     * @param iv the imageView of the figure you want to know the color of
     * @return whether the imageView contains an image of a black figure
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
     *
     * @return the ID-number of the conversionFigure
     */
    protected int getConversionFigure() {
        String item = (String) getChoiceBoxConversion().getSelectionModel().getSelectedItem();
        if (item.equals(queen)) {
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
     * returns the figure of the field on the gridPane
     *
     * @param field of the figure you want to get
     * @return the figure of the field
     */
    protected ImageView getFigure(Rectangle field) {
        return (ImageView) getImageViewByIndex(GridPane.getColumnIndex(field), GridPane.getRowIndex(field));
    }

    /**
     * returns an array list with all possible fields the figure of the actualField can move to
     *
     * @param actualField the field of the figure you want to know the possible moves of
     * @param board       the current chessboard
     * @return an array list with all possible fields the figure of the actualField can move to
     */
    protected List<Rectangle> getPossibleFields(Rectangle actualField, Board board) {

        Position actualPosition = new Position(GridPane.getColumnIndex(actualField) - 1, 8 - GridPane.getRowIndex(actualField));

        List<Position> positions = Rules.possibleTargetFields(actualPosition, board);
        List<Rectangle> fields = new ArrayList<>();

        for (Position position : positions) {
            fields.add((Rectangle) getField(8 - position.getPosY(), position.getPosX() + 1));
        }

        return fields;
    }

    /**
     * returns the field that is on the given position on the gridPane
     *
     * @param row    rowIndex of the field you want to get
     * @param column columnIndex of the field you want to get
     * @return the field with the rowIndex row and the columnIndex column
     */
    private Node getField(int row, int column) {
        for (Node node : gridPane.getChildren()) {
            if (GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == column) {
                return node;
            }
        }
        return null;
    }

    /**
     * Marks or unmarks the field and if selected the possible moves of the figure on the field
     *
     * @param field the field you want to (un-)mark
     * @param mark  whether the field should be marked or unmarked
     * @param board the current chessboard
     */
    protected void setMark(Rectangle field, boolean mark, Board board) {
        if (mark) {
            field.setStroke(valueOf("#00A8C6"));
            field.setStrokeWidth(5);
            field.setStrokeType(StrokeType.INSIDE);
            if (((CheckBox) settings.getChildren().get(1)).isSelected()) {
                for (Rectangle f : getPossibleFields(field, board)) {
                    f.setStroke(valueOf("#8fbe00"));
                    f.setStrokeWidth(5);
                    f.setStrokeType(StrokeType.INSIDE);
                }
            }
        } else {
            field.setStrokeWidth(0);
            for (Rectangle f : getPossibleFields(field, board)) {
                f.setStrokeWidth(0);
            }
        }
    }

    /**
     * returns whether the player has enabled / disabled the single select option
     *
     * @return whether the single select option is enabled / disabled
     */
    protected boolean isSingleSelect() {
        return ((CheckBox) settings.getChildren().get(0)).isSelected();
    }

    /**
     * shows in the gui whether the computer is calculating or not
     *
     * @param isCalculating whether the computer (ki) is currently calculating
     */
    protected void setCalculating(boolean isCalculating) {
        if (isCalculating) {
            getLabelCalculating().setVisible(true);
            gridPane.setMouseTransparent(true);
        } else {
            getLabelCalculating().setVisible(false);
            gridPane.setMouseTransparent(false);
        }
    }

    /**
     * returns the gui-element with which the player can enable / disable the board turning
     *
     * @return the gui-element with which the player can enable / disable the board turning
     */
    protected CheckBox getRotateBoard() {
        return (CheckBox) settings.getChildren().get(2);
    }

    /**
     * returns the gui-element in which the player can select the PawnConversion Figure
     *
     * @return the gui-element (choiceBox) in which the player can select the PawnConversion Figure
     */
    private ChoiceBox getChoiceBoxConversion() {
        return (ChoiceBox) menu.getChildren().get(4);
    }

    /**
     * returns the gui-element that shows whether a player is in check or not
     *
     * @return the gui-element (label) that shows whether a player is in check or not
     */
    private Label getLabelCheck() {
        return (Label) menu.getChildren().get(2);
    }

    /**
     * returns the gui-element that shows whether the computer is calculating or not
     *
     * @return the gui-element (label) that shows whether the computer is calculating or not
     */
    private Label getLabelCalculating() {
        return (Label) menu.getChildren().get(1);
    }

    /**
     * returns the gui-element that shows whether it is whites turn
     *
     * @return the gui-element (rectangle) that shows whether it is whites turn
     */
    private Rectangle getRectangleWhite() {
        return (Rectangle) menu.getChildren().get(7);
    }

    /**
     * returns the gui-element that shows whether it is blacks turn
     *
     * @return the gui-element (rectangle) that shows whether it is blacks turn
     */
    private Rectangle getRectangleBlack() {
        return (Rectangle) menu.getChildren().get(8);
    }
}