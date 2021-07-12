package chess.controller;

import chess.figures.Pawn;
import chess.model.Board;
import chess.model.Move;
import chess.model.Position;
import chess.model.Rules;

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
    private boolean activePlayerBlack = false;
    private boolean gameOver = false;

    private final List<Board> MOVE_HISTORY = new ArrayList<>();

    /**
     * the constructor of CoreGame
     */
    public CoreGame() {
        currentBoard = new Board();
    }

    /**
     * Checks if the move is valid and executes move
     *
     * @param move parsed move the user gave in
     * @return whether the chessMove is valid according to the chess rules
     */
    public boolean chessMove(Move move) {

        //check valid input
        if (move.getACTUAL_POSITION().getPOS_X() == move.getTARGET_POSITION().getPOS_X() && move.getACTUAL_POSITION().getPOS_Y() == move.getTARGET_POSITION().getPOS_Y() || currentBoard.getFigure(move.getACTUAL_POSITION()).isBlack() != activePlayerBlack) {
            //User command fails
            System.out.println("!Move not allowed");
            return false;
        }

        //check pawn move
        if (pawnMove(move)) return true;

        //check king move
        if (kingMove(move)) return true;

        //checkValidDefaultMove
        if (Rules.checkDefaultMove(move.getACTUAL_POSITION(), move.getTARGET_POSITION(), currentBoard)) {
            Rules.performDefaultMove(move.getACTUAL_POSITION(), move.getTARGET_POSITION(), currentBoard);
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
     *
     * @param move consists out of the actual and target position and the ID of the conversion figure
     * @return true if a pawn makes a conversion or an en passant
     */
    private boolean pawnMove(Move move) {
        //check EnPassant
        if (Rules.checkEnPassant(move.getACTUAL_POSITION(), move.getTARGET_POSITION(), currentBoard)) {
            Rules.performEnPassantMove(move.getACTUAL_POSITION(), move.getTARGET_POSITION(), currentBoard);
            updateChanges(move);
            return true;
        }
        //check Pawn conversion
        if (Rules.checkPawnConversion(move.getACTUAL_POSITION(), move.getTARGET_POSITION(), currentBoard)) {
            Rules.performPawnConversion(move.getACTUAL_POSITION(), move.getTARGET_POSITION(), move.getPawnConversionTo(), currentBoard);
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
     *
     * @param move consists out of the actual and target position and the ID of the conversion figure
     * @return true if the king performs castling
     */
    private boolean kingMove(Move move) {
        if (Rules.checkCastling(move.getACTUAL_POSITION(), move.getTARGET_POSITION(), currentBoard)) {
            Rules.performCastlingMove(move.getACTUAL_POSITION(), move.getTARGET_POSITION(), currentBoard);
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

    public void setCurrentBoard(Board newBoard) {
        this.currentBoard = newBoard;
    }

    /*
     * <------System-components---------------------------------------------------------------------------------------->
     */

    /**
     * switch active player
     */
    private void switchPlayer() {
        activePlayerBlack = !activePlayerBlack;
    }

    /**
     * resets EnPassant
     *
     * @param targetPos the target position
     */
    private void resetEnPassant(Position targetPos) {
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                if (x != targetPos.getPOS_X() && y != targetPos.getPOS_Y() && currentBoard.getFigure(x, y) instanceof Pawn) {
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
        resetEnPassant(move.getTARGET_POSITION());
        Board.kingInCheck(currentBoard, activePlayerBlack);
        Board.kingInCheck(currentBoard, !activePlayerBlack);
        if (Board.checkChessAndStaleMate(currentBoard, activePlayerBlack)) {
            if (currentBoard.isCheckMateFlag(false)) System.out.println("Player white is checkmate!");
            if (currentBoard.isCheckMateFlag(true)) System.out.println("Player black is checkmate!");
            if (currentBoard.isStaleMateFlag()) System.out.println("Game ends because stalemate!");
            gameOver = true;
        }
        if (currentBoard.isCheckFlag(false)) System.out.println("Player white is in check!");
        if (currentBoard.isCheckFlag(true)) System.out.println("Player black is in check!");
        MOVE_HISTORY.add(new Board(currentBoard));
    }

    //--------------getter / setter---------------------------------------------------------------------------------------------------------------

    /**
     * Check GameOver
     *
     * @return gameover
     */
    public boolean isGameOver() {
        return gameOver;
    }

    /**
     * Set active player manual. Important for JUnit test
     *
     * @param activePlayer the active player, true if its blacks turn
     */
    public void setActivePlayerBlack(boolean activePlayer) {
        this.activePlayerBlack = activePlayer;
    }

    /**
     * return the active player
     *
     * @return activePlayer, true if its blacks turn
     */
    public boolean isActivePlayerBlack() {
        return activePlayerBlack;
    }

    /**
     * returns the MOVE_HISTORY
     *
     * @return MOVE_HISTORY
     */
    public List<Board> getMoveHistory() {
        return MOVE_HISTORY;
    }

}
