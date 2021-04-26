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
            if (board.getFigure(posX, posY).getTeam() == activePlayer) {
                //check EnPassant
                if (checkEnPassant(posX, posY, newX, newY, board)) {
                    performEnPassantMove(posX, posY, newX, newY, board);
                    switchPlayer();
                    checkChessMate(activePlayer);
                    return true;
                }
                //check Castling
                if (checkCastling(posX, posY, newX, newY, board)) {
                    performCastlingMove(posX, posY, newX, newY, board);
                    switchPlayer();
                    checkChessMate(activePlayer);
                    return true;
                }
                //check Pawn conversion
                if (checkPawnConversion(move, board)) {
                    performPawnConversion(posX, posY, newX, newY, move, board);
                    switchPlayer();
                    checkChessMate(activePlayer);
                    return true;
                }
                //checkValidDefaultMove
                if (checkValidDefaultMove(posX, posY, newX, newY, board)) {
                    performDefaultMove(posX, posY, newX, newY, board);
                    switchPlayer();
                    checkChessMate(activePlayer);
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

        actualFigure.setAlreadyMoved(true);
        board.setFigure(newX, newY, actualFigure);
        board.setFigure(posX, posY, targetFigure);
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
    public boolean checkCastling(int posX, int posY, int newX, int newY, Board board) {

        Figure actualFigure = board.getFigure(posX, posY);

        // castle left
        if (actualFigure instanceof King && newX == posX - 2 && !actualFigure.isAlreadyMoved() && !board.getFigure(0, posY).isAlreadyMoved()) {
            // check, whether all field between are empty and are not threatened
            for (int j = 2; j < posX; j++) {
                if (!(board.getFigure(j, posY) instanceof None) || isThreatened(board, 2,j,posY)) {
                    return false;
                }
            }
            return true;
        }
        // castle right
        if (actualFigure instanceof King && !actualFigure.isAlreadyMoved() && newX == posX + 2 && !board.getFigure(7, posY).isAlreadyMoved()) {
            // check, whether all field between are empty and are not threatened
            for (int j = posX + 1; j < 7; j++) {
                if (!(board.getFigure(j, posY) instanceof None) || isThreatened(board, actualFigure.getTeam(),j,posY)) {
                    return false;
                }
            }
            return true;
        }

        return false;
    }

    public void performCastlingMove(int posX, int posY, int newX, int newY, Board board) {

        Figure actualFigure = board.getFigure(posX, posY);
        Figure targetFigure = board.getFigure(newX, newY);

        // black castle queenside (top left)
        if (newX == posX - 2 && actualFigure.getTeam() == 1) {
            board.getFigure(0, posY).setAlreadyMoved(true);
            board.setFigure(newX + 1, newY, board.getFigure(0, posY));   // move rook
            board.setFigure(0, 0, new None());                        // replace field where rook was standing
        }
        // black castle kingside (top right)
        if (newX == posX + 2 && actualFigure.getTeam() == 1) {
            board.getFigure(7, posY).setAlreadyMoved(true);
            board.setFigure(newX - 1, newY, board.getFigure(7, posY));   // move rook
            board.setFigure(7, 0, new None());                        // replace field where rook was standing
        }
        // white castle kingside (bottom left)
        if (newX == posX - 2 && actualFigure.getTeam() == 0) {
            board.getFigure(0, posY).setAlreadyMoved(true);
            board.setFigure(newX + 1, newY, board.getFigure(0, posY));   // move rook
            board.setFigure(0, 0, new None());                        // replace field where rook was standing
        }
        // white castle queenside (bottom right)
        if (newX == posX + 2 && actualFigure.getTeam() == 0) {
            board.getFigure(7, posY).setAlreadyMoved(true);
            board.setFigure(newX - 1, newY, board.getFigure(7, posY));   // move rook
            board.setFigure(7, 0, new None());                        // replace field where rook was standing
        }

        board.setFigure(newX, newY, actualFigure);          // move king
        board.setFigure(posX, posY, targetFigure);          // replace field where king was standing
        actualFigure.setAlreadyMoved(true);
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
    public boolean checkPawnConversion(int posX, int posY, int newX, int newY) {
        Figure actualFigure = board.getFigure(posX, posY);

        if(newY == 8 || newY == 1 && actualFigure instanceof Pawn) {
            return true;
        }
        return false;
    }

    /**
     * makes a pawn conversion move on the board.
     *
     * @param move Figure position
     * @param posX, posY, newX, newY
     */
    public void performPawnConversion (int posX, int posY, int newX, int newY, Map<String, Integer> move, Board board) {
        Figure actualFigure = board.getFigure(posX, posY);

        //convert white pawn
        if (newY == 8 && actualFigure instanceof Pawn && actualFigure.getTeam() == 0) {
            //to knight
            switch (move.get("convertPawnTo")) {
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
        if (newY == 1 && actualFigure instanceof Pawn && actualFigure.getTeam() == 1) {
            switch (move.get("convertPawnTo")) {
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
        //create local Variables
        int kingX = 0;
        int kingY = 0;
        //Searching target King position
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                if (board.getFigure(x, y) instanceof King && board.getFigure(x, y).getTeam() == team) {
                    kingX = x;
                    kingY = y;
                    break;
                }
            }
        }
        return isThreatened(board, team, kingX, kingY);
        /*//Check if any enemy figure can do a valid move to King Position
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                if (board.getFigure(x, y).getTeam() != team) {
                    Map<String, Integer> tmpMove = new HashMap<String, Integer>();
                    tmpMove.put("posX", x);
                    tmpMove.put("posY", y);
                    tmpMove.put("newX", kingX);
                    tmpMove.put("newY", kingY);
                    if (board.getFigure(x, y).validMove(tmpMove.get("posX"), tmpMove.get("posY"), tmpMove.get("newX"), tmpMove.get("newY"), board)) {
                        return true;
                    }
                }
            }
        }
        return false;*/
    }

    /**
     * Check chessMate
     *
     * @param team Target king
     * @return
     */
    public boolean checkChessMate(int team) {
        boolean possibleSolution = false;
        Board tmpBoard = new Board();

        // test all possible moves and check whether your king is still in check
        for (int y = 0; y < 8; y++) {                                                   // for all your own figures on the board
            for (int x = 0; x < 8; x++) {
                Figure actualFigure = board.getFigure(x, y);
                if (actualFigure.getTeam() == team) {
                    tmpBoard = board.copyBoard();                                       // create a temporary board

                    for (int newX = 0; newX < 8; newX++) {                              // and test all possible moves to every possible targetField
                        for (int newY = 0; newY < 8; newY++) {

                            if (checkEnPassant(x, y, newX, newY, tmpBoard)) {           // check EnPassant and eventually perform it on the temporary board
                                performEnPassantMove(x, y, newX, newY, tmpBoard);
                            }
                            if (checkCastling(x, y, newX, newY, tmpBoard)) {            // check Castling and eventually perform it on the temporary board
                                performCastlingMove(x, y, newX, newY, tmpBoard);
                            }
                            Map<String, Integer> tmpMove = new HashMap<String, Integer>();
                            tmpMove.put("posX", x);
                            tmpMove.put("posY", y);
                            tmpMove.put("newX", newX);
                            tmpMove.put("newY", newY);
                            if (checkPawnConversion(tmpMove, tmpBoard)) {               // check Pawn conversion and eventually perform it on the temporary board
                                performPawnConversion(x,y,newX,newY,tmpMove,tmpBoard);
                            }
                            if (checkValidDefaultMove(x, y, newX, newY, tmpBoard)) {    // checkValidDefaultMove and eventually perform it on the temporary board
                                performDefaultMove(x, y, newX, newY, tmpBoard);
                            }

                            if (!kingInCheck(tmpBoard, team)) {                         // and at least check whether the king would still be in check after every possible move
                                possibleSolution = true;
                            }
                        }
                    }
                }
            }
        }
        return possibleSolution;
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
        //Check if any enemy figure can do a valid move to King Position
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                Board tmpBoard = board.copyBoard();
                if (tmpBoard.getFigure(x, y).getTeam() != team) {
                    Map<String, Integer> tmpMove = new HashMap<String, Integer>();
                    tmpMove.put("posX", x);
                    tmpMove.put("posY", y);
                    tmpMove.put("newX", targetPosX);
                    tmpMove.put("newY", targetPosY);
                    if (tmpBoard.getFigure(x, y).validMove(tmpMove.get("posX"), tmpMove.get("posY"), tmpMove.get("newX"), tmpMove.get("newY"), tmpBoard)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


    /**
     * switch active player
     */
    public void switchPlayer() {
        if (activePlayer == 0) activePlayer = 1;
        else activePlayer = 0;

        // activePlayer = activePlayer == 0 ? 1 : 0;
    }


/**
 * <------System-components--------------------------------------------------------------------------------------------->
 */

    /**
     * Converts user input into coordinates. e.g. a3 == x: 0 y: 2
     *
     * @param input User input
     * @return Move coordinates
     */
    /*public Map<String, Integer> parse(String input) {
        Map<String, Integer> pos = new HashMap<String, Integer>();
        //"a3-b4" or "a3-b4Q"
        if ((input.length() == 5 || input.length() == 6) && input.charAt(2) == 45) {
            //split "a3-b4Q" to "a3" and "b4Q"
            String[] result = input.split("-");
            if (result.length == 2) {
                String[] typ = {"pos", "new"};
                for (int i = 0; i < 2; i++) {
                    String[] xyPosition = result[i].split("");
                    //convert letters to numbers with ASCII code
                    if (xyPosition[0].charAt(0) >= 97 && xyPosition[0].charAt(0) <= 104) {
                        pos.put(typ[i] + "X", (int) xyPosition[0].charAt(0) - 97);
                    }
                    //convert numbers to numbers
                    if (xyPosition[1].charAt(0) >= 49 && xyPosition[1].charAt(0) <= 56) {
                        pos.put(typ[i] + "Y", Integer.parseInt(xyPosition[1]) - 1);
                    }
                }
            }
            //split "a7-a8Q" to "a7" and "a8" and "Q" (corresponds to 0)
            if (input.matches("^[a-h][27]-[a-h][18][Q]$")) {
                pos.put("convertPawnTo", 0);
            }
            //split "a7-a8N" to "a7" and "a8" and "N" (corresponds to 1)
            if (input.matches("^[a-h][27]-[a-h][18][N]$")) {
                pos.put("convertPawnTo", 1);
            }
            //split "a7-a8B" to "a7" and "a8" and "B" (corresponds to 2)
            if (input.matches("^[a-h][27]-[a-h][18][B]$")) {
                pos.put("convertPawnTo", 2);
            }
        }
        //if pos is less than 4 then invalid entry
        return pos;
    }*/
}
