package chess.model;

import java.util.ArrayList;

public class CoreGame {

    private Board board;
    private int activePlayer = 0;
    private int gameMode = 0;

    private ArrayList<Board> moveHistory = new ArrayList<>();

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
    public boolean chessMove(Move move) {

        Position actualPos = move.getActualPosition();
        Position targetPos = move.getTargetPosition();
        int pawnConversion = move.getPawnConversionTo();

        if (board.getFigure(actualPos).getTeam() == activePlayer) {
            //check EnPassant
            if (Rules.checkEnPassant(actualPos, targetPos, board)) {
                Rules.performEnPassantMove(actualPos, targetPos, board);
                System.out.println("EnPassant");
                updateChanges(actualPos, targetPos, pawnConversion);
                return Board.checkChessMate(board, activePlayer);
            }
            //check Castling
            if (Rules.checkCastling(actualPos, targetPos, board) == 1) {
                Rules.performCastlingMoveLeft(actualPos, targetPos, board);
                board.getFigure(0, actualPos.getPosX()).setAlreadyMoved(true);         // muss hier aufgerufen werden, da sonst auch bei der Überprüfung von Schachmatt ggf. die Figur auf
                board.getFigure(actualPos).setAlreadyMoved(true);         // setAlreadyMoved=true gesetzt wird (da Figure unabhängig von board bzw. tmpBoard)
                System.out.println("Castling left");
                updateChanges(actualPos, targetPos, pawnConversion);
                return Board.checkChessMate(board, activePlayer);
            }
            if (Rules.checkCastling(actualPos, targetPos, board) == 2) {
                Rules.performCastlingMoveRight(actualPos, targetPos, board);
                board.getFigure(7, actualPos.getPosY()).setAlreadyMoved(true);         // muss hier aufgerufen werden, da sonst auch bei der Überprüfung von Schachmatt ggf. die Figur auf
                board.getFigure(actualPos).setAlreadyMoved(true);         // setAlreadyMoved=true gesetzt wird (da Figure unabhängig von board bzw. tmpBoard)
                System.out.println("Castling right");
                updateChanges(actualPos, targetPos, pawnConversion);
                return Board.checkChessMate(board, activePlayer);
            }
            //check Pawn conversion
            if (Rules.checkPawnConversion(actualPos, targetPos, board)) {
                Rules.performPawnConversion(actualPos, targetPos, pawnConversion, board);
                board.getFigure(actualPos).setAlreadyMoved(true);          // muss hier aufgerufen werden, da sonst auch bei der Überprüfung von Schachmatt ggf. die Figur auf setAlreadyMoved=true gesetzt wird (da Figure unabhängig von board bzw. tmpBoard)
                System.out.println("PawnConversion");
                updateChanges(actualPos, targetPos, pawnConversion);
                return Board.checkChessMate(board, activePlayer);
            }
            //checkValidDefaultMove
            if (Rules.checkValidDefaultMove(actualPos, targetPos, board)) {
                Rules.performDefaultMove(actualPos, targetPos, board);
                System.out.println("Default move");
                updateChanges(actualPos, targetPos, pawnConversion);
                return Board.checkChessMate(board, activePlayer);
            }
        }

        //User command fails
        System.out.println("!Move not allowed");
        return false;
    }

    /*
     * <------Default-commands------------------------------------------------------------------------------------------>
     */

    public Board getBoard() {
        return board;
    }

    // doppelt
    public ArrayList<Figure> getBeatenFigures() {
        return board.getBeatenFigures();
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
                    if (board.getFigure(x, y) instanceof Pawn) {
                        ((Pawn) board.getFigure(x, y)).resetEnPassant();
                    }
                }
            }
        }
    }

    /**
     * Does the standard tasks after each move.
     * (Prints the latest move and adds it to the history, switches the actual player and proofs if the game is finished)
     *
     * @param actualPos      current position
     * @param targetPos      new position
     * @param pawnConversion figure in which the pawn converts
     */
    private void updateChanges(Position actualPos, Position targetPos, int pawnConversion) {
        System.out.println("![" + Character.toString(actualPos.getPosX() + 97) + (actualPos.getPosY() + 1) + "-" + Character.toString(targetPos.getPosX() + 97) + (targetPos.getPosY() + 1) + getPawnLetter(pawnConversion) + "]");
        switchPlayer();
        resetEnPassant(targetPos.getPosX(), targetPos.getPosY());
        moveHistory.add(new Board(board));
    }

    /**
     * Return Figure Letter
     *
     * @param pawnConversion ID of the figure you want the pawn to convert to
     * @return Letter of the figure you want the pawn to convert to
     */
    public String getPawnLetter(int pawnConversion){
        switch(pawnConversion){
            case 1: return "P";
            case 2: return "R";
            case 3: return "N";
            case 4: return "B";
            case 5: return "";
            case 6: return "K";
        }
        return "";
    }
}
