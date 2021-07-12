package chess.gui;

import chess.enums.GameMode;
import chess.model.Board;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages the undo-/-redo-functionality in the gui
 *
 * @author Lydia Engelhardt, Sophia Kuhlmann, Vincent Schiller, Friederike Weilbeer
 * 2021-07-02
 */
public class UndoRedo {

    private final Controller CONTROLLER;
    private int pointer;
    private final List<Text> UNDO_REDO_MOVES_AS_TEXT;
    private final List<Board> UNDO_REDO_MOVES_AS_BOARD;

    /**
     * undo/redo constructor
     *
     * @param pointer    pointer in coreGame's moveHistory to the current board
     * @param CONTROLLER the controller
     */
    public UndoRedo(int pointer, Controller CONTROLLER) {
        this.CONTROLLER = CONTROLLER;
        this.pointer = pointer;
        UNDO_REDO_MOVES_AS_TEXT = new ArrayList<>();
        UNDO_REDO_MOVES_AS_BOARD = new ArrayList<>();
    }

    /**
     * undo function in GUI game
     *
     * @param history the gui element history as gridPane which contains all made moves
     * @param logic   the logic
     */
    public void undo(GridPane history, Logic logic) {
        int loopIndex = 0;
        if (logic.getGameMode() == GameMode.NORMAL || logic.getGameMode() == GameMode.NETWORK) {
            loopIndex = 1;
        } else if (logic.getGameMode() == GameMode.COMPUTER) {
            if (logic.getComputer().isAlive()) {
                logic.getComputer().killComputer();
                loopIndex = 1;
            } else {
                loopIndex = 2;
            }
            if (!logic.getComputer().isBlack() && pointer - loopIndex < 0) {
                loopIndex = 0;
            }
        }
        if (pointer - loopIndex >= -1) {
            for (int i = 0; i < loopIndex; i++) {
                performUndo(history, logic);
            }
        }
    }

    /**
     * unmarks all fields
     */
    private void unmark() {
        for (int i = 0; i < 96; i++) {
            if (CONTROLLER.getBoard().getChildren().get(i) instanceof Rectangle) {
                ((Rectangle) CONTROLLER.getBoard().getChildren().get(i)).setStrokeWidth(0);
            }
        }
    }

    /**
     * redo a move (button clicked)
     *
     * @param history the gui element history as gridPane which contains all made moves
     * @param logic   the logic
     */
    public void redo(GridPane history, Logic logic) {
        int loopIndex = 0;
        if (logic.getGameMode() == GameMode.NORMAL || logic.getGameMode() == GameMode.NETWORK) {
            loopIndex = 1;
        } else if (logic.getGameMode() == GameMode.COMPUTER) {
            loopIndex = 2;
        }
        if (pointer + loopIndex < history.getChildren().size()) {
            for (int i = 0; i < loopIndex; i++) {
                performRedo(history, logic);
            }
        }
    }

    /**
     * resets the undo/redo lists when a normal move is done
     *
     * @param history the gui element history as gridPane which contains all made moves
     * @param logic   the logic
     */
    public void resetUndoRedo(GridPane history, Logic logic) {
        // Texts (for display)
        for (Text move : UNDO_REDO_MOVES_AS_TEXT) {
            history.getChildren().removeIf(node -> node.equals(move));
        }
        UNDO_REDO_MOVES_AS_TEXT.clear();

        // Boards (for the logic)
        for (Board board : UNDO_REDO_MOVES_AS_BOARD) {
            logic.getCoreGame().getMoveHistory().remove(board);
        }
        pointer = logic.getCoreGame().getMoveHistory().size() - 1;
        UNDO_REDO_MOVES_AS_BOARD.clear();
    }

    /**
     * undo / redo a move when the user clicks on a move in the history-panel
     *
     * @param history the gui element history as gridPane which contains all made moves
     * @param logic   the logic
     * @param pointer the pointer
     */
    public void undoRedoClicked(GridPane history, Logic logic, int pointer) {
        // In the game against the computer, only every second move can be clicked
        if (logic.getGameMode() == GameMode.COMPUTER) {
            if (pointer % 2 == 0 && logic.getComputer().isBlack() || pointer % 2 != 0 && !logic.getComputer().isBlack()) {
                return;
            }
            logic.getComputer().killComputer();
        }

        int oldPointer = this.pointer;
        this.pointer = pointer;

        // Select the appropriate board condition
        Board newBoard = pointer >= 0 ? logic.getCoreGame().getMoveHistory().get(pointer) : new Board();

        // add all moves between to the list/ remove all moves between form the list
        if (oldPointer > pointer) {
            pushToList(history, logic);
        } else if (oldPointer < pointer) {
            removeFromList(history, logic, oldPointer);
        }
        logic.getCoreGame().setCurrentBoard(new Board(newBoard));

        // possibly change of players
        logic.getCoreGame().setActivePlayerBlack(pointer % 2 == 0);

        CONTROLLER.getScene().updateScene();
        unmark();
    }

    /**
     * pushes all undo-moves to the lists undoRedoMovesAsBoard and undoRedoMovesAsText
     *
     * @param history the gui element history as gridPane which contains all made moves
     * @param logic   the logic
     */
    private void pushToList(GridPane history, Logic logic) {
        for (int i = pointer + 1; i < history.getRowCount() - 1; i++) {
            // Boards (for the logic)
            UNDO_REDO_MOVES_AS_BOARD.add(logic.getCoreGame().getMoveHistory().get(i));
            // Texts (for the Display)
            Text undoMove = (Text) history.getChildren().get(i);
            undoMove.setOpacity(0.5);
            UNDO_REDO_MOVES_AS_TEXT.add(undoMove);
        }
    }

    /**
     * removes all undo-moves from the lists undoRedoMovesAsBoard and undoRedoMovesAsText
     *
     * @param history    the gui element history as gridPane which contains all made moves
     * @param logic      the logic
     * @param oldPointer the oldPointer
     */
    private void removeFromList(GridPane history, Logic logic, int oldPointer) {
        for (int i = oldPointer + 1; i <= pointer; i++) {
            // Boards (for the logic)
            UNDO_REDO_MOVES_AS_BOARD.remove(logic.getCoreGame().getMoveHistory().get(i));
            // Texts (for the Display)
            Text undoMove = (Text) history.getChildren().get(i);
            undoMove.setOpacity(1);
            UNDO_REDO_MOVES_AS_TEXT.remove(undoMove);
        }
    }

    //--------------Undo / Redo---------------------------------------------------------------------------------------------------------------

    /**
     * Perform the undo action
     *
     * @param history list of all moves
     * @param logic   gui
     */
    private void performUndo(GridPane history, Logic logic) {
        Text undoMove = (Text) history.getChildren().get(pointer);
        undoMove.setOpacity(0.5);
        UNDO_REDO_MOVES_AS_TEXT.add(undoMove);

        UNDO_REDO_MOVES_AS_BOARD.add(logic.getCoreGame().getMoveHistory().get(pointer));

        pointer--;
        Board newBoard;
        if (pointer >= 0) {
            newBoard = logic.getCoreGame().getMoveHistory().get(pointer);
        } else {
            newBoard = new Board();
        }
        logic.getCoreGame().setCurrentBoard(new Board(newBoard));

        // possibly change of players
        logic.getCoreGame().setActivePlayerBlack(pointer % 2 == 0);

        CONTROLLER.getScene().updateScene();
        unmark();
    }

    /**
     * Perform the redo action
     *
     * @param history list of all moves
     * @param logic   gui
     */
    private void performRedo(GridPane history, Logic logic) {
        pointer++;
        Text undoMove = (Text) history.getChildren().get(pointer);
        undoMove.setOpacity(1);
        UNDO_REDO_MOVES_AS_TEXT.remove(UNDO_REDO_MOVES_AS_TEXT.size() - 1);

        Board currentBoard;
        if (pointer >= 0) {
            currentBoard = logic.getCoreGame().getMoveHistory().get(pointer);
        } else {
            currentBoard = logic.getCoreGame().getMoveHistory().get(0);
        }
        UNDO_REDO_MOVES_AS_BOARD.remove(logic.getCoreGame().getMoveHistory().get(pointer));
        logic.getCoreGame().setCurrentBoard(new Board(currentBoard));

        // possibly change of players
        logic.getCoreGame().setActivePlayerBlack(pointer % 2 == 0);

        CONTROLLER.getScene().updateScene();

    }

    //--------------getter / setter---------------------------------------------------------------------------------------------------------------

    public void setPointer(int pointer) {
        this.pointer = pointer;
    }

    public List<Text> getUNDO_REDO_MOVES_AS_TEXT() {
        return UNDO_REDO_MOVES_AS_TEXT;
    }

    public int getPointer() {
        return pointer;
    }
}
