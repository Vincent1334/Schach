package chess.gui;

import chess.enums.GameMode;
import chess.managers.LanguageManager;
import chess.managers.WindowManager;
import chess.model.*;
import chess.network.NetworkPlayer;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.effect.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
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
    private Scene scene;

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
        scene = new Scene(this);
        logic = new Logic(gameMode, isBlack, networkPlayer);
        undoRedo = new UndoRedo(logic.getCoreGame().getMoveHistory().size() - 1, this);

        // style fields (hover effect)
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
        LanguageManager.networkID = "";
        logic.killNetworkPlayer();

        WindowManager.initialWindow("MenuStage", "menu_title");
        WindowManager.showStage("MenuStage");
        // Close all other Stage
        WindowManager.closeStage("PromotionStage");
        WindowManager.closeStage("GameStage");
    }

    //----------------------------------Undo/Redo----------------------------------------------------------------------------------------------

    /**
     * undo a move (button "undo")
     */
    @FXML
    private void undo() {
        undoRedo.undo(getHistory(), logic);
        if(logic.getGameMode()==GameMode.NETWORK){
            logic.getNetwork().sendUndoRedo(undoRedo.getPointer());
        }
    }

    /**
     * redo a move after an undo (button "redo")
     */
    @FXML
    private void redo() {
        undoRedo.redo(getHistory(), logic);
        if(logic.getGameMode()==GameMode.NETWORK){
            logic.getNetwork().sendUndoRedo(undoRedo.getPointer());
        }
    }

    /**
     * is triggered if the user clicks on a move in the history-panel
     * undoes/redoes to the clicked move
     * @param mouseEvent the mouseEvent
     */
    @FXML
    private void undoRedoClicked(MouseEvent mouseEvent) {
        if (mouseEvent.getTarget() instanceof Text) {
            int index = 0;
            // which move was clicked?
            for (int i = 0; i < getHistory().getRowCount() - 1; i++) {
                if (getHistory().getChildren().get(i).equals(mouseEvent.getTarget())) {
                    index = i;
                }
            }
            if(logic.getGameMode()==GameMode.NETWORK){
                logic.getNetwork().sendUndoRedo(index);
            }
            undoRedo.undoRedoClicked(getHistory(), logic, index);
        }
        // makes the ScrollPane of the history scrollable
        getScrollPaneHistory().vvalueProperty().unbind();
    }

    // ----------------------------------Update----------------------------------------------------------------------------------------------

    /**
     * update FlagButton event
     */
    @FXML
    private void updateFlagButton() {
        scene.updateNotifications();
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
    private void markPossibleFields(Rectangle field, boolean mark, Board board) {
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


    //--------------setter---------------------------------------------------------------------------------------------------------------

    /**
     * switches the language of the gui elements
     * @param event the mouse event, used to know which flag was clicked
     */
    @FXML
    private void setLanguage(MouseEvent event) {
        //the url of the clicked image
        String url = ((ImageView) event.getTarget()).getImage().getUrl();
        //sets the language after the name of the image
        LanguageManager.setLanguage(url.substring(url.length()-6,url.length()-4));
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


    //--------------is...---------------------------------------------------------------------------------------------------------------

    /**
     * performs a move if the target position is clicked after the start position was clicked and if the move is allowed
     *
     * @param mouseEvent the clicked field
     */
    @FXML
    private void isFieldClicked(MouseEvent mouseEvent) {
        if (mouseEvent.getTarget() instanceof Rectangle) {
            Rectangle clickedField = (Rectangle) mouseEvent.getTarget();
            logic.handleFieldClick(clickedField, logic.getCoreGame().isActivePlayerBlack());
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

    //--------------getter---------------------------------------------------------------------------------------------------------------

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
            fields.add((Rectangle) getField(8 - position.getPOS_Y(), position.getPOS_X() + 1));
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
    protected Node getImageViewByIndex(int column, int row) {
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


    //-----simple getter---------------------------------------------------------------------------------------------------------------

    /**
     * returns the gui element gridPane which shows white's beaten figures
     *
     * @return the gui element gridPane which shows white's beaten figures
     */
    protected GridPane getBeatenFiguresWhite() {
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
    protected Label getLabelCheck() {
        return (Label) menu.getChildren().get(4);
    }

    /**
     * returns the gui label which shows whether the computer is still calculating
     *
     * @return the gui label which shows whether the computer is still calculating
     */
    protected Label getLabelCalculating() {
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
    protected Rectangle getRectangleWhite() {
        return (Rectangle) menu.getChildren().get(11);
    }

    /**
     * returns the gui element "black rectangle" which shows whether it is black's turn
     *
     * @return the gui element "black rectangle" which shows whether it is black's turn
     */
    protected Rectangle getRectangleBlack() {
        return (Rectangle) menu.getChildren().get(12);
    }

    /**
     * returns the gui element gridPane which shows black's beaten figures
     *
     * @return the gui element gridPane which shows black's beaten figures
     */
    protected GridPane getBeatenFiguresBlack() {
        return (GridPane) menu.getChildren().get(13);
    }

    /**
     * returns the UndoRedo class
     * @return the UndoRedo class
     */
    protected UndoRedo getUndoRedo() {
        return undoRedo;
    }

    protected Logic getLogic(){
        return logic;
    }

    protected Scene getScene(){
        return scene;
    }
}