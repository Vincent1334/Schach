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

    private final Controller controller;
    private int pointer;
    private final List<Text> undoRedoMovesAsText;
    private final List<Board> undoRedoMovesAsBoard;

    /**
     * undo/redo constructor
     *
     * @param pointer    pointer in coreGame's moveHistory to the current board
     * @param controller the controller
     */
    public UndoRedo(int pointer, Controller controller) {
        this.controller = controller;
        this.pointer = pointer;
        undoRedoMovesAsText = new ArrayList<>();
        undoRedoMovesAsBoard = new ArrayList<>();
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
            undoRedoMovesAsText.add(undoMove);

            undoRedoMovesAsBoard.add(logic.getCoreGame().getMoveHistory().get(pointer));

            // im Computermodus jeweils zwei Züge zurück gehen
            if (logic.getGameMode() == GameMode.COMPUTER && pointer > 0) {
                Text undoMove2 = (Text) history.getChildren().get(pointer - 1);
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
            logic.getCoreGame().setActivePlayer(pointer % 2 == 0);

            controller.updateScene();
            unmark();
        }
    }

    /**
     * unmarks all fields
     */
    private void unmark() {
        for (int i = 0; i < 96; i++) {
            if (controller.getBoard().getChildren().get(i) instanceof Rectangle) {
                ((Rectangle) controller.getBoard().getChildren().get(i)).setStrokeWidth(0);
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
        if (undoRedoMovesAsText.size() > 0) {
            pointer++;
            if (history.getChildren().size() != pointer) {
                Text undoMove = (Text) history.getChildren().get(pointer);
                undoMove.setOpacity(1);
                undoRedoMovesAsText.remove(undoRedoMovesAsText.size() - 1);

                // im Computermodus werden jeweils zwei Züge wiederhergestellt
                if (logic.getGameMode() == GameMode.COMPUTER) {
                    Text undoMove2 = (Text) history.getChildren().get(pointer + 1);
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
                logic.getCoreGame().setActivePlayer(pointer % 2 == 0);

                controller.updateScene();
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
        // Texte (für Anzeige)
        for (Text move : undoRedoMovesAsText) {
            history.getChildren().removeIf(node -> node.equals(move));
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
     * undo / redo a move when the user clicks on a move in the history-panel
     *
     * @param history     the gui element history as gridPane which contains all made moves
     * @param logic       the logic
     * @param clickedText the move that was clicked
     */
    public void undoRedoClicked(GridPane history, Logic logic, Text clickedText) {
        int oldPointer = pointer;

        // auf welchen Zug wurde geklickt?
        for (int i = 0; i < history.getRowCount() - 1; i++) {
            if (history.getChildren().get(i).equals(clickedText)) {
                pointer = i;
            }
        }
        // im Spiel gegen den Computer ist nur jeder zweite Zug anklickbar
        if (logic.getGameMode() == GameMode.COMPUTER && pointer % 2 == 0) {
            return;
        }
        // Wähle entsprechenden BoardZustand
        Board newBoard = pointer >= 0 ? logic.getCoreGame().getMoveHistory().get(pointer) : new Board();

        // pack alle Züge dazwischen auf eine Liste / entferne alle Züge dazwischen von Liste
        if (oldPointer > pointer) {
            pushToList(history, logic);
        } else if (oldPointer < pointer) {
            removeFromList(history, logic, oldPointer);
        }
        logic.getCoreGame().setCurrentBoard(new Board(newBoard));

        // ggf. Spielerwechsel
        logic.getCoreGame().setActivePlayer(pointer % 2 == 0);

        controller.updateScene();
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
            // Boards (für Logik)
            undoRedoMovesAsBoard.add(logic.getCoreGame().getMoveHistory().get(i));
            // Texte (für Anzeige)
            Text undoMove = (Text) history.getChildren().get(i);
            undoMove.setOpacity(0.5);
            undoRedoMovesAsText.add(undoMove);
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
            // boards (für Logik)
            undoRedoMovesAsBoard.remove(logic.getCoreGame().getMoveHistory().get(i));
            // texte (für Anzeige)
            Text undoMove = (Text) history.getChildren().get(i);
            undoMove.setOpacity(1);
            undoRedoMovesAsText.remove(undoMove);
        }
    }

    //--------------getter / setter---------------------------------------------------------------------------------------------------------------

    public void setPointer(int pointer) {
        this.pointer = pointer;
    }

    public List<Text> getUndoRedoMovesAsText() {
        return undoRedoMovesAsText;
    }

}
