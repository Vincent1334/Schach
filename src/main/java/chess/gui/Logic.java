package chess.gui;

import chess.controller.*;
import chess.ki.Computer;
import chess.model.*;
import javafx.application.Platform;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;

/**
 * transposes the logic of a move
 *
 * @author Lydia Engelhardt, Sophia Kuhlmann, Vincent Schiller, Friederike Weilbeer
 * 2021-06-09
 */
public class Logic implements Runnable {

    private static CoreGame coreGame;
    private Computer computer;
    private Rectangle startField;
    private Controller controller;

    private int gameMode;


    /**
     * initializes gameMode, coreGame, computer, beatenFigureList and conversion
     *
     * @param gameMode         against a local friend (0) or a network game (1) or against the computer (2)
     * @param playerColorBlack the colour you want to play
     * @param controller the controller
     */
    public Logic(int gameMode, boolean playerColorBlack, Controller controller) {
        this.gameMode = gameMode;
        coreGame = new CoreGame();
        this.controller = controller;
        computer = new Computer(!playerColorBlack, this);

        if (gameMode == 2) {
            controller.getRotateBoard().setDisable(true);
            if (playerColorBlack) {
                computerMove();
            }
        }
    }

    /**
     * performs a move if the target position is clicked after the start position was clicked and if the move is allowed
     *
     * @param clickedField the clicked field
     * @param blacksTurn is black on turn?
     */
    public void handleFieldClick(Rectangle clickedField, boolean blacksTurn) {
        if (startField == null && controller.getFigure(clickedField) != null && controller.isImageBlack(controller.getFigure(clickedField)) == blacksTurn) {
            startField = clickedField;
            controller.setMark(startField, true, coreGame.getCurrentBoard());
        } else if (startField != null && controller.getFigure(startField) != null) {
            performMove(getMove(startField, clickedField));
        }
    }

    /**
     * Returns the move from the startField to the targetField
     *
     * @param startField  the start field of the move
     * @param targetField the target field of the move
     * @return move from the startField to the targetField
     */
    private Move getMove(Rectangle startField, Rectangle targetField) {
        Position startPosition = new Position(GridPane.getColumnIndex(startField) - 1, 8 - GridPane.getRowIndex(startField));
        Position targetPosition = new Position(GridPane.getColumnIndex(targetField) - 1, 8 - GridPane.getRowIndex(targetField));
        return new Move(startPosition, targetPosition, controller.getConversionFigure());
    }

    /**
     * performs the move if possible and updates scene
     *
     * @param move the move which should be performed
     */
    private void performMove(Move move) {
        controller.setMark(startField, false, coreGame.getCurrentBoard());
        if (coreGame.chessMove(move)) {
            controller.updateScene(move, coreGame);
            startField = null;
            if (gameMode == 2) {
                computerMove();
            }
        } else if (controller.isSingleSelect() && !controller.getPossibleFields(startField, coreGame.getCurrentBoard()).isEmpty()) {
            controller.setMark(startField, true, coreGame.getCurrentBoard());
        } else {
            startField = null;
        }
    }

    /**
     * starts a computer move
     */
    private void computerMove() {
        computer.makeMove(coreGame.getCurrentBoard());
        controller.setCalculating(true);
    }

    /**
     * Turns the task into thread if the computer thread is terminated
     */
    public void computerIsFinish() {
        Platform.runLater(this);
    }

    /**
     * executes the computer move
     */
    @Override
    public void run() {
        controller.setCalculating(false);
        Move computerMove = computer.getMove();
        coreGame.chessMove(computerMove);
        controller.updateScene(computerMove, coreGame);
    }

}
