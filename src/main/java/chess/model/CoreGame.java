package chess.model;

import java.util.ArrayList;
import java.util.List;

/**
 * This class contains the information about the processes when the player does a chess move.
 * It will detect the correct type of move and execute it and it will also detect when the game is over.
 *
 * @author Lydia Engelhardt, Sophia Kuhlmann, Vincent Schiller, Friederike Weilbeer
 * 2021-05-07
 */
public class CoreGame {

    private Board currentBoard;
    private boolean activePlayer = false;
    private int gameMode = 0;
    private boolean gameOver = false;

    private List<Board> moveHistory = new ArrayList<>();

    /**
     * the constructor of CoreGame
     *
     * @param gameMode mode like local game, network game or a.i game
     */
    public CoreGame(int gameMode) {
        currentBoard = new Board();
        this.gameMode = gameMode;
    }

    /**
     * Checks if the move is valid and executes move
     *
     * @param move parsed move the user gave in
     * @return whether the chessMove is valid according to the chess rules
     */
    public boolean chessMove(Move move) {

        //check valid input
        if (move.getActualPosition().getPosX() == move.getTargetPosition().getPosX() && move.getActualPosition().getPosY() == move.getTargetPosition().getPosY() || currentBoard.getFigure(move.getActualPosition()).isBlackTeam() != activePlayer) {
            //User command fails
            System.out.println("!Move not allowed");
            return false;
        }

        //check pawn move
        if (pawnMove(move)) return true;

        //check king move
        if (kingMove(move)) return true;

        //checkValidDefaultMove
        if (Rules.checkDefaultMove(move.getActualPosition(), move.getTargetPosition(), currentBoard)) {
            Rules.performDefaultMove(move.getActualPosition(), move.getTargetPosition(), currentBoard);
            updateChanges(move);
            return true;
        }

        //User command fails
        System.out.println("!Move not allowed");
        return false;
    }

    /*
     * <------Pawn-move------------------------------------------------------------------------------------------------>
     */

    /**
     * check special move for pawn
     * @param move
     * @return
     */
    private boolean pawnMove(Move move) {
        //check EnPassant
        if (Rules.checkEnPassant(move.getActualPosition(), move.getTargetPosition(), currentBoard)) {
            Rules.performEnPassantMove(move.getActualPosition(), move.getTargetPosition(), currentBoard);
            updateChanges(move);
            return true;
        }
        //check Pawn conversion
        if (Rules.checkPawnConversion(move.getActualPosition(), move.getTargetPosition(), currentBoard)) {
            Rules.performPawnConversion(move.getActualPosition(), move.getTargetPosition(), move.getPawnConversionTo(), currentBoard);
            updateChanges(move);
            return true;
        }
        return false;
    }

    /*
     * <------King-move------------------------------------------------------------------------------------------------>
     */

    /**
     * Check special moves for King
     * @param move
     * @return
     */
    private boolean kingMove(Move move) {
        if (Rules.checkCastling(move.getActualPosition(), move.getTargetPosition(), currentBoard)) {
            Rules.performCastlingMove(move.getActualPosition(), move.getTargetPosition(), currentBoard);
            updateChanges(move);
            return true;
        }
        return false;
    }

    /*
     * <------Default-commands----------------------------------------------------------------------------------------->
     */

    /**
     * return the current board
     *
     * @return Board
     */
    public Board getCurrentBoard() {
        return currentBoard;
    }

    /*
     * <------System-components---------------------------------------------------------------------------------------->
     */

    /**
     * switch active player
     */
    private void switchPlayer() {
        activePlayer = !activePlayer;
    }

    /**
     * resets EnPassant
     *
     * @param targetPos
     */
    private void resetEnPassant(Position targetPos) {
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                if (x != targetPos.getPosX() && y != targetPos.getPosY() && currentBoard.getFigure(x, y) instanceof Pawn) {
                    Pawn tmpPawn = (Pawn) currentBoard.getFigure(x, y);
                    tmpPawn.setEnPassant(false);
                }
            }
        }
    }

    /**
     * Does the standard tasks after each move.
     * (Prints the latest move and adds it to the history, switches the actual player and proofs if the game is finished)
     *
     * @param move the actual chess move
     */
    private void updateChanges(Move move) {
        System.out.println("!" + move.toString());
        switchPlayer();
        resetEnPassant(move.getTargetPosition());
        moveHistory.add(new Board(currentBoard));
        if (Board.checkChessMate(currentBoard, activePlayer)) {
            gameOver = true;
        }
    }

    /**
     * Check GameOver
     * @return gameover
     */
    public boolean isGameOver() {
        return gameOver;
    }

    /**
     * Set active player manual. Important for JUnit test
     * @param activePlayer
     */
    public void setActivePlayer(boolean activePlayer) {
        this.activePlayer = activePlayer;
    }
}
