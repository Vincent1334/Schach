package chess.model;

import chess.figures.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * This class contains the information about the chess board which contains the chess pieces/figures
 *
 * @author Lydia Engelhardt, Sophia Kuhlmann, Vincent Schiller, Friederike Weilbeer
 * 2021-05-07
 */
public class Board {

    private Figure[][] internalBoard;
    private List<Figure> beatenFigures = new ArrayList<>();

    //flags
    private boolean[] castling = new boolean[2];
    private boolean[] check = new boolean[2];
    private boolean[] checkMate = new boolean[2];
    private boolean staleMate;

    /**
     * Creates a new board with the standard figure setup
     */
    public Board() {
        internalBoard = new Figure[8][8];
        boolean blackTeam = false;

        for (int i = 0; i <= 1; i++) {
            // set team
            blackTeam = i != 0;

            //create Pawns
            for (int j = 0; j < 8; j++) {
                internalBoard[j][1 + i * 5] = new Pawn(blackTeam);
            }
            //create King
            internalBoard[4][i * 7] = new King(blackTeam);

            //create Queen
            internalBoard[3][i * 7] = new Queen(blackTeam);

            //create Rooks
            for (int j = 0; j <= 1; j++) {
                internalBoard[j * 7][i * 7] = new Rook(blackTeam);
            }
            //create Bishops
            for (int j = 0; j <= 1; j++) {
                internalBoard[2 + j * 3][i * 7] = new Bishop(blackTeam);
            }
            //create Knights
            for (int j = 0; j <= 1; j++) {
                internalBoard[1 + j * 5][i * 7] = new Knight(blackTeam);
            }
        }
        //create None
        for (int y = 2; y < 6; y++) {
            for (int x = 0; x < 8; x++) {
                internalBoard[x][y] = new None();
            }
        }
    }

    /**
     * Copy Constructor
     * copies the current board
     *
     * @param sourceClass the board-object you want to copy
     */
    public Board(Board sourceClass) {
        internalBoard = new Figure[8][8];
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                copyFigures(sourceClass.getFigure(x, y), x, y);
            }
        }
        // beatenFigures wird allerdings nicht verwendet
        beatenFigures = new ArrayList<>();
        beatenFigures.addAll(sourceClass.getBeatenFigures());
    }

    /**
     * copies figures from one board to another
     * @param figure the figure
     * @param x the x position
     * @param y the y position
     */
    private void copyFigures(Figure figure, int x, int y){
        switch (figure.getFigureID()) {
            case 0:
                internalBoard[x][y] = new None((None) figure);
                break;
            case 1:
                internalBoard[x][y] = new Pawn((Pawn) figure);
                break;
            case 2:
                internalBoard[x][y] = new Rook((Rook) figure);
                break;
            case 3:
                internalBoard[x][y] = new Knight((Knight) figure);
                break;
            case 4:
                internalBoard[x][y] = new Bishop((Bishop) figure);
                break;
            case 5:
                internalBoard[x][y] = new Queen((Queen) figure);
                break;
            case 6:
                internalBoard[x][y] = new King((King) figure);
                break;
        }
    }

    /**
     * Get the figure at a specified position on the board
     *
     * @param x x-coordinate of the position
     * @param y y-coordinate of the position
     * @return the figure at this position of the board
     */
    public Figure getFigure(int x, int y) {
        return internalBoard[x][y];
    }

    /**
     * Get the figure at a specified position on the board
     *
     * @param position the position on the board where you want to get the figure from
     * @return the figure at this position of the board
     */
    public Figure getFigure(Position position) {
        return internalBoard[position.getPosX()][position.getPosY()];
    }

    /**
     * Set a figure to a specified position on the board
     *
     * @param x      x-coordinate of the position
     * @param y      y-coordinate of the position
     * @param figure the figure you want to set to the position on the board
     */
    public void setFigure(int x, int y, Figure figure) {
        internalBoard[x][y] = figure;
    }

    /**
     * Set a figure to a specified position on the board
     *
     * @param position the position on the board where you want to set the figure to
     * @param figure   the figure you want to set to the position on the board
     */
    public void setFigure(Position position, Figure figure) {
        internalBoard[position.getPosX()][position.getPosY()] = figure;
    }

    /**
     * Get the list of beatenFigures of the board
     *
     * @return the list of beatenFigures of the board
     */
    public List<Figure> getBeatenFigures() {
        return this.beatenFigures;
    }

    /*
    <-------Board-analysis--------------------------------------------------------------------------------------------->
     */

    /**
     * Get the position of the target king
     *
     * @param board     current chessboard
     * @param blackTeam team-ID of the king you want to get
     * @return position of target king, {0,0} if there is no king found
     */
    public static Position getKingPos(Board board, boolean blackTeam) {
        //Searching target King position
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                if (board.getFigure(x, y) instanceof King && board.getFigure(x, y).isBlackTeam() == blackTeam) {
                    //Return target king position
                    return new Position(x, y);
                }
            }
        }
        //Return nothing
        return new Position(0, 0);
    }

    /**
     * Checks if a field is threatened by the selected team
     *
     * @param tmpBoard  current chessboard
     * @param blackTeam opposite team that may threaten the figure
     * @param targetPos position of the figure you want to check
     * @return whether the figure is threatened by the other team
     */
    public static boolean isThreatened(Board tmpBoard, Position targetPos, boolean blackTeam) {
        //Check if the target team threatened the target field
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                //don't check the target position, don't check empty fields
                if (!(x == targetPos.getPosX() && y == targetPos.getPosY())
                        && tmpBoard.getFigure(x, y).isBlackTeam() == blackTeam
                        && !(tmpBoard.getFigure(new Position(x, y)) instanceof None)
                        && tmpBoard.getFigure(new Position(x, y)).validMove(new Position(x, y), targetPos, tmpBoard)){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Checks whether the king is in check
     * @param board     current chessboard
     * @param blackTeam The team ID of the target King
     * @return Whether the king is in check or not
     */
    public static boolean kingInCheck(Board board, boolean blackTeam) {
        if(isThreatened(board, Board.getKingPos(board, blackTeam), !blackTeam)){
            board.setCheckFlag(true, blackTeam);
            return true;
        }
        board.setCheckFlag(false, blackTeam);
        return false;
    }

    /**
     * Check chessmate and stalemate
     * @param board     current chessboard
     * @param blackTeam the team of the target king
     * @return whether the king of "team"-color is in checkmate or stalemate
     */
    public static boolean checkChessAndStaleMate(Board board, boolean blackTeam) {
        // test all possible moves and check whether your king is still in check
        for (int y = 0; y < 8; y++) {                                           // for all your own figures on the board
            for (int x = 0; x < 8; x++) {
                if (!(board.getFigure(new Position(x, y)) instanceof None) && board.getFigure(new Position(x, y)).isBlackTeam() == blackTeam && checkPossibleTarget(board, x, y, blackTeam)) {
                    return false;
                }
            }
        }

        //check chessMate or staleMate
        if(Board.kingInCheck(board, blackTeam)){
            board.setCheckMateFlag(true, blackTeam);
            board.setStaleMateFlag(false);
        }else{
            board.setCheckMateFlag(false, blackTeam);
            board.setStaleMateFlag(true);
        }
        return true;
    }

    /**
     * tests if a figure can make any move
     * @param board the chess-board you play on
     * @param x the x Position of the Figure you want to test
     * @param y the y Position of the Figure you want to test
     * @param blackTeam the team of the Figure you want to test
     * @return true, if the figure on the position x,y of the board can makes any move
     */
    private static boolean checkPossibleTarget(Board board, int x, int y, boolean blackTeam){
        for (int newX = 0; newX < 8; newX++) {                      // test all possible moves to every possible targetField
            for (int newY = 0; newY < 8; newY++) {
                Board tmpBoard = new Board(board);                  // on a copy of the board
                if (possibleSolution(new Position(x, y), new Position(newX, newY), tmpBoard, blackTeam))
                    return true;
            }
        }
        return false;
    }

    /**
     * Check if target figure can perform a possible move
     * @param actualPos the actual position of the figure
     * @param targetPos the target position of the figure
     * @param tmpBoard the temporary board for testing
     * @param blackTeam the team of the figure
     * @return true, if the figure can make a move that doesn't makes the king in check
     */
    private static boolean possibleSolution(Position actualPos, Position targetPos, Board tmpBoard, boolean blackTeam) {
        if (Rules.checkEnPassant(actualPos, targetPos, tmpBoard)) {             // check EnPassant and eventually perform it on the temporary board
            Rules.performEnPassantMove(actualPos, targetPos, tmpBoard);
        }
        if (Rules.checkDefaultMove(actualPos, targetPos, tmpBoard)) { // checkValidDefaultMove and eventually perform it on the temporary board
            Rules.performDefaultMove(actualPos, targetPos, tmpBoard);
        }
        //All other moves are not allowed in this case!
        return !kingInCheck(tmpBoard, blackTeam);
    }

    /**
     * Important for copy constructor
     * @param other the other board
     * @return true, if the other board and the actual board are the same
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        Board board1 = (Board) other;
        return Arrays.deepEquals(internalBoard, board1.internalBoard) && Objects.equals(beatenFigures, board1.beatenFigures);
    }

    /**
     * return hashCode
     * @return the hash code of the board
     */
    @Override
    public int hashCode() {
        int result = Objects.hash(beatenFigures);
        result = 31 * result + Arrays.deepHashCode(internalBoard);
        return result;
    }

    /**
     * sets a flag when castling is true
     * @param castling, isBlack
     */
    public void setCastlingFlag(boolean castling, boolean isBlack){
        this.castling[isBlack ? 1 : 0] = castling;
    }

    /**
     * getter for the castling flag
     * @param isBlack
     * @return flag
     */
    public boolean isCastlingFlag(boolean isBlack){
        return castling[isBlack ? 1 : 0];
    }

    /**
     * sets a flag when checkMate is true
     * @param checkMate, isBlack
     */
    public void setCheckMateFlag(boolean checkMate, boolean isBlack){
        this.checkMate[isBlack ? 1 : 0] = checkMate;
    }
    /**
     * getter for the checkMate flag flag
     * @param isBlack
     * @return flag
     */
    public boolean isCheckMateFlag(boolean isBlack){
        return checkMate[isBlack ? 1 : 0];
    }
    /**
     * sets a flag when staleMate is true
     * @param staleMate
     */
    public void setStaleMateFlag(boolean staleMate){
            this.staleMate = staleMate;
    }

    /**
     * getter for the staleMate flag
     * @return flag
     */
    public boolean isStaleMateFlag(){
        return staleMate;
    }

    /**
     * sets a flag when check is true
     * @param check, isBlack
     */
    public void setCheckFlag(boolean check, boolean isBlack){
        this.check[isBlack ? 1 : 0] = check;
    }

    /**
     * getter for the check flag
     * @param isBlack
     * @return flag
     */
    public boolean isCheckFlag(boolean isBlack){
        return check[isBlack ? 1 : 0];
    }
}
