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

    private final Controller CONTROLLER;

    /**
     * initializes Scene
     *
     * @param controller the controller with all FXML Objects
     */
    public Scene(Controller controller) {
        this.CONTROLLER = controller;
    }

    /**
     * updates the scene (the board, the history, the beaten-figure-list, possible notifications, possible board turns)
     */
    public void updateScene() {
        drawBoard();
        updateNotifications();
        updateBeatenFigures();
        if (CONTROLLER.getRotate().isSelected()) {
            CONTROLLER.getLogic().turnBoard(false);
        }
        setPlayerLabel(CONTROLLER.getLogic().getCoreGame().isActivePlayerBlack());
    }

    /**
     * Sets a label in the gui if a player is in check, checkmate or stalemate
     */
    protected void updateNotifications() {
        Board board = CONTROLLER.getLogic().getCoreGame().getCurrentBoard();
        Label label = CONTROLLER.getLabelCheck();
        label.setVisible(false);
        if (CONTROLLER.getShowFlags().isSelected()) {
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
    public void updateHistory(Move move) {
        int row = CONTROLLER.getHistory().getRowCount();
        String space = "   ";
        if (row < 10) {
            space = "       ";
        } else if (row < 100) {
            space = "     ";
        }
        Text t = new Text(row + space + move.toString());
        t.setFill(valueOf("#515151"));
        t.setFont(new Font("Calibri", 15.0));

        if (CONTROLLER.getLogic().getGameMode() != GameMode.COMPUTER || CONTROLLER.getLogic().getComputer().isBlack() && row % 2 == 0 || !CONTROLLER.getLogic().getComputer().isBlack() && row % 2 != 0) {
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

        CONTROLLER.getHistory().add(t, 0, row);

        CONTROLLER.getUndoRedo().setPointer(CONTROLLER.getLogic().getCoreGame().getMoveHistory().size() - 1);
        CONTROLLER.getScrollPaneHistory().vvalueProperty().bind(CONTROLLER.getHistory().heightProperty());
    }

    /**
     * updates the beaten figure list in the gui
     */
    public void updateBeatenFigures() {
        int whiteIndex = 0;
        int blackIndex = 0;
        int whiteCol = 0;
        int blackCol = 0;

        CONTROLLER.getBeatenFiguresBlack().getChildren().clear();
        CONTROLLER.getBeatenFiguresWhite().getChildren().clear();

        for (Figure figure : CONTROLLER.getLogic().getCoreGame().getCurrentBoard().getBeatenFigures()) {
            ImageView iv = new ImageView(ImageManager.getImageBySymbol(figure.getSymbol()));
            iv.setEffect(new DropShadow(2.0, 2.0, 2.0, valueOf("#777777")));
            iv.preserveRatioProperty().setValue(true);
            iv.setFitHeight(50.0);
            iv.setRotate(0);

            GridPane.setColumnIndex(iv, figure.isBlack() ? blackCol : whiteCol);
            GridPane.setRowIndex(iv, figure.isBlack() ? blackIndex : whiteIndex);
            if (figure.isBlack()) {
                CONTROLLER.getBeatenFiguresBlack().getChildren().add(iv);
                blackIndex++;
                if (blackIndex == 8) {
                    blackIndex = 0;
                    blackCol = 1;
                }
            } else {
                CONTROLLER.getBeatenFiguresWhite().getChildren().add(iv);
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
                CONTROLLER.getBoard().getChildren().remove(CONTROLLER.getImageViewByIndex(x + 1, 8 - y));
                if (ImageManager.getImageBySymbol(CONTROLLER.getLogic().getCoreGame().getCurrentBoard().getFigure(x, y).getSymbol()) != null) {
                    ImageView iv = new ImageView(ImageManager.getImageBySymbol(CONTROLLER.getLogic().getCoreGame().getCurrentBoard().getFigure(x, y).getSymbol()));
                    iv.preserveRatioProperty().setValue(true);
                    iv.setFitHeight(50.0);
                    iv.setMouseTransparent(true);
                    iv.setEffect(new Reflection(0.0, 0.12, 0.24, 0.0));
                    CONTROLLER.getBoard().add(iv, x + 1, 8 - y);
                }
            }
        }
        if (CONTROLLER.getLogic().getGameMode() != GameMode.NORMAL && CONTROLLER.getLogic().isPlayerBlack()) {
            CONTROLLER.getLogic().turnFigures(180);
        }
    }

    /**
     * updates the label that shows which player's turn it is.
     *
     * @param isActivePlayerBlack true if the actual player is black
     */
    protected void setPlayerLabel(boolean isActivePlayerBlack) {
        Rectangle b = CONTROLLER.getRectangleBlack();
        Rectangle w = CONTROLLER.getRectangleWhite();
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