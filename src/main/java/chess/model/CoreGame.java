package chess.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CoreGame {

    private Board board;
    private int activePlayer = 0;
    private int gameMode = 0;
    private ArrayList<Figure> beatenFigures = new ArrayList<Figure>();

    //Chess Events
    boolean enPassant = false;

    public CoreGame(int gameMode) {
        board = new Board();
        this.gameMode = gameMode;
    }

    public Board getBoard() {
        return board;
    }

    /**
     * Checks valid move and passes move order to figure
     * move.get(0) == posX
     * move.get(1) == posY
     * move.get(2) == newPosX
     * move.get(3) == newPosY
     *
     * @param move parsed move the user gave in
     * @return true if valid move
     */
    public boolean chessMove(Map<String, Integer> move) {
        //translate user input to position
//        Map<String, Integer> move = parse(in);

        //check valid move
        if (move.size() >= 4) {

            Integer posX = move.get("posX");
            Integer posY = move.get("posY");
            Integer newX = move.get("newX");
            Integer newY = move.get("newY");
            Integer pawnConversion = move.get("convertPawnTo");

            if (board.getFigure(posX, posY).getTeam() == activePlayer) {
                //check EnPassant
                if (checkEnPassant(posX, posY, newX, newY, board)) {
                    performEnPassantMove(posX, posY, newX, newY, board);
                    switchPlayer();
                    resetEnPassant(newX, newY);
                    checkChessMate(activePlayer);
                    System.out.println("EnPassant");
                    return true;
                }
                //check Castling
                if (checkCastling(posX, posY, newX, newY, board) == 1) {
                    performCastlingMoveLeft(posX, posY, newX, newY, board);
                    switchPlayer();
                    resetEnPassant(newX, newY);
                    checkChessMate(activePlayer);
                    System.out.println("Castling left");
                    return true;
                }
                if (checkCastling(posX, posY, newX, newY, board) == 2) {
                    performCastlingMoveRight(posX, posY, newX, newY, board);

                    switchPlayer();
                    resetEnPassant(newX, newY);
                    checkChessMate(activePlayer);
                    System.out.println("Castling right");
                    return true;
                }
                //check Pawn conversion
                if (checkPawnConversion(posX, posY, newX, newY, board)) {
                    performPawnConversion(posX, posY, newX, newY, pawnConversion, board);
                    switchPlayer();
                    resetEnPassant(newX, newY);
                    checkChessMate(activePlayer);
                    System.out.println("PawnConversion");
                    return true;
                }
                //checkValidDefaultMove
                if (checkValidDefaultMove(posX, posY, newX, newY, board)) {
                    performDefaultMove(posX, posY, newX, newY, board);
                    switchPlayer();
                    resetEnPassant(newX, newY);
                    checkChessMate(activePlayer);
                    System.out.println("Default move");
                    return true;
                }
            }
        }

        //User command fails
        return false;
    }

    /**
     * <------DefaultMove----------------------------------------------------------------------------------------------->
     */

    /**
     * checks whether a standard move is valid or not
     *
     * @param posX, posY, newX, newY
     * @param board
     * @return Whether move is possible or not
     */
    public boolean checkValidDefaultMove(int posX, int posY, int newX, int newY, Board board) {

        Figure actualFigure = board.getFigure(posX, posY);
        Figure targetFigure = board.getFigure(newX, newY);

        if (actualFigure.validMove(posX, posY, newX, newY, board)) {
            //create a tmpBoard with the new untested figure position
            Board tmpBoard = board.copyBoard();
            //perform the Figure move on a temporary board. IMPORTANT this move is untested and can be illegal
            tmpBoard.setFigure(posX, posY, new None());
            tmpBoard.setFigure(newX, newY, actualFigure);

            if (!kingInCheck(tmpBoard, actualFigure.getTeam()) && actualFigure.getTeam() != targetFigure.getTeam()) {
                return true;
            }
        }
        return false;
    }

    /**
     * makes a standard move on the board.
     *
     * @param posX, posY, newX, newY
     * @param board
     */
    public void performDefaultMove(int posX, int posY, int newX, int newY, Board board) {

        Figure actualFigure = board.getFigure(posX, posY);
        Figure targetFigure = board.getFigure(newX, newY);

        if (!(targetFigure instanceof None)) {
            beatenFigures.add(targetFigure);
        }

        //Update Board
        board.setFigure(newX, newY, actualFigure);
        board.setFigure(posX, posY, new None());

        //Set Figure to AlreadyMoved
        board.getFigure(newX, newY).setAlreadyMoved(true);
    }

    /**
     * <------EnPassant------------------------------------------------------------------------------------------------->
     */

    /**
     * check valid enPassant move
     *
     * @param posX  actual x-position for Pawn
     * @param posY  actual y-position for Pawn
     * @param newX  new input x-position for Pawn
     * @param newY  new input y-position for Pawn
     * @param board
     * @return Whether the move is valid or not
     */
    public boolean checkEnPassant(int posX, int posY, int newX, int newY, Board board) {
        if ((board.getFigure(newX, posY) instanceof Pawn) && (board.getFigure(posX, posY) instanceof Pawn)
                && (board.getFigure(newX, posY).getTeam() != board.getFigure(posX, posY).getTeam())
                && (Math.abs(posX - newX) == 1)
                && ((board.getFigure(posX, posY).getTeam() == 0 && newY - posY == 1)
                || (board.getFigure(posX, posY).getTeam() == 1 && newY - posY == -1))) {
            if (((Pawn) board.getFigure(newX, posY)).isEnPassant()) {
                return true;
            }
        }
        return false;
    }

    /**
     * makes a enPassant move on the board.
     *
     * @param posX  actual x-position for Pawn
     * @param posY  actual y-position for Pawn
     * @param newX  new input x-position for Pawn
     * @param newY  new input y-position for Pawn
     * @param board
     */
    public void performEnPassantMove(int posX, int posY, int newX, int newY, Board board) {
        beatenFigures.add(board.getFigure(newX, posY));
        board.setFigure(newX, posY, new None());
        board.setFigure(newX, newY, board.getFigure(posX, posY));
        board.setFigure(posX, posY, new None());
    }

    /**
     * <------Castling-------------------------------------------------------------------------------------------------->
     */

    /**
     * check possible castling
     *
     * @param posX, posY, newX, newY
     * @param board
     * @return W
     */
    public int checkCastling(int posX, int posY, int newX, int newY, Board board) {

        Figure actualFigure = board.getFigure(posX, posY);

        // castle left
        if (actualFigure instanceof King && !(actualFigure.isAlreadyMoved()) && newX == posX - 2 && !(board.getFigure(0, posY).isAlreadyMoved())) {
            // check, whether all field between are empty and are not threatened
            for (int j = 1; j < posX; j++) {
                if (!(board.getFigure(j, posY) instanceof None) || isThreatened(board, actualFigure.getTeam(), j, posY)) {
                    return 0;
                }
            }
            return 1;
        }
        // castle right
        if (actualFigure instanceof King && !(actualFigure.isAlreadyMoved()) && newX == posX + 2 && !(board.getFigure(7, posY).isAlreadyMoved())) {
            // check, whether all field between are empty and are not threatened
            for (int j = posX + 1; j < 7; j++) {
                if (!(board.getFigure(j, posY) instanceof None) || isThreatened(board, actualFigure.getTeam(), j, posY)) {
                    return 0;
                }
            }
            return 2;
        }

        return 0;
    }

    public void performCastlingMoveLeft(int posX, int posY, int newX, int newY, Board board) {

        Figure actualFigure = board.getFigure(posX, posY);
        Figure targetFigure = board.getFigure(newX, newY);

        board.setFigure(newX + 1, newY, board.getFigure(0, posY));    // move rook
        board.setFigure(0, newY, new None());                            // replace field where rook was standing

        board.setFigure(newX, newY, actualFigure);                          // move king
        board.setFigure(posX, posY, targetFigure);                          // replace field where king was standing

        board.getFigure(0, posY).setAlreadyMoved(true);                 //Update AlreadyMoved
        board.getFigure(posX, posY).setAlreadyMoved(true);                 //Update AlreadyMoved
    }

    public void performCastlingMoveRight(int posX, int posY, int newX, int newY, Board board) {

        Figure actualFigure = board.getFigure(posX, posY);
        Figure targetFigure = board.getFigure(newX, newY);

        board.setFigure(newX - 1, newY, board.getFigure(7, posY));      // move rook
        board.setFigure(7, newY, new None());                              // replace field where rook was standing

        board.setFigure(newX, newY, actualFigure);                            // move king
        board.setFigure(posX, posY, targetFigure);                            // replace field where king was standing

        board.getFigure(7, posY).setAlreadyMoved(true);                    //Update AlreadyMoved
        board.getFigure(posX, posY).setAlreadyMoved(true);                    //Update AlreadyMoved
    }

    /**
     * <------Pawn-conversion------------------------------------------------------------------------------------------->
     */

    /**
     * check possible pawn conversion
     *
     * @param posX, posY, newX, newY
     * @return Whether the move is valid or not
     */
    public boolean checkPawnConversion(int posX, int posY, int newX, int newY, Board board) {
        Figure actualFigure = board.getFigure(posX, posY);

        //Check
        if(actualFigure instanceof Pawn){
            if(actualFigure.validMove(posX, posY, newX, newY, board)){
                if((newY == 7 && actualFigure.getTeam() == 0) || (newY == 0 && actualFigure.getTeam() == 1)){
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * makes a pawn conversion move on the board.
     *
     * @param posX, posY, newX, newY, figureID
     */
    public void performPawnConversion(int posX, int posY, int newX, int newY, int figureID, Board board) {
        Figure actualFigure = board.getFigure(posX, posY);

        //convert white pawn
        if (newY == 7 && actualFigure instanceof Pawn && actualFigure.getTeam() == 0) {
            //to knight
            switch (figureID) {
                case 1: {
                    board.setFigure(newX, newY, new Knight(0));
                    break;
                }
                case 2: {
                    board.setFigure(newX, newY, new Bishop(0));
                    break;
                }
                default: {
                    board.setFigure(newX, newY, new Queen(0));
                    break;
                }
            }
        }

        //convert black pawn
        if (newY == 0 && actualFigure instanceof Pawn && actualFigure.getTeam() == 1) {
            switch (figureID) {
                case 1: {
                    board.setFigure(newX, newY, new Knight(1));
                    break;
                }
                case 2: {
                    board.setFigure(newX, newY, new Bishop(1));
                    break;
                }
                default: {
                    board.setFigure(newX, newY, new Queen(1));
                    break;
                }
            }
        }

        board.getFigure(newX, newY).setAlreadyMoved(true);
        board.setFigure(posX, posY, new None());
    }


    /**
     * <------Default-commands------------------------------------------------------------------------------------------>
     */

    /**
     * Check if the king is in check
     *
     * @param board A board for unchecked figure positions.
     * @param team  The team ID of the checked King
     * @return Whether the king is in check or not
     */
    public boolean kingInCheck(Board board, int team) {
        int[] kingPos = board.getKing(team);
        return isThreatened(board, team, kingPos[0], kingPos[1]);
    }

    /**
     * Check chessMate
     *
     * @param team Target king
     * @return whether king of "team"-color is in checkmate
     */
    public boolean checkChessMate(int team) {
        boolean possibleSolution = false;

        // test all possible moves and check whether your king is still in check
        for (int y = 0; y < 8; y++) {                                                   // for all your own figures on the board
            for (int x = 0; x < 8; x++) {
                Figure actualFigure = board.getFigure(x, y);
                if (actualFigure.getTeam() == team) {
                    Board tmpBoard = board.copyBoard();                                 // create a temporary board

                    for (int newX = 0; newX < 8; newX++) {                              // and test all possible moves to every possible targetField
                        for (int newY = 0; newY < 8; newY++) {

                            if (checkEnPassant(x, y, newX, newY, tmpBoard)) {           // check EnPassant and eventually perform it on the temporary board
                                performEnPassantMove(x, y, newX, newY, tmpBoard);
                                if (!kingInCheck(tmpBoard, team)) {                         // and at least check whether the king would still be in check after every possible move
                                    possibleSolution = true;
                                }
                            }
                            if (checkCastling(x, y, newX, newY, tmpBoard)==1) {            // check Castling and eventually perform it on the temporary board
                                performCastlingMoveLeft(x, y, newX, newY, tmpBoard);
                                if (!kingInCheck(tmpBoard, team)) {                         // and at least check whether the king would still be in check after every possible move
                                    possibleSolution = true;
                                }
                            }
                            if (checkCastling(x, y, newX, newY, tmpBoard)==2) {            // check Castling and eventually perform it on the temporary board
                                performCastlingMoveRight(x, y, newX, newY, tmpBoard);
                                if (!kingInCheck(tmpBoard, team)) {                         // and at least check whether the king would still be in check after every possible move
                                    possibleSolution = true;
                                }
                            }
                            if (checkValidDefaultMove(x, y, newX, newY, tmpBoard)) {    // checkValidDefaultMove and eventually perform it on the temporary board
                                performDefaultMove(x, y, newX, newY, tmpBoard);
                                if (!kingInCheck(tmpBoard, team)) {                         // and at least check whether the king would still be in check after every possible move
                                    possibleSolution = true;
                                }
                            }
                        }
                    }
                }
            }
        }
        return !possibleSolution;
    }


    /**
     * Checks if the figure is threatened by the other team
     *
     * @param board      actual board
     * @param team       team of the figure you want to check
     * @param targetPosX x-position of the figure you want to check
     * @param targetPosY y-position of the figure you want to check
     * @return whether the figure is threatened by the other team
     */
    public boolean isThreatened(Board board, int team, int targetPosX, int targetPosY) {
        //Check if any enemy figure can do a valid move to target Position
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                Board tmpBoard = board.copyBoard();
                if (tmpBoard.getFigure(x, y).getTeam() != team && tmpBoard.getFigure(x, y).validMove(x, y, targetPosX, targetPosY, tmpBoard)) {
                    return true;
                }
            }
        }
        return false;
    }





/**
 * <------System-components--------------------------------------------------------------------------------------------->
 */

    /**
     * switch active player
     */
    public void switchPlayer() {
        activePlayer = activePlayer == 0 ? 1 : 0;
    }

    /**
     *  reset EnPassant
     */
    public void resetEnPassant(int newX, int newY){
        for(int y = 0; y < 8; y++){
            for(int x = 0; x < 8; x++){
                if(x != newX && y != newY){
                    if(board.getFigure(x, y) instanceof Pawn){
                        ((Pawn) board.getFigure(x, y)).resetEnPassant();
                    }
                }
            }
        }
    }
}
