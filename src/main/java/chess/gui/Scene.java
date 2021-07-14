package chess.gui;

import chess.enums.GameMode;
import chess.figures.Figure;
import chess.managers.ImageManager;
import chess.managers.LanguageManager;
import chess.model.Board;
import chess.model.Move;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Reflection;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import static javafx.scene.paint.Color.BLACK;
import static javafx.scene.paint.Color.valueOf;

/**
 * This class updates the Scene
 *
 * @author Lydia Engelhardt, Sophia Kuhlmann, Vincent Schiller, Friederike Weilbeer
 * 2021-06-09
 */
public class Scene {

    private final ChessBoard CHESSBOARD;

    /**
     * initializes Scene
     *
     * @param CHESSBOARD the CHESSBOARD with all FXML Objects
     */
    protected Scene(ChessBoard CHESSBOARD) {
        this.CHESSBOARD = CHESSBOARD;
    }

    /**
     * updates the scene (the board, the history, the beaten-figure-list, possible notifications, possible board turns)
     */
    protected void updateScene() {
        drawBoard();
        updateNotifications();
        updateBeatenFigures();
        if(CHESSBOARD.getLogic().getGameMode() == GameMode.COMPUTER && !CHESSBOARD.getLogic().getComputer().isBlack()){
            CHESSBOARD.getBoard().setRotate(180);
            CHESSBOARD.getLogic().turnFigures(180);
        }
        if (CHESSBOARD.getRotate().isSelected() && !CHESSBOARD.getRotate().isDisabled()) {
            CHESSBOARD.getLogic().turnBoard(false);
        }
        setPlayerLabel(CHESSBOARD.getLogic().getCoreGame().isActivePlayerBlack());
    }

    /**
     * Sets a label in the gui if a player is in check, checkmate or stalemate
     */
    protected void updateNotifications() {
        Board board = CHESSBOARD.getLogic().getCoreGame().getCurrentBoard();
        Label label = CHESSBOARD.getLabelCheck();
        label.setVisible(false);
        if (CHESSBOARD.getShowFlags().isSelected()) {
            if (board.isCheckFlag(true)) {
                label.setVisible(true);
                label.setText(LanguageManager.getText("blackCheck_label"));
            }
            if (board.isCheckFlag(false)) {
                label.setVisible(true);
                label.setText(LanguageManager.getText("whiteCheck_label"));
            }
            if (board.isCheckMateFlag(true)) {
                label.setVisible(true);
                label.setText(LanguageManager.getText("blackCheckmate_label"));
            }
            if (board.isCheckMateFlag(false)) {
                label.setVisible(true);
                label.setText(LanguageManager.getText("whiteCheckmate_label"));
            }
            if (board.isStaleMateFlag()) {
                label.setVisible(true);
                label.setText(LanguageManager.getText("stalemate_label"));
            }
        }
    }

    /**
     * updates the history in the gui
     *
     * @param move the move that should be added to the history
     */
    protected void updateHistory(Move move) {
        int row = CHESSBOARD.getHistory().getRowCount();
        String space = "   ";
        if (row < 10) {
            space = "       ";
        } else if (row < 100) {
            space = "     ";
        }
        Text t = new Text(row + space + move.toString());
        t.setFill(valueOf("#515151"));
        t.setFont(new Font("Calibri", 15.0));

        if (CHESSBOARD.getLogic().getGameMode() != GameMode.COMPUTER || CHESSBOARD.getLogic().getComputer().isBlack() && row % 2 == 0 || !CHESSBOARD.getLogic().getComputer().isBlack() && row % 2 != 0) {
            t.setCursor(Cursor.HAND);
            t.setOnMouseEntered((event) -> {
                t.setFill(valueOf("#8fbe00"));
                t.setFont(new Font("Calibri", 16.0));
            });
            t.setOnMouseExited((event) -> {
                t.setFill(valueOf("#515151"));
                t.setFont(new Font("Calibri", 15.0));
            });
        }

        CHESSBOARD.getHistory().add(t, 0, row);

        CHESSBOARD.getUndoRedo().setPointer(CHESSBOARD.getLogic().getCoreGame().getMoveHistory().size() - 1);
        CHESSBOARD.getScrollPaneHistory().vvalueProperty().bind(CHESSBOARD.getHistory().heightProperty());
    }

    /**
     * updates the beaten figure list in the gui
     */
    protected void updateBeatenFigures() {
        int whiteIndex = 0;
        int blackIndex = 0;
        int whiteCol = 0;
        int blackCol = 0;

        CHESSBOARD.getBeatenFiguresBlack().getChildren().clear();
        CHESSBOARD.getBeatenFiguresWhite().getChildren().clear();

        for (Figure figure : CHESSBOARD.getLogic().getCoreGame().getCurrentBoard().getBeatenFigures()) {
            ImageView iv = new ImageView(ImageManager.getImageBySymbol(figure.getSymbol()));
            iv.setEffect(new DropShadow(2.0, 2.0, 2.0, valueOf("#777777")));
            iv.preserveRatioProperty().setValue(true);
            iv.setFitHeight(50.0);
            iv.setRotate(0);

            GridPane.setColumnIndex(iv, figure.isBlack() ? blackCol : whiteCol);
            GridPane.setRowIndex(iv, figure.isBlack() ? blackIndex : whiteIndex);
            if (figure.isBlack()) {
                CHESSBOARD.getBeatenFiguresBlack().getChildren().add(iv);
                blackIndex++;
                if (blackIndex == 8) {
                    blackIndex = 0;
                    blackCol = 1;
                }
            } else {
                CHESSBOARD.getBeatenFiguresWhite().getChildren().add(iv);
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
                CHESSBOARD.getBoard().getChildren().remove(CHESSBOARD.getImageViewByIndex(x + 1, 8 - y));
                if (ImageManager.getImageBySymbol(CHESSBOARD.getLogic().getCoreGame().getCurrentBoard().getFigure(x, y).getSymbol()) != null) {
                    ImageView iv = new ImageView(ImageManager.getImageBySymbol(CHESSBOARD.getLogic().getCoreGame().getCurrentBoard().getFigure(x, y).getSymbol()));
                    iv.preserveRatioProperty().setValue(true);
                    iv.setFitHeight(50.0);
                    iv.setMouseTransparent(true);
                    iv.setEffect(new Reflection(0.0, 0.12, 0.24, 0.0));
                    CHESSBOARD.getBoard().add(iv, x + 1, 8 - y);
                }
            }
        }
    }

    /**
     * updates the label that shows which player's turn it is.
     *
     * @param isActivePlayerBlack true if the actual player is black
     */
    protected void setPlayerLabel(boolean isActivePlayerBlack) {
        Rectangle b = CHESSBOARD.getRectangleBlack();
        Rectangle w = CHESSBOARD.getRectangleWhite();
        if (isActivePlayerBlack) {
            b.setStroke(valueOf("#8fbe00"));
            b.setStrokeWidth(3);
            w.setStroke(BLACK);
            w.setStrokeWidth(1);
        } else {
            w.setStroke(valueOf("#8fbe00"));
            w.setStrokeWidth(3);
            b.setStroke(BLACK);
            b.setStrokeWidth(1);
        }
    }

}