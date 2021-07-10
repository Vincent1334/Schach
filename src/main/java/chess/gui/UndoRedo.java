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
        if (pointer >= 0) {
            Text undoMove = (Text) history.getChildren().get(pointer);
            undoMove.setOpacity(0.5);
            UNDO_REDO_MOVES_AS_TEXT.add(undoMove);

            UNDO_REDO_MOVES_AS_BOARD.add(logic.getCoreGame().getMoveHistory().get(pointer));

            // in computermode always go back two moves
            if (logic.getGameMode() == GameMode.COMPUTER && pointer > 0) {
                Text undoMove2 = (Text) history.getChildren().get(pointer - 1);
                undoMove2.setOpacity(0.5);
                UNDO_REDO_MOVES_AS_TEXT.add(undoMove2);

                UNDO_REDO_MOVES_AS_BOARD.add(logic.getCoreGame().getMoveHistory().get(pointer - 1));
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

            // possibly change of players
            logic.getCoreGame().setActivePlayerBlack(pointer % 2 == 0);

            CONTROLLER.updateScene();
            unmark();
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
        if (UNDO_REDO_MOVES_AS_TEXT.size() > 0) {
            pointer++;
            if (history.getChildren().size() != pointer) {
                Text undoMove = (Text) history.getChildren().get(pointer);
                undoMove.setOpacity(1);
                UNDO_REDO_MOVES_AS_TEXT.remove(UNDO_REDO_MOVES_AS_TEXT.size() - 1);

                // in computermode always two moves are restored
                if (logic.getGameMode() == GameMode.COMPUTER) {
                    Text undoMove2 = (Text) history.getChildren().get(pointer + 1);
                    undoMove2.setOpacity(1);
                    UNDO_REDO_MOVES_AS_TEXT.remove(UNDO_REDO_MOVES_AS_TEXT.size() - 1);

                    pointer++;
                    UNDO_REDO_MOVES_AS_BOARD.remove(logic.getCoreGame().getMoveHistory().get(pointer - 1));
                }

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

                CONTROLLER.updateScene();
            } else {
                pointer--;
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
     * @param history     the gui element history as gridPane which contains all made moves
     * @param logic       the logic
     * @param pointer     the pointer
     */
    public void undoRedoClicked(GridPane history, Logic logic, int pointer) {
        int oldPointer = this.pointer;
        this.pointer = pointer;

        // In the game against the computer, only every second move can be clicked
        if (logic.getGameMode() == GameMode.COMPUTER && pointer % 2 == 0) {
            return;
        }
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

        CONTROLLER.updateScene();
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

    //--------------getter / setter---------------------------------------------------------------------------------------------------------------

    public void setPointer(int pointer) {
        this.pointer = pointer;
    }

    public List<Text> getUNDO_REDO_MOVES_AS_TEXT() {
        return UNDO_REDO_MOVES_AS_TEXT;
    }

}
