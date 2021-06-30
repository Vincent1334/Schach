package chess.gui;

import chess.GameMode;
import chess.figures.Figure;
import chess.managers.LanguageManager;
import chess.managers.WindowManager;
import chess.model.*;
import chess.network.NetworkPlayer;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventTarget;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

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

    private Logic logic;
    private int pointer;
    private List<Text> undoRedoMovesAsText = new ArrayList<>();
    private List<Board> undoRedoMovesAsBoard = new ArrayList<>();


    @FXML
    private Pane menu;

    /**
     * initiates the controller and the logic
     *
     * @param gameMode against a local friend (0) or a network game (1) or against the computer (2)
     */
    public void init(GameMode gameMode, boolean isBlack, NetworkPlayer networkPlayer) {
        logic = new Logic(gameMode, isBlack, this, networkPlayer);
        pointer = logic.getCoreGame().getMoveHistory().size() - 1;
        for (int i=0; i<96 ;i++) {
            int finalI = i;
            this.getBoard().getChildren().get(i).setOnMouseEntered((event) ->{
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
            this.getBoard().getChildren().get(i).setOnMouseExited((event) ->{
                MotionBlur b = new MotionBlur();
                b.setAngle(45.0);
                b.setRadius(7.0);
                this.getBoard().getChildren().get(finalI).setEffect(b);
            });
        }

    }

    /**
     * closes the actual window and opens the menu-window
     *
     * @param event the mouse event
     */
    public void backToMenu(MouseEvent event) {
        logic.killNetworkPlayer();

        Stage stage = new Stage();
        stage.setTitle(LanguageManager.getText("menu_title"));
        stage.setScene(new Scene(WindowManager.createWindow("MainMenu.fxml")));
        stage.show();

        // Hide this current window
        if(logic.getPromotionStage() != null){
            logic.getPromotionStage().close();
        }
        ((Node) (event.getSource())).getScene().getWindow().hide();
    }

    //----------------------------------Undo/Redo----------------------------------------------------------------------------------------------

    /**
     * undo a move (button "undo")
     *
     * @param actionEvent
     */
    public void undo(ActionEvent actionEvent) {
        if (pointer >= 0) {
            Text undoMove = (Text) getHistory().getChildren().get(pointer);
            undoMove.setOpacity(0.5);
            undoRedoMovesAsText.add(undoMove);

            undoRedoMovesAsBoard.add(logic.getCoreGame().getMoveHistory().get(pointer));

            // im Computermodus jeweils zwei Züge zurück gehen
            if (logic.getGameMode() == GameMode.COMPUTER && pointer > 0) {
                Text undoMove2 = (Text) getHistory().getChildren().get(pointer - 1);
                undoMove2.setOpacity(0.5);
                undoRedoMovesAsText.add(undoMove2);

                undoRedoMovesAsBoard.add(logic.getCoreGame().getMoveHistory().get(pointer - 1));
                pointer--;
            }
            pointer--;

            Board newBoard;
            if (pointer >= 0) {
                newBoard = logic.getCoreGame().getMoveHistory().get(pointer);
            } else {
                newBoard = new Board();
            }
            logic.getCoreGame().setCurrentBoard(new Board(newBoard));

            // ggf. Spielerwechsel
            if (pointer % 2 == 0) {
                logic.getCoreGame().setActivePlayer(true);
            } else {
                logic.getCoreGame().setActivePlayer(false);
            }
            updateScene();
            unmark();
        }
    }

    private void unmark(){
        for (int i=0; i<96 ;i++) {
            if(this.getBoard().getChildren().get(i) instanceof Rectangle)
            ((Rectangle)this.getBoard().getChildren().get(i)).setStrokeWidth(0);
        }
    }

    /**
     * redo a move after an undo (button "redo")
     *
     * @param actionEvent
     */
    public void redo(ActionEvent actionEvent) {
        if (undoRedoMovesAsText.size() > 0) {
            pointer++;

            Text undoMove = (Text) getHistory().getChildren().get(pointer);
            undoMove.setOpacity(1);
            undoRedoMovesAsText.remove(undoRedoMovesAsText.size() - 1);

            // im Computermodus werden jeweils zwei Züge wiederhergestellt
            if (logic.getGameMode() == GameMode.COMPUTER) {
                Text undoMove2 = (Text) getHistory().getChildren().get(pointer + 1);
                undoMove2.setOpacity(1);
                undoRedoMovesAsText.remove(undoRedoMovesAsText.size() - 1);

                pointer++;
                undoRedoMovesAsBoard.remove(logic.getCoreGame().getMoveHistory().get(pointer - 1));
            }

            Board currentBoard;
            if (pointer >= 0) {
                currentBoard = logic.getCoreGame().getMoveHistory().get(pointer);
            } else {
                currentBoard = logic.getCoreGame().getMoveHistory().get(0);
            }
            undoRedoMovesAsBoard.remove(logic.getCoreGame().getMoveHistory().get(pointer));
            logic.getCoreGame().setCurrentBoard(new Board(currentBoard));


            // ggf- Spielerwechsel
            if (pointer % 2 == 0) {
                logic.getCoreGame().setActivePlayer(true);
            } else {
                logic.getCoreGame().setActivePlayer(false);
            }
            //Board.checkChessAndStaleMate(logic.getCoreGame().getCurrentBoard(),logic.getCoreGame().getActivePlayer());
            updateScene();
        }
    }

    /**
     * clears the undo/redo-lists, is triggered if the player wants to make a move after an undo
     */
    public void resetUndoRedo() {
        // Texte (für Anzeige)
        for (Text move : undoRedoMovesAsText) {
            getHistory().getChildren().removeIf(node -> node.equals(move));
        }
        undoRedoMovesAsText.clear();

        // Boards (für Logik)
        for (Board board : undoRedoMovesAsBoard) {
            logic.getCoreGame().getMoveHistory().remove(board);
        }
        pointer = logic.getCoreGame().getMoveHistory().size() - 1;
        undoRedoMovesAsBoard.clear();
    }

    /**
     * is triggered if the user clicks on a move in the history-panel
     *
     * @param mouseEvent
     */
    public void undoRedoClicked(MouseEvent mouseEvent) {
        if(mouseEvent.getTarget() instanceof Text){
            Text clickedText = (Text) mouseEvent.getTarget();
            int oldPointer = pointer;

            // auf welchen Zug wurde geklickt?
            for (int i = 0; i < getHistory().getRowCount() - 1; i++) {
                if (getHistory().getChildren().get(i).equals(clickedText)) {
                    pointer = i;
                }
            }
            // im Spiel gegen den Computer ist nur jeder zweite Zug anklickbar
            if (logic.getGameMode() != GameMode.COMPUTER || pointer % 2 == 1) {
                // Wähle entsprechenden BoardZustand
                Board newBoard;
                if (pointer >= 0) {
                    newBoard = logic.getCoreGame().getMoveHistory().get(pointer);
                } else {
                    newBoard = new Board();
                }
                // pack alle Züge dazwischen auf eine Liste / entferne alle Züge dazwischen von Liste
                if (oldPointer > pointer) {
                    for (int i = pointer + 1; i < getHistory().getRowCount() - 1; i++) {
                        // Boards (für Logik)
                        undoRedoMovesAsBoard.add(logic.getCoreGame().getMoveHistory().get(i));
                        // Texte (für Anzeige)
                        Text undoMove = (Text) getHistory().getChildren().get(i);
                        undoMove.setOpacity(0.5);
                        undoRedoMovesAsText.add(undoMove);
                    }
                } else if (oldPointer < pointer) {
                    for (int i = oldPointer + 1; i <= pointer; i++) {
                        // boards (für Logik)
                        undoRedoMovesAsBoard.remove(logic.getCoreGame().getMoveHistory().get(i));
                        // texte (für Anzeige)
                        Text undoMove = (Text) getHistory().getChildren().get(i);
                        undoMove.setOpacity(1);
                        undoRedoMovesAsText.remove(undoMove);
                    }
                }

                logic.getCoreGame().setCurrentBoard(new Board(newBoard));

                // ggf. Spielerwechsel
                if (pointer % 2 == 0) {
                    logic.getCoreGame().setActivePlayer(true);
                } else {
                    logic.getCoreGame().setActivePlayer(false);
                }

                updateScene();
                unmark();
            }
        }
    }

    //----------------------------------Update----------------------------------------------------------------------------------------------

    /**
     * updates the scene (the board, the history, the beaten-figure-list, possible notifications, possible board turns)
     */
    public void updateScene() {
        drawBoard();
        updateNotifications();
        updateBeatenFigures();
        if (getRotate().isSelected()) {
            turnBoard(false);
        }

        setPlayerLabel(logic.getCoreGame().getActivePlayer());
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
        String space = "";
        if(getHistory().getRowCount()<10){
            space = "       ";
        }else if(getHistory().getRowCount()<100){
            space = "     ";
        }else{
            space = "   ";
        }
        Text t = new Text(getHistory().getRowCount() + space + move.toString());
        t.setFill(valueOf("#515151"));
        t.setFont(new Font("Calibri", 15.0));
        t.setCursor(Cursor.HAND);


        if (logic.getGameMode() == GameMode.COMPUTER && getHistory().getRowCount() % 2 == 1) {
            t.setCursor(Cursor.DEFAULT);
        }else{
            t.setOnMouseEntered((event) ->{
                t.setFill(valueOf("#8fbe00"));
            });
            t.setOnMouseExited((event) ->{
                t.setFill(valueOf("#515151"));
            });
        }

        getHistory().add(t, 0, getHistory().getRowCount());

        pointer = logic.getCoreGame().getMoveHistory().size() - 1;
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
        if (logic.getGameMode() != GameMode.NORMAL && logic.isBlack()) turnFigures(180);
    }

    /**
     * turns the chessboard so that the figures of the actualPlayer are always on the bottom
     *
     * @param reset reset the boardTurning
     */
    public void turnBoard(boolean reset) {
        if (reset) {
            getBoard().setRotate(0);
            turnFigures(0);
        } else {
            if (logic.getCoreGame().getActivePlayer()) {
                getBoard().setRotate(180);
                turnFigures(180);
            } else {
                getBoard().setRotate(0);
                turnFigures(0);
            }
        }
    }

    /**
     * rotates the figures themselves
     *
     * @param angle the angle around which the figures are rotated
     */
    public void turnFigures(int angle) {
        for (Node node : getBoard().getChildren()) {
            node.setRotate(angle);
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
        turnBoard(!getRotate().isSelected());
    }

    //--------------set---------------------------------------------------------------------------------------------------------------

    /**
     * sets the language
     */
    @FXML
    private void setLanguage() {
        ResourceBundle oldLanguage = LanguageManager.getResourceBundle();
        LanguageManager.nextLocale();

        switchFlagLanguage(oldLanguage, "blackCheck_label");
        switchFlagLanguage(oldLanguage, "whiteCheck_label");
        switchFlagLanguage(oldLanguage, "blackCheckmate_label");
        switchFlagLanguage(oldLanguage, "whiteCheckmate_label");
        switchFlagLanguage(oldLanguage, "stalemate_label");
        getBackToMenu().setText(LanguageManager.getText("menu_button"));
        getLanguage().setText(LanguageManager.getText("language"));
        getLabelHistory().setText(LanguageManager.getText("history_label"));
        getTouchMove().setText(LanguageManager.getText("touch_move_button"));
        getPossibleMove().setText(LanguageManager.getText("possible_moves_button"));
        getRotate().setText(LanguageManager.getText("rotate_button"));
        getShowFlags().setText(LanguageManager.getText("flag_button"));
    }

    /**
     * switch the language of the gui elements
     *
     * @param oldLanguage the current language of the gui elements
     * @param label the label of the language you want to switch to (e.g. "fr" according to french)
     */
    private void switchFlagLanguage(ResourceBundle oldLanguage, String label) {
        if (getLabelCheck().getText().equals(oldLanguage.getString(label))) {
            getLabelCheck().setText(LanguageManager.getText(label));
        }
    }

    /**
     * updates the label that shows which player's turn it is.
     *
     * @param black true if the actual player is black
     */
    private void setPlayerLabel(boolean black) {
        if (black) {
            getRectangleBlack().setStroke(valueOf("#8fbe00"));
            getRectangleBlack().setStrokeWidth(3);
            getRectangleWhite().setStroke(BLACK);
            getRectangleWhite().setStrokeWidth(1);
        } else {
            getRectangleWhite().setStroke(valueOf("#8fbe00"));
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
     * mark all fields where a piece could go
     *
     * @param field the field on the chessboard where the figure is standing
     * @param mark mark or unmark
     * @param board the current chessboard
     */
    protected void markPossibleFields(Rectangle field, boolean mark, Board board) {
        if (mark) {
            if (getPossibleMove().isSelected()) {
                for (Rectangle f : getPossibleFields(field, board)) {
                    f.setStroke(valueOf("#8fbe00"));
                    f.setStrokeWidth(5);
                    f.setStrokeType(StrokeType.INSIDE);
                }
            }
        } else {
            for (Rectangle f : getPossibleFields(field, board)) {
                f.setStrokeWidth(0);
            }
        }
    }

    /**
     * shows in the gui whether the computer is calculating or not
     *
     * @param isCalculating whether the computer (ki) is currently calculating
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


    //--------------------------------------is----------------------------------------------------------------------------------------------

    /**
     * performs a move if the target position is clicked after the start position was clicked and if the move is allowed
     *
     * @param mouseEvent the clicked field
     */
    public void isFieldClicked(MouseEvent mouseEvent) {
        if (mouseEvent.getTarget() instanceof Rectangle) {
            Rectangle clickedField = (Rectangle) mouseEvent.getTarget();
            logic.handleFieldClick(clickedField, logic.getCoreGame().getActivePlayer());
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

    //-------------get---------------------------------------------------------------------------------------------------------------

    /**
     * returns the figure of the field on the gridPane
     *
     * @param field of the figure you want to get
     * @return the figure of the field
     */
    protected ImageView getFigure(Rectangle field) {
        return (ImageView) getImageViewByIndex(GridPane.getColumnIndex(field), GridPane.getRowIndex(field));
    }

    public List<Text> getUndoRedoMovesAsText() {
        return undoRedoMovesAsText;
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


    //-------------get gui elements---------------------------------------------------------------------------------------------------------------

    protected GridPane getBeatenFiguresWhite() {
        return (GridPane) menu.getChildren().get(0);
    }

    protected GridPane getBoard() {
        return (GridPane) menu.getChildren().get(1);
    }

    private Label getLabelHistory() {
        return (Label) menu.getChildren().get(2);
    }

    protected ScrollPane getScrollPaneHistory() {
            return (ScrollPane) menu.getChildren().get(3);
        }

    protected GridPane getHistory() {
        return (GridPane) getScrollPaneHistory().getContent();
    }

    private Label getLabelCheck() {
        return (Label) menu.getChildren().get(4);
    }

    private Label getLabelCalculating() {
        return (Label) menu.getChildren().get(5);
    }

    protected CheckBox getTouchMove() {
        return (CheckBox) menu.getChildren().get(6);
    }

    protected CheckBox getPossibleMove() {
        return (CheckBox) menu.getChildren().get(7);
    }

    protected CheckBox getRotate() {
        return (CheckBox) menu.getChildren().get(8);
    }

    protected CheckBox getShowFlags() {
        return (CheckBox) menu.getChildren().get(9);
    }

    private Button getBackToMenu() {
        return (Button) menu.getChildren().get(10);
    }

    private Rectangle getRectangleWhite() {
        return (Rectangle) menu.getChildren().get(11);
    }

    private Rectangle getRectangleBlack() {
        return (Rectangle) menu.getChildren().get(12);
    }

    private Button getLanguage() {
        return (Button) menu.getChildren().get(13);
    }

    private GridPane getBeatenFiguresBlack() {
        return (GridPane) menu.getChildren().get(14);
    }
}