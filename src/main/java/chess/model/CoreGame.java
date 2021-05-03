package chess.model;

import java.util.ArrayList;
import java.util.Map;

public class CoreGame {

    private Board board;
    private int activePlayer = 0;
    private int gameMode = 0;
    private ArrayList<Figure> beatenFigures = new ArrayList<Figure>();
    private ArrayList<String> moveHistory = new ArrayList<String>();

    public CoreGame(int gameMode) {
        board = new Board();
        this.gameMode = gameMode;
    }

    /**
     * Checks if the move is valid and executes move
     *
     * @param move parsed move the user gave in
     * @return whether the chessMove is valid according to the chess rules
     */
    public boolean chessMove(Map<String, Integer> move) {

        Integer posX = move.get("posX");
        Integer posY = move.get("posY");
        Integer newX = move.get("newX");
        Integer newY = move.get("newY");
        Integer pawnConversion = move.get("convertPawnTo");

        //BackUp for Documentation file
        Figure actualFigure = board.getFigure(posX, posY);
        Figure targetFigure = board.getFigure(newX, newY);

        if (board.getFigure(posX, posY).getTeam() == activePlayer) {
            //check EnPassant
            if (checkEnPassant(posX, posY, newX, newY, board)) {
                performEnPassantMove(posX, posY, newX, newY, board);
                System.out.println("EnPassant");
                updateChanges(posX, posY, newX, newY, pawnConversion, actualFigure, targetFigure);
                return checkChessMate(activePlayer);
            }
            //check Castling
            if (checkCastling(posX, posY, newX, newY, board) == 1) {
                performCastlingMoveLeft(posX, posY, newX, newY, board);
                board.getFigure(0, posY).setAlreadyMoved(true);         // muss hier aufgerufen werden, da sonst auch bei der Überprüfung von Schachmatt ggf. die Figur auf
                board.getFigure(posX, posY).setAlreadyMoved(true);         // setAlreadyMoved=true gesetzt wird (da Figure unabhängig von board bzw. tmpBoard)
                System.out.println("Castling left");
                updateChanges(posX, posY, newX, newY, pawnConversion, actualFigure, targetFigure);
                return checkChessMate(activePlayer);
            }
            if (checkCastling(posX, posY, newX, newY, board) == 2) {
                performCastlingMoveRight(posX, posY, newX, newY, board);
                board.getFigure(7, posY).setAlreadyMoved(true);         // muss hier aufgerufen werden, da sonst auch bei der Überprüfung von Schachmatt ggf. die Figur auf
                board.getFigure(posX, posY).setAlreadyMoved(true);         // setAlreadyMoved=true gesetzt wird (da Figure unabhängig von board bzw. tmpBoard)
                System.out.println("Castling right");
                updateChanges(posX, posY, newX, newY, pawnConversion, actualFigure, targetFigure);
                return checkChessMate(activePlayer);
            }
            //check Pawn conversion
            if (checkPawnConversion(posX, posY, newX, newY, board)) {
                performPawnConversion(posX, posY, newX, newY, pawnConversion, board);
                board.getFigure(posX, posY).setAlreadyMoved(true);          // muss hier aufgerufen werden, da sonst auch bei der Überprüfung von Schachmatt ggf. die Figur auf setAlreadyMoved=true gesetzt wird (da Figure unabhängig von board bzw. tmpBoard)
                System.out.println("PawnConversion");
                updateChanges(posX, posY, newX, newY, pawnConversion, actualFigure, targetFigure);
                return checkChessMate(activePlayer);
            }
            //checkValidDefaultMove
            if (checkValidDefaultMove(posX, posY, newX, newY, board)) {
                performDefaultMove(posX, posY, newX, newY, board);
                System.out.println("Default move");
                updateChanges(posX, posY, newX, newY, pawnConversion, actualFigure, targetFigure);
                return checkChessMate(activePlayer);
            }
        }

        //User command fails
        System.out.println("!Move not allowed");
        return false;
    }

    /*
     * <------DefaultMove----------------------------------------------------------------------------------------------->
     */

    /**
     * checks whether a standard move is valid or not
     *
     * @param posX  current x-position of the figure you want to move
     * @param posY  current y-position of the figure you want to move
     * @param newX  target x-position of the figure you want to move
     * @param newY  target y-position of the figure you want to move
     * @param board the current chessboard
     * @return whether normal move is possible
     */
    private boolean checkValidDefaultMove(int posX, int posY, int newX, int newY, Board board) {

        Figure actualFigure = board.getFigure(posX, posY);
        Figure targetFigure = board.getFigure(newX, newY);

        if (actualFigure.validMove(posX, posY, newX, newY, board)) {
            //create a tmpBoard with the new untested figure position
            Board tmpBoard = board.copyBoard();
            //perform the Figure move on a temporary board. IMPORTANT this move is untested and can be illegal
            tmpBoard.setFigure(posX, posY, new None());
            tmpBoard.setFigure(newX, newY, actualFigure);

            return !kingInCheck(tmpBoard, actualFigure.getTeam()) && actualFigure.getTeam() != targetFigure.getTeam();
        }
        return false;
    }

    /**
     * executes a standard move on the board
     *
     * @param posX  current x-position of the figure you want to move
     * @param posY  current y-position of the figure you want to move
     * @param newX  target x-position of the figure you want to move
     * @param newY  target y-position of the figure you want to move
     * @param board the current chessboard
     */
    private void performDefaultMove(int posX, int posY, int newX, int newY, Board board) {

        Figure actualFigure = board.getFigure(posX, posY);
        Figure targetFigure = board.getFigure(newX, newY);

        if (!(targetFigure instanceof None)) {
            beatenFigures.add(targetFigure);
        }

        //Update Board
        board.setFigure(newX, newY, actualFigure);
        board.setFigure(posX, posY, new None());

        //Set Figure to AlreadyMoved
        //board.getFigure(newX, newY).setAlreadyMoved(true);        kann hier nicht aufgerufen werden, da sonst Fehler bei schachMattPrüfung (siehe ChessMove())
    }

    /*
     * <------EnPassant------------------------------------------------------------------------------------------------->
     */

    /**
     * checks valid enPassant move
     *
     * @param posX  current x-position of the figure you want to move
     * @param posY  current y-position of the figure you want to move
     * @param newX  target x-position of the figure you want to move
     * @param newY  target y-position of the figure you want to move
     * @param board the current chessboard
     * @return Whether enPassant is possible
     */
    private boolean checkEnPassant(int posX, int posY, int newX, int newY, Board board) {
        if ((board.getFigure(newX, posY) instanceof Pawn) && (board.getFigure(posX, posY) instanceof Pawn)
                && (board.getFigure(newX, posY).getTeam() != board.getFigure(posX, posY).getTeam())
                && (Math.abs(posX - newX) == 1)
                && ((board.getFigure(posX, posY).getTeam() == 0 && newY - posY == 1)
                || (board.getFigure(posX, posY).getTeam() == 1 && newY - posY == -1))) {
            return ((Pawn) board.getFigure(newX, posY)).isEnPassant();
        }
        return false;
    }

    /**
     * executes an enPassant move on the board
     *
     * @param posX  current x-position of the figure you want to move
     * @param posY  current y-position of the figure you want to move
     * @param newX  target x-position of the figure you want to move
     * @param newY  target y-position of the figure you want to move
     * @param board the current chessboard
     */
    private void performEnPassantMove(int posX, int posY, int newX, int newY, Board board) {
        beatenFigures.add(board.getFigure(newX, posY));
        board.setFigure(newX, posY, new None());
        board.setFigure(newX, newY, board.getFigure(posX, posY));
        board.setFigure(posX, posY, new None());
    }

    /*
     * <------Castling-------------------------------------------------------------------------------------------------->
     */

    /**
     * checks valid castling move
     *
     * @param posX  current x-position of the figure you want to move
     * @param posY  current y-position of the figure you want to move
     * @param newX  target x-position of the figure you want to move
     * @param newY  target y-position of the figure you want to move
     * @param board the current chessboard
     * @return 0 if castling is not possible, 1 if a queenside castling is possible, 2 if a kingside castling is possible
     */
    private int checkCastling(int posX, int posY, int newX, int newY, Board board) {

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

    /**
     * executes a queenside (left) castling move on the board
     *
     * @param posX  current x-position of the figure you want to move
     * @param posY  current y-position of the figure you want to move
     * @param newX  target x-position of the figure you want to move
     * @param newY  target y-position of the figure you want to move
     * @param board the current chessboard
     */
    private void performCastlingMoveLeft(int posX, int posY, int newX, int newY, Board board) {

        Figure actualFigure = board.getFigure(posX, posY);
        Figure targetFigure = board.getFigure(newX, newY);

        board.setFigure(newX + 1, newY, board.getFigure(0, posY));    // move rook
        board.setFigure(0, newY, new None());                            // replace field where rook was standing

        board.setFigure(newX, newY, actualFigure);                          // move king
        board.setFigure(posX, posY, targetFigure);                          // replace field where king was standing

        // kann hier nicht aufgerufen werden, da sonst Fehler bei schachMattPrüfung (siehe ChessMove())
        // board.getFigure(0, posY).setAlreadyMoved(true);                  //Update AlreadyMoved
        // board.getFigure(posX, posY).setAlreadyMoved(true);               //Update AlreadyMoved
    }

    /**
     * executes a kingside (right) castling move on the board
     *
     * @param posX  current x-position of the figure you want to move
     * @param posY  current y-position of the figure you want to move
     * @param newX  target x-position of the figure you want to move
     * @param newY  target y-position of the figure you want to move
     * @param board the current chessboard
     */
    private void performCastlingMoveRight(int posX, int posY, int newX, int newY, Board board) {

        Figure actualFigure = board.getFigure(posX, posY);
        Figure targetFigure = board.getFigure(newX, newY);

        board.setFigure(newX - 1, newY, board.getFigure(7, posY));      // move rook
        board.setFigure(7, newY, new None());                              // replace field where rook was standing

        board.setFigure(newX, newY, actualFigure);                            // move king
        board.setFigure(posX, posY, targetFigure);                            // replace field where king was standing

        // kann hier nicht aufgerufen werden, da sonst Fehler bei schachMattPrüfung (siehe ChessMove())
        // board.getFigure(7, posY).setAlreadyMoved(true);                    //Update AlreadyMoved
        // board.getFigure(posX, posY).setAlreadyMoved(true);                 //Update AlreadyMoved
    }

    /*
     * <------Pawn-conversion------------------------------------------------------------------------------------------->
     */

    /**
     * checks valid pawn conversion
     *
     * @param posX  current x-position of the figure you want to move
     * @param posY  current y-position of the figure you want to move
     * @param newX  target x-position of the figure you want to move
     * @param newY  target y-position of the figure you want to move
     * @param board the current chessboard
     * @return Whether a pawn conversion is possible
     */
    private boolean checkPawnConversion(int posX, int posY, int newX, int newY, Board board) {
        Figure actualFigure = board.getFigure(posX, posY);

        if (actualFigure instanceof Pawn) {
            if (actualFigure.validMove(posX, posY, newX, newY, board)) {
                return (newY == 7 && actualFigure.getTeam() == 0) || (newY == 0 && actualFigure.getTeam() == 1);
            }
        }
        return false;
    }

    /**
     * executes a pawn conversion on the board
     *
     * @param posX     current x-position of the figure you want to move
     * @param posY     current y-position of the figure you want to move
     * @param newX     target x-position of the figure you want to move
     * @param newY     target y-position of the figure you want to move
     * @param figureID the number of the figure you want the pawn to convert to (0 for queen, 1 for knight, 2 for bishop)
     * @param board    the current chessboard
     */
    private void performPawnConversion(int posX, int posY, int newX, int newY, int figureID, Board board) {
        Figure actualFigure = board.getFigure(posX, posY);

        //convert white pawn
        if (newY == 7 && actualFigure instanceof Pawn && actualFigure.getTeam() == 0) {

            switch (figureID) {
                //to knight
                case 3: {
                    board.setFigure(newX, newY, new Knight(0));
                    break;
                }
                //to bishop
                case 4: {
                    board.setFigure(newX, newY, new Bishop(0));
                    break;
                }
                //to rook
                case 2: {
                    board.setFigure(newX, newY, new Rook(0));
                }
                //to queen
                default: {
                    board.setFigure(newX, newY, new Queen(0));
                    break;
                }
            }
        }

        //convert black pawn
        if (newY == 0 && actualFigure instanceof Pawn && actualFigure.getTeam() == 1) {
            switch (figureID) {
                //to knight
                case 3: {
                    board.setFigure(newX, newY, new Knight(1));
                    break;
                }
                //to bishop
                case 4: {
                    board.setFigure(newX, newY, new Bishop(1));
                    break;
                }
                //to rook
                case 2: {
                    board.setFigure(newX, newY, new Rook(1));
                }
                //to queen
                default: {
                    board.setFigure(newX, newY, new Queen(1));
                    break;
                }
            }
        }

        board.getFigure(newX, newY).setAlreadyMoved(true);
        board.setFigure(posX, posY, new None());
    }


    /*
     * <------Default-commands------------------------------------------------------------------------------------------>
     */

    /**
     * Checks whether the king is in check
     *
     * @param board the current chessboard
     * @param team  The team ID of the target King
     * @return Whether the king is in check or not
     */
    private boolean kingInCheck(Board board, int team) {
        int[] kingPos = board.getKing(team);
        if (isThreatened(board, team, kingPos[0], kingPos[1])) {
            System.out.println("You are in check");
            return true;
        }
        return false;
    }

    /**
     * Check chessMate
     *
     * @param team the team of the target king
     * @return whether the king of "team"-color is in checkmate
     */
    private boolean checkChessMate(int team) {
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
                            if (checkCastling(x, y, newX, newY, tmpBoard) == 1) {            // check Castling and eventually perform it on the temporary board
                                performCastlingMoveLeft(x, y, newX, newY, tmpBoard);
                                if (!kingInCheck(tmpBoard, team)) {                         // and at least check whether the king would still be in check after every possible move
                                    possibleSolution = true;
                                }
                            }
                            if (checkCastling(x, y, newX, newY, tmpBoard) == 2) {            // check Castling and eventually perform it on the temporary board
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
        if (!possibleSolution) {
            System.out.println("You are checkmate");
        }
        return !possibleSolution;
    }


    /**
     * Checks if the figure is threatened by the other team
     *
     * @param board      current chessboard
     * @param team       team of the figure you want to check
     * @param targetPosX x-position of the figure you want to check
     * @param targetPosY y-position of the figure you want to check
     * @return whether the figure is threatened by the other team
     */
    private boolean isThreatened(Board board, int team, int targetPosX, int targetPosY) {
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
     * Return Board
     * @return board
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Get a list of beaten figures
     * @return Beaten figures
     */
    public ArrayList<Figure> getBeatenFigures() {
        return this.beatenFigures;
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
     * reset EnPassant
     */
    private void resetEnPassant(int newX, int newY) {
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                if (x != newX && y != newY) {
                    if (board.getFigure(x, y) instanceof Pawn) {
                        ((Pawn) board.getFigure(x, y)).resetEnPassant();
                    }
                }
            }
        }
    }

    /**
     * Does the standard tasks after each move.
     * (Prints the done move out and adds it to the history, switches the actual player and proofs if the game is finished)
     * @param posX actual x Position
     * @param posY actual y Position
     * @param newX new x Position
     * @param newY new y Position
     * @param pawnConversion figure in which the pawn converts
     */
    private void updateChanges(int posX, int posY, int newX, int newY, int pawnConversion, Figure actualFigure, Figure targetFigure){
        System.out.println("![" + posX + posY + "-" + newX + newY + pawnConversion + "]");
        switchPlayer();
        resetEnPassant(newX, newY);
        moveHistory.add(""); //TODO:
    }
}
