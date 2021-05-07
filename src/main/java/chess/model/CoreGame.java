package chess.model;

import java.util.ArrayList;
import java.util.List;

/**
 * This class contains the information about the processes when the player does a chess move.
 * It will detect the correct type of move and execute it and it will also detect when the game is over.
 *
 * @author Lydia Engelhardt, Sophia Kuhlmann, Vincent Schiller, Friederike Weilbeer
 * 2021-05-07
 *
 */
public class CoreGame {

    private Board currentBoard;
    private int activePlayer = 0;
    private int gameMode = 0;
    private boolean gameOver = false;

    private ArrayList<Board> moveHistory = new ArrayList<>();

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

        Position actualPos = move.getActualPosition();
        Position targetPos = move.getTargetPosition();
        int pawnConversion = move.getPawnConversionTo();

        if(actualPos.getPosX() == targetPos.getPosX() && actualPos.getPosY() == targetPos.getPosY()){
            //User command fails
            System.out.println("!Move not allowed");
            return false;
        }

        if (currentBoard.getFigure(actualPos).getTeam() == activePlayer) {
            //check EnPassant
            if (Rules.checkEnPassant(actualPos, targetPos, currentBoard)) {
                Rules.performEnPassantMove(actualPos, targetPos, currentBoard);
                System.out.println("EnPassant");
                updateChanges(move);
                return true;
            }
            //check Castling
            if (Rules.checkCastling(actualPos, targetPos, currentBoard) == 1) {
                Rules.performCastlingMoveLeft(actualPos, targetPos, currentBoard);
                currentBoard.getFigure(0, actualPos.getPosX()).setAlreadyMoved(true);         // muss hier aufgerufen werden, da sonst auch bei der Überprüfung von Schachmatt ggf. die Figur auf
                currentBoard.getFigure(actualPos).setAlreadyMoved(true);         // setAlreadyMoved=true gesetzt wird (da Figure unabhängig von board bzw. tmpBoard)
                System.out.println("Castling left");
                updateChanges(move);
                return true;
            }
            if (Rules.checkCastling(actualPos, targetPos, currentBoard) == 2) {
                Rules.performCastlingMoveRight(actualPos, targetPos, currentBoard);
                currentBoard.getFigure(7, actualPos.getPosY()).setAlreadyMoved(true);         // muss hier aufgerufen werden, da sonst auch bei der Überprüfung von Schachmatt ggf. die Figur auf
                currentBoard.getFigure(actualPos).setAlreadyMoved(true);         // setAlreadyMoved=true gesetzt wird (da Figure unabhängig von board bzw. tmpBoard)
                System.out.println("Castling right");
                updateChanges(move);
                return true;
            }
            //check Pawn conversion
            if (Rules.checkPawnConversion(actualPos, targetPos, currentBoard)) {
                Rules.performPawnConversion(actualPos, targetPos, pawnConversion, currentBoard);
                currentBoard.getFigure(actualPos).setAlreadyMoved(true);          // muss hier aufgerufen werden, da sonst auch bei der Überprüfung von Schachmatt ggf. die Figur auf setAlreadyMoved=true gesetzt wird (da Figure unabhängig von board bzw. tmpBoard)
                System.out.println("PawnConversion");
                updateChanges(move);
                return true;
            }
            //checkValidDefaultMove
            if (Rules.checkValidDefaultMove(actualPos, targetPos, currentBoard)) {
                Rules.performDefaultMove(actualPos, targetPos, currentBoard);
                System.out.println("Default move");
                updateChanges(move);
                return true;
            }
        }

        //User command fails
        System.out.println("!Move not allowed");
        return false;
    }

    /*
     * <------Default-commands------------------------------------------------------------------------------------------>
     */

    public Board getCurrentBoard() {
        return currentBoard;
    }

    // doppelt
    public List<Figure> getBeatenFigures() {
        return currentBoard.getBeatenFigures();
    }

    /*
     * <------System-components--------------------------------------------------------------------------------------------->
     */

    /**
     * switch active player
     */
    private void switchPlayer() {
        activePlayer = activePlayer == 0 ? 1 : 0;
    }

    /**
     * resets EnPassant
     * @param newX
     * @param newY
     */
    private void resetEnPassant(int newX, int newY) {
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                if (x != newX && y != newY) {
                    if (currentBoard.getFigure(x, y) instanceof Pawn) {
                        ((Pawn) currentBoard.getFigure(x, y)).resetEnPassant();
                    }
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
        move.toString();
        switchPlayer();
        resetEnPassant(move.getTargetPosition().getPosX(), move.getTargetPosition().getPosY());
        moveHistory.add(new Board(currentBoard));
        if (Board.checkChessMate(currentBoard, activePlayer)) {
            gameOver = true;
        }
    }

    public boolean isGameOver() {
        return gameOver;
    }

    /*public String getPawnLetter(int pawnConversion){
        switch(pawnConversion){
            case 1: return "P";
            case 2: return "R";
            case 3: return "N";
            case 4: return "B";
            case 5: return "Q";
            case 6: return "K";
        }
        return "";
    }*/

    // zu Testzwecken
    public void setActivePlayer(int activePlayer) {
        this.activePlayer = activePlayer;
    }
}
