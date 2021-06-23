package chess.gui;

import chess.GameMode;
import chess.controller.*;
import chess.figures.Figure;
import chess.model.*;
import chess.network.NetworkPlayer;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
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
    private List<Figure> beatenFigureList;
    private Logic logic;
    private static ResourceBundle messages = ResourceBundle.getBundle("/languages/MessagesBundle", Gui.locale);
    final private static String QUEEN = "Queen";
    private int promotionID = 0;

    @FXML
    private Pane menu;


    /**
     * initiates the controller and the logic
     *
     * @param gameMode         against a local friend (0) or a network game (1) or against the computer (2)
     */
    public void init(GameMode gameMode, boolean isBlack, NetworkPlayer networkPlayer) {
        beatenFigureList = new ArrayList<>();
        logic = new Logic(gameMode, isBlack,this, networkPlayer);
    }

    /**
     * closes the actual window and opens the menu-window
     *
     * @param event the mouse event
     */
    public void backToMenu(MouseEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Menu.fxml"));
            fxmlLoader.setResources(ResourceBundle.getBundle("/languages/MessagesBundle", messages.getLocale()));
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

    protected void showPromotionFigureWindow(){
        getPromotion().setVisible(true);
        getPromotion().setDisable(false);
        promotionID = 0;
    }
    protected void hidePromotionFigureWindow(){
        getPromotion().setVisible(false);
        getPromotion().setDisable(true);
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

        if (getRotate().isSelected()) {
            turnBoard(false);
        }

        setPlayerLabel(logic.getCoreGame().getActivePlayer());
    }

    /**
     * Sets a label in the gui if a player is in check, checkmate or stalemate
     *
     * @param board the current chessboard
     */
    private void updateNotifications(Board board) {
        getLabelCheck().setVisible(false);
        if (getShowFlags().isSelected()) {
            if (board.isCheckFlag(true)) {
                getLabelCheck().setVisible(true);
                getLabelCheck().setText(messages.getString("blackCheck_label"));
            }
            if (board.isCheckFlag(false)) {
                getLabelCheck().setVisible(true);
                getLabelCheck().setText(messages.getString("whiteCheck_label"));
            }
            if (board.isCheckMateFlag(true)) {
                getLabelCheck().setVisible(true);
                getLabelCheck().setText(messages.getString("blackCheckmate_label"));
            }
            if (board.isCheckMateFlag(false)) {
                getLabelCheck().setVisible(true);
                getLabelCheck().setText(messages.getString("whiteCheckmate_label"));
            }
            if (board.isStaleMateFlag()) {
                getLabelCheck().setVisible(true);
                getLabelCheck().setText(messages.getString("stalemate_label"));
            }
        }

    }

    /**
     * updates the history in the gui
     *
     * @param move the move that should be added to the history
     */
    public void updateHistory(Move move) {
        Text t = new Text(move.toString());

        getHistory().add(t, 2, getHistory().getRowCount());
        getHistory().add(new Text("   " + getHistory().getRowCount()), 0, getHistory().getRowCount() - 1);
    }

    /**
     * draws the chess board
     *
     * @param board the current chessboard
     */
    private void drawBoard(Board board) {
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                this.getBoard().getChildren().remove(getImageViewByIndex(x + 1, 8 - y));
                if (getImageBySymbol(board.getFigure(x, y).getSymbol()) != null) {
                    ImageView iv = new ImageView(getImageBySymbol(board.getFigure(x, y).getSymbol()));
                    iv.preserveRatioProperty().setValue(true);
                    iv.setFitHeight(50.0);
                    iv.setMouseTransparent(true);
                    iv.setEffect(new Reflection(0.0, 0.12, 0.24, 0.0));
                    this.getBoard().add(iv, x + 1, 8 - y);
                }
            }
        }
    }

    /**
     * turns the chessboard so that the figures of the actualPlayer are always on the bottom
     */
    private void turnBoard(boolean reset) {
        if(reset){
            getBoard().setRotate(0);
            turnFigures(0);
        }else{
            if(logic.getCoreGame().getActivePlayer()){
                getBoard().setRotate(180);
                turnFigures(180);
            }else{
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
    private void turnFigures(int angle) {
        for (Node node : getBoard().getChildren()) {
            node.setRotate(angle);
        }
    }


    /**
     * update FlagButton event
     */
    @FXML
    private void updateFlagButton() {
        updateNotifications(logic.getCoreGame().getCurrentBoard());
    }

    /**
     * update PossibleMove event
     */
    @FXML
    private void updatePossibleMovesButton() {
        markPossibleFields(logic.getStartField(),getPossibleMove().isSelected(), logic.getCoreGame().getCurrentBoard());
    }

    /**
     * update RotateButton event
     */
    @FXML
    private void updateRotateButton() {
        turnBoard(!getRotate().isSelected());
    }

    /**
     * update TouchMove event
     */
    @FXML
    private void updateTouchMoveButton() {

    }

    //--------------set---------------------------------------------------------------------------------------------------------------

    /**
     * sets the language
     */
    @FXML
    private void setLanguage() {
        ResourceBundle oldLanguage = messages;
        if (messages.getLocale().getCountry().equals("US")) {
            messages = ResourceBundle.getBundle("/languages/MessagesBundle", new Locale("de", "GE"));
        } else if (messages.getLocale().getCountry().equals("DE")) {
            messages = ResourceBundle.getBundle("/languages/MessagesBundle", new Locale("fr", "FR"));
        } else {
            messages = ResourceBundle.getBundle("/languages/MessagesBundle", new Locale("en", "US"));
        }

        getLabelCalculating().setText(messages.getString("calculating_label"));
        switchFlagLanguage(oldLanguage, "blackCheck_label");
        switchFlagLanguage(oldLanguage, "whiteCheck_label");
        switchFlagLanguage(oldLanguage, "blackCheckmate_label");
        switchFlagLanguage(oldLanguage, "whiteCheckmate_label");
        switchFlagLanguage(oldLanguage, "stalemate_label");
        getLabelPromotion().setText(messages.getString("promotion_label"));
        getButtonQueen().setText(messages.getString("queen_label"));
        getButtonBishop().setText(messages.getString("bishop_label"));
        getButtonRook().setText(messages.getString("rook_label"));
        getButtonKnight().setText(messages.getString("knight_label"));
        getBackToMenu().setText(messages.getString("menu_button"));
        getLanguage().setText(messages.getString("language"));
        getLabelHistory().setText(messages.getString("history_label"));
        getTouchMove().setText(messages.getString("touch_move_button"));
        getPossibleMove().setText(messages.getString("possible_moves_button"));
        getRotate().setText(messages.getString("rotate_button"));
        getShowFlags().setText(messages.getString("flag_button"));

    }

    private void switchFlagLanguage(ResourceBundle oldLanguage, String label) {
        if (getLabelCheck().getText().equals(oldLanguage.getString(label))) {
            getLabelCheck().setText(messages.getString(label));
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
            iv.setEffect(new DropShadow(2.0,2.0,2.0,valueOf("#777777")));
            iv.preserveRatioProperty().setValue(true);
            iv.setFitHeight(50.0);
            iv.setRotate(0);

            if (logic.getCoreGame().getActivePlayer()) {
                int indexBeatenFiguresBlack = 0;
                for (Figure figure : beatenFigures) {
                    if (figure.isBlackTeam()) {
                        indexBeatenFiguresBlack += 1;
                    }
                }
                if(indexBeatenFiguresBlack <9){
                    GridPane.setColumnIndex(iv, 0);
                    GridPane.setRowIndex(iv, indexBeatenFiguresBlack);
                }else{
                    GridPane.setColumnIndex(iv, 1);
                    GridPane.setRowIndex(iv, indexBeatenFiguresBlack-8);
                }

            } else {
                int indexBeatenFiguresWhite = 0;
                for (Figure figure : beatenFigures) {
                    if (!figure.isBlackTeam()) {
                        indexBeatenFiguresWhite += 1;
                    }
                }
                if(indexBeatenFiguresWhite <9){
                    GridPane.setColumnIndex(iv, 0);
                    GridPane.setRowIndex(iv, indexBeatenFiguresWhite);
                }else{
                    GridPane.setColumnIndex(iv, 1);
                    GridPane.setRowIndex(iv, indexBeatenFiguresWhite-8);
                }
            }

            if(logic.getCoreGame().getActivePlayer()){
                this.getBeatenFiguresBlack().getChildren().add(iv);
            }else{
                this.getBeatenFiguresWhite().getChildren().add(iv);
            }
            this.beatenFigureList.add(beatenFigures.get(beatenFigures.size() - 1));
        }
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
            markPossibleFields(field, true, board);
        } else {
            field.setStrokeWidth(0);
            markPossibleFields(field, false, board);
        }
    }

    protected void markPossibleFields(Rectangle field, boolean mark, Board board){
        if(mark){
            if (getPossibleMove().isSelected()) {
                for (Rectangle f : getPossibleFields(field, board)) {
                    f.setStroke(valueOf("#8fbe00"));
                    f.setStrokeWidth(5);
                    f.setStrokeType(StrokeType.INSIDE);
                }
            }
        }else{
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
    protected void setCalculating(boolean isCalculating) {
        if (isCalculating) {
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

    //figure

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
     * returns the ID-number of the conversionFigure
     *
     * @return the ID-number of the conversionFigure
     */
    @FXML
    private void getPromotionFigure(MouseEvent event) {
        String item = ((Button) event.getSource()).getText();
        if (item.equals(messages.getString("queen_label"))) {
            promotionID = 5;
        }
        if (item.equals(messages.getString("bishop_label"))) {
            promotionID = 4;
        }
        if (item.equals(messages.getString("rook_label"))) {
            promotionID = 2;
        }
        if (item.equals(messages.getString("knight_label"))) {
            promotionID = 3;
        }
        promotionID = 5;
    }

    protected int getPromotionID(){
        return promotionID;
    }


    //field

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


    //image

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
                return ImageHandler.getInstance().getImage(QUEEN + color);
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


    //labels and buttons

    protected GridPane getBeatenFiguresWhite(){
        return (GridPane) menu.getChildren().get(3);
    }

    protected GridPane getBoard(){
        return (GridPane) menu.getChildren().get(4);
    }

    private Label getLabelHistory(){
        return (Label) menu.getChildren().get(5);
    }

    protected GridPane getHistory(){
        return (GridPane) ((ScrollPane) menu.getChildren().get(6)).getContent();
    }

    private Label getLabelCheck(){
        return (Label) menu.getChildren().get(7);
    }

    private Label getLabelCalculating(){
        return (Label) menu.getChildren().get(8);
    }

    protected CheckBox getTouchMove(){
        return (CheckBox) menu.getChildren().get(9);
    }

    protected CheckBox getPossibleMove(){
        return (CheckBox) menu.getChildren().get(10);
    }

    protected CheckBox getRotate(){
        return (CheckBox) menu.getChildren().get(11);
    }

    protected CheckBox getShowFlags(){
        return (CheckBox) menu.getChildren().get(12);
    }

    private Button getBackToMenu(){
        return (Button) menu.getChildren().get(13);
    }

    /**
     * returns the gui-element that shows whether it is whites turn
     *
     * @return the gui-element (rectangle) that shows whether it is whites turn
     */
    private Rectangle getRectangleWhite() {
        return (Rectangle) menu.getChildren().get(14);
    }

    /**
     * returns the gui-element that shows whether it is blacks turn
     *
     * @return the gui-element (rectangle) that shows whether it is blacks turn
     */
    private Rectangle getRectangleBlack() {
        return (Rectangle) menu.getChildren().get(15);
    }

    private Button getLanguage(){
        return (Button) menu.getChildren().get(16);
    }

    private GridPane getBeatenFiguresBlack(){
        return  (GridPane) menu.getChildren().get(17);
    }

    private Pane getPromotion(){
        return (Pane) menu.getChildren().get(18);
    }

    private Label getLabelPromotion(){
        return (Label) getPromotion().getChildren().get(1);
    }

    private Button getButtonQueen(){
        return (Button) getPromotion().getChildren().get(2);
    }
    private Button getButtonBishop(){
        return (Button) getPromotion().getChildren().get(3);
    }
    private Button getButtonRook(){
        return (Button) getPromotion().getChildren().get(4);
    }
    private Button getButtonKnight(){
        return (Button) getPromotion().getChildren().get(5);
    }



}