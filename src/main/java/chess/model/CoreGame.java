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
        if (Rules.checkValidDefaultMove(move.getActualPosition(), move.getTargetPosition(), currentBoard)) {
            Rules.performDefaultMove(move.getActualPosition(), move.getTargetPosition(), currentBoard);
            updateChanges(move);
            return true;
        }

        //User command fails
        System.out.println("!Move not allowed");

        //debug
        debug(move);
        return false;
    }

    /*
     * <------Pawn-move------------------------------------------------------------------------------------------------>
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
    private boolean kingMove(Move move) {
        if (Rules.checkCastling(move.getActualPosition(), move.getTargetPosition(), currentBoard)) {
            Rules.performCastlingMove(move.getActualPosition(), move.getTargetPosition(), currentBoard);
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
                    tmpPawn.resetEnPassant();
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

    public boolean isGameOver() {
        return gameOver;
    }

    // zu Testzwecken
    public void setActivePlayer(boolean activePlayer) {
        this.activePlayer = activePlayer;
    }

    //debug
    public void debug(Move move){
        System.out.println("==============");
        System.out.println(" ActualFigure");
        System.out.println("==============");
        switch(currentBoard.getFigure(move.getActualPosition()).getFigureID()){
            case 0: System.out.println("+ Type: None"); break;
            case 1: System.out.println("+ Type: Pawn"); break;
            case 2: System.out.println("+ Type: Rook"); break;
            case 3: System.out.println("+ Type: Knight"); break;
            case 4: System.out.println("+ Type: Bishop"); break;
            case 5: System.out.println("+ Type: Queen"); break;
            case 6: System.out.println("+ Type: King"); break;
        }
        System.out.println("+ AlreadyMove: " + currentBoard.getFigure(move.getActualPosition()).isAlreadyMoved());
        System.out.println("+ Team: " + currentBoard.getFigure(move.getActualPosition()).isBlackTeam());
        System.out.println("+ validMove " + currentBoard.getFigure(move.getActualPosition()).validMove(move.getActualPosition(), move.getTargetPosition(), currentBoard));
        if(currentBoard.getFigure(move.getActualPosition()) instanceof Pawn){
            System.out.println("+ Direction: " + ((Pawn)currentBoard.getFigure(move.getActualPosition())).checkRightDirection(move.getActualPosition(), move.getTargetPosition()));
            System.out.println("+ EnPassant: " + ((Pawn)currentBoard.getFigure(move.getActualPosition())).isEnPassant());
        }
        System.out.println("==============");
        System.out.println(" TargetFigure");
        System.out.println("==============");
        switch(currentBoard.getFigure(move.getTargetPosition()).getFigureID()){
            case 0: System.out.println("+ Type: None"); break;
            case 1: System.out.println("+ Type: Pawn"); break;
            case 2: System.out.println("+ Type: Rook"); break;
            case 3: System.out.println("+ Type: Knight"); break;
            case 4: System.out.println("+ Type: Bishop"); break;
            case 5: System.out.println("+ Type: Queen"); break;
            case 6: System.out.println("+ Type: King"); break;
        }
        System.out.println("+ AlreadyMove: " + currentBoard.getFigure(move.getTargetPosition()).isAlreadyMoved());
        System.out.println("+ Team: " + currentBoard.getFigure(move.getTargetPosition()).isBlackTeam());
        System.out.println("+ validMove " + currentBoard.getFigure(move.getTargetPosition()).validMove(move.getActualPosition(), move.getTargetPosition(), currentBoard));
        if(currentBoard.getFigure(move.getTargetPosition()) instanceof Pawn){
            System.out.println("+ Direction: " + ((Pawn)currentBoard.getFigure(move.getTargetPosition())).checkRightDirection(move.getActualPosition(), move.getTargetPosition()));
            System.out.println("+ EnPassant: " + ((Pawn)currentBoard.getFigure(move.getTargetPosition())).isEnPassant());
        }
        System.out.println("=============");
        System.out.println(" Game stuff");
        System.out.println("=============");
        System.out.println("+ ActivePlayer: " +  activePlayer);
        System.out.println("+ King in check: " + Board.kingInCheck(currentBoard, activePlayer));
    }
}
