package chess.gui;

import chess.enums.GameMode;
import chess.figures.Figure;
import chess.managers.LanguageManager;
import chess.managers.WindowManager;
import chess.model.*;
import chess.network.NetworkPlayer;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.effect.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.*;

import static javafx.scene.paint.Color.*;

/**
 * This class connects the internal game logic to the graphical user interface.
 * Every time the user interacts with the gui, this class will react (e.g. it will start a new chess game or perform a move).
 *
 * @author Lydia Engelhardt, Sophia Kuhlmann, Vincent Schiller, Friederike Weilbeer
 * 2021-06-09
 */
@SuppressWarnings({"PMD.UnusedPrivateMethod"})
// the methods updateFlagButton(), updatePossibleMovesButton() and updateRotateButton() are used by the gui but PMD does not recognize this
public class Controller {

    private final Color disabledColor = valueOf("#8fbe00");
    private Logic logic;
    private UndoRedo undoRedo;

    @FXML
    public Pane menu;

    /**
     * initiates the controller and the logic
     *
     * @param gameMode      against a local friend (0) or a network game (1) or against the computer (2)
     * @param isBlack       the current player
     * @param networkPlayer the network player
     */
    public void init(GameMode gameMode, boolean isBlack, NetworkPlayer networkPlayer) {
        logic = new Logic(gameMode, isBlack, this, networkPlayer);
        undoRedo = new UndoRedo(logic.getCoreGame().getMoveHistory().size() - 1, this);

        // style fields (hover effekt)
        for (int i = 0; i < 96; i++) {
            int finalI = i;
            this.getBoard().getChildren().get(i).setOnMouseEntered((event) -> {
                MotionBlur b = new MotionBlur();
                b.setAngle(45.0);
                b.setRadius(7.0);
                InnerShadow s = new InnerShadow();
                s.setColor(valueOf("#9cd7ff"));
                s.setChoke(0.14);
                s.setHeight(150.0);
                s.setWidth(150.0);
                b.setInput(s);
                this.getBoard().getChildren().get(finalI).setEffect(b);
            });
            this.getBoard().getChildren().get(i).setOnMouseExited((event) -> {
                MotionBlur b = new MotionBlur();
                b.setAngle(45.0);
                b.setRadius(7.0);
                this.getBoard().getChildren().get(finalI).setEffect(b);
            });
        }
    }

    /**
     * closes the actual window and opens the menu-window
     */
    public void backToMenu() {
        WindowManager.initialWindow("MenuStage", "menu_title");
        WindowManager.showStage("MenuStage");

        // Close all other Stage
        WindowManager.closeStage("PromotionStage");
        WindowManager.closeStage("GameStage");
        logic.killNetworkPlayer();
    }

    //----------------------------------Undo/Redo----------------------------------------------------------------------------------------------

    /**
     * undo a move (button "undo")
     */
    @FXML
    public void undo() {
        undoRedo.undo(getHistory(), logic);
    }

    /**
     * redo a move after an undo (button "redo")
     */
    @FXML
    public void redo() {
        undoRedo.redo(getHistory(), logic);
    }

    /**
     * is triggered if the user clicks on a move in the history-panel
     *
     * @param mouseEvent the mouseEvent
     */
    @FXML
    public void undoRedoClicked(MouseEvent mouseEvent) {
        if (mouseEvent.getTarget() instanceof Text) {
            undoRedo.undoRedoClicked(getHistory(), logic, (Text) mouseEvent.getTarget());
        }
    }

    // ----------------------------------Update----------------------------------------------------------------------------------------------

    /**
     * updates the scene (the board, the history, the beaten-figure-list, possible notifications, possible board turns)
     */
    public void updateScene() {
        drawBoard();
        updateNotifications();
        updateBeatenFigures();
        if (getRotate().isSelected()) {
            logic.turnBoard(false);
        }
        setPlayerLabel(logic.getCoreGame().isActivePlayer());
    }

    /**
     * Sets a label in the gui if a player is in check, checkmate or stalemate
     */
    private void updateNotifications() {
        getLabelCheck().setVisible(false);
        if (getShowFlags().isSelected()) {
            if (logic.getCoreGame().getCurrentBoard().isCheckFlag(true)) {
                getLabelCheck().setVisible(true);
                getLabelCheck().setText(LanguageManager.getText("blackCheck_label"));
            }
            if (logic.getCoreGame().getCurrentBoard().isCheckFlag(false)) {
                getLabelCheck().setVisible(true);
                getLabelCheck().setText(LanguageManager.getText("whiteCheck_label"));
            }
            if (logic.getCoreGame().getCurrentBoard().isCheckMateFlag(true)) {
                getLabelCheck().setVisible(true);
                getLabelCheck().setText(LanguageManager.getText("blackCheckmate_label"));
            }
            if (logic.getCoreGame().getCurrentBoard().isCheckMateFlag(false)) {
                getLabelCheck().setVisible(true);
                getLabelCheck().setText(LanguageManager.getText("whiteCheckmate_label"));
            }
            if (logic.getCoreGame().getCurrentBoard().isStaleMateFlag()) {
                getLabelCheck().setVisible(true);
                getLabelCheck().setText(LanguageManager.getText("stalemate_label"));
            }
        }
    }

    /**
     * updates the history in the gui
     *
     * @param move the move that should be added to the history
     */
    public void updateHistory(Move move) {
        String space = "   ";
        if (getHistory().getRowCount() < 10) {
            space = "       ";
        } else if (getHistory().getRowCount() < 100) {
            space = "     ";
        }
        Text t = new Text(getHistory().getRowCount() + space + move.toString());
        t.setFill(valueOf("#515151"));
        t.setFont(new Font("Calibri", 15.0));

        if (logic.getGameMode() != GameMode.COMPUTER || getHistory().getRowCount() % 2 == 0) {
            t.setCursor(Cursor.HAND);
            t.setOnMouseEntered((event) -> {
                t.setFill(disabledColor);
                t.setFont(new Font("Calibri", 16.0));
            });
            t.setOnMouseExited((event) -> {
                t.setFill(valueOf("#515151"));
                t.setFont(new Font("Calibri", 15.0));
            });
        }

        getHistory().add(t, 0, getHistory().getRowCount());

        undoRedo.setPointer(logic.getCoreGame().getMoveHistory().size() - 1);
        getScrollPaneHistory().vvalueProperty().bind(getHistory().heightProperty());
    }


    /**
     * updates the beaten figure list in the gui
     */
    public void updateBeatenFigures() {
        int whiteIndex = 0;
        int blackIndex = 0;
        int whiteCol = 0;
        int blackCol = 0;

        getBeatenFiguresBlack().getChildren().clear();
        getBeatenFiguresWhite().getChildren().clear();

        for (Figure figure : logic.getCoreGame().getCurrentBoard().getBeatenFigures()) {
            ImageView iv = new ImageView(ImageHandler.getImageBySymbol(figure.getSymbol()));
            iv.setEffect(new DropShadow(2.0, 2.0, 2.0, valueOf("#777777")));
            iv.preserveRatioProperty().setValue(true);
            iv.setFitHeight(50.0);
            iv.setRotate(0);

            GridPane.setColumnIndex(iv, figure.isBlack() ? blackCol : whiteCol);
            GridPane.setRowIndex(iv, figure.isBlack() ? blackIndex : whiteIndex);
            if (figure.isBlack()) {
                getBeatenFiguresBlack().getChildren().add(iv);
                blackIndex++;
                if (blackIndex == 8) {
                    blackIndex = 0;
                    blackCol = 1;
                }
            } else {
                getBeatenFiguresWhite().getChildren().add(iv);
                whiteIndex++;
                if (whiteIndex == 8) {
                    whiteIndex = 0;
                    whiteCol = 1;
                }
            }
        }
    }

    /**
     * draws the chess board
     */
    private void drawBoard() {
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                this.getBoard().getChildren().remove(getImageViewByIndex(x + 1, 8 - y));
                if (ImageHandler.getImageBySymbol(logic.getCoreGame().getCurrentBoard().getFigure(x, y).getSymbol()) != null) {
                    ImageView iv = new ImageView(ImageHandler.getImageBySymbol(logic.getCoreGame().getCurrentBoard().getFigure(x, y).getSymbol()));
                    iv.preserveRatioProperty().setValue(true);
                    iv.setFitHeight(50.0);
                    iv.setMouseTransparent(true);
                    iv.setEffect(new Reflection(0.0, 0.12, 0.24, 0.0));
                    this.getBoard().add(iv, x + 1, 8 - y);
                }
            }
        }
        if (logic.getGameMode() != GameMode.NORMAL && logic.isPlayerBlack()) {
            logic.turnFigures(180);
        }
    }

    /**
     * update FlagButton event
     */
    @FXML
    private void updateFlagButton() {
        updateNotifications();
    }

    /**
     * update PossibleMove event
     */
    @FXML
    private void updatePossibleMovesButton() {
        markPossibleFields(logic.getStartField(), getPossibleMove().isSelected(), logic.getCoreGame().getCurrentBoard());
    }

    /**
     * update RotateButton event
     */
    @FXML
    private void updateRotateButton() {
        logic.turnBoard(!getRotate().isSelected());
    }

    /**
     * mark all fields where a piece could go
     *
     * @param field the field on the chessboard where the figure is standing
     * @param mark  mark or unmark
     * @param board the current chessboard
     */
    protected void markPossibleFields(Rectangle field, boolean mark, Board board) {
        for (Rectangle f : getPossibleFields(field, board)) {
            if (mark && getPossibleMove().isSelected()) {
                f.setStroke(disabledColor);
                f.setStrokeWidth(5);
                f.setStrokeType(StrokeType.INSIDE);
            } else {
                f.setStrokeWidth(0);
            }
        }
    }


    //--------------setter / getter---------------------------------------------------------------------------------------------------------------

    /**
     * updates the label that shows which player's turn it is.
     *
     * @param black true if the actual player is black
     */
    private void setPlayerLabel(boolean black) {
        if (black) {
            getRectangleBlack().setStroke(disabledColor);
            getRectangleBlack().setStrokeWidth(3);
            getRectangleWhite().setStroke(BLACK);
            getRectangleWhite().setStrokeWidth(1);
        } else {
            getRectangleWhite().setStroke(disabledColor);
            getRectangleWhite().setStrokeWidth(3);
            getRectangleBlack().setStroke(BLACK);
            getRectangleBlack().setStrokeWidth(1);
        }
    }

    /**
     * Marks or unmarks one single field and, if selected, the possible moves of the figure on the field
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
            markPossibleFields(field, true, board);
        } else {
            field.setStrokeWidth(0);
            markPossibleFields(field, false, board);
        }
    }

    /**
     * shows in the gui whether the computer is calculating or not
     *
     * @param isCalculating whether the computer (AI) is currently calculating
     * @param message the calculating message
     */
    protected void setCalculating(boolean isCalculating, String message) {
        if (isCalculating) {
            getLabelCalculating().setText(message);
            getLabelCalculating().setVisible(true);
            getBoard().setMouseTransparent(true);
        } else {
            getLabelCalculating().setVisible(false);
            getBoard().setMouseTransparent(false);
        }
    }

    /**
     * performs a move if the target position is clicked after the start position was clicked and if the move is allowed
     *
     * @param mouseEvent the clicked field
     */
    public void isFieldClicked(MouseEvent mouseEvent) {
        if (mouseEvent.getTarget() instanceof Rectangle) {
            Rectangle clickedField = (Rectangle) mouseEvent.getTarget();
            logic.handleFieldClick(clickedField, logic.getCoreGame().isActivePlayer());
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
     * returns the field that is on the given position on the gridPane
     *
     * @param row    rowIndex of the field you want to get
     * @param column columnIndex of the field you want to get
     * @return the field with the rowIndex row and the columnIndex column
     */
    private Node getField(int row, int column) {
        for (Node node : getBoard().getChildren()) {
            if (GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == column) {
                return node;
            }
        }
        return null;
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
     * returns the ImageView that is at the given position in the gridPane
     *
     * @param column of the ImageView you want to get
     * @param row    of the ImageView you want to get
     * @return the ImageView with the columnIndex column and the rowIndex row in the gridPane
     */
    private Node getImageViewByIndex(int column, int row) {
        Node result = null;
        ObservableList<Node> children = getBoard().getChildren();

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
     * switches the language of the gui elements
     */
    @FXML
    private void setLanguage(MouseEvent event) {
        LanguageManager.setLanguage(((ImageView) event.getTarget()).getImage().getUrl().substring(64,66));
    }


    //-----simple setter / getter---------------------------------------------------------------------------------------------------------------

    /**
     * returns the gui element gridPane which shows white's beaten figures
     *
     * @return the gui element gridPane which shows white's beaten figures
     */
    public GridPane getBeatenFiguresWhite() {
        return (GridPane) menu.getChildren().get(0);
    }

    /**
     * returns the gui element "chessboard" as a gridPane
     *
     * @return the gui element "chessboard" as a gridPane
     */
    protected GridPane getBoard() {
        return (GridPane) menu.getChildren().get(1);
    }

    /**
     * returns the gui element "History" as a scrollPane
     *
     * @return the gui element "History" as a scrollPane
     */
    protected ScrollPane getScrollPaneHistory() {
        return (ScrollPane) menu.getChildren().get(3);
    }

    /**
     * returns the gui elements within the "History"-scrollPane as a gridPane
     *
     * @return the gui elements within the "History"-scrollPane as a gridPane
     */
    protected GridPane getHistory() {
        return (GridPane) getScrollPaneHistory().getContent();
    }

    /**
     * returns the gui label which shows whether one team is in check
     *
     * @return the gui label which shows whether one team is in check
     */
    private Label getLabelCheck() {
        return (Label) menu.getChildren().get(4);
    }

    /**
     * returns the gui label which shows whether the computer is still calculating
     *
     * @return the gui label which shows whether the computer is still calculating
     */
    private Label getLabelCalculating() {
        return (Label) menu.getChildren().get(5);
    }

    /**
     * returns the gui checkBox which shows whether the "touch-move"-rule is activated or not
     *
     * @return the gui checkBox which shows whether the "touch-move"-rule is activated or not
     */
    protected CheckBox getTouchMove() {
        return (CheckBox) menu.getChildren().get(6);
    }

    /**
     * returns the gui checkBox which shows whether the "show-possible-moves"-rule is activated or not
     *
     * @return the gui checkBox which shows whether the "show-possible-moves"-rule is activated or not
     */
    protected CheckBox getPossibleMove() {
        return (CheckBox) menu.getChildren().get(7);
    }

    /**
     * returns the gui checkBox which shows whether the "rotate"-rule is activated or not
     *
     * @return the gui checkBox which shows whether the "rotate"-rule is activated or not
     */
    protected CheckBox getRotate() {
        return (CheckBox) menu.getChildren().get(8);
    }

    /**
     * returns the gui checkBox which shows whether the "show-flag"-rule is activated or not
     *
     * @return the gui checkBox which shows whether the "show-flag"-rule is activated or not
     */
    protected CheckBox getShowFlags() {
        return (CheckBox) menu.getChildren().get(9);
    }

    /**
     * returns the gui element "white rectangle" which shows whether it is white's turn
     *
     * @return the gui element "white rectangle" which shows whether it is white's turn
     */
    private Rectangle getRectangleWhite() {
        return (Rectangle) menu.getChildren().get(11);
    }

    /**
     * returns the gui element "black rectangle" which shows whether it is white's turn
     *
     * @return the gui element "black rectangle" which shows whether it is white's turn
     */
    private Rectangle getRectangleBlack() {
        return (Rectangle) menu.getChildren().get(12);
    }

    /**
     * returns the gui element gridPane which shows black's beaten figures
     *
     * @return the gui element gridPane which shows black's beaten figures
     */
    private GridPane getBeatenFiguresBlack() {
        return (GridPane) menu.getChildren().get(13);
    }

    public UndoRedo getUndoRedo() {
        return undoRedo;
    }
}