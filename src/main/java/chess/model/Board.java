package chess.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class Board {

    private Figure[][] board;
    private ArrayList<Figure> beatenFigures = new ArrayList<Figure>();

    /**
     * Creates a new board with the standard figure setup
     */
    public Board() {
        board = new Figure[8][8];

        for (int team = 0; team <= 1; team++) {
            //create Pawns
            for (int i = 0; i < 8; i++) {
                board[i][1 + team * 5] = new Pawn(team);
            }
            //create King
            board[4][team * 7] = new King(team);

            //create Queen
            board[3][team * 7] = new Queen(team);

            //create Rook
            for (int i = 0; i <= 1; i++) {
                board[i * 7][team * 7] = new Rook(team);
            }
            //create Bishop
            for (int i = 0; i <= 1; i++) {
                board[2 + i * 3][team * 7] = new Bishop(team);
            }
            //create Knight
            for (int i = 0; i <= 1; i++) {
                board[1 + i * 5][team * 7] = new Knight(team);
            }
        }
        //create None
        for (int y = 2; y < 6; y++) {
            for (int x = 0; x < 8; x++) {
                board[x][y] = new None();
            }
        }
    }

    /**
     * Copy Constructor
     * copies actual board
     *
     * @return copy of the actual board
     */

    public Board(Board sourceClass) {
        board = new Figure[8][8];
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                switch (sourceClass.getFigure(x, y).getFigureID()){
                    case 0: board[x][y] = new None((None) sourceClass.getFigure(x, y)); break;
                    case 1: board[x][y] = new Pawn((Pawn) sourceClass.getFigure(x, y)); break;
                    case 2: board[x][y] = new Rook((Rook) sourceClass.getFigure(x, y)); break;
                    case 3: board[x][y] = new Knight((Knight) sourceClass.getFigure(x, y)); break;
                    case 4: board[x][y] = new Bishop((Bishop) sourceClass.getFigure(x, y)); break;
                    case 5: board[x][y] = new Queen((Queen) sourceClass.getFigure(x, y)); break;
                    case 6: board[x][y] = new King((King) sourceClass.getFigure(x, y)); break;
                }
            }
        }
    }



    public Figure[][] getBoard() {
        return board;
    }

    public Figure getFigure(int x, int y) {
        return board[x][y];
    }

    public void setFigure(int x, int y, Figure figure) {
        board[x][y] = figure;
    }

    public ArrayList<Figure> getBeatenFigures(){
        return this.beatenFigures;
    }

    /*
    <-------Board-analysis--------------------------------------------------------------------------------------------->
     */
    /**
     * Get the position of the target king
     *
     * @param team
     * @return position of target king, {0,0} if there is no king found
     */
    public static Position getKing(Board board, int team) {
        //Searching target King position
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                if (board.getFigure(x, y) instanceof King && board.getFigure(x, y).getTeam() == team) {
                    //Return target king position
                    return new Position(x, y);
                }
            }
        }
        //Return nothing
        return new Position(0, 0);
    }

    /**
     * Checks if the figure is threatened by the other team
     *
     * @param tmpBoard      current chessboard
     * @param team       team of the figure you want to check
     * @param targetPos  position of the figure you want to check
     * @return whether the figure is threatened by the other team
     */
    public static boolean isThreatened(Board tmpBoard, Position targetPos, int team) {
        //Check if any enemy figure can do a valid move to target Position
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                if (tmpBoard.getFigure(x, y).getTeam() != team && tmpBoard.getFigure(x, y).validMove(new Position(x, y), targetPos, tmpBoard)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Check chessMate
     *
     * @param team the team of the target king
     * @return whether the king of "team"-color is in checkmate
     */
    public static boolean checkChessMate(Board board, int team) {
        boolean possibleSolution = false;

        // test all possible moves and check whether your king is still in check
        for (int y = 0; y < 8; y++) {                                                   // for all your own figures on the board
            for (int x = 0; x < 8; x++) {
                Figure actualFigure = board.getFigure(x, y);
                if (actualFigure.getTeam() == team) {
                    //create a copy of Board
                    Board tmpBoard = new Board(board);                             // create a temporary board

                    for (int newX = 0; newX < 8; newX++) {                              // and test all possible moves to every possible targetField
                        for (int newY = 0; newY < 8; newY++) {

                            if (Rules.checkEnPassant(new Position(x, y), new Position(newX, newY), tmpBoard)) {           // check EnPassant and eventually perform it on the temporary board
                                Rules.performEnPassantMove(new Position(x, y), new Position(newX, newY), tmpBoard);
                                if (!kingInCheck(tmpBoard, team)) {                         // and at least check whether the king would still be in check after every possible move
                                    possibleSolution = true;
                                }
                            }
                            if (Rules.checkCastling(new Position(x, y), new Position(newX, newY), tmpBoard) == 1) {            // check Castling and eventually perform it on the temporary board
                                Rules.performCastlingMoveLeft(new Position(x, y), new Position(newX, newY), tmpBoard);
                                if (!kingInCheck(tmpBoard, team)) {                         // and at least check whether the king would still be in check after every possible move
                                    possibleSolution = true;
                                }
                            }
                            if (Rules.checkCastling(new Position(x, y), new Position(newX, newY), tmpBoard) == 2) {            // check Castling and eventually perform it on the temporary board
                                Rules.performCastlingMoveRight(new Position(x, y), new Position(newX, newY), tmpBoard);
                                if (!kingInCheck(tmpBoard, team)) {                         // and at least check whether the king would still be in check after every possible move
                                    possibleSolution = true;
                                }
                            }
                            if (Rules.checkValidDefaultMove(new Position(x, y), new Position(newX, newY), tmpBoard)) {    // checkValidDefaultMove and eventually perform it on the temporary board
                                Rules.performDefaultMove(new Position(x, y), new Position(newX, newY), tmpBoard);
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
     * Checks whether the king is in check
     *
     * @param board the current chessboard
     * @param team  The team ID of the target King
     * @return Whether the king is in check or not
     */
    public static boolean kingInCheck(Board board, int team) {
        if (isThreatened(board, Board.getKing(board, team), team)) {
            System.out.println("You are in check");
            return true;
        }
        return false;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Board) {
            return ((((Board) other).getBoard()) == board) && (((Board) other).getBeatenFigures() == beatenFigures);
        }
        return false;
    }

}
